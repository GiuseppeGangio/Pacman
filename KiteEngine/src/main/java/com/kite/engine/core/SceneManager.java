package com.kite.engine.core;

import com.kite.engine.event.Event;

import java.util.HashMap;

public class SceneManager
{
    private final HashMap<String, Scene> m_Scenes = new HashMap<>();
    private String m_CurrentScene = "";

    public Scene CreateEmptyScene (String id)
    {
        if (id.isEmpty())
            throw new Error("Empty string is considered an invalid id");

        Scene scene = new Scene();
        m_Scenes.put(id, scene);

        if (m_CurrentScene.isEmpty())
            m_CurrentScene = id;

        return scene;
    }

    public boolean SelectScene (String id)
    {
        if (m_Scenes.containsKey(id))
        {
            m_CurrentScene = id;
            return true;
        }

        return false;
    }

    public boolean IsEmpty () { return m_CurrentScene.isEmpty(); }

    public Scene GetCurrentScene ()
    {
        return m_Scenes.get(m_CurrentScene);
    }

    void OnEvent (Event e)
    {
        m_Scenes.forEach((String id, Scene scene)->
                scene.OnEvent(e));
    }

    void ReloadSettings ()
    {
        m_Scenes.forEach((String id, Scene scene)->
                scene.ReloadSettings());
    }
}
