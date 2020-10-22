package com.jukaio.jumpandrun.ecs.componentmodule.components;

import com.jukaio.jumpandrun.ecs.entitymodule.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class ComponentManager
{
    public ComponentManager()
    {
        m_type_to_index = new HashMap<>();
        m_component_look_ups = new ArrayList<>();
        m_registered_components = 0;
        LinkedHashMap<Integer, Component> map = new LinkedHashMap<>();
    }

    public void register_components(int p_signature)
    {
        ComponentType[] values = ComponentType.values();
        for(ComponentType value : values)
        {
            if((p_signature & value.as_bitmask()) == value.as_bitmask())
            {
                register_component(value);
            }
        }
    }

    public void register_component(ComponentType p_type)
    {
        if(m_type_to_index.containsKey(p_type))
            return;
        m_type_to_index.put(p_type, m_registered_components);
        m_registered_components++;
        m_component_look_ups.add(new HashMap<Integer, Component>());
    }

    public void add_component(Entity p_entity, Component p_component)
    {
        int index = m_type_to_index.get(p_component.get_type());
        m_component_look_ups.get(index).put(p_entity.get_id(), p_component);
    }


    public void remove_component(Entity p_entity, ComponentType p_type)
    {
        int index = m_type_to_index.get(p_type);
        m_component_look_ups.get(index).remove(p_entity.get_id());
    }

    public <T extends Component> T get_component(Entity p_entity, ComponentType p_type)
    {
        int index = m_type_to_index.get(p_type);
        return (T) m_component_look_ups.get(index).get(p_entity.get_id());
    }
    
    public <T extends Component> Collection<T> get_all_components_of_type(ComponentType p_type)
    {
        HashMap<Integer, Component> map = m_component_look_ups.get(m_type_to_index.get(p_type));
        return (Collection<T>) map.values();
    }
    
    public void destory()
    {
        m_type_to_index.clear();
        m_component_look_ups.clear();
        m_registered_components = 0;
        m_type_to_index = null;
        m_component_look_ups = null;
    }

    int m_registered_components;
    HashMap<ComponentType, Integer> m_type_to_index;
    ArrayList<HashMap<Integer, Component>> m_component_look_ups;
}
