package com.jukaio.jumpandrun.ecs.systemmodule;

import java.util.HashMap;

public class SystemManager
{
    HashMap<SystemType, Integer> m_system_signature;
    HashMap<SystemType, System> m_systems;

    public SystemManager()
    {
        m_system_signature = new HashMap<SystemType, Integer>();
        m_systems = new HashMap<SystemType, System>();
    }

    public void register_system(System p_system, Integer p_signature)
    {
        m_system_signature.put(p_system.get_type(), p_signature);
        m_systems.put(p_system.get_type(), p_system);
    }

    public <T extends System> T get_system(SystemType p_type)
    {
        return (T) m_systems.get(p_type);
    }

    public int get_signature(System system)
    {
        return m_system_signature.get(system.get_type());
    }
    public int get_signature(SystemType p_type)
    {
        return m_system_signature.get(p_type);
    }
}
