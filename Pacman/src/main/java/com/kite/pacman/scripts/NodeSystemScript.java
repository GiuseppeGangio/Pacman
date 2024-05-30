package com.kite.pacman.scripts;

import com.kite.engine.ecs.components.ScriptComponent;
import com.kite.engine.ecs.components.TransformComponent;
import org.joml.Vector2f;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

public class NodeSystemScript extends ScriptComponent
{
    public enum Direction
    {
        NONE, UP, DOWN, LEFT, RIGHT
    }

    private TransformComponent m_TransformComponent;
    private HashMap<Integer, PathNode> m_Nodes;

    @Override
    public void OnAttach ()
    {
        m_TransformComponent = entity.GetComponent(TransformComponent.class);
        m_Nodes = new HashMap<>();
    }

    public void Load (String path)
    {
        String content = ReadFileAsString(path);

        if (content != null)
        {
            Reset();
            LoadSavedContent(content);
        }
    }

    public int Move (int currentNode, Direction direction)
    {
        PathNode node = m_Nodes.get(currentNode);

        if (node != null)
        {
            switch (direction)
            {
                case UP -> {
                    return (node.m_Up != null) ? node.m_Up.GetId() : -1;
                }

                case DOWN -> {
                    return (node.m_Down != null) ? node.m_Down.GetId() : -1;
                }

                case LEFT -> {
                    return (node.m_Left != null) ? node.m_Left.GetId() : -1;
                }

                case RIGHT -> {
                    return (node.m_Right != null) ? node.m_Right.GetId() : -1;
                }

                case NONE -> {
                    return currentNode;
                }
            }
        }

        return -1;
    }

    public Vector2f GetPosition (int nodeID)
    {
        PathNode node = m_Nodes.get(nodeID);

        if (node != null)
            return node.GetPosition();

        return null;
    }

    public int GetNearestNode (Vector2f position)
    {
        int id = -1;
        float distance = Float.MAX_VALUE;

        for (var entry : m_Nodes.entrySet())
        {
            PathNode node = entry.getValue();
            float nodeDistance = node.GetPosition().distanceSquared(position);

            if (nodeDistance < distance)
            {
                distance = nodeDistance;
                id = node.GetId();
            }
        }

        return id;
    }

    private void Reset ()
    {
        for (TransformComponent nodeTransform : m_TransformComponent.GetChildren())
            nodeTransform.Entity.Delete();
    }

    private void LoadSavedContent (String content)
    {
        ArrayList<TempNode> tempNodes = new ArrayList<>();

        int cursor = 0;
        while ((cursor = content.indexOf("Node:", cursor)) != -1)
        {
            cursor += 5;

            TempNode tempNode = new TempNode();
            tempNode.ID = GetID(content, "ID", cursor);
            tempNode.Type = GetID(content, "Type", cursor);
            tempNode.Position = GetPosition(content, cursor);
            tempNode.UpID = GetID(content, "Up", cursor);
            tempNode.DownID = GetID(content, "Down", cursor);
            tempNode.LeftID = GetID(content, "Left", cursor);
            tempNode.RightID = GetID(content, "Right", cursor);

            tempNodes.add(tempNode);
            CreateNode(tempNode);
        }

        for (TempNode node : tempNodes)
        {
            PathNode nodeScript = m_Nodes.get(node.ID);

            if (nodeScript != null)
            {
                nodeScript.SetConnections(
                        m_Nodes.get(node.UpID),
                        m_Nodes.get(node.DownID),
                        m_Nodes.get(node.LeftID),
                        m_Nodes.get(node.RightID)
                );
            }
        }
    }

    private void CreateNode (TempNode tempNode)
    {
        PathNode node = new PathNode();
        node.Init(tempNode.ID, tempNode.Type, tempNode.Position);
        m_Nodes.put(tempNode.ID, node);
    }

    private static Vector2f GetPosition (String data, int startIndex)
    {
        final String NODE_POSITION_LABEL = "Position";

        int labelStart = data.indexOf(NODE_POSITION_LABEL, startIndex);
        int contentStart = data.indexOf("[", labelStart + NODE_POSITION_LABEL.length());
        int contentEnd = data.indexOf("]", contentStart);
        String content = data.substring(contentStart + 1, contentEnd);

        String[] values = content.split(",");
        Vector2f position = new Vector2f(
                Float.parseFloat(values[0]),
                Float.parseFloat(values[1])
        );

        return position;
    }

    private static int GetID (String data, String label, int startIndex)
    {
        int labelStart = data.indexOf(label, startIndex);
        int contentStart = data.indexOf("[", labelStart + label.length());
        int contentEnd = data.indexOf("]", contentStart);

        String content = data.substring(contentStart + 1, contentEnd);

        return Integer.parseInt(content);
    }

    private String ReadFileAsString (String path)
    {
        File f = new File(path);
        String content;

        try {
            content = Files.readString(f.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (content != null)
        {
            content = content.replace("\n", "").replace("\t", "").replace(" ", "");
            content = content.strip();
        }

        return content;
    }

    public static class PathNode
    {
        public enum NodeType
        {
            DEFAULT, GATE
        }

        private int m_ID = -1;
        private Vector2f m_Position;
        private NodeType m_Type = NodeType.DEFAULT;
        private PathNode m_Up = null, m_Down = null, m_Left = null, m_Right = null;

        public int GetId () { return m_ID; }
        public NodeType GetType() { return m_Type; }
        public Vector2f GetPosition() { return m_Position; }

        void Init (int id, int type, Vector2f position)
        {
            m_ID = id;
            m_Type = NodeType.values()[type];
            m_Position = position;
        }

        void SetConnections (PathNode up, PathNode down, PathNode left, PathNode right)
        {
            m_Up = up;
            m_Down = down;
            m_Left = left;
            m_Right = right;
        }
    }

    private static class TempNode
    {
        public int ID, Type;
        public Vector2f Position;
        public int UpID = -1, DownID = -1, LeftID = -1, RightID = -1;
    }
}
