package com.kite.engine.ecs.registry;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * Read-only class that groups all the entities with the specified component. <p>
 * Can be iterated, the iteration returns the ids of the entities <p>
 * <b>Example:</b>
 * <pre>{@code
 * View<ExampleComponent> view;
 * for (int entityID : view)
 *     // do something}</pre>

 * @param <T> : The type of the component
 */
public class View <T> implements Iterable<Integer>
{
    private final int[] m_Entities;
    private final T[] m_Components;

    /**
     * Creates a View object with the specified entities and components <p>
     * @param entities The entities to view
     * @param components The corresponding components.
     *
     * <p></p>
     * <b>Note:</b>
     * To create a valid View object,
     * entities.length must be equal to components.length
     *          and
     * the ith index of the component array should refer to the entity whose id is at
     * the ith index of the entities array.
     */
    View (int[] entities, T[] components)
    {
        m_Entities = entities;
        m_Components = components;
    }

    View ()
    {
        m_Entities = new int[0];
        m_Components = null;
    }

    /**
     * @return how many entities there are inside this view
     */
    public int Size () { return m_Entities.length; }

    /**
     * @param entity A valid entity id
     * @return The component of type T associated with that entity
     * @throws NullPointerException if this View is not {@link View#IsValid() valid}
     */
    public T Get (int entity)
    {
        if (!IsValid())
            throw new NullPointerException();

        for (int i = 0; i < m_Entities.length; i++)
            if (entity == m_Entities[i])
                return (T) m_Components[i];

        return null;
    }

    /**
     * Concatenates 2 views of the same type
     * @param other The View to be concatenated to this view
     * @return A View with all the entities and components of this View and the other View
     * @throws NullPointerException if the other View is null or this View is not valid
     * @throws InvalidParameterException if the other view is invalid or of incompatible type
     */
    public View<T> Concat (View<T> other)
    {
        if (other == null || !IsValid())
            throw new NullPointerException();

        if (!other.IsValid() || m_Components.getClass() != other.m_Components.getClass())
            throw new InvalidParameterException();

        int[] entities = Arrays.copyOf(m_Entities, Size() + other.Size());
        T[] components = Arrays.copyOf(m_Components, Size() + other.Size());

        for (int i = 0; i < other.Size(); i++)
        {
            entities[i + Size()] = other.m_Entities[i];
            components[i + Size()] = other.m_Components[i];
        }

        return new View<T>(entities, components);
    }

    /**
     * Groups this View with another one into a Group
     * @param <V> The type of the other View
     * @param other Another valid View
     * @return A group with all the entities who have both the components
     * @throws NullPointerException if this view is not {@link View#IsValid() valid}
     * @throws InvalidParameterException if the other view is Null or invalid
     */
    @SuppressWarnings("unchecked")
    protected <V> Group<T, V> GroupWith (View<V> other)
    {
        if (!IsValid())
            throw new NullPointerException();

        if (other == null || !other.IsValid())
            throw new InvalidParameterException();

        int[] commonEntities;

        // Find common entities
        {
            Set<Integer> set = new HashSet<>();
            for (int i : m_Entities) {
                set.add(i);
            }
            List<Integer> commonValues = new ArrayList<>();
            for (int i : other.m_Entities) {
                if (set.contains(i)) {
                    commonValues.add(i);
                    set.remove(i);
                }
            }

            commonEntities = new int[commonValues.size()];
            for (int i = 0; i < commonValues.size(); i++) {
                commonEntities[i] = commonValues.get(i);
            }
        }

        Object[] components = new Object[commonEntities.length];
        Object[] otherComponents = new Object[commonEntities.length];

        for (int i = 0; i < commonEntities.length; i++)
        {
            components[i] = Get(commonEntities[i]);
            otherComponents[i] = other.Get(commonEntities[i]);
        }

        return new Group<T, V>(commonEntities, (T[]) components, (V[]) otherComponents);
    }

    /**
     * Indicates whether this View is utilizable or just a placeholder. <p>
     * Invalid Views are guaranteed to have 0 elements, such that loops are skipped when iterated.
     * @return true is this View is valid, false otherwise
     */
    public boolean IsValid ()
    {
        return m_Components != null && m_Entities != null && m_Entities.length == m_Components.length;
    }

    @Override
    public Iterator<Integer> iterator()
    {
        return new ViewIterator();
    }

    private class ViewIterator implements Iterator<Integer>
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
