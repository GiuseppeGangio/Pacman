package com.kite.pacman;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Common
{
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

    public static Font GetFont () { return s_Font; }
}
