package com.jukaio.jumpandrun.ecs.entitymodule;

public class Entity
{
    public static int MAX_ENTITIES = 3000;
    private int m_id = -1;

    Entity(int p_id)
    {
        m_id = p_id;
    }

    public int get_id()
    {
        return m_id ;
    }

}
