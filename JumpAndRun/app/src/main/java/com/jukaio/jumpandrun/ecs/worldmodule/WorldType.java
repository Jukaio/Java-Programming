package com.jukaio.jumpandrun.ecs.worldmodule;

public enum WorldType
{
    INVALID(-1),
    LEVEL_ONE(0);

    WorldType(int p_value)
    {
        m_value = p_value;
    }
    public int get_int()
    {
        return m_value;
    }

    private int m_value;
}
