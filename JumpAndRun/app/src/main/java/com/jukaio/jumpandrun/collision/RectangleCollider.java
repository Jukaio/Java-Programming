package com.jukaio.jumpandrun.collision;

import android.graphics.Rect;

public class RectangleCollider extends Collider
{
    private Rect m_bounds;

    public RectangleCollider()
    {
        super(ColliderType.RECTANGLE_COLLIDER);
        m_bounds = new Rect(0, 0, 0, 0);
    }

    public RectangleCollider(Rect p_bounds)
    {
        super(ColliderType.RECTANGLE_COLLIDER);
        m_bounds = new Rect(p_bounds);
    }

    public RectangleCollider(RectangleCollider p_other)
    {
        super(ColliderType.RECTANGLE_COLLIDER);
        m_bounds = new Rect(p_other.m_bounds);
    }
}
