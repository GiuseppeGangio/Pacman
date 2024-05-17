package com.kite.engine.core;

import com.kite.engine.event.Event;
import com.kite.engine.event.EventHandler;
import com.kite.engine.event.applicationevents.ApplicationInitializedEvent;
import com.kite.engine.event.applicationevents.ApplicationStartedEvent;
import com.kite.engine.event.applicationevents.ApplicationStoppedEvent;
import com.kite.engine.event.windowevents.WindowClosedEvent;
import com.kite.engine.event.windowevents.WindowResizeEvent;
import com.kite.engine.rendering.Renderer;

public class Application
{
    private static Application s_Instance;

    private Settings m_Settings;
    private boolean m_Running = true;
    private Window m_Window;
    private Scene m_Scene;
    private LayerStack m_LayerStack;

    private Application(Settings settings)
    {
        m_Settings = settings;
        Settings.ApplicationSettings appSettings = settings._ApplicationSettings;

        m_Window = new Window(appSettings.WindowWidth, appSettings.WindowHeight, appSettings.WindowTitle);
        m_Window.SetCallback(this::OnEvent);

        m_Scene = new Scene();
        m_LayerStack = new LayerStack();
    }

    public static void Create (Settings settings)
    {
        s_Instance = new Application(settings);
        s_Instance.Initialize();
    }

    public static void Create () { Create(new Settings()); }

    public static Application Get ()
    {
        return s_Instance;
    }

    public static void Run ()
    {
        s_Instance.sRun();
    }

    public void AddLayer (Layer layer)
    {
        m_LayerStack.AddLayer(layer);
    }

    public Settings GetSettings () { return m_Settings; }

    public void ReloadSettings ()
    {
        Renderer.ReloadSettings();
        m_Scene.ReloadSettings();
    }

    public Scene GetScene () { return m_Scene; }

    public Window GetWindow () { return m_Window; }

    private void Initialize ()
    {
        EventHandler.SetCallback(this::OnEvent);
        Time.Initialize();
        Renderer.Init();
        MousePicker.Initialize();
        ReloadSettings();

        EventHandler.PropagateEvent(new ApplicationInitializedEvent());
    }

    private void OnEvent (Event event)
    {
        Event.Dispatch(WindowClosedEvent.class, this::OnWindowCloseEvent, event);
        Event.Dispatch(WindowResizeEvent.class, this::OnWindowResizeEvent, event);

        m_Scene.OnEvent(event);
        m_LayerStack.PropagateEvent(event);

        EventHandler.Notify(event);
    }

    private void sRun ()
    {
        EventHandler.PropagateEvent(new ApplicationStartedEvent());

        m_Scene.Start();
        long lastFrameTime = Time.CurrentTime();
        while (m_Running)
        {
            long currentTime = Time.CurrentTime();
            long deltaTime = currentTime - lastFrameTime;
            lastFrameTime = currentTime;
            Time.s_DeltaTime = deltaTime;

            m_Window.Run();
            m_Scene.Run();
            m_LayerStack.Run();
        }
        m_Scene.End();

        EventHandler.PropagateEvent(new ApplicationStoppedEvent());
    }

    private boolean OnWindowCloseEvent (WindowClosedEvent event)
    {
        m_Running = false;
        return true;
    }

    private boolean OnWindowResizeEvent (WindowResizeEvent event)
    {
        int width = event.GetWidth();
        int height = event.GetHeight();

        m_Settings._ApplicationSettings.WindowWidth = width;
        m_Settings._ApplicationSettings.WindowHeight = height;

        Renderer.SetViewportSize(width, height);

        return false;
    }
}
