package com.jukaio.jumpandrun.extramath;

import android.graphics.Point;

public class Circle
{
    Point m_center;
    float m_radius;

    public Circle()
    {
        m_center = new Point(0, 0);
        m_radius = 0.0f;
    }

    public Circle(Circle p_other)
    {
        m_center = new Point(p_other.m_center);
        m_radius = p_other.m_radius;
    }

    public Circle(int x, int y, float radius)
    {
        m_center = new Point(x, y);
        m_radius = radius;
    }
}
