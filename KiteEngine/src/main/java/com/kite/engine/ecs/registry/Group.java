package com.kite.engine.ecs.registry;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Read-only class that groups all the entities who have both the specified components
 * <p></p>
 * Can be iterated, the iteration returns the ids of the entities
 * <p>
 * <b>Example:</b>
 * <pre>{@code
 *  Group<ExampleComponent1, ExampleComponent2> group;
 *  for (int entityID : group)
 *      // do something
 * }</pre>
 *<p>
 * @param <T> : The type of the first component
 * @param <V> : The type of the second component
 */

public class Group <T, V> implements Iterable<Integer>
{
    private final int[] m_Entities;
    private final T[] m_ComponentsT;
    private final V[] m_ComponentsV;

    /**
     * Creates a Group object with the specified entities and components
     * @param entities The entities to view
     * @param componentsT:</b> The corresponding components of the first type.</p>
     * @param componentsV:</b> The corresponding components of the second type.</p>
     * <p></p>
     * <b>Note:</b>
     * To create a valid View object,
     * componentsT.length and componentsV.length must be equal to entities.length
     *          and
     * the ith index of the component arrays should refer to the entity whose id is at
     * the ith index of the entities array.
     */
    protected Group (int[] entities, T[] componentsT, V[] componentsV)
    {
        m_Entities = entities;
        m_ComponentsT = componentsT;
        m_ComponentsV = componentsV;
    }

    Group ()
    {
        m_Entities = new int[0];
        m_ComponentsT = null;
        m_ComponentsV = null;
    }

    /**
     * @return
     * A {@link GroupNode GroupNode} containing the corresponding components of the specified entity,
     * or Null if the entity is not inside the Group
     *
     * @throws NullPointerException if this Group is not {@link Group#IsValid() valid}
     */
    public GroupNode<T, V> Get (int entity)
    {
        if (!IsValid())
            throw new NullPointerException();

        for (int i = 0; i < m_Entities.length; i++)
            if (entity == m_Entities[i])
                return new GroupNode<T, V>(m_ComponentsT[i], m_ComponentsV[i]);

        return null;
    }

    /**
     * Concatenates 2 Groups with the same types
     * @param other The Group to be concatenated to this Group
     * @return A Group with all the entities and components of this Group and the other Group
     * @throws NullPointerException if the other Group is null or this Group is not valid
     * @throws InvalidParameterException if the other Group is invalid or the types are incompatible
     */
    public Group<T, V> Concat (Group<T, V> other)
    {
        if (other == null || !IsValid())
            throw new NullPointerException();

        if (!other.IsValid() ||
                m_ComponentsT.getClass() != other.m_ComponentsT.getClass() ||
                m_ComponentsV.getClass() != other.m_ComponentsV.getClass())
            throw new InvalidParameterException();

        int[] entities = Arrays.copyOf(m_Entities, Size() + other.Size());
        T[] componentsT = Arrays.copyOf(m_ComponentsT, Size() + other.Size());
        V[] componentsV = Arrays.copyOf(m_ComponentsV, Size() + other.Size());

        for (int i = 0; i < other.Size(); i++)
        {
            entities[i + Size()] = other.m_Entities[i];
            componentsT[i + Size()] = other.m_ComponentsT[i];
            componentsV[i + Size()] = other.m_ComponentsV[i];
        }

        return new Group<T,V>(entities, componentsT, componentsV);
    }

    /**
     * Return how many entities there are inside this group
     */
    public int Size () { return m_Entities.length; }

    /**
     * Indicates whether this Group is utilizable or just a placeholder. <p>
     * Invalid Groups are guaranteed to have 0 elements, such that loops are skipped when iterated.
     * @return true is this Group is valid, false otherwise
     */
    public boolean IsValid () { return m_ComponentsT != null && m_ComponentsV != null; }

    @Override public Iterator<Integer> iterator()
    {
        return new GroupIterator();
    }

    /**
     * Represents a pair of components
     *
     * @param <T> : The type of the first component
     * @param <V> : The type of the second component
     */
    public static class GroupNode<T, V>
    {
        public T First;
        public V Second;

        public GroupNode (T first, V second)
        {
            First = first;
            Second = second;
        }
    }

    private class GroupIterator implements Iterator<Integer>
    {
        int index = 0;

        @Override public boolean hasNext()
        {
            return index < m_Entities.length;
        }

        @Override public Integer next()
        {
            return m_Entities[index++];
        }
    }
}
