package com.kite.pacman.scripts;

import com.kite.engine.core.Time;
import com.kite.engine.ecs.components.ScriptComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class GameStateScript extends ScriptComponent
{
    public enum GameAction
    {
        GAME_INITIALIZED,
        GAME_STARTED,
        CHASE_STARTED,
        GAME_PAUSE_START,
        GAME_PAUSE_STOP,
        GAME_ENDED,
        PLAYER_KILLED,
        ENERGIZER_EFFECT_START,
        ENERGIZER_EFFECT_END,
        SCORE_INCREASED,
        SCORE_RESET,
        GAME_OVER
    }

    private static final String GAME_CHRONOMETER = "GAME_STARTED_CHRONOMETER";
    private static final String GAME_COUNTDOWN_TIMER = "GAME_COUNTDOWN_TIMER";
    private static final String GAME_PAUSE_COUNTDOWN_TIMER = "GAME_PAUSE_COUNTDOWN_TIMER";
    private static final String PLAYER_PREPARATION_TIMER = "PLAYER_PREPARATION_TIMER";
    private static final String ENERGIZER_EFFECT_COUNTDOWN = "ENERGIZER_EFFECT_COUNTDOWN";

    private static final double GAME_COUNTDOWN_TIME_SECONDS = 3d;
    private static final double PLAYER_PREPARATION_TIME_SECONDS = 10d;
    private static final double ENERGIZER_EFFECT_COUNTDOWN_TIME_SECONDS = 10d;
    public static final int MAX_SCORE = 999999999;

    private final HashMap<GameAction, ArrayList<Consumer<Void>>> m_Subscription = new HashMap<>();
    private double m_SecondsFromStart = -1f;
    private int m_Lives = 3;
    private int m_Score = 0;

    public void Subscribe (GameAction gameAction, Consumer<Void> callback)
    {
        if (!m_Subscription.containsKey(gameAction))
            m_Subscription.put(gameAction, new ArrayList<>());

        ArrayList<Consumer<Void>> list = m_Subscription.get(gameAction);
        list.add(callback);
    }

    public void InitializeGame ()
    {
        Time.StartTimer(GAME_COUNTDOWN_TIMER, (long) (GAME_COUNTDOWN_TIME_SECONDS * 1000));

        NotifyCallbacks(GameAction.GAME_INITIALIZED);
    }

    public void StartGame ()
    {
        m_SecondsFromStart = 0;
        Time.StartChronometer(GAME_CHRONOMETER);

        Time.StartTimer(PLAYER_PREPARATION_TIMER, (long) (PLAYER_PREPARATION_TIME_SECONDS * 1000));

        NotifyCallbacks(GameAction.GAME_STARTED);
    }

    public void StartChase ()
    {
        NotifyCallbacks(GameAction.CHASE_STARTED);
    }

    public void KillPlayer ()
    {
        if (m_Lives == 0)
        {
            GameOver();
            return;
        }

        m_Lives--;

        NotifyCallbacks(GameAction.PLAYER_KILLED);
        PauseGame(3);
    }

    public void EnergizeEaten ()
    {
        NotifyCallbacks(GameAction.ENERGIZER_EFFECT_START);
        Time.StartTimer(ENERGIZER_EFFECT_COUNTDOWN, (long) (ENERGIZER_EFFECT_COUNTDOWN_TIME_SECONDS * 1000));
    }

    public void IncreaseScore (int quantity)
    {
        if (m_Score + quantity <= MAX_SCORE)
        {
            m_Score += quantity;
            NotifyCallbacks(GameAction.SCORE_INCREASED);
        }
    }

    public void ResetScore () { m_Score = 0; NotifyCallbacks(GameAction.SCORE_RESET); }

    public int GetScore () { return m_Score; }

    public void GameOver ()
    {
        NotifyCallbacks(GameAction.GAME_OVER);
        EndGame();
    }

    public void EndGame ()
    {
        Time.StopChronometer(GAME_CHRONOMETER);
        NotifyCallbacks(GameAction.GAME_ENDED);
    }

    public boolean HasChaseStarted () { return Time.GetChronometerSeconds(GAME_CHRONOMETER) >= PLAYER_PREPARATION_TIME_SECONDS; }

    public int PlayerLives () { return m_Lives; }

    @Override
    public void OnUpdate ()
    {
        if (m_SecondsFromStart >= 0)
            m_SecondsFromStart = Time.GetChronometerSeconds(GAME_CHRONOMETER);

        if (Time.HasTimerFinished(GAME_COUNTDOWN_TIMER))
            StartGame();

        if (Time.HasTimerFinished(PLAYER_PREPARATION_TIMER))
            StartChase();

        if (Time.HasTimerFinished(GAME_PAUSE_COUNTDOWN_TIMER))
            UnpauseGame();

        if (Time.HasTimerFinished(ENERGIZER_EFFECT_COUNTDOWN))
            NotifyCallbacks(GameAction.ENERGIZER_EFFECT_END);
    }

    private void PauseGame (double seconds)
    {
        Time.StartTimer(GAME_PAUSE_COUNTDOWN_TIMER, (long) (seconds * 1000));
        NotifyCallbacks(GameAction.GAME_PAUSE_START);
    }

    private void UnpauseGame ()
    {
        NotifyCallbacks(GameAction.GAME_PAUSE_STOP);
        StartGame();
    }

    private void NotifyCallbacks (GameAction gameAction)
    {
        ArrayList<Consumer<Void>> callbacks = m_Subscription.get(gameAction);
        if (callbacks != null)
            for (Consumer<Void> callback : callbacks)
                callback.accept(null);
    }
}
