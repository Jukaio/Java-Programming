package com.jukaio.jumpandrun.collision;

import android.graphics.Point;

import com.jukaio.jumpandrun.extramath.Circle;

public class PolygonCollider extends Collider
{
    private Point[] m_bounds;

    public PolygonCollider(PolygonCollider p_bounds)
    {
        super(ColliderType.CIRCLE_COLLIDER);
        m_bounds = new Point[p_bounds.m_bounds.length];
        for(int i = 0; i < m_bounds.length; i++)
        {
            m_bounds[i] = new Point(p_bounds.m_bounds[i]);
        }
    }

    public PolygonCollider(Point[] p_points)
    {
        super(ColliderType.CIRCLE_COLLIDER);
        m_bounds = p_points;
    }
}
