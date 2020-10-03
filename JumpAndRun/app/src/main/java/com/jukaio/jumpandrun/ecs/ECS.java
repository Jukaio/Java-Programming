package com.jukaio.jumpandrun.ecs;

import android.graphics.Color;

import com.jukaio.jumpandrun.ecs.componentmodule.components.Component;
import com.jukaio.jumpandrun.ecs.componentmodule.components.ComponentManager;
import com.jukaio.jumpandrun.ecs.componentmodule.components.ComponentType;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.RenderCanvas;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.SharedComponent;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.SharedComponentType;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.SharedComponentsManager;
import com.jukaio.jumpandrun.ecs.entitymodule.Entity;
import com.jukaio.jumpandrun.ecs.entitymodule.EntityManager;
import com.jukaio.jumpandrun.ecs.systemmodule.System;
import com.jukaio.jumpandrun.ecs.systemmodule.SystemManager;
import com.jukaio.jumpandrun.ecs.systemmodule.SystemType;

import java.util.ArrayList;
import java.util.HashMap;

public class ECS
{
    private HashMap<SystemType, Integer> m_signatures;

    private EntityManager e = null;
    private ComponentManager c = null;
    private SystemManager s = null;
    private SharedComponentsManager sc = null;
    private WorldManager w = null;

    private ArrayList<System> m_systems;

    public ECS()
    {
        m_systems = new ArrayList<System>();

        e = new EntityManager();
        c = new ComponentManager();
        s = new SystemManager();
        sc = new SharedComponentsManager();
        w = new WorldManager();
    }

    public Entity create_entity()
    {
        return e.create_entity();
    }

    public void create_world(WorldType p_type)
    {
        World world = new World();
        world.m_entities = new ArrayList<>();
        w.add_world(world, p_type);
    }
    public void set_world_active(WorldType p_type)
    {
        w.set_world_active(p_type);
    }

    public void init()
    {
        for(WorldType type : WorldType.values())
        {
            for(System system : m_systems)
            {
                for(WorldType world_type : WorldType.values())
                {
                    if(world_type == WorldType.INVALID)
                        continue;;

                    system.init_system(this, w.get_world(world_type).m_entities);
                }
            }
        }
    }

    public void update()
    {
        ElementSignaturePair<World> world_data = w.get_active_world_pair();
        for(System system : m_systems)
        {
            final int system_signature = s.get_signature(system);
            if((system_signature & world_data.m_signature) == system_signature)
            {
                system.update(this, world_data.m_element.m_entities);
            }
        }
    }

    public void render()
    {
        RenderCanvas renderer = sc.get_shared_component(SharedComponentType.RENDER_CANVAS);

        if(!lock_canvas(renderer))
            return;
        renderer.m_canvas.drawColor(Color.BLACK);

        ElementSignaturePair<World> world_data = w.get_active_world_pair();
        for(System system : m_systems)
        {
            final int system_signature = s.get_signature(system);
            if((system_signature & world_data.m_signature) == system_signature)
            {
                system.render(this, world_data.m_element.m_entities);
            }
        }

        renderer.m_holder.unlockCanvasAndPost(renderer.m_canvas);

        s.get_system(SystemType.UNLOCK_CANVAS);
    }

    public void place_entity(Entity p_enity, WorldType p_world)
    {
        ElementSignaturePair<World> world = w.get_pair(p_world);
        world.m_signature |= e.get_signature(p_enity);
        world.m_element.m_entities.add(p_enity);
    }

    public void add_world(World p_world, WorldType p_type, int p_signature)
    {
        w.add_world(p_world, p_type, p_signature);
    }

    public void register_shared_component(SharedComponent p_component)
    {
        sc.add_shared_component(p_component);
    }

    public void add_shared_component(Entity p_entity, ComponentType p_type)
    {
        Component component = get_component(p_entity, p_type);
        add_component(p_entity, component);
    }

    public <T extends SharedComponent> T get_shared_component(SharedComponentType p_type)
    {
        return (T) sc.get_shared_component(p_type);
    }

    public void register_components(int p_signature)
    {
        c.register_components(p_signature);
    }

    public void register_component(ComponentType p_type)
    {
        c.register_component(p_type);
    }

    public int components_to_signature(Component[] p_components)
    {
        int signature = 0;
        for(Component component : p_components)
        {
            signature |= component.get_type().as_bitmask();
        }
        return signature;
    }

    public int components_to_signature(ComponentType[] p_types)
    {
        int signature = 0;
        for(ComponentType component : p_types)
        {
            signature |= component.as_bitmask();
        }
        return signature;
    }

    // Add Array of components and return signature
    public int add_components(Entity p_entity, Component[] p_component)
    {
        for(Component component : p_component)
        {
            add_component(p_entity, component);
        }
        return e.get_signature(p_entity);
    }

    public void add_component(Entity p_entity, Component p_component)
    {
        e.add_signature(p_entity, p_component.get_type().as_bitmask());
        c.add_component(p_entity, p_component);
    }


    public void remove_component(Entity p_entity, Component p_component)
    {
        e.remove_signature(p_entity, p_component.get_type().as_bitmask());
        c.remove_component(p_entity, p_component.get_type());
    }

    public void add_system(System p_system)
    {
        m_systems.add(p_system);
    }

    public void register_system(System p_system, Integer p_signature)
    {
        s.register_system(p_system, p_signature);
    }

    public int get_system_signature(SystemType p_type)
    {
        return s.get_signature(p_type);
    }
    public int get_system_signature(System p_system)
    {
        return s.get_signature(p_system.get_type());
    }

    public int get_entity_signature(Entity p_entity)
    {
        return e.get_signature(p_entity);
    }

    public <T extends Component> T get_component(Entity p_entity, ComponentType p_type)
    {
        return c.get_component(p_entity, p_type);
    }

    private boolean lock_canvas(RenderCanvas p_renderer)
    {
        if(!p_renderer.m_holder.getSurface().isValid())
            return false;

        p_renderer.m_canvas = p_renderer.m_holder.lockCanvas();
        return (p_renderer.m_canvas != null);
    }
}
