package com.jukaio.jumpandrun.collision;

public abstract class Collider
{
    private ColliderType m_type;

    Collider(ColliderType p_type)
    {
        m_type = p_type;
    }

    final public ColliderType get_type()
    {
        return m_type;
    }
}
