package com.jukaio.jumpandrun.ecs.componentmodule.components;

public enum ComponentType
{
    // TODO: ADD TYPES HERE
    INVALID(-1),
    TRANSFORM(0),
    TILE_MAP(1),
    TILE_MAP_COLLIDER(2),
    TILE_SET(3),
    SOURCE_XML(4),
    GRID(5),
    SPRITE(6),
    RECTANGLE_COLLIDER(7),
    PLAYER(8);

    private int m_value;
    private ComponentType(int value)
    {
        m_value = value;
    }

    public int as_int()
    {
        return m_value;
    }

    public int as_bitmask()
    {
        return 1 << m_value;
    }
}
