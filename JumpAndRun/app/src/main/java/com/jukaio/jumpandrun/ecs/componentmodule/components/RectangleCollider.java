package com.jukaio.jumpandrun.ecs.componentmodule.components;

import android.graphics.RectF;

public class RectangleCollider extends Component
{
    public RectF m_bounds = new RectF();

    @Override
    public ComponentType get_type() {
        return ComponentType.RECTANGLE_COLLIDER;
    }
}
