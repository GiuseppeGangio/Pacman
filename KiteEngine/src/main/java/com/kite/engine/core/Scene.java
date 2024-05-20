package com.kite.engine.core;

import com.kite.engine.ecs.Entity;
import com.kite.engine.ecs.components.*;
import com.kite.engine.ecs.registry.*;
import com.kite.engine.event.*;
import com.kite.engine.event.entityevents.*;
import com.kite.engine.event.sceneEvents.*;
import com.kite.engine.rendering.*;
import com.kite.engine.rendering.gizmo.GizmoRenderer;
import org.dyn4j.collision.CategoryFilter;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.ContinuousDetectionMode;
import org.dyn4j.dynamics.PhysicsBody;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Transform;
import org.dyn4j.world.NarrowphaseCollisionData;
import org.dyn4j.world.World;
import org.dyn4j.world.listener.CollisionListenerAdapter;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.Vector;


public class Scene
{
    private final Registry m_Registry;
    private final World<PhysicsBody> m_World;
    private boolean m_IsRunning = false;

    public Scene ()
    {
        m_Registry = new Registry();
        m_World = new World<>();

        m_World.addCollisionListener(new CollisionListenerAdapter<PhysicsBody, BodyFixture>()
        {
            @Override
            public boolean collision(NarrowphaseCollisionData<PhysicsBody, BodyFixture> collision)
            {
                return OnCollision(collision);
            }
        });

        EventHandler.Subscribe(EventType.ENTITY_COMPONENT_ADDED, this::OnEntityComponentAdded);
        EventHandler.Subscribe(EventType.ENTITY_COMPONENT_REMOVED, this::OnEntityComponentRemoved);
        EventHandler.Subscribe(EventType.ENTITY_DELETED, this::OnEntityDeleted);
    }

    public void ReloadSettings ()
    {
        Settings.PhysicsSettings physicsSettings = Application.Get().GetSettings()._PhysicsSettings;

        org.dyn4j.dynamics.Settings dyn4jSettings = new org.dyn4j.dynamics.Settings();
        dyn4jSettings.setStepFrequency(physicsSettings.StepFrequency);
        dyn4jSettings.setVelocityConstraintSolverIterations(physicsSettings.VelocityConstraintSolverIterations);
        dyn4jSettings.setPositionConstraintSolverIterations(physicsSettings.PositionConstraintSolverIterations);

        switch (physicsSettings.ContinuousDetectionMode)
        {
            case NONE:
                dyn4jSettings.setContinuousDetectionMode(ContinuousDetectionMode.NONE);
            case BULLETS_ONLY:
                dyn4jSettings.setContinuousDetectionMode(ContinuousDetectionMode.BULLETS_ONLY);
                break;
            case ALL:
                dyn4jSettings.setContinuousDetectionMode(ContinuousDetectionMode.ALL);
                break;
        }

        m_World.setSettings(dyn4jSettings);
        m_World.setGravity(physicsSettings.Gravity.x, physicsSettings.Gravity.y);
    }

    public Entity CreateEntity (String name)
    {
        Entity entity = new Entity(m_Registry.CreateEntity(), this);
        entity.AddComponent(new TagComponent(!(name.isEmpty()) ? name : "Entity"));

        TransformComponent transform = new TransformComponent();
        transform.Entity = entity;
        entity.AddComponent(transform);

        EventHandler.PropagateEvent(new EntityCreatedEvent(this, entity.GetId()));

        return entity;
    }

    public Entity CreateEntity () { return CreateEntity(""); }

    public Entity GetEntity (String name)
    {
        View<TagComponent> tags = m_Registry.ViewComponent(TagComponent.class);

        for (int entityID : tags)
        {
            TagComponent tagComponent = tags.Get(entityID);
            if (tagComponent.Tag.equals(name))
                return new Entity(entityID, this);
        }

        return new Entity(Registry.INVALID_ENTITY_ID, this);
    }

    public void Start ()
    {
        m_IsRunning = true;

        View<RigidBodyComponent> rigidBodies = m_Registry.ViewComponent(RigidBodyComponent.class);

        for (int entity : rigidBodies)
        {
            TransformComponent transform = m_Registry.GetComponent(TransformComponent.class, entity);
            RigidBodyComponent rigidBody = rigidBodies.Get(entity);
            ColliderComponent collider = m_Registry.GetComponent(ColliderComponent.class, entity);

            PhysicsBody body = CreatePhysicsBody(transform, rigidBody, collider);
            rigidBody.BodyRef = body;
            m_World.addBody(body);
        }

        EventHandler.PropagateEvent(new SceneStartedEvent(this));
    }

    public void End ()
    {
        m_IsRunning = false;
        EventHandler.PropagateEvent(new SceneEndedEvent(this));
    }

    public Entity MousePick (int mouseX, int mouseY)
    {
        int colorID = MousePicker.MousePick(mouseX, mouseY);

        View<MousePickableComponent> mousePickables = m_Registry.ViewComponent(MousePickableComponent.class);

        for (int entityID : mousePickables)
            if (mousePickables.Get(entityID).GetID() == colorID)
                return new Entity(entityID, this);

        return new Entity(Registry.INVALID_ENTITY_ID, this);
    }

    public Registry GetRegistry () { return m_Registry; }

    public void OnEvent (Event event)
    {
        View<ScriptComponent> scriptComponents = m_Registry.ViewComponent(ScriptComponent.class);

        for (int id : scriptComponents)
        {
            ScriptComponent scriptComponent = scriptComponents.Get(id);
            scriptComponent.OnEvent(event);
        }
    }

    public void Run ()
    {
        // Script components
        View<ScriptComponent> scriptComponents = m_Registry.ViewComponent(ScriptComponent.class);

        for (int id : scriptComponents)
        {
            ScriptComponent scriptComponent = scriptComponents.Get(id);
            scriptComponent.OnUpdate();
        }

        //Physics
        {
            m_World.update(Time.s_DeltaTime / 1000d);

            View<RigidBodyComponent> rigidBodies = m_Registry.ViewComponent(RigidBodyComponent.class);
            for (int entity : rigidBodies)
            {
                RigidBodyComponent rigidBody = rigidBodies.Get(entity);
                TransformComponent transform = m_Registry.GetComponent(TransformComponent.class, entity);

                PhysicsBody body = rigidBody.BodyRef;

                Transform dyn4jTransform = body.getTransform();

                Vector2f absolutePosition = new Vector2f((float)dyn4jTransform.getTranslationX(), (float)dyn4jTransform.getTranslationY());
                Vector2f parentPosition = transform.GetParent() != null ? transform.GetParent().GetAbsolutePosition() : new Vector2f();
                transform.Position = absolutePosition.sub(parentPosition);

                transform.Rotation = (float) Math.toDegrees(dyn4jTransform.getRotationAngle());
            }


        }

        // Rendering
        Camera camera = null;
        Matrix4f cameraTransform = null;
        {
            Group<TransformComponent, CameraComponent> group =
                    m_Registry.GroupComponents(TransformComponent.class, CameraComponent.class);

            for (int entity : group)
            {
                Group.GroupNode<TransformComponent, CameraComponent> node = group.Get(entity);

                camera = node.Second.Camera;
                cameraTransform = ComponentHelper.GetTransform(node.First, new Matrix4f());
                break;
            }
        }

        if (camera != null)
        {
            // Normal rendering
            {
                Renderer.Clear();
                Renderer.StartScene(cameraTransform, camera.GetProjectionMatrix());
                {
                    Group<TransformComponent, SpriteComponent> group =
                            m_Registry.GroupComponents(TransformComponent.class, SpriteComponent.class);

                    Matrix4f transform = new Matrix4f();
                    for (int entity : group)
                    {
                        Group.GroupNode<TransformComponent, SpriteComponent> node = group.Get(entity);
                        Renderer.Render(
                                ComponentHelper.GetTransform(node.First, transform),
                                node.Second.Sprite);
                    }

                    // text rendering
                    Group<TransformComponent, LabelComponent> textGroup =
                            m_Registry.GroupComponents(TransformComponent.class, LabelComponent.class);

                    Matrix4f textTransform = new Matrix4f();
                    for (int entity : textGroup)
                    {
                        Group.GroupNode<TransformComponent, LabelComponent> node = textGroup.Get(entity);
                        TransformComponent transformComponent = node.First;
                        LabelComponent labelComponent = node.Second;
                        Renderer.RenderText(
                                ComponentHelper.GetTransform(transformComponent, transform),
                                labelComponent.Color,
                                labelComponent.UsedFont,
                                labelComponent.Text);
                    }
                }

                Renderer.EndScene();
            }

            // Mouse picking rendering
            {
                MousePicker.GetFrameBuffer().Bind();

                Renderer.Clear();
                Renderer.StartScene(cameraTransform, camera.GetProjectionMatrix());
                {
                    Group<TransformComponent, SpriteComponent> group =
                            m_Registry.GroupComponents(TransformComponent.class, SpriteComponent.class);

                    Matrix4f transform = new Matrix4f();

                    // TODO: test for texture, might be broken due to references
                    Texture tmpTexture;
                    final Vector4f tmpColor = new Vector4f();

                    for (int entity : group)
                    {
                        if (m_Registry.HasComponent(MousePickableComponent.class, entity))
                        {
                            Group.GroupNode<TransformComponent, SpriteComponent> node = group.Get(entity);
                            MousePickableComponent pick = m_Registry.GetComponent(MousePickableComponent.class, entity);

                            tmpTexture = node.Second.Sprite.Texture;
                            tmpColor.set(node.Second.Sprite.Color);

                            node.Second.Sprite.Texture = Sprite.WhiteTexture();
                            node.Second.Sprite.Color = Utils.IntegerToRGBAColor(pick.GetID());

                            Renderer.Render(
                                ComponentHelper.GetTransform(node.First, transform),
                                node.Second.Sprite);

                            node.Second.Sprite.Texture = tmpTexture;
                            node.Second.Sprite.Color.set(tmpColor);
                        }
                    }
                }

                Renderer.EndScene();

                MousePicker.GetFrameBuffer().Unbind();
            }

            // Gizmo rendering
            {
                for (int id : scriptComponents)
                {
                    ScriptComponent scriptComponent = scriptComponents.Get(id);
                    scriptComponent.OnGizmo();
                }

                GizmoRenderer.Flush(cameraTransform, camera.GetProjectionMatrix());
            }
        }
    }

    private boolean OnCollision (NarrowphaseCollisionData<PhysicsBody, BodyFixture> collisionData)
    {
        Group<RigidBodyComponent, ColliderComponent> group = m_Registry.GroupComponents(RigidBodyComponent.class, ColliderComponent.class);

        PhysicsBody body1 = collisionData.getBody1();
        PhysicsBody body2 = collisionData.getBody2();

        int entity1 = Registry.INVALID_ENTITY_ID;
        int entity2 = Registry.INVALID_ENTITY_ID;

        for (int entity : group)
        {
            Group.GroupNode<RigidBodyComponent, ColliderComponent> node = group.Get(entity);
            RigidBodyComponent rigidBodyComponent = node.First;
            ColliderComponent colliderComponent = node.Second;

            if (entity1 == Registry.INVALID_ENTITY_ID && rigidBodyComponent.BodyRef == body1)
            {
                entity1 = entity;
            }
            else if (entity2 == Registry.INVALID_ENTITY_ID && rigidBodyComponent.BodyRef == body2)
            {
                entity2 = entity;
            }

            if (entity1 != Registry.INVALID_ENTITY_ID && entity2 != Registry.INVALID_ENTITY_ID)
            {
                break;
            }
        }

        if (entity1 != Registry.INVALID_ENTITY_ID && entity2 != Registry.INVALID_ENTITY_ID)
        {
            Vector<ScriptComponent> scriptComponents1 = m_Registry.GetComponents(ScriptComponent.class, entity1);
            Vector<ScriptComponent> scriptComponents2 = m_Registry.GetComponents(ScriptComponent.class, entity2);

            for (ScriptComponent script : scriptComponents1)
                script.OnCollision(new Entity(entity2, this));

            for (ScriptComponent script : scriptComponents2)
                script.OnCollision(new Entity(entity1, this));
        }

        return true;
    }

    private void OnEntityComponentAdded (Boolean isTest, EntityComponentAddedEvent event)
    {
        if (!isTest && event.SceneRef == this)
        {
            if (m_IsRunning)
            {
                if (event.ComponentType.equals(RigidBodyComponent.class))
                {
                    TransformComponent transform = m_Registry.GetComponent(TransformComponent.class, event.EntityID);
                    RigidBodyComponent rigidBody = m_Registry.GetComponent(RigidBodyComponent.class, event.EntityID);
                    PhysicsBody body = CreatePhysicsBody(transform, rigidBody, null);

                    rigidBody.BodyRef = body;
                    m_World.addBody(body);
                }
                else if (event.ComponentType.equals(ColliderComponent.class))
                {
                    TransformComponent transform = m_Registry.GetComponent(TransformComponent.class, event.EntityID);
                    RigidBodyComponent rigidBody = m_Registry.GetComponent(RigidBodyComponent.class, event.EntityID);
                    ColliderComponent collider = m_Registry.GetComponent(ColliderComponent.class, event.EntityID);

                    PhysicsBody body = CreatePhysicsBody(transform, rigidBody, collider);

                    m_World.removeBody(rigidBody.BodyRef);
                    rigidBody.BodyRef = body;
                    m_World.addBody(body);
                }
            }
        }
    }

    private void OnEntityComponentRemoved (Boolean isTest, EntityComponentRemovedEvent event)
    {
        if (!isTest && event.SceneRef == this)
        {
            if (event.ComponentType.equals(RigidBodyComponent.class))
            {
                RigidBodyComponent rigidBody = (RigidBodyComponent) event.Component;
                m_World.removeBody(rigidBody.BodyRef);
            }
            else if (event.ComponentType.equals(ColliderComponent.class))
            {
                TransformComponent transform = m_Registry.GetComponent(TransformComponent.class, event.EntityID);
                RigidBodyComponent rigidBody = m_Registry.GetComponent(RigidBodyComponent.class, event.EntityID);
                PhysicsBody body = CreatePhysicsBody(transform, rigidBody, null);

                m_World.removeBody(rigidBody.BodyRef);
                rigidBody.BodyRef = body;
                m_World.addBody(body);
            }
        }
    }

    private void OnEntityDeleted (boolean isTest, EntityDeletedEvent event)
    {
        if (!isTest && event.SceneRef == this)
        {
            TransformComponent transform = m_Registry.GetComponent(TransformComponent.class, event.EntityID);
            TransformComponent[] childrenTransforms = transform.GetChildren();

            transform.SetParent(null);
            for (TransformComponent children : childrenTransforms)
                children.SetParent(null);
        }
    }

    private PhysicsBody CreatePhysicsBody (TransformComponent transform, RigidBodyComponent rigidBody, ColliderComponent collider)
    {
        PhysicsBody body = new Body();

        MassType massType = MassType.NORMAL;
        switch (rigidBody.Type)
        {
            case STATIC:
                massType = MassType.INFINITE;
                break;
            case ROTATIONAL_STATIC:
                massType = MassType.FIXED_ANGULAR_VELOCITY;
                break;
            case LINEARLY_STATIC:
                massType = MassType.FIXED_LINEAR_VELOCITY;
                break;
        }

        Vector2f position = transform.GetAbsolutePosition();

        body.translateToOrigin();
        body.translate(position.x, position.y);
        body.rotate(Math.toRadians(transform.Rotation), position.x, position.y);

        if (collider != null)
        {
            Convex convex = Geometry.createRectangle(
                    transform.Scale.x * collider.Size.x,
                    transform.Scale.y * collider.Size.y);

            BodyFixture fixture = new BodyFixture(convex);
            fixture.setFilter(new CategoryFilter(collider.FilterCategory, collider.FilterMask));
            fixture.setDensity(collider.Density);
            fixture.setFriction(collider.Friction);
            fixture.setRestitution(collider.Restitution);
            fixture.setRestitutionVelocity(collider.RestitutionThreshold);
            fixture.setSensor(collider.Traversable);

            body.addFixture(fixture);
        }

        body.setMass(massType);

        return body;
    }
}
