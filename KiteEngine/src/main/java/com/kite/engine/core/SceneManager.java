package com.kite.engine.core;

import com.kite.engine.event.Event;

import java.util.HashMap;

public class SceneManager
{
    private final HashMap<String, Scene> m_Scenes = new HashMap<>();
    private String m_CurrentScene = "";
    private String m_RequestedSceneChange = "";

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
        if (id.equals(m_CurrentScene))
            return true;

        if (m_Scenes.containsKey(id))
        {
            m_RequestedSceneChange = id;
            return true;
        }

        return false;
    }

    public boolean IsEmpty () { return m_CurrentScene.isEmpty(); }

    public Scene GetCurrentScene ()
    {
        return m_Scenes.get(m_CurrentScene);
    }

    void Start ()
    {
        if (!m_CurrentScene.isEmpty())
            m_Scenes.get(m_CurrentScene).Start();
    }

    void End ()
    {
        if (!m_CurrentScene.isEmpty())
            m_Scenes.get(m_CurrentScene).End();
    }

    void Run ()
    {
        if (!m_CurrentScene.isEmpty())
        {
            m_Scenes.get(m_CurrentScene).Run();

            if (!m_RequestedSceneChange.isEmpty())
            {
                m_CurrentScene = m_RequestedSceneChange;
                m_RequestedSceneChange = "";

                m_Scenes.get(m_CurrentScene).Start();
            }
        }

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
