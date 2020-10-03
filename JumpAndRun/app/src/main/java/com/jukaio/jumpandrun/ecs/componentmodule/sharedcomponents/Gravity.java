package com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents;

import com.jukaio.jumpandrun.extramath.Vector2;

public class Gravity extends SharedComponent
{
    @Override
    public SharedComponentType get_type()
    {
        return SharedComponentType.GRAVITY;
    }

    public float m_drag;
    public Vector2 m_direction;

}
