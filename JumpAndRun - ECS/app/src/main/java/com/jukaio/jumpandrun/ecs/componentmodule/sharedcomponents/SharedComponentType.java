package com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents;

public enum SharedComponentType
{
    // TODO: ADD TYPES HERE
    INVALID(-1),
    RENDER_CANVAS(0),
    GRAVITY(1),
    INPUT_CONTROLLER(2);

    private int m_value;
    private SharedComponentType(int value)
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
