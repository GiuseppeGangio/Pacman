package com.kite.pacman.scripts;

import com.kite.engine.ecs.Entity;
import com.kite.engine.ecs.components.LabelComponent;
import com.kite.engine.ecs.components.ScriptComponent;
import com.kite.engine.ecs.components.TransformComponent;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ScoreScript extends ScriptComponent
{
    private static final int MAX_SCORE_DIGIT_COUNT = GetDigitCount(GameStateScript.MAX_SCORE);
    private static final Font s_Font;

    static
    {
        try {
            Font font = Font.createFonts(new File("assets/fonts/Retro_Gaming.ttf"))[0];
            s_Font = font.deriveFont(48.0f);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private GameStateScript m_GameStateScript;
    private int m_PreviousScore = 0;
    private Entity m_ScoreDigits;

    @Override
    public void OnAttach ()
    {
        Entity gameState = entity.GetScene().GetEntity("GameState");
        m_GameStateScript = gameState.GetComponent(GameStateScript.class);

        CreateScoreTextEntity();
        CreateScoreDigitsEntity();
        UpdateScore();
    }

    @Override
    public void OnUpdate ()
    {
        if (m_PreviousScore != m_GameStateScript.GetScore())
            UpdateScore();
    }

    public void UpdateScore ()
    {
        int score = m_GameStateScript.GetScore();
        m_PreviousScore = score;

        String scoreStr = Integer.toString(score);
        TransformComponent[] digits = m_ScoreDigits.GetComponent(TransformComponent.class).GetChildren();

        for (int i = 0; i < digits.length; i++)
        {
            Entity currentDigit = digits[i].Entity;
            LabelComponent label = currentDigit.GetComponent(LabelComponent.class);

            if (i < scoreStr.length())
            {
                char digitChar = scoreStr.charAt(i);
                label.Text = Character.toString(digitChar);
            }
            else
            {
                label.Text = " ";
            }
        }
    }

    private void CreateScoreTextEntity ()
    {
        Entity scoreText = entity.GetScene().CreateEntity("ScoreText");
        TransformComponent scoreTransform = scoreText.GetComponent(TransformComponent.class);
        scoreTransform.SetParent(entity.GetComponent(TransformComponent.class));
        scoreTransform.SetPosition(1, 1);
        scoreTransform.SetScale(2.5f, 1);
        LabelComponent labelComponent = scoreText.AddComponent(new LabelComponent());
        labelComponent.UsedFont = s_Font;
        labelComponent.Text = "Score: ";
    }

    private void CreateScoreDigitsEntity ()
    {
        m_ScoreDigits = entity.GetScene().CreateEntity("ScoreDigits");
        TransformComponent scoreTransform = m_ScoreDigits.GetComponent(TransformComponent.class);
        scoreTransform.SetParent(entity.GetComponent(TransformComponent.class));
        scoreTransform.SetPosition(2.7f, 0.9f);
        scoreTransform.SetScale(0.5f, 0.8f);

        for (int i = 0; i < MAX_SCORE_DIGIT_COUNT; i++)
        {
            Entity digit = entity.GetScene().CreateEntity("Digit_" + i);
            TransformComponent digitTransform = digit.GetComponent(TransformComponent.class);
            digitTransform.SetParent(m_ScoreDigits.GetComponent(TransformComponent.class));
            digitTransform.SetPosition(0.5f * i, 0);
            digitTransform.SetScale(1, 1);

            LabelComponent labelComponent = digit.AddComponent(new LabelComponent());
            labelComponent.UsedFont = s_Font;
            labelComponent.Text = "0";
        }
    }

    private static int GetDigitCount (int number) { return Integer.toString(number).length(); }
}
