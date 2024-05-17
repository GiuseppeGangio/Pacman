package com.kite.engine.rendering.gizmo;

import com.kite.engine.rendering.Renderer;
import com.kite.engine.rendering.Texture;
import org.joml.Matrix4f;

import java.util.Vector;

public class GizmoRenderer
{
    private static final Vector<GizmoPrimitive> s_Primitives = new Vector<>();
    final static private Texture s_WhiteTexture = new Texture(1, 1, new int[] {0xffffffff});

    public static void Submit (GizmoPrimitive primitive)
    {
        s_Primitives.add(primitive);
    }

    public static void Flush (Matrix4f viewMatrix, Matrix4f projectionMatrix)
    {
        Renderer.StartScene(viewMatrix, projectionMatrix);

        for (GizmoPrimitive primitive : s_Primitives)
            RenderPrimitive(primitive);

        Renderer.EndScene();

        s_Primitives.clear();
    }

    private static void RenderPrimitive (GizmoPrimitive primitive)
    {
        GizmoPrimitive.Primitive primitiveType = primitive.GetPrimitive();

        switch (primitiveType)
        {
            case QUAD:
                Renderer.RenderQuad(primitive.GetTransform(), primitive.Color, s_WhiteTexture);
                break;
            case LINE:
                Renderer.RenderLine(primitive.GetTransform(), primitive.Width, primitive.Color);
                break;
            case VECTOR:
                for (GizmoPrimitive component : primitive.Components)
                    Renderer.RenderLine(component.GetTransform(), primitive.Width, primitive.Color);
                break;
        }
    }
}
