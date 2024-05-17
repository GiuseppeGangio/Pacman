package com.kite.engine.core;

import org.joml.Vector4f;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utils
{
    public static String ReadFile (String path)
    {
        // TODO: Better error catching
        try
        {
            byte[] bytes = Files.readAllBytes(Paths.get(path));
            return new String(bytes);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage ImportImage(String path)
    {
        BufferedImage img = null;

        // TODO: Better error catching
        try
        {
            img = ImageIO.read(new File(path));
        }
        catch (IOException ignored)
        {

        }

        return img;
    }

    public static BufferedImage FlipImageVertically(BufferedImage image)
    {
        AffineTransform at = new AffineTransform();
        at.concatenate(AffineTransform.getScaleInstance(1, -1));
        at.concatenate(AffineTransform.getTranslateInstance(0, -image.getHeight()));

        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(image, null);
    }

    public static IntBuffer ConvertImageToIntBuffer(BufferedImage bi)
    {
        int width = bi.getWidth();
        int height = bi.getHeight();

        int[] pixels = new int[width * height];
        bi.getRGB(0, 0, width, height, pixels, 0, width);

        int[] data = new int[width * height];
        for (int i = 0; i < width * height; i++)
        {
            int a = (pixels[i] & 0xff000000) >> 24;
            int r = (pixels[i] & 0xff0000) >> 16;
            int g = (pixels[i] & 0xff00) >> 8;
            int b = (pixels[i] & 0xff);

            data[i] = a << 24 | b << 16 | g << 8 | r;
        }

        IntBuffer buffer = ByteBuffer.allocateDirect(data.length * 4)
                .order(ByteOrder.nativeOrder()).asIntBuffer();
        buffer.put(data).flip();

        return buffer;
    }

    public static Vector4f IntegerToRGBAColor (int color)
    {
        int r = (color & 0xff000000) >>> 24;
        int g = (color & 0xff0000) >>> 16;
        int b = (color & 0xff00) >>> 8;
        int a = (color & 0xff);

        return new Vector4f(
                (float) r / 255f,
                (float) g / 255f,
                (float) b / 255f,
                (float) a / 255f
        );
    }

    public static int RGBAColorToInteger (float r, float g, float b, float a)
    {
        int r_int = Math.round(r * 255f);
        int g_int = Math.round(g * 255f);
        int b_int = Math.round(b * 255f);
        int a_int = Math.round(a * 255f);

        return r_int << 24 |
               g_int << 16 |
               b_int << 8  |
               a_int;
    }
}
