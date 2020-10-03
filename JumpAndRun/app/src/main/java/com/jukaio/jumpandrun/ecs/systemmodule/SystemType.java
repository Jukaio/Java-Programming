package com.jukaio.jumpandrun.ecs.systemmodule;

public enum SystemType
{
    // TODO: ADD TYPES HERE
    INVALID(-1),
    LOCK_CANVAS(0),
    UNLOCK_CANVAS(1),
    TILE_MAP(2),
    GRID_RENDERER(3),
    PHYSICS(4),
    BITMAP_RENDERER(5),
    PLAYER(6);

    private int m_value;
    private SystemType(int value)
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