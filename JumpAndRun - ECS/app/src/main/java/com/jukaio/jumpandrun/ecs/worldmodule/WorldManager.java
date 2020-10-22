package com.jukaio.jumpandrun.ecs.worldmodule;

import java.util.HashMap;
import java.util.Map;

public class WorldManager
{
    public WorldManager()
    {
        m_worlds = new HashMap<WorldType, World>();
    }

    public void set_world_active(WorldType p_type)
    {
        m_active = m_worlds.get(p_type);
    }

    public World get_active_world()
    {
        return m_active;
    }

    public void add_world(World p_world, WorldType p_type)
    {
        m_worlds.put(p_type, p_world);
    }

    public World get_world(WorldType p_type)
    {
        return m_worlds.get(p_type);
    }

    public void destroy()
    {
        for(Map.Entry<WorldType, World> world : m_worlds.entrySet())
        {
            world.getValue().m_entities.clear();
            world.getValue().m_entities = null;
        }
        
        m_worlds.clear();
        m_worlds = null;
        m_active = null;
    }


    private HashMap<WorldType, World> m_worlds;
    private World m_active;
}
