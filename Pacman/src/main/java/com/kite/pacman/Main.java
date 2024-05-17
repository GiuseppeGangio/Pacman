package com.kite.pacman;

import com.kite.engine.core.Application;
import com.kite.engine.core.Settings;

public class Main
{
    public static void main (String[] args)
    {
        Settings settings = new Settings();
        Settings.ApplicationSettings applicationSettings = settings._ApplicationSettings;
        applicationSettings.WindowWidth = 1280;
        applicationSettings.WindowHeight = 720;
        applicationSettings.WindowTitle = "Pacman";

        Application.Create(settings);
        Application.Get().AddLayer(new GameLayer());
        Application.Run();
    }
}
