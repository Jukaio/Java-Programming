package com.jukaio.asteroids;

import android.graphics.Point;

public class Vector2
{
    public static final Vector2 INVALID = new Vector2(Float.NaN, Float.NaN);
    public static final Vector2 ZERO    = new Vector2(0.0f, 0.0f);
    public static final Vector2 LEFT    = new Vector2(-1.0f, 0.0f);
    public static final Vector2 RIGHT   = new Vector2(1.0f, 0.0f);
    public static final Vector2 UP      = new Vector2(0.0f, -1.0f);
    public static final Vector2 DOWN    = new Vector2(0.0f, 1.0f);

    public Vector2()
    {
        m_x = Float.NaN;
        m_y = Float.NaN;
    }
    public Vector2(Vector2 p_other)
    {
        m_x = p_other.m_x;
        m_y = p_other.m_y;
    }
    public Vector2(float p_x, float p_y)
    {
        m_x = p_x;
        m_y = p_y;
    }
    
    
    public float m_x;
    public float m_y;
}
