package com.jukaio.asteroids;

import android.opengl.Matrix;


public abstract class Transform
{

    private Vector2 m_position = null;
    private Vector2 m_scale = null;
    private Vector2 m_dimenstions = null;
    private float m_rotation = 0.0f;
    private Vector2 m_origin = null;
    private float m_z = 0.0f;

    public Transform()
    {
        m_position = new Vector2(0,0);
        m_scale = new Vector2(1, 1);
        m_dimenstions = new Vector2(0, 0);
        m_rotation = 0.0f;
        m_origin = new Vector2(0.5f, 0.5f);
    }

    public Transform(Transform p_other)
    {
        m_position = new Vector2(p_other.m_position);
        m_scale = new Vector2(p_other.m_scale);
        m_dimenstions = new Vector2(p_other.m_dimenstions);
        m_rotation = p_other.m_rotation;
    }

    final public void set_z(float p_z)
    {
        m_z = p_z;
    }
    
    final public float get_z()
    {
        return m_z;
    }

    final public void set_origin(float x, float y)
     {
         m_origin.m_x = x;
         m_origin.m_y = y;
     }
   final public void set_position(float x, float y)
    {
        m_position.m_x = x;
        m_position.m_y = y;
    }
    final public void set_scale(float w, float h)
    {
        m_scale.m_x = w;
        m_scale.m_y = h;
    }
    final public void set_dimensions(float w, float h)
    {
        m_dimenstions.m_x = w;
        m_dimenstions.m_y = h;
    }
    final public void set_rotation(float p_degrees)
    {
        m_rotation = p_degrees;
    }

    final public Vector2 get_origin()
    {
        return m_origin;
    }
    final public Vector2 get_position()
    {
        return m_position;
    }
    final public Vector2 get_scale()
    {
        return m_scale;
    }
    final public float get_rotation()
    {
        return m_rotation;
    }
    final public Vector2 get_dimensions()
    {
        return m_dimenstions;
    }

}
