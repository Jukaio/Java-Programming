package com.jukaio.jumpandrun.ecs.componentmodule.components;

import android.graphics.Point;
import android.graphics.PointF;

import com.jukaio.jumpandrun.extramath.Line;

import java.util.ArrayList;

public class LineCollider extends Component
{
    @Override
    public ComponentType get_type()
    {
        return ComponentType.LINE_COLLIDER;
    }
    
    public Line m_line = new Line();
}
