package com.kite.engine.core;

import com.kite.engine.event.Event;
import com.kite.engine.event.keyevents.*;
import com.kite.engine.event.mouseEvents.*;
import com.kite.engine.event.windowevents.*;
import org.lwjgl.glfw.*;

import java.util.function.Consumer;
import static org.lwjgl.glfw.GLFW.*;

public class Window
{
    private static class WindowData
    {
        public String Title;
        public int Width, Height;
        public Consumer<Event> CallbackFunction;
    }

    private long m_Handle;
    private WindowData m_Data;

    protected Window (int width, int height, String title)
    {
        Init(width, height, title);
    }

    public long GetHandle () { return m_Handle; }

    public int GetWidth () { return m_Data.Width; }
    public int GetHeight () { return m_Data.Height; }

    protected void Run ()
    {
        glfwSwapBuffers(m_Handle); // swap the color buffers
        glfwPollEvents();
    }

    protected boolean ShouldClose () { return glfwWindowShouldClose(m_Handle); }

    protected void SetCallback (Consumer<Event> callback)
    {
        m_Data.CallbackFunction = callback;
    }

    private void Init(int width, int height, String title)
    {
        m_Data = new WindowData();
        m_Data.Width = width;
        m_Data.Height = height;
        m_Data.Title = title;

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Create the window
        m_Handle = glfwCreateWindow(width, height, title, 0, 0);
        if ( m_Handle == 0 )
            throw new RuntimeException("Failed to create the GLFW window");

        // Make the OpenGL context current
        glfwMakeContextCurrent(m_Handle);

        // Enable v-sync
        glfwSwapInterval(1);

        glfwSetKeyCallback(m_Handle, new GLFWKeyCallback()
        {
            @Override
            public void invoke (long window, int key, int scancode, int action, int mods)
            {
                switch (action)
                {
                    case GLFW_PRESS:
                    {
                        KeyPressedEvent event = new KeyPressedEvent(key, false);
                        m_Data.CallbackFunction.accept(event);
                        break;
                    }

                    case GLFW_RELEASE:
                    {
                        KeyReleasedEvent event = new KeyReleasedEvent(key);
                        m_Data.CallbackFunction.accept(event);
                        break;
                    }

                    case GLFW_REPEAT:
                    {
                        KeyPressedEvent event = new KeyPressedEvent(key, true);
                        m_Data.CallbackFunction.accept(event);
                        break;
                    }
                }
            }
        });

        glfwSetCursorPosCallback(m_Handle, new GLFWCursorPosCallback()
        {
            @Override
            public void invoke (long window, double xpos, double ypos)
            {
                MouseMovedEvent event = new MouseMovedEvent((float) xpos, (float)ypos);
                m_Data.CallbackFunction.accept(event);
            }
        });

        glfwSetMouseButtonCallback(m_Handle, new GLFWMouseButtonCallback()
        {
            @Override
            public void invoke (long window, int button, int action, int mods)
            {
                switch (action)
                {
                    case GLFW_PRESS:
                    {
                        MouseButtonPressedEvent event = new MouseButtonPressedEvent(button);
                        m_Data.CallbackFunction.accept(event);
                        break;
                    }

                    case GLFW_RELEASE:
                    {
                        MouseButtonReleasedEvent event = new MouseButtonReleasedEvent(button);
                        m_Data.CallbackFunction.accept(event);
                        break;
                    }
                }
            }
        });

        glfwSetScrollCallback(m_Handle, new GLFWScrollCallback()
        {
            @Override
            public void invoke (long window, double xoffset, double yoffset)
            {
                MouseScrolledEvent event = new MouseScrolledEvent((float)yoffset);
                m_Data.CallbackFunction.accept(event);
            }
        });

        glfwSetWindowSizeCallback(m_Handle, new GLFWWindowSizeCallback()
        {
            @Override
            public void invoke (long window, int width, int height)
            {
                WindowResizeEvent event = new WindowResizeEvent(width, height);
                m_Data.Width = width;
                m_Data.Height = height;
                m_Data.CallbackFunction.accept(event);
            }
        });

        glfwSetWindowCloseCallback(m_Handle, new GLFWWindowCloseCallback()
        {
            @Override
            public void invoke (long window)
            {
                WindowClosedEvent event = new WindowClosedEvent();
                m_Data.CallbackFunction.accept(event);
            }
        });
    }
}
