package com.kite.pacman.scripts.editor;

import com.kite.engine.core.Time;
import com.kite.engine.ecs.Entity;
import com.kite.engine.ecs.components.*;
import com.kite.engine.event.Event;
import com.kite.engine.event.keyevents.KeyPressedEvent;
import com.kite.engine.input.Input;
import com.kite.engine.input.InputKeys;
import com.kite.engine.rendering.gizmo.GizmoPrimitive;
import com.kite.engine.rendering.gizmo.GizmoRenderer;
import com.kite.pacman.scripts.MazeVisualizerScript;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class EditorScript extends ScriptComponent
{
    private enum Mode
    {
        PAN,
        NODE_ADD_DEFAULT,
        NODE_ADD_GATE,
        NODE_LINK
    }

    private static final Vector4f POSITIONED_DEFAULT_NODE_COLOR = new Vector4f(0.2f, 0.8f, 0.2f, 1f);
    private static final Vector4f PHANTOM_DEFAULT_NODE_COLOR = new Vector4f(0.2f, 0.8f, 0.2f, 0.3f);
    private static final Vector4f POSITIONED_GATE_NODE_COLOR = new Vector4f(1f, 0.55f, 0f, 1f);
    private static final Vector4f PHANTOM_GATE_NODE_COLOR =  new Vector4f(1f, 0.55f, 0f, 0.3f);
    private static final Vector4f SELECTED_NODE_COLOR = new Vector4f(0.8f, 0.2f, 0.2f, 1f);
    private static final float NODE_SCALE = MazeVisualizerScript.MAZE_SCALE / 2f;

    private TransformComponent m_TransformComponent;
    private Mode m_Mode = Mode.PAN;

    private Entity m_Nodes;
    private Entity m_DefaultNodeToAdd, m_GateNodeToAdd;
    private Entity m_SelectedNode1, m_SelectedNode2;
    private Vector2f m_LastPositionedNodePosition;

    @Override
    public void OnAttach ()
    {
        m_TransformComponent = entity.GetComponent(TransformComponent.class);
        m_Nodes = entity.GetScene().CreateEntity("Nodes");

        CreateMaze();
    }

    @Override
    public void OnUpdate ()
    {
        for (TransformComponent node : m_Nodes.GetComponent(TransformComponent.class).GetChildren())
        {
            NodeScript nodeScript = node.Entity.GetComponent(NodeScript.class);

            switch (nodeScript.Type)
            {
                case DEFAULT ->  SetNodeColor(node.Entity, POSITIONED_DEFAULT_NODE_COLOR);
                case GATE -> SetNodeColor(node.Entity, POSITIONED_GATE_NODE_COLOR);
            }
        }

        SetNodeColor(m_DefaultNodeToAdd, PHANTOM_DEFAULT_NODE_COLOR);
        SetNodeColor(m_GateNodeToAdd, PHANTOM_GATE_NODE_COLOR);
        SetNodeColor(m_SelectedNode1, SELECTED_NODE_COLOR);
        SetNodeColor(m_SelectedNode2, SELECTED_NODE_COLOR);

        VisualizeConnections();

        if (m_Mode == Mode.NODE_ADD_DEFAULT || m_Mode == Mode.NODE_ADD_GATE)
        {
            if (Input.IsMouseButtonPressed(1))
            {
                int[] mousePos = Input.GetMousePosition();
                Entity selected = entity.GetScene().MousePick(mousePos[0], mousePos[1]);

                if (selected.IsValid() && selected.HasComponent(NodeScript.class))
                {
                    InvalidNodeConnections(selected.GetComponent(NodeScript.class));
                    selected.Delete();
                }
            }
        }

        switch (m_Mode)
        {
            case NODE_ADD_DEFAULT ->
            {
                if (m_DefaultNodeToAdd == null)
                {
                    m_DefaultNodeToAdd = entity.GetScene().CreateEntity("DefaultNode");

                    TransformComponent transform = m_DefaultNodeToAdd.GetComponent(TransformComponent.class);
                    transform.SetScale(NODE_SCALE, NODE_SCALE);
                    if (m_LastPositionedNodePosition != null)
                        transform.SetPosition(m_LastPositionedNodePosition.x, m_LastPositionedNodePosition.y);

                    SpriteComponent spriteComponent = m_DefaultNodeToAdd.AddComponent(new SpriteComponent());
                    spriteComponent.Sprite.Color = PHANTOM_DEFAULT_NODE_COLOR;

                    NodeScript nodeScript = m_DefaultNodeToAdd.AddComponent(new NodeScript());
                    nodeScript.Movable = true;
                    nodeScript.Type = NodeScript.NodeType.DEFAULT;
                    nodeScript.SetSpacing(NODE_SCALE / 2f);
                }

                if (Input.IsKeyPressed(InputKeys.KEY_ENTER))
                {
                    if (!Time.IsTimerValid("COOL-DOWN") || Time.HasTimerFinished("COOL-DOWN"))
                    {
                        TransformComponent transform = m_DefaultNodeToAdd.GetComponent(TransformComponent.class);
                        NodeScript nodeScript = m_DefaultNodeToAdd.GetComponent(NodeScript.class);
                        SpriteComponent spriteComponent = m_DefaultNodeToAdd.GetComponent(SpriteComponent.class);
                        m_DefaultNodeToAdd.AddComponent(new MousePickableComponent());

                        transform.SetParent(m_Nodes.GetComponent(TransformComponent.class));
                        m_LastPositionedNodePosition = transform.Position;
                        spriteComponent.Sprite.Color = POSITIONED_DEFAULT_NODE_COLOR;
                        nodeScript.Movable = false;

                        m_DefaultNodeToAdd = null;
                        Time.StartTimer("COOL-DOWN", 1000);
                    }
                }
            }

            case NODE_ADD_GATE ->
            {
                if (m_GateNodeToAdd == null)
                {
                    m_GateNodeToAdd = entity.GetScene().CreateEntity("GateNode");

                    TransformComponent transform = m_GateNodeToAdd.GetComponent(TransformComponent.class);
                    transform.SetScale(NODE_SCALE, NODE_SCALE);
                    if (m_LastPositionedNodePosition != null)
                        transform.SetPosition(m_LastPositionedNodePosition.x, m_LastPositionedNodePosition.y);

                    SpriteComponent spriteComponent = m_GateNodeToAdd.AddComponent(new SpriteComponent());
                    spriteComponent.Sprite.Color = PHANTOM_GATE_NODE_COLOR;

                    NodeScript nodeScript = m_GateNodeToAdd.AddComponent(new NodeScript());
                    nodeScript.Movable = true;
                    nodeScript.Type = NodeScript.NodeType.GATE;
                    nodeScript.SetSpacing(NODE_SCALE / 2f);
                }

                if (Input.IsKeyPressed(InputKeys.KEY_ENTER))
                {
                    if (!Time.IsTimerValid("COOL-DOWN") || Time.HasTimerFinished("COOL-DOWN"))
                    {
                        TransformComponent transform = m_GateNodeToAdd.GetComponent(TransformComponent.class);
                        NodeScript nodeScript = m_GateNodeToAdd.GetComponent(NodeScript.class);
                        SpriteComponent spriteComponent = m_GateNodeToAdd.GetComponent(SpriteComponent.class);
                        m_GateNodeToAdd.AddComponent(new MousePickableComponent());

                        transform.SetParent(m_Nodes.GetComponent(TransformComponent.class));
                        m_LastPositionedNodePosition = transform.Position;
                        spriteComponent.Sprite.Color = POSITIONED_GATE_NODE_COLOR;
                        nodeScript.Movable = false;

                        m_GateNodeToAdd = null;
                        Time.StartTimer("COOL-DOWN", 1000);
                    }
                }
            }

            case NODE_LINK ->
            {
                if (Input.IsMouseButtonPressed(0))
                {
                    int[] mousePos = Input.GetMousePosition();
                    Entity selected = entity.GetScene().MousePick(mousePos[0], mousePos[1]);

                    if ((!Time.IsTimerValid("COOL-DOWN") || Time.HasTimerFinished("COOL-DOWN")) &&
                            selected.IsValid() && selected.HasComponent(NodeScript.class))
                    {
                        if (m_SelectedNode1 == null)
                            m_SelectedNode1 = selected;
                        else if (selected.GetId() != m_SelectedNode1.GetId())
                            m_SelectedNode2 = selected;
                        else
                            m_SelectedNode1 = null;

                        Time.StartTimer("COOL-DOWN", 500);
                    }
                }

                if (m_SelectedNode1 != null && m_SelectedNode2 != null)
                {
                    NodeScript node1 = m_SelectedNode1.GetComponent(NodeScript.class);
                    NodeScript node2 = m_SelectedNode2.GetComponent(NodeScript.class);

                    if (Input.IsKeyPressed(InputKeys.KEY_UP))
                        node2.CreateUpDownConnection(node1);
                    else if (Input.IsKeyPressed(InputKeys.KEY_DOWN))
                        node1.CreateUpDownConnection(node2);
                    else if (Input.IsKeyPressed(InputKeys.KEY_LEFT))
                        node2.CreateLeftRightConnection(node1);
                    else if (Input.IsKeyPressed(InputKeys.KEY_RIGHT))
                        node1.CreateLeftRightConnection(node2);

                    m_SelectedNode1 = null;
                    m_SelectedNode2 = null;
                }
            }
        }
    }

    private void VisualizeConnections ()
    {
        for (TransformComponent nodeTransform : m_Nodes.GetComponent(TransformComponent.class).GetChildren())
        {
            NodeScript nodeScript = nodeTransform.Entity.GetComponent(NodeScript.class);

            if (nodeScript.Up != null)
            {
                Vector2f pos1 = nodeTransform.Position.add(new Vector2f(NODE_SCALE / 3f, NODE_SCALE / 2f), new Vector2f());
                Vector2f pos2 = nodeScript.Up.GetPosition().add(new Vector2f(NODE_SCALE / 3f, -NODE_SCALE / 2f), new Vector2f());
                Vector2f distance = pos2.sub(pos1, new Vector2f());
                GizmoRenderer.Submit(GizmoPrimitive.Vector(pos1, distance));
            }

            if (nodeScript.Down != null)
            {
                Vector2f pos1 = nodeTransform.Position.add(new Vector2f(-NODE_SCALE / 3f, -NODE_SCALE / 2f), new Vector2f());
                Vector2f pos2 = nodeScript.Down.GetPosition().add(new Vector2f(-NODE_SCALE / 3f, NODE_SCALE / 2f), new Vector2f());
                Vector2f distance = pos2.sub(pos1, new Vector2f());
                GizmoRenderer.Submit(GizmoPrimitive.Vector(pos1, distance));
            }
            if (nodeScript.Left != null)
            {
                Vector2f pos1 = nodeTransform.Position.add(new Vector2f(-NODE_SCALE / 2f, NODE_SCALE / 3f), new Vector2f());
                Vector2f pos2 = nodeScript.Left.GetPosition().add(new Vector2f(NODE_SCALE / 2f, NODE_SCALE / 3f), new Vector2f());
                Vector2f distance = pos2.sub(pos1, new Vector2f());
                GizmoRenderer.Submit(GizmoPrimitive.Vector(pos1, distance));
            }
            if (nodeScript.Right != null)
            {
                Vector2f pos1 = nodeTransform.Position.add(new Vector2f(NODE_SCALE / 2f, -NODE_SCALE / 3f), new Vector2f());
                Vector2f pos2 = nodeScript.Right.GetPosition().add(new Vector2f(-NODE_SCALE / 2f, -NODE_SCALE / 3f), new Vector2f());
                Vector2f distance = pos2.sub(pos1, new Vector2f());
                GizmoRenderer.Submit(GizmoPrimitive.Vector(pos1, distance));
            }
        }
    }

    private void CreateMaze ()
    {
        Entity maze = entity.GetScene().CreateEntity("Maze");
        maze.AddComponent(new MazeVisualizerScript());
    }

    private void SwitchMode()
    {
        int modeIndex = 0;
        for (int i = 0; i < Mode.values().length; i++)
            if (Mode.values()[i] == m_Mode)
                modeIndex = i;

        if (modeIndex == Mode.values().length - 1)
            modeIndex = 0;
        else
            modeIndex++;

        m_Mode = Mode.values()[modeIndex];

        if (m_DefaultNodeToAdd != null)
            m_DefaultNodeToAdd.Delete();
        m_DefaultNodeToAdd = null;

        if (m_GateNodeToAdd != null)
            m_GateNodeToAdd.Delete();
        m_GateNodeToAdd = null;

        m_SelectedNode1 = null;
        m_SelectedNode2 = null;

        System.out.println("Selected mode " + m_Mode);
    }

    @Override
    public void OnEvent (Event event)
    {
        Event.Dispatch(KeyPressedEvent.class, this::OnKeyPressedEvent, event);
    }

    private boolean OnKeyPressedEvent (KeyPressedEvent event)
    {
        if (event.KeyCode() == InputKeys.KEY_M)
        {
            SwitchMode();
            return true;
        }

        if (event.KeyCode() == InputKeys.KEY_C)
        {
            String saveFilePath = "assets/saves/editor/" + Time.CurrentTime() + ".txt";
            EditorSaver.Save(saveFilePath, m_Nodes.GetComponent(TransformComponent.class).GetChildren());
            return true;
        }

        if (event.KeyCode() == InputKeys.KEY_L)
        {
            String loadPath = "assets/saves/editor/" + "1717052611042" + ".txt";
            EditorSaver.SavedNode[] nodes = EditorSaver.Load(loadPath);

            if (nodes != null)
            {
                Reset();

                for (EditorSaver.SavedNode node : nodes)
                {
                    Entity nodeEntity = entity.GetScene().CreateEntity("Node");

                    TransformComponent transform = nodeEntity.GetComponent(TransformComponent.class);
                    transform.SetParent(m_Nodes.GetComponent(TransformComponent.class));
                    transform.SetScale(NODE_SCALE, NODE_SCALE);
                    transform.SetPosition(node.Position.x, node.Position.y);

                    SpriteComponent spriteComponent = nodeEntity.AddComponent(new SpriteComponent());
                    spriteComponent.Sprite.Color = new Vector4f(0.2f, 0.8f, 0.2f, 1f);

                    NodeScript nodeScript = nodeEntity.AddComponent(new NodeScript());
                    nodeScript.Type = NodeScript.NodeType.values()[node.Type];
                    nodeScript.SetID(node.ID);
                    nodeScript.Movable = false;
                    nodeScript.SetSpacing(NODE_SCALE / 2f);

                    nodeEntity.AddComponent(new MousePickableComponent());
                }

                for (EditorSaver.SavedNode node : nodes)
                {
                    NodeScript nodeScript = FindNode(node.ID);

                    if (nodeScript != null)
                    {
                        nodeScript.Up = FindNode(node.UpID);
                        nodeScript.Down = FindNode(node.DownID);
                        nodeScript.Left = FindNode(node.LeftID);
                        nodeScript.Right = FindNode(node.RightID);
                    }
                }
            }

            return true;
        }


        return false;
    }

    private void SetNodeColor (Entity node, Vector4f color)
    {
        if (node != null)
        {
            SpriteComponent spriteComponent = node.GetComponent(SpriteComponent.class);
            spriteComponent.Sprite.Color = color;
        }
    }

    private NodeScript FindNode (int nodeID)
    {
        for (TransformComponent node : m_Nodes.GetComponent(TransformComponent.class).GetChildren())
        {
            NodeScript current = node.Entity.GetComponent(NodeScript.class);

            if (current.GetID() == nodeID)
                return current;
        }

        return null;
    }

    private void InvalidNodeConnections (NodeScript nodeScript)
    {
        if (nodeScript.Up != null)
            nodeScript.Up.Down = null;

        if (nodeScript.Down != null)
            nodeScript.Down.Up = null;

        if (nodeScript.Left != null)
            nodeScript.Left.Right = null;

        if (nodeScript.Right != null)
            nodeScript.Right.Left = null;
    }

    private void Reset ()
    {
        m_Mode = Mode.PAN;

        if (m_DefaultNodeToAdd != null)
            m_DefaultNodeToAdd.Delete();
        m_DefaultNodeToAdd = null;

        m_SelectedNode1 = null;
        m_SelectedNode2 = null;

        m_LastPositionedNodePosition = new Vector2f();

        for (TransformComponent node : m_Nodes.GetComponent(TransformComponent.class).GetChildren())
            node.Entity.Delete();
    }
}
