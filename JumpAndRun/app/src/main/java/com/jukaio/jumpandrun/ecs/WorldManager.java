package com.jukaio.jumpandrun.ecs;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class WorldManager
{
    public WorldManager()
    {
        m_worlds = new HashMap<WorldType, ElementSignaturePair<World>>();
    }

    public void set_world_active(WorldType p_type)
    {
        m_active = m_worlds.get(p_type);
    }

    public ElementSignaturePair<World> get_active_world_pair()
    {
        return m_active;
    }

    public void add_world(World p_world, WorldType p_type)
    {
        m_worlds.put(p_type, new ElementSignaturePair<World>(p_world, 0));
    }

    public void add_world(World p_world, WorldType p_type, int p_signature)
    {
        m_worlds.put(p_type, new ElementSignaturePair<World>(p_world, p_signature));
    }

    public void add_signature(WorldType p_type, int p_signature)
    {
        m_worlds.get(p_type).m_signature |= p_signature;
    }

    public World get_world(WorldType p_type)
    {
        return m_worlds.get(p_type).m_element;
    }

    public ElementSignaturePair<World> get_pair(WorldType p_type)
    {
        return m_worlds.get(p_type);
    }

    public int get_signature(WorldType p_type)
    {
        return m_worlds.get(p_type).m_signature;
    }

    private HashMap<WorldType, ElementSignaturePair<World>> m_worlds;
    private ElementSignaturePair<World> m_active;
}
