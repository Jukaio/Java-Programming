package com.jukaio.jumpandrun.collision;

import android.graphics.Rect;

import com.jukaio.jumpandrun.extramath.Line;

public class LineCollider extends Collider
{
    private Line m_bounds;

    LineCollider()
    {
        super(ColliderType.LINE_COLLIDER);
        m_bounds = new Line(0, 0, 0, 0);
    }

    LineCollider(Line p_bounds)
    {
        super(ColliderType.LINE_COLLIDER);
        m_bounds = new Line(p_bounds);
    }

    LineCollider(LineCollider p_other)
    {
        super(ColliderType.LINE_COLLIDER);
        m_bounds = new Line(p_other.m_bounds);
    }
}
