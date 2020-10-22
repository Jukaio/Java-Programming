package com.jukaio.jumpandrun.ecs.systemmodule;

public enum SystemType
{
    // TODO: ADD TYPES HERE
    INVALID(-1),
    TILE_MAP(0),
    GRID_RENDERER(1),
    MOVEMENT_SENSORS_TILEMAP_COLLISION(2),
    SINGLE_TILE_RENDERER(3),
    UPDATE_COLLIDER_POSITION(4),
    COLLIDER_RENDERER_DEBUG(5),
    MOVEMENT_SENSOR_RENDERER_DEBUG(6),
    UPDATE_MOVEMENT_SENSOR_POSITION(7),
    INPUT_HANDLER(8),
    APPLY_FORCES(9),
    PREPARE_JUMP(10),
    JUMP(11);

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