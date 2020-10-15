package com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents;

import java.util.HashMap;

public class SharedComponentsManager
{
    public SharedComponentsManager()
    {
        m_shared_components = new HashMap<>();
    }

    public void add_shared_component(SharedComponent p_component)
    {
        m_shared_components.put(p_component.get_type(), p_component);
    }

    public <T extends SharedComponent> T get_shared_component(SharedComponentType p_type)
    {
        return (T) m_shared_components.get(p_type);
    }
    
    public boolean is_registered(SharedComponentType p_type)
    {
        return m_shared_components.containsKey(p_type);
    }

   private HashMap<SharedComponentType, SharedComponent> m_shared_components;
   
   public void destroy()
   {
        m_shared_components.clear();
        m_shared_components = null;
   }
}
