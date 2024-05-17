package com.kite.engine.ecs.registry;

import java.security.InvalidParameterException;
import java.util.Hashtable;
import java.util.Vector;

/**
 *  Represent a ECS registry.
 *
 */
public class Registry
{
    /**
     * universal ID for invalid entities
     */
    public static final int INVALID_ENTITY_ID = -1;

    /**
     * contains the next valid entity id
     */
    private int m_NextID;
    private final Vector<RegisteredComponent<?>> m_RegisteredComponents;

    /**
     * Creates a brand-new Registry
     */
    public Registry ()
    {
        m_NextID = 0;
        m_RegisteredComponents = new Vector<>();
    }

    /**
     * Creates a copy of the specified registry
     *
     * @param copy a valid registry to copy
     */
    public Registry (Registry copy)
    {
        m_NextID = 0;
        m_RegisteredComponents = new Vector<>(copy.m_RegisteredComponents.size());

        copy.m_RegisteredComponents.forEach((RegisteredComponent<?> componentCopy) ->
                m_RegisteredComponents.add(new RegisteredComponent<>(componentCopy)));
    }

    /**
     * Creates an entity
     * @return  the id of the created entity
     */
    public int CreateEntity () { return m_NextID++; }

    /**
     * Deletes the specified entity from this Registry
     * @param entityID The entity to be removed
     */
    public void DeleteEntity (int entityID)
    {
        for (RegisteredComponent<?> regComponent : m_RegisteredComponents)
            regComponent.Remove(entityID);
    }

    /**
     * Adds the specified component to the specified entity. <p>
     * @param <T> The type of the component to add
     * @param component The component to add
     * @param entityID  The ID of a valid entity, must be between 0 and m_NextID - 1
     * @return The newly added component
     * @throws NullPointerException if component is null
     * @throws InvalidParameterException if entityID is an invalid id
     * <p></p>
     * <b>Special cases:</b> <p>
     * - if the entity is valid and already has the component
     *     it will be overwritten
     */
    public <T> T AddComponent (T component, int entityID)
    {
        if (component == null)
            throw new NullPointerException("Cannot add a null component to an entity");

        if (!IsAValidID(entityID))
            throw new InvalidParameterException("The specified ID is not valid");

        for (RegisteredComponent<?> regComponent : m_RegisteredComponents)
        {
            if (regComponent.IsOfType(component.getClass()))
            {
                regComponent.Add(component, entityID);
                return component;
            }
        }

        RegisteredComponent<T> newComponent = new RegisteredComponent<>(component.getClass());
        newComponent.Add(component, entityID);
        m_RegisteredComponents.add(newComponent);

        return component;
    }

    /**
     * Removes the component of the specified class from the specified entity. <p>
     * @param componentType The class of the component to remove
     * @param entityID The ID of a valid entity. Must be between 0 and m_NextID - 1
     *
     * @return The removed component is found and has been removed successfully,
     * null if the component could not be found or could not be removed
     *
     * @throws NullPointerException if the componentType is null
     * @throws InvalidParameterException if entityID is not a valid ID
     * <p>
     * <b>Special cases:</b> <p>
     * - if entityID is a valid ID but the associated entity does not contain the specified component,
     *      the method does nothing and returns null
     */
    @SuppressWarnings("unchecked")
    public <T> T RemoveComponent (Class<T> componentType, int entityID)
    {
        if (componentType == null)
            throw new NullPointerException();

        if (!IsAValidID(entityID))
            throw new InvalidParameterException();

        for (RegisteredComponent<?> regComponent : m_RegisteredComponents)
        {
            if (regComponent.IsOfType(componentType))
            {
                return (T) regComponent.Remove(entityID);
            }
        }

        return null;
    }

    /**
     * Gets the component of the specified type from the specified entity <p>
     *
     * @param componentType The type of the component
     * @param  entityID The id of the entity from which the component will be retrieved
     *
     * @return The component instance, if found. Null otherwise
     *
     * @throws InvalidParameterException if entityID is not a valid ID
     */
    public <T> T GetComponent (Class<T> componentType, int entityID)
    {
        if (!IsAValidID(entityID))
            throw new InvalidParameterException();

        for (RegisteredComponent<?> regComponent : m_RegisteredComponents)
        {
            if (regComponent.IsOfType(componentType) && regComponent.Has(entityID))
                return componentType.cast(regComponent.Get(entityID));
        }

        return null;
    }

    /**
     * Gets the components of the specified supertype from the specified entity <p>
     *
     * @param componentType The supertype of the component
     * @param  entityID The id of the entity from which the components will be retrieved
     *
     * @return The Vector containing all the found components.
     *
     * @throws InvalidParameterException if entityID is not a valid ID
     */
    public <T> Vector<T> GetComponents (Class<T> componentType, int entityID)
    {
        if (!IsAValidID(entityID))
            throw new InvalidParameterException();

        Vector<T> components = new Vector<>();

        for (RegisteredComponent<?> regComponent : m_RegisteredComponents)
        {
            if (regComponent.IsOfType(componentType) && regComponent.Has(entityID))
                components.add(componentType.cast(regComponent.Get(entityID)));
        }

        return components;
    }

    /**
     * Retrieves a list of all the components associated with the specified entity. <p>
     * <b>Warning:</b> this method is slow and should be used carefully <p>
     *
     * @param entityID A valid entity
     *
     * @return An array of Object containing all the components associated with the specified entity.
     *
     * @throws InvalidParameterException if entityID is not a valid ID
     */
    public Object[] GetAllComponents (int entityID)
    {
        if (!IsAValidID(entityID))
            throw new InvalidParameterException();

        Vector<Object> components = new Vector<>();

        for (RegisteredComponent<?> regComponent : m_RegisteredComponents)
            if (regComponent.Has(entityID))
                components.add(regComponent.Get(entityID));

        return components.toArray(new Object[0]);
    }

    /**
     * Verifies if the specified entity has a component of  the specified type <p>
     *
     * @param componentType The class of the component to find
     * @param entityID The ID of a valid entity. Must be between 0 and m_NextID - 1
     *
     * @return
     * -true if the entity has the specified component <p>
     * -false otherwise
     *
     * @throws NullPointerException if componentType is nul
     * @throws InvalidParameterException if entityID is not a valid ID
     */
    public boolean HasComponent (Class<?> componentType, int entityID)
    {
        if (componentType == null)
            throw new NullPointerException();

        if (!IsAValidID(entityID))
            throw new InvalidParameterException();

        for (RegisteredComponent<?> regComponent : m_RegisteredComponents)
            if (regComponent.IsOfType(componentType))
                return regComponent.Has(entityID);

        return false;
    }

    /**
     * Checks if the provided id is a valid EntityID.
     * @param id The id of an Entity to check
     * @return
     * true if the ID is valid, <p>
     * false otherwise
     */
    public boolean IsAValidID (int id) { return id >= 0 && id < m_NextID; }

    /**
     * Creates a view of all the components of the specified type
     * @param componentType The type of the component to view
     * @return
     * A valid {@link View View} containing all the entities having the component of specified type, if any. <p>
     * An  {@link View#IsValid() invalid view}  if no entity having that component could be found
     *
     * @throws NullPointerException if the specified type is null
     */
    @SuppressWarnings("unchecked")
    public <T> View<T> ViewComponent (Class<?> componentType)
    {
        if (componentType == null)
            throw new NullPointerException();

        View<T> components = null;

        for (RegisteredComponent<?> regComponent : m_RegisteredComponents)
        {
            if (regComponent.IsOfType(componentType))
            {
                View<T> componentsToAdd = (View<T>) regComponent.GetAllEntities();

                if (components == null)
                    components = componentsToAdd;
                else
                    components = components.Concat(componentsToAdd);
            }
        }

        return (components == null) ? new View<>() : components;
    }

    /**
     * Creates a group grouping the components of the specified types.
     * @param firstComponentType The type of first the component off the group
     * @param secondComponentType The type of second the component off the group
     * @return
     * A valid {@link Group Group} containing all the entities having the component of specified type, if any. <p>
     * An  {@link Group#IsValid() invalid group}  if no entity having that component could be found
     *
     * @throws NullPointerException if one of the specified types is null
     */
    @SuppressWarnings("unchecked cast")
    public <T, V> Group<T, V> GroupComponents (Class<?> firstComponentType, Class<?> secondComponentType)
    {
        if (firstComponentType == null || secondComponentType == null)
            throw new NullPointerException();

        RegisteredComponent<T> firstReg = null;
        RegisteredComponent<V> secondReg = null;

        for (RegisteredComponent<?> regComponent : m_RegisteredComponents)
        {
            if (regComponent.IsOfType(firstComponentType))
            {
                firstReg = (RegisteredComponent<T>) regComponent;
                if (secondReg != null) break;
            }
            else if (regComponent.IsOfType(secondComponentType))
            {
                secondReg = (RegisteredComponent<V>) regComponent;
                if (firstReg != null) break;
            }
        }

        if (firstReg != null && secondReg != null)
        {
            return firstReg.GetAllEntities().GroupWith(secondReg.GetAllEntities());
        }

        return new Group<T, V>();
    }


    /**
     * Represents a component type in the registry.
     * @param <T> The type of the component
     */
    static class RegisteredComponent <T>
    {
        /**
         * The type of this component, used for dynamic casting
         */
        private final Class<T> m_Type;

        /**
         *  the component map maps an integer, which is the ID of an entity,
         *  to a component of type T
         */
        private final Hashtable<Integer, T> m_ComponentMap;

        /**
         *  <b>Constructor:</b><p>
         *  Creates a RegisteredComponent of the specified type
         *  @param componentType: The class of the type parameter. Must be equal to T.getClass();
         */
        @SuppressWarnings("unchecked cast")
        RegisteredComponent (Class<?> componentType)
        {
            m_Type = (Class<T>) componentType;
            m_ComponentMap = new Hashtable<>();
        }

        /**
         *  <b>Constructor:</b><p>
         *  Creates a RegisteredComponent which is the copy of the specified RegisteredComponent of the same type
         *  @param copy A valid Registered component of the same type from which data is drawn
         */
        RegisteredComponent (RegisteredComponent<T> copy)
        {
            m_Type = copy.m_Type;
            m_ComponentMap = new Hashtable<>(copy.m_ComponentMap);
        }

        /**
         * Adds a component to an entity. <p>
         * @param component The instance of the component of type T to add.
         *                  must be such that this.IsOfType(component.getClass()) == true
         *
         * @param entity The ID of the entity the component is added to
         *
         * <p></p>
         * <b>Special cases:</b><p>
         * - if the entity already had the component,
         *   the component will be overwritten
         */
        void Add (Object component, int entity)
        {
            m_ComponentMap.put(entity, m_Type.cast(component));
        }

        /**
         *  Removes this component from the specified entity. <p>
         *  @param entity The ID of the entity from which this component is removed
         *  @return The removed component, or null if the entity did not have the component
         */
        T Remove (int entity)
        {
            return m_ComponentMap.remove(entity);
        }

        /**
         * Returns the instance of the component of this type associated with the specified entity <p>
         * @param entity The ID of the entity from which this component is retrieved
         * @return
         * - The component in question, if found <p>
         * - Null, if the entity has not the component
         */
        T Get (int entity)
        {
            return m_ComponentMap.get(entity);
        }

        /**
         * Verifies is the specified entity has a component of this type
         * @param entity The ID of the entity to check
         * @return
         * - True, if the entity has the component, <p>
         * - False, otherwise
         */
        boolean Has (int entity)
        {
            return m_ComponentMap.containsKey(entity);
        }

        /**
         * Checks if the type of this component matches the specified type
         * @param type The component type
         * @return whether this object represents a component of the specified type
         * @throws NullPointerException if the specified type is Null
         */
        boolean IsOfType (Class<?> type)
        {
            if (type == null)
                throw new NullPointerException();

            return type.isAssignableFrom(m_Type);
        }

        /**
         * @return a valid {@link View View} which features all the entities who have this component
         */
        @SuppressWarnings("unchecked")
        public View<T> GetAllEntities ()
        {
            int[] entities = new int[m_ComponentMap.size()];
            Object[] components = new Object[m_ComponentMap.size()];

            final int[] index = {0};
            m_ComponentMap.forEach((Integer entity, T component) ->
            {
                entities[index[0]] = entity;
                components[index[0]] = component;
                index[0]++;
            });

            return new View<T>(entities, (T[]) components);
        }

        @Override
        public boolean equals(Object obj)
        {
            if(this == obj)
                return true;

            if(obj instanceof RegisteredComponent<?>)
                return m_Type == ((RegisteredComponent<?>) obj).m_Type;

            return false;
        }

        @Override
        public int hashCode ()
        {
            return m_Type.getName().hashCode();
        }
    }
}
