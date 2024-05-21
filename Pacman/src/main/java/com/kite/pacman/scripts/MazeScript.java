package com.kite.pacman.scripts;

import com.kite.engine.core.Scene;
import com.kite.engine.ecs.Entity;
import com.kite.engine.ecs.components.*;
import com.kite.engine.rendering.Texture;
import com.kite.engine.rendering.gizmo.GizmoPrimitive;
import com.kite.engine.rendering.gizmo.GizmoRenderer;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;

public class MazeScript extends ScriptComponent
{
    public static final int MAZE_WIDTH = 29;
    public static final int MAZE_HEIGHT = 32;
    public static final float MAZE_SCALE = 0.5f;
    private static final int[] MAZE = new int[] {
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1,
            1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1,
            0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0,
            1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1,
            0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0,
            1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1,
            1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1,
            1, 1, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
    };

    private static final int FOOD_WIDTH = 26;
    private static final int FOOD_HEIGHT = 29;

    private static final int[] FOOD = new int[] {
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1,
            1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1,
            0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1,
            1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1,
            0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0,
            0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0,
            1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
    };

    private static final int PATH_NODES_WIDTH = 28;
    private static final int PATH_NODES_HEIGHT = 29;

    private static final int[] PATH_NODES = new int[] {
            0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
            0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0,
            0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0,
            0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0,
            0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
            0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0,
            0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0,
            0, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0,
            0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0,
            0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
            0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0,
            0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0,
            0, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 0,
            0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0,
            0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0,
            0, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0,
            0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0,
            0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0,
            0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
    };

    private enum Wall
    {
        WALL_ONE_UP, WALL_ONE_DOWN, WALL_ONE_LEFT, WALL_ONE_RIGHT,
        WALL_TWO_HORIZONTAL, WALL_TWO_VERTICAL,
        WALL_THREE_UP, WALL_THREE_DOWN, WALL_THREE_LEFT, WALL_THREE_RIGHT,
        WALL_ANGLE_TOP_LEFT, WALL_ANGLE_TOP_RIGHT, WALL_ANGLE_BOTTOM_LEFT, WALL_ANGLE_BOTTOM_RIGHT,
    }

    public static final float PLAYER_SPAWN_OFFSET_X = 14 * MAZE_SCALE;
    public static final float PLAYER_SPAWN_OFFSET_Y = 23.5f * MAZE_SCALE;
    public static final float PLAYER_SCALE = MAZE_SCALE * 2f;

    public static final float LEFT_TELEPORT_LOCATION_X = -2 * MAZE_SCALE;
    public static final float LEFT_TELEPORT_LOCATION_Y = 14.5f * MAZE_SCALE;

    public static final float RIGHT_TELEPORT_LOCATION_X = 30 * MAZE_SCALE;
    public static final float RIGHT_TELEPORT_LOCATION_Y = 14.5f * MAZE_SCALE;

    public static final float TELEPORT_SCALE = PLAYER_SCALE;

    public static final float BLINKY_SPAWN_LOCATION_X = 14 * MAZE_SCALE;
    public static final float BLINKY_SPAWN_LOCATION_Y =  11.5f * MAZE_SCALE;

    private final static Texture wallTexture1 = new Texture("assets/textures/wall1.png");
    private final static Texture wallTexture2 = new Texture("assets/textures/wall2.png");
    private final static Texture wallTexture3 = new Texture("assets/textures/wall3.png");
    private final static Texture wallTextureAngle = new Texture("assets/textures/wall_angle.png");

    private TransformComponent m_Transform;
    private Scene m_SceneRef;

    @Override
    public void OnAttach ()
    {
        m_Transform = entity.GetComponent(TransformComponent.class);
        m_SceneRef = entity.GetScene();
        CreateMaze();
        AddFood();

        AddTeleport(LEFT_TELEPORT_LOCATION_X, LEFT_TELEPORT_LOCATION_Y, RIGHT_TELEPORT_LOCATION_X - TELEPORT_SCALE, RIGHT_TELEPORT_LOCATION_Y);
        AddTeleport(RIGHT_TELEPORT_LOCATION_X, RIGHT_TELEPORT_LOCATION_Y, LEFT_TELEPORT_LOCATION_X + TELEPORT_SCALE, LEFT_TELEPORT_LOCATION_Y);

        SpawnPlayer();
        SpawnGhosts();
    }

    public ArrayList<Vector2f> PathFind (Vector2f startPos, Vector2f endPos)
    {
        int startPosIndex = GetNearestWalkableNodeIndex(startPos);
        int endPosIndex = GetNearestWalkableNodeIndex(endPos);

        ArrayList<Integer> path = PathFinder.AStar(PATH_NODES, PATH_NODES_WIDTH, PATH_NODES_HEIGHT, startPosIndex, endPosIndex);

        ArrayList<Vector2f> directions = new ArrayList<>();

        if (!path.isEmpty())
        {
            for (int i = path.size() - 1; i > 0; i--)
            {
                directions.add(IndicesToDirection(path.get(i), path.get(i - 1)));
            }
        }
        return directions;
    }

    private void SpawnGhosts ()
    {
        Entity Blinky = m_SceneRef.CreateEntity("blinky");
        TransformComponent blinkyTransform = Blinky.GetComponent(TransformComponent.class);
        blinkyTransform.SetParent(m_Transform);
        blinkyTransform.SetPosition(BLINKY_SPAWN_LOCATION_X, -BLINKY_SPAWN_LOCATION_Y);
        blinkyTransform.SetScale(PLAYER_SCALE, PLAYER_SCALE);
        RigidBodyComponent rigidBody = Blinky.AddComponent(new RigidBodyComponent());
        rigidBody.Type = RigidBodyComponent.BodyType.ROTATIONAL_STATIC;
        ColliderComponent collider = Blinky.AddComponent(new ColliderComponent());
        collider.Traversable = true;
        Blinky.AddComponent(new SpriteComponent());
        Blinky.AddComponent(new BlinkyScript());
    }

    private void SpawnPlayer ()
    {
        Entity player = m_SceneRef.CreateEntity("player");

        TransformComponent playerTransform = player.GetComponent(TransformComponent.class);
        playerTransform.SetParent(m_Transform);
        playerTransform.SetScale(PLAYER_SCALE, PLAYER_SCALE);
        playerTransform.SetPosition(PLAYER_SPAWN_OFFSET_X, -PLAYER_SPAWN_OFFSET_Y);

        RigidBodyComponent rbc = player.AddComponent(new RigidBodyComponent());
        player.AddComponent(new ColliderComponent());
        rbc.Type = RigidBodyComponent.BodyType.ROTATIONAL_STATIC;

        player.AddComponent(new SpriteComponent());
        player.AddComponent(new PlayerScript());
    }

    private void AddFood ()
    {
        final float offset = 1.5f;

        for (int y = 0; y < FOOD_HEIGHT; y++)
        {
            for (int x = 0; x < FOOD_WIDTH; x++)
            {
                int index = x + y * FOOD_WIDTH;

                if (FOOD[index] == 1)
                    SpawnFood(offset + x, offset + y);
            }
        }
    }

    private void CreateMaze ()
    {
        for (int y = 0; y < MAZE_HEIGHT; y++)
        {
            for (int x = 0; x < MAZE_WIDTH; x++)
            {
                int index = x + y * MAZE_WIDTH;
                int wall = MAZE[index];

                if (wall == 1)
                {
                    Wall wall_type = Wall.WALL_ONE_UP;
                    int up = -1;
                    int down = -1;
                    int left = -1;
                    int right = -1;

                    if (y > 0)
                        up = MAZE[x + (y - 1) * MAZE_WIDTH];

                    if (y < MAZE_HEIGHT - 1)
                        down =  MAZE[x + (y + 1) * MAZE_WIDTH];

                    if (x > 0)
                        left = MAZE[(x - 1) + y * MAZE_WIDTH];

                    if (x < MAZE_WIDTH - 1)
                        right = MAZE[(x + 1) + y * MAZE_WIDTH];

                    int count = 0;

                    if (up == 1)
                        count++;

                    if (down == 1)
                        count++;

                    if (left == 1)
                        count++;

                    if (right == 1)
                        count++;

                    if (count == 1)
                    {
                        if (up == 1)
                            wall_type = Wall.WALL_ONE_UP;
                        else if (down == 1)
                            wall_type = Wall.WALL_ONE_DOWN;
                        else if (left == 1)
                            wall_type = Wall.WALL_ONE_LEFT;
                        else if (right == 1)
                            wall_type = Wall.WALL_ONE_RIGHT;
                    }

                    if (count == 2)
                    {
                        if (left == 1 && right == 1)
                            wall_type = Wall.WALL_TWO_HORIZONTAL;
                        else if (up == 1 && down == 1)
                            wall_type = Wall.WALL_TWO_VERTICAL;
                        else if (up == 1 && right == 1)
                            wall_type = Wall.WALL_ANGLE_BOTTOM_LEFT;
                        else if (up == 1 && left == 1)
                            wall_type = Wall.WALL_ANGLE_BOTTOM_RIGHT;
                        else if (down == 1 && right == 1)
                            wall_type = Wall.WALL_ANGLE_TOP_LEFT;
                        else if (down == 1 && left == 1)
                            wall_type = Wall.WALL_ANGLE_TOP_RIGHT;
                    }

                    if (count == 3)
                    {
                        if (up != 1)
                            wall_type = Wall.WALL_THREE_UP;
                        else if (down != 1)
                            wall_type = Wall.WALL_THREE_DOWN;
                        else if (left != 1)
                            wall_type = Wall.WALL_THREE_LEFT;
                        else if (right != 1)
                            wall_type = Wall.WALL_THREE_RIGHT;
                    }

                    CreateWall(x, y, wall_type);
                }

            }
        }
    }

    private void CreateWall (int x, int y, Wall wallType)
    {
        Entity wall = m_SceneRef.CreateEntity("wall");

        TransformComponent wallTransform = wall.GetComponent(TransformComponent.class);
        wallTransform.SetParent(m_Transform);
        wallTransform.SetScale(MAZE_SCALE, MAZE_SCALE);
        wallTransform.SetPosition(x * MAZE_SCALE, -y * MAZE_SCALE);

        RigidBodyComponent rbc = wall.AddComponent(new RigidBodyComponent());
        ColliderComponent collider = wall.AddComponent(new ColliderComponent());
        rbc.Type = RigidBodyComponent.BodyType.STATIC;
        collider.Size = new Vector2f(0.5f, 0.5f);

        SpriteComponent wallSprite = wall.AddComponent(new SpriteComponent());

        switch (wallType)
        {
            case WALL_ONE_UP ->
            {
                wallSprite.Sprite.Texture = wallTexture1;
                wallTransform.SetRotation(-90);
            }
            case WALL_ONE_DOWN ->
            {
                wallSprite.Sprite.Texture = wallTexture1;
                wallTransform.SetRotation(90);
            }
            case WALL_ONE_LEFT ->
            {
                wallSprite.Sprite.Texture = wallTexture1;
            }
            case WALL_ONE_RIGHT ->
            {
                wallSprite.Sprite.Texture = wallTexture1;
                wallTransform.SetRotation(180);
            }
            case WALL_TWO_HORIZONTAL ->
            {
                wallSprite.Sprite.Texture = wallTexture2;
            }
            case WALL_TWO_VERTICAL ->
            {
                wallTransform.SetRotation(90);
                wallSprite.Sprite.Texture = wallTexture2;
            }
            case WALL_THREE_UP ->
            {
                wallTransform.SetRotation(180);
                wallSprite.Sprite.Texture = wallTexture3;
            }
            case WALL_THREE_DOWN ->
            {
                wallSprite.Sprite.Texture = wallTexture3;
            }
            case WALL_THREE_LEFT ->
            {
                wallTransform.SetRotation(-90);
                wallSprite.Sprite.Texture = wallTexture3;
            }
            case WALL_THREE_RIGHT ->
            {
                wallTransform.SetRotation(90);
                wallSprite.Sprite.Texture = wallTexture3;
            }
            case WALL_ANGLE_TOP_LEFT ->
            {
                wallSprite.Sprite.Texture = wallTextureAngle;
            }
            case WALL_ANGLE_TOP_RIGHT ->
            {
                wallTransform.SetRotation(-90);
                wallSprite.Sprite.Texture = wallTextureAngle;
            }
            case WALL_ANGLE_BOTTOM_LEFT ->
            {
                wallTransform.SetRotation(90);
                wallSprite.Sprite.Texture = wallTextureAngle;
            }
            case WALL_ANGLE_BOTTOM_RIGHT ->
            {
                wallTransform.SetRotation(180);
                wallSprite.Sprite.Texture = wallTextureAngle;
            }
        }
    }

    private void SpawnFood (float x, float y)
    {
        final float scale = 0.2f;

        Entity food = m_SceneRef.CreateEntity("food");

        TransformComponent foodTransform = food.GetComponent(TransformComponent.class);
        foodTransform.SetParent(m_Transform);
        foodTransform.SetScale(MAZE_SCALE * scale, MAZE_SCALE * scale);
        foodTransform.SetPosition(MAZE_SCALE * x,  MAZE_SCALE * -y);

        RigidBodyComponent rbc = food.AddComponent(new RigidBodyComponent());
        ColliderComponent collider = food.AddComponent(new ColliderComponent());
        rbc.Type = RigidBodyComponent.BodyType.STATIC;
        collider.Traversable = true;

        SpriteComponent foodSprite = food.AddComponent(new SpriteComponent());
        foodSprite.Sprite.Color = new Vector4f(1.0f, 0.7216f, 0.6f, 1.0f);

        food.AddComponent(new FoodScript());
    }

    private void AddTeleport (float posx, float posy, float tpx, float tpy)
    {
        Entity teleport = m_SceneRef.CreateEntity("teleport");

        TransformComponent teleportTransform = teleport.GetComponent(TransformComponent.class);
        teleportTransform.SetParent(m_Transform);
        teleportTransform.SetPosition(posx,  -posy);
        teleportTransform.SetScale(TELEPORT_SCALE, TELEPORT_SCALE);

        RigidBodyComponent rbc = teleport.AddComponent(new RigidBodyComponent());
        ColliderComponent collider = teleport.AddComponent(new ColliderComponent());
        rbc.Type = RigidBodyComponent.BodyType.STATIC;
        collider.Traversable = true;

        TeleportScript teleportScript = new TeleportScript();
        teleportScript.setTeleportLocation(tpx, -tpy);
        teleport.AddComponent(teleportScript);
    }

    private int GetNearestWalkableNodeIndex (Vector2f position)
    {
        int index = PositionToPathIndex(position);

        if (PATH_NODES[index] == 1)
            return index;

        int[] neighbors = GetNodeNeighbors(index);

        for (int nIndex : neighbors)
        {
            if (nIndex != -1 && PATH_NODES[nIndex] == 1)
                return nIndex;
        }

        return index;
    }

    private int[] GetNodeNeighbors (int nodeIndex)
    {
        int originX = nodeIndex % PATH_NODES_WIDTH;
        int originY = (nodeIndex - originX) / PATH_NODES_WIDTH;

        int top = -1;
        int bottom = -1;
        int left = -1;
        int right = -1;

        if (originX > 0)
            left = nodeIndex - 1;

        if (originX < PATH_NODES_WIDTH - 1)
            right = nodeIndex + 1;

        if (originY > 0)
            top = nodeIndex - PATH_NODES_WIDTH;

        if (originY < PATH_NODES_HEIGHT - 1)
            bottom = nodeIndex + PATH_NODES_WIDTH;

        return new int[] {top, bottom, left, right};
    }

    private int PositionToPathIndex (Vector2f position)
    {
        int x = (int) ((position.x / (MAZE_SCALE)) - 0.5f);
        int y = (int) ((-position.y / (MAZE_SCALE)) - 1.5f);

        return x + y * PATH_NODES_WIDTH;
    }

    private Vector2f IndicesToDirection (int indexA, int indexB)
    {
        if (indexB == indexA + 1)
            return new Vector2f(1, 0);

        if (indexB == indexA - 1)
            return new Vector2f(-1, 0);

        if (indexB == indexA - PATH_NODES_WIDTH)
            return new Vector2f(0, 1);

        if (indexB == indexA + PATH_NODES_WIDTH)
            return new Vector2f(0, -1);

        return new Vector2f();
    }

    @Override
    public void OnUpdate ()
    {
        final float offset = 1.5f;

        for (int x = 0; x < PATH_NODES_WIDTH; x++)
        {
            for (int y = 0; y < PATH_NODES_HEIGHT; y++)
            {
                int index = x + y * PATH_NODES_WIDTH;
                if (PATH_NODES[index] == 1)
                {
                    float posx = MAZE_SCALE * (x + offset - 1);
                    float posy = MAZE_SCALE * (y + offset);
                    GizmoPrimitive quad = GizmoPrimitive.Quad(new Vector2f(posx, -posy), new Vector2f(0.2f, 0.2f));
                    GizmoRenderer.Submit(quad);
                }

            }
        }
    }
}
