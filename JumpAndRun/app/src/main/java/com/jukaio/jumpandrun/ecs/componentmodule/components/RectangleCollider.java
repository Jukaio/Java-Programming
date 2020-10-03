package com.jukaio.jumpandrun.ecs.componentmodule.components;

import android.graphics.RectF;

public class RectangleCollider extends Component
{
    public RectF bounts;

    @Override
    public ComponentType get_type() {
        return ComponentType.RECTANGLE_COLLIDER;
    }
}
