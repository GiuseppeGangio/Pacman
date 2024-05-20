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
    private static final int MAX_SCORE = 9999;
    private static final int MAX_SCORE_DIGIT_COUNT = GetDigitCount(MAX_SCORE);

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

    private int m_Score = 0;

    private Entity m_ScoreText;
    private Entity m_ScoreDigits;

    @Override
    public void OnAttach ()
    {
        CreateScoreTextEntity();
        CreateScoreDigitsEntity();
        SetScore(0);
    }

    public void SetScore (int score)
    {
        m_Score = Math.min(score, MAX_SCORE);

        String scoreStr = Integer.toString(m_Score);
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

    public void AddScore (int score)
    {
        SetScore(m_Score + score);
    }

    private void CreateScoreTextEntity ()
    {
        m_ScoreText = entity.GetScene().CreateEntity("ScoreText");
        TransformComponent scoreTransform = m_ScoreText.GetComponent(TransformComponent.class);
        scoreTransform.SetParent(entity.GetComponent(TransformComponent.class));
        scoreTransform.SetPosition(1, 1);
        scoreTransform.SetScale(2.5f, 1);
        LabelComponent labelComponent = m_ScoreText.AddComponent(new LabelComponent());
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
