package com.kite.engine.core;

import com.kite.engine.event.EventHandler;
import com.kite.engine.event.EventType;
import com.kite.engine.event.applicationevents.ApplicationInitializedEvent;
import com.kite.engine.event.windowevents.WindowResizeEvent;
import com.kite.engine.rendering.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;

public class MousePicker
{
    private static int s_ScreenWidth = 1, s_ScreenHeight = 1;
    private static final FrameBuffer s_FrameBuffer = new FrameBuffer();

    protected static void Initialize ()
    {
        EventHandler.Subscribe(EventType.WINDOW_RESIZED, MousePicker::OnWindowResizeEvent);
        EventHandler.Subscribe(EventType.APPLICATION_INITIALIZED, MousePicker::OnApplicationInitializedEvent);
    }

    protected static int MousePick (int mouseX, int mouseY) // From upper left corner, returns id
    {
        if (mouseX < 0 || mouseX > s_ScreenWidth ||
                mouseY < 0 || mouseY > s_ScreenHeight)
            return 0x00000000; // valid ids have alpha = 255

        float[] buffer = new float[4];

        s_FrameBuffer.Bind();
        glReadPixels(mouseX, s_ScreenHeight - mouseY, 1, 1, GL_RGBA, GL_FLOAT, buffer);
        s_FrameBuffer.Unbind();

        int id = Utils.RGBAColorToInteger(buffer[0], buffer[1], buffer[2], buffer[3]);

        return id;
    }

    protected static FrameBuffer GetFrameBuffer () { return s_FrameBuffer; }

    private static void RegenerateTexture ()
    {
        Texture tex = new Texture(s_ScreenWidth, s_ScreenHeight);

        s_FrameBuffer.Bind();
        s_FrameBuffer.AttachTexture(tex, GL_COLOR_ATTACHMENT0);
        s_FrameBuffer.Unbind();
    }

    private static void OnApplicationInitializedEvent (boolean isTest, ApplicationInitializedEvent event)
    {
        if (!isTest)
        {
            Window windowRef = Application.Get().GetWindow();

            s_ScreenWidth = windowRef.GetWidth();
            s_ScreenHeight = windowRef.GetHeight();
            RegenerateTexture();
        }
    }

    private static void OnWindowResizeEvent (boolean isTest, WindowResizeEvent event)
    {
        if (!isTest)
        {
            s_ScreenWidth = event.GetWidth();
            s_ScreenHeight = event.GetHeight();
            RegenerateTexture();
        }
    }
}
