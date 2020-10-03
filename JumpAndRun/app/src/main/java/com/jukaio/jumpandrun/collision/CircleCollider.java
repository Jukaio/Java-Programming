package com.jukaio.jumpandrun.collision;

import android.graphics.Rect;

import com.jukaio.jumpandrun.extramath.Circle;

public class CircleCollider extends Collider
{
    private Circle m_bounds;

    CircleCollider()
    {
        super(ColliderType.CIRCLE_COLLIDER);
        m_bounds = new Circle(0, 0, 0);
    }

    CircleCollider(Circle p_bounds)
    {
        super(ColliderType.CIRCLE_COLLIDER);
        m_bounds = new Circle(p_bounds);
    }

    CircleCollider(CircleCollider p_other)
    {
        super(ColliderType.CIRCLE_COLLIDER);
        m_bounds = new Circle(p_other.m_bounds);
    }
}
