package com.kite.pacman.scripts;

import com.kite.engine.core.Time;
import com.kite.engine.ecs.components.ScriptComponent;
import com.kite.engine.ecs.components.TransformComponent;
import org.joml.Vector2f;

public class MovableEntityScript extends ScriptComponent
{
    TransformComponent m_TransformComponent;
    NodeSystemScript m_NodeSystem;
    int m_CurrentNodeID = -1;
    NodeSystemScript.Direction m_MovingDirection;
    float m_Speed;

    private boolean m_IsReversing = false;

    @Override
    public void OnAttach ()
    {
        m_TransformComponent = entity.GetComponent(TransformComponent.class);
        m_NodeSystem = entity.GetScene().GetEntity("Node System").GetComponent(NodeSystemScript.class);

        m_CurrentNodeID = m_NodeSystem.GetNearestNode(m_TransformComponent.Position);
        m_MovingDirection = NodeSystemScript.Direction.NONE;
        m_Speed = 0f;

        Vector2f position = m_NodeSystem.GetPosition(m_CurrentNodeID);
        m_TransformComponent.SetPosition(position.x, position.y);
    }

    boolean Move (NodeSystemScript.Direction direction)
    {
        int nextNodeID = m_IsReversing ? m_CurrentNodeID : m_NodeSystem.Move(m_CurrentNodeID, direction);

        if (nextNodeID == -1 || (!m_IsReversing && nextNodeID == m_CurrentNodeID))
            return true;

        Vector2f nextNodePosition = m_NodeSystem.GetPosition(nextNodeID);
        float deltaTime = Time.DeltaTime() / 1000f;
        Vector2f step = DirectionToVector(direction).mul(m_Speed * deltaTime);

        Vector2f previousPos = m_TransformComponent.Position;
        Vector2f nextPosition = previousPos.add(step, new Vector2f());
        m_TransformComponent.SetPosition(nextPosition.x, nextPosition.y);

        if (IsOverShooting(m_TransformComponent.Position, nextNodePosition, direction))
        {
            if (!m_IsReversing)
            {
                m_TransformComponent.SetPosition(nextNodePosition.x, nextNodePosition.y);
            }
            else
            {
                m_IsReversing = false;

                if (m_MovingDirection == NodeSystemScript.Direction.LEFT || m_MovingDirection == NodeSystemScript.Direction.RIGHT)
                    m_TransformComponent.SetPosition(m_TransformComponent.Position.x, nextNodePosition.y);
                else if (m_MovingDirection == NodeSystemScript.Direction.UP || m_MovingDirection == NodeSystemScript.Direction.DOWN)
                    m_TransformComponent.SetPosition(nextNodePosition.x, m_TransformComponent.Position.y);
            }

            m_CurrentNodeID = nextNodeID;
            return true;
        }

        return false;
    }

    boolean MoveInstantly (NodeSystemScript.Direction direction)
    {
        int nextNodeID = m_NodeSystem.Move(m_CurrentNodeID, direction);

        if (nextNodeID == -1 || nextNodeID == m_CurrentNodeID)
            return true;

        Vector2f nextNodePosition = m_NodeSystem.GetPosition(nextNodeID);
        m_TransformComponent.SetPosition(nextNodePosition.x, nextNodePosition.y);
        m_CurrentNodeID = nextNodeID;
        return true;
    }

    boolean ChangeDirection (NodeSystemScript.Direction direction)
    {
        if (m_MovingDirection == NodeSystemScript.Direction.NONE)
        {
            m_MovingDirection = direction;
            return true;
        }
        else if (AreOpposites(m_MovingDirection, direction))
        {
            m_MovingDirection = direction;
            m_IsReversing = true;
            return true;
        }

        return false;
    }

    float SquaredDistanceToTarget ()
    {
        int targetNodeID = m_NodeSystem.Move(m_CurrentNodeID, m_MovingDirection);

        if (targetNodeID == -1)
            return 0;

        Vector2f targetPosition = m_NodeSystem.GetPosition(targetNodeID);
        return targetPosition.distanceSquared(m_TransformComponent.Position);
    }

    private Vector2f DirectionToVector (NodeSystemScript.Direction direction)
    {
        switch (direction)
        {
            case UP:
                return new Vector2f(0, 1);
            case DOWN:
                return new Vector2f(0, -1);
            case LEFT:
                return new Vector2f(-1, 0);
            case RIGHT:
                return new Vector2f(1, 0);
        }

        return new Vector2f();
    }


    private boolean IsOverShooting (Vector2f currentPosition, Vector2f targetPosition, NodeSystemScript.Direction direction)
    {
        Vector2f dir = DirectionToVector(direction);
        Vector2f actualDir = targetPosition.sub(currentPosition, new Vector2f());

        boolean isOverShootingX = (direction == NodeSystemScript.Direction.RIGHT || direction == NodeSystemScript.Direction.LEFT) &&
                Math.signum(dir.x) != Math.signum(actualDir.x);

        boolean isOverShootingY = (direction == NodeSystemScript.Direction.UP || direction == NodeSystemScript.Direction.DOWN) &&
                Math.signum(dir.y) != Math.signum(actualDir.y);

        return isOverShootingX || isOverShootingY;
    }

    private boolean AreOpposites (NodeSystemScript.Direction dir1, NodeSystemScript.Direction dir2)
    {
        boolean a = dir1 == NodeSystemScript.Direction.UP && dir2 == NodeSystemScript.Direction.DOWN;
        boolean b = dir1 == NodeSystemScript.Direction.DOWN && dir2 == NodeSystemScript.Direction.UP;
        boolean c = dir1 == NodeSystemScript.Direction.LEFT && dir2 == NodeSystemScript.Direction.RIGHT;
        boolean d = dir1 == NodeSystemScript.Direction.RIGHT && dir2 == NodeSystemScript.Direction.LEFT;

        return a || b || c || d;
    }
}
