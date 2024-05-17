package com.kite.engine.ecs;
import com.kite.engine.core.Scene;
import com.kite.engine.ecs.components.ScriptComponent;
import com.kite.engine.ecs.registry.Registry;
import com.kite.engine.event.EventHandler;
import com.kite.engine.event.entityevents.EntityComponentAddedEvent;
import com.kite.engine.event.entityevents.EntityComponentRemovedEvent;
import com.kite.engine.event.entityevents.EntityDeletedEvent;

import java.security.InvalidParameterException;
import java.util.Vector;

/**
 * <p>
 * Represents an ECS Entity.
 * </p>
 *
 * <p>
 * An entity is just an integer representing the id of the entity in the registry,
 * but this class provides a useful wrapper to interface with the registry.
 * </p>
 *
 * <p>
 * An entity lives within the scene, therefore to create an entity
 * you need both a scene and a valid entity ID provided by a Registry.
 * </p>
 */
public class Entity
{
    /**
     * The ID of this entity
     */
    private int m_SceneID = Registry.INVALID_ENTITY_ID;

    /**
     * The scene in which this entity lives
     */
    private Scene m_Scene = null;

    /**
     * <p><b>Empty constructor.</b></p>
     * The object will be initialized to have: <p>
     *  -an invalid sceneID <p>
     *  -an invalid scene reference
     */
    protected Entity () { }

    /**
     * <p><b>Constructor.</b></p>
     * The object will be initialized to have: <p>
     *  -the specified sceneID <p>
     *  -the specified scene reference
     *
     * @param sceneID The id of this Entity
     * @param scene The scene in which this Entity lives
     */
    public Entity (int sceneID, Scene scene)
    {
        m_SceneID = sceneID;
        m_Scene = scene;
    }

    /**
     * Checks if an entity is valid, i.e. if it has been initialized correctly
     * @return
     * - True, if this entity is valid <p>
     * - False, otherwise
     */
    public boolean IsValid () { return m_Scene.GetRegistry().IsAValidID(m_SceneID); }

    /**
     * <p> Checks if the entity has the specified component </p>
     *
     * @param componentType The class of the component to check
     *
     * @return
     * -true if this entity has the specified component <p>
     * -false otherwise
     *
     * @throws NullPointerException if componentType is null
     * @throws InvalidParameterException if this entity has not a valid ID
     */
    public boolean HasComponent (Class<?> componentType)
    {
        return m_Scene.GetRegistry().HasComponent(componentType, m_SceneID);
    }

    /**
     * <p> Adds the specified component to this entity. </p>
     *
     * @param <T> The type of the component to add
     * @param component The component to add
     *
     * @return The newly added component
     *
     * @throws NullPointerException if component is null
     * @throws InvalidParameterException if this entity has not a valid ID
     *
     * <p> </p>
     * <p>
     * <b>Special cases:</b> <p>
     * - if this entity already has the component the component will be overwritten
     * </p>
     */
    public <T> T AddComponent (T component)
    {
        if (ScriptComponent.class.isAssignableFrom(component.getClass()))
        {
            ((ScriptComponent) component).SetEntity(this);
            ((ScriptComponent) component).OnAttach();
        }

        T addedComponent = m_Scene.GetRegistry().AddComponent(component, m_SceneID);
        EventHandler.PropagateEvent(new EntityComponentAddedEvent(m_Scene, m_SceneID, component.getClass(), component));

        return addedComponent;
    }

    /**
     * <p> Removes the component of the specified class from this entity. </p>
     *
     * @param componentType The class of the component to remove
     *
     * @return
     * The removed component, if it was removed successfully, null otherwise
     *
     * @throws NullPointerException if the componentType is null
     * @throws InvalidParameterException if this entity has not a valid ID
     *
     * <p> </p>
     * <p>
     * <b>Special cases:</b> <p>
     * - if this entity does not contain the specified component,
     *      the method does nothing and returns null
     * </p>
     */
    public <T> T RemoveComponent (Class<T> componentType)
    {
        T component = m_Scene.GetRegistry().RemoveComponent(componentType, m_SceneID);

        if (component != null)
            EventHandler.PropagateEvent(new EntityComponentRemovedEvent(m_Scene, m_SceneID, componentType, component));

        return component;
    }

    /**
     * Deletes this entity from the scene, this ID will be invalidated
     */
    public void Delete ()
    {
        EventHandler.PropagateEvent(new EntityDeletedEvent(m_Scene, m_SceneID));
        m_Scene.GetRegistry().DeleteEntity(m_SceneID);
        m_SceneID = Registry.INVALID_ENTITY_ID;
    }

    /**
     * <p>Gets the component of the specified type from this Entity</p>
     *
     * @param <T> The type of the component
     * @param componentType The class of the component
     *
     * @return
     * The component instance, if found <p>
     * Null, if this entity does not contain the component
     *
     * @throws InvalidParameterException if this entity has not a valid ID
     */
    public <T> T GetComponent (Class<T> componentType)
    {
        return m_Scene.GetRegistry().GetComponent(componentType, m_SceneID);
    }

    /**
     * Gets the components of the specified supertype from this Entity<p>
     *
     * @param componentType The supertype of the component
     *
     * @return The Vector containing all the found components.
     *
     * @throws InvalidParameterException if entityID is not a valid ID
     */
    public <T> Vector<T> GetComponents (Class<T> componentType)
    {
        return m_Scene.GetRegistry().GetComponents(componentType, m_SceneID);
    }

    /**
     * <p>Retrieves a list of all the components associated with this entity.</p>
     * <p><b>Warning:</b> this method is slow and should be used carefully</p>
     *
     * @return An array of Object containing all the components associated with this entity.
     *
     * @throws InvalidParameterException if this entity is not {@link Entity#IsValid() valid}
     */
    public Object[] GetAllComponents () { return m_Scene.GetRegistry().GetAllComponents(m_SceneID); }

    public int GetId () { return m_SceneID; }
    public Scene GetScene () { return m_Scene; }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;

        if (obj instanceof Entity)
        {
            return m_SceneID == ((Entity) obj).m_SceneID;
        }

        return false;
    }

    @Override
    public int hashCode ()
    {
        return Integer.hashCode(m_SceneID);
    }

    @Override
    public String toString ()
    {
        return "[Entity: (id: " + m_SceneID + ")]";
    }
}
