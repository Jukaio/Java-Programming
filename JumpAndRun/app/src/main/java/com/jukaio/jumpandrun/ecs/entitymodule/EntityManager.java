package com.jukaio.jumpandrun.ecs.entitymodule;


import java.util.ArrayList;


public class EntityManager
{
    private ArrayList<Entity> m_available_entities = new ArrayList<Entity>();
    private int[] m_signatures = new int[Entity.MAX_ENTITIES];

    private long m_last_entity;

    public EntityManager()
    {
        m_last_entity = 0;

        for(int i = 0; i < Entity.MAX_ENTITIES; i++)
        {
            m_available_entities.add(new Entity(i));
            m_signatures[i] = 0;
        }
    }

    public Entity create_entity()
    {
        // TODO: Check if m_last_entity > MAX_ENTITIES
        Entity id = m_available_entities.get(0);
        m_available_entities.remove(0);
        m_last_entity++;
        return id;
    }

    Entity destroy_entity(Entity p_entity)
    {
        // TODO: Check if m_last_entity > MAX_ENTITIES
        Entity id = m_available_entities.get(0);
        m_signatures[p_entity.get_id()] = 0;
        m_available_entities.remove(p_entity.get_id());
        m_last_entity--;
        return id;
    }

    public void set_signature(Entity p_entity, int p_signature)
    {
        m_signatures[p_entity.get_id()] = p_signature;
    }

    public void add_signature(Entity p_entity, int p_signature)
    {
        m_signatures[p_entity.get_id()] |= p_signature;
    }

    public void remove_signature(Entity p_entity, int p_signature)
    {
        int signature=  m_signatures[p_entity.get_id()];
        signature = signature & ~p_signature;
        m_signatures[p_entity.get_id()] |= signature;
    }

    public int get_signature(Entity p_entity)
    {
        return m_signatures[p_entity.get_id()];
    }

}
