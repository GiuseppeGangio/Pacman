package com.kite.pacman.scripts.editor;

import com.kite.engine.ecs.components.TransformComponent;
import org.joml.Vector2f;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Vector;

public class EditorSaver
{
    static class SavedNode
    {
        public int ID = -1;
        public int Type = -1;
        public Vector2f Position = new Vector2f();
        public int UpID = -1, DownID = -1, LeftID = -1, RightID = -1;

        public String Serialize ()
        {
            return "Node: {\n" +
                    "\tID:       " + "[" + ID + "]\n" +
                    "\tType:     " + "[" + Type + "]\n" +
                    "\tPosition: " + "[" + Position.x + ", " + Position.y + "]\n" +
                    "\tUp:       " + "[" + UpID + "]\n" +
                    "\tDown:     " + "[" + DownID + "]\n" +
                    "\tLeft:     " + "[" + LeftID + "]\n" +
                    "\tRight:    " + "[" + RightID + "]\n" +
                    "}\n\n";
        }
    }

    private static final int NODE_SIZE_BYTES = 8;

    static void Save (String filePath, TransformComponent[] nodes)
    {
        StringBuilder info = new StringBuilder();
        info.append("NodeCount: ").append(nodes.length).append(";\n\n");

        for (int i = 0; i < nodes.length; i++)
        {
            TransformComponent nodeTransform = nodes[i];
            NodeScript nodeScript = nodeTransform.Entity.GetComponent(NodeScript.class);

            SavedNode savedNode = new SavedNode();

            savedNode.ID = nodeScript.GetID();
            savedNode.Type = nodeScript.Type.ordinal();
            savedNode.Position = new Vector2f(nodeTransform.Position);

            if (nodeScript.Up != null)
                savedNode.UpID = nodeScript.Up.GetID();

            if (nodeScript.Down != null)
                savedNode.DownID = nodeScript.Down.GetID();

            if (nodeScript.Left != null)
                savedNode.LeftID = nodeScript.Left.GetID();

            if (nodeScript.Right != null)
                savedNode.RightID = nodeScript.Right.GetID();

            info.append(savedNode.Serialize());
        }

        try (PrintWriter out = new PrintWriter(filePath)) {
            out.println(info);
        }
        catch (Exception ignored) {}
    }

    static SavedNode[] Load (String filePath)
    {
        File f = new File(filePath);
        String content;

        try {
            content = Files.readString(f.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (content != null)
        {
            int nodeCount = GetNodeCount(content);
            Vector<SavedNode> savedNodes = new Vector<>(nodeCount);

            content = content.replace("\n", "").replace("\t", "").replace(" ", "");
            content = content.strip();

            int cursor = 0;
            while ((cursor = content.indexOf("Node:", cursor)) != -1)
            {
                cursor += 5;

                SavedNode savedNode = new SavedNode();
                savedNode.ID = GetID(content, "ID", cursor);
                savedNode.Type = GetID(content, "Type", cursor);
                savedNode.UpID = GetID(content, "Up", cursor);
                savedNode.DownID = GetID(content, "Down", cursor);
                savedNode.LeftID = GetID(content, "Left", cursor);
                savedNode.RightID = GetID(content, "Right", cursor);
                savedNode.Position = GetPosition(content, cursor);

                savedNodes.add(savedNode);
            }

            return savedNodes.toArray(new SavedNode[0]);
        }

        return null;
    }

    private static int GetNodeCount (String data)
    {
        final String NODE_COUNT_FLAG = "NodeCount: ";

        int startIndex = data.indexOf(NODE_COUNT_FLAG) + NODE_COUNT_FLAG.length();
        int endIndex = data.indexOf(";", startIndex);
        String nodeCount = data.substring(startIndex, endIndex);
        return Integer.parseInt(nodeCount);
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
}
