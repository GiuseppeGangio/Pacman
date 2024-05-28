package com.kite.engine.core;

import com.kite.engine.event.EventHandler;
import com.kite.engine.event.EventType;
import com.kite.engine.event.applicationevents.ApplicationStartedEvent;

import java.util.Hashtable;

public class Time
{
    private static class Timer
    {
        public long StartTime;
        public long Duration;
    }

    protected static long s_DeltaTime = 0;
    private static long s_StartTime = 0; // The time at which the application is started

    private static final Hashtable<String, Timer> s_Timers = new Hashtable<>();
    private static final Hashtable<String, Long> s_Chronometers = new Hashtable<>();

    protected static void Initialize ()
    {
        EventHandler.Subscribe(EventType.APPLICATION_STARTED, Time::OnApplicationStart);
    }

    public static long ElapsedTime () { return System.currentTimeMillis() - s_StartTime; }
    public static long CurrentTime () { return System.currentTimeMillis(); }
    public static long DeltaTime () { return s_DeltaTime; }
    public static int FrameRate () { return (int) (1000d / s_DeltaTime); }

    public static void StartTimer (String timerName, long durationMillis)
    {
        Timer timer = new Timer();
        timer.Duration = durationMillis;
        timer.StartTime = System.currentTimeMillis();
        s_Timers.put(timerName, timer);
    }

    public static boolean IsTimerValid (String timerName)
    {
        Timer timer = s_Timers.get(timerName);
        return timer != null;
    }

    public static boolean HasTimerFinished (String timerName)
    {
        Timer timer = s_Timers.get(timerName);

        if (timer != null && System.currentTimeMillis() - timer.StartTime >= timer.Duration)
        {
            s_Timers.remove(timerName);
            return true;
        }

        return false;
    }

    public static void StartChronometer (String chronometerName)
    {
        long startTime = System.currentTimeMillis();
        s_Chronometers.put(chronometerName, startTime);
    }

    public static long GetChronometer (String chronometerName)
    {
        Long startTime = s_Chronometers.get(chronometerName);

        if (startTime != null)
            return System.currentTimeMillis() - startTime;

        return 0;
    }

    public static double GetChronometerSeconds (String chronometerName)
    {
        return GetChronometer(chronometerName) / 1000d;
    }

    public static long StopChronometer (String chronometerName)
    {
        long time = GetChronometer(chronometerName);
        s_Chronometers.remove(chronometerName);

        return time;
    }

    private static void OnApplicationStart (boolean isTest, ApplicationStartedEvent event)
    {
        if (!isTest)
            s_StartTime = System.currentTimeMillis();
    }
}
