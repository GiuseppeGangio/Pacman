package com.kite.pacman.scripts;

import org.joml.Vector2i;

import java.util.ArrayList;

public class PathFinder
{
    private static class AStarNode
    {
        public int Index;
        public AStarNode Parent = null;
        public float MovementCost, DistanceCost, TotalCost;
    }

    public static ArrayList<Integer> AStar (int[] map, int mapWidth, int mapHeight, int startPos, int targetPos)
    {
        ArrayList<AStarNode> open = new ArrayList<>();
        ArrayList<AStarNode> closed = new ArrayList<>();

        Vector2i startPosVec = IndexToPosition(mapWidth, mapHeight, startPos);
        Vector2i targetPosVec = IndexToPosition(mapWidth, mapHeight, targetPos);

        AStarNode startNode = new AStarNode();
        startNode.Index = startPos;
        startNode.MovementCost = 0;
        startNode.DistanceCost = EvaluateDistance(startPosVec.x, startPosVec.y, targetPosVec.x, targetPosVec.y);
        startNode.TotalCost = startNode.MovementCost + startNode.DistanceCost;
        open.add(startNode);

        boolean foundPath = false;
        while (!foundPath)
        {
            try
            {
                if (open.isEmpty())
                    break;

                AStarNode currentNode = GetLowerCostNode(open);
                open.remove(currentNode);
                closed.add(currentNode);

                if (currentNode.Index == targetPos)
                    foundPath = true;

                int[] neighbors = GetNeighbors(mapWidth, mapHeight, currentNode.Index);
                for (int nIndex : neighbors)
                {
                    try
                    {
                        AStarNode nClosedNode = FindNodeByIndex(closed, nIndex);

                        if (nIndex == -1 || map[nIndex] == 0 || nClosedNode != null)
                            continue;

                        AStarNode nOpenNode = FindNodeByIndex(open, nIndex);
                        boolean inOpen = nOpenNode != null;

                        AStarNode nNode = EvaluateNode(mapWidth, mapHeight, currentNode, nIndex, targetPos);

                        if (inOpen && nNode.TotalCost < nOpenNode.TotalCost)
                        {
                            open.remove(nOpenNode);
                            open.add(nNode);
                        }

                        if (!inOpen)
                        {
                            open.add(nNode);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        ArrayList<Integer> path = new ArrayList<>();

        if (foundPath)
        {
            AStarNode currentNode = closed.getLast();
            path.add(currentNode.Index);

            while (currentNode.Parent != null)
            {
                path.add(currentNode.Parent.Index);
                currentNode = currentNode.Parent;
            }
        }

        return path;
    }

    private static AStarNode EvaluateNode (int mapWidth, int mapHeight, AStarNode origin, int index, int target)
    {
        Vector2i nodePos = IndexToPosition(mapWidth, mapHeight, index);
        Vector2i targetPos = IndexToPosition(mapWidth, mapHeight, target);

        AStarNode node = new AStarNode();
        node.Index = index;
        node.Parent = origin;
        node.MovementCost = origin.MovementCost + 1;
        node.DistanceCost = EvaluateDistance(nodePos.x, nodePos.y, targetPos.x, targetPos.y);
        node.TotalCost = node.MovementCost + node.DistanceCost;

        return node;
    }

    private static Vector2i IndexToPosition (int mapWidth, int mapHeight, int index)
    {
        int posx = index % mapWidth;
        int posY = (index - posx) / mapWidth;

        return new Vector2i(posx, posY);
    }

    private static AStarNode FindNodeByIndex (ArrayList<AStarNode> list, int index)
    {
        for (AStarNode node : list)
            if (node.Index == index)
                return node;

        return null;
    }

    private static int[] GetNeighbors (int mapWidth, int mapHeight, int origin)
    {
        int originX = origin % mapWidth;
        int originY = (origin - originX) / mapWidth;

        int top = -1;
        int bottom = -1;
        int left = -1;
        int right = -1;

        if (originX > 0)
            left = origin - 1;

        if (originX < mapWidth - 1)
            right = origin + 1;

        if (originY > 0)
            top = origin - mapWidth;

        if (originY < mapHeight - 1)
            bottom = origin + mapWidth;

        return new int[] {top, bottom, left, right};
    }

    private static AStarNode GetLowerCostNode (ArrayList<AStarNode> nodes)
    {
        AStarNode lower = nodes.get(0);

        for (AStarNode node : nodes)
        {
            if (node.TotalCost < lower.TotalCost)
                lower = node;
        }

        return lower;
    }

    private static float EvaluateDistance (int ax, int ay, int bx, int by)
    {
        int xDist = ax - bx;
        int yDist = ay - by;
        return xDist * xDist + yDist * yDist;
    }
}
