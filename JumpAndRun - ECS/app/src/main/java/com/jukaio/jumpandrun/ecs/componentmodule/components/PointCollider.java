package com.jukaio.jumpandrun.ecs.componentmodule.components;

import android.graphics.PointF;

public class PointCollider extends Component
{
    @Override
    public ComponentType get_type()
    {
        return ComponentType.POINT_COLLIDER;
    }
    
    public PointF m_point = new PointF();
}
