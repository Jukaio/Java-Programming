package com.jukaio.jumpandrun.ecs.componentmodule.components;

import android.graphics.Matrix;
import android.graphics.RectF;

import com.jukaio.jumpandrun.extramath.Vector2;

public class Transform extends Component
{
    private Matrix m_transform = null;
    private Vector2 m_position = null;
    private Vector2 m_scale = null;
    private Vector2 m_dimenstions = null;
    private float m_rotation = 0.0f;
    
    private Vector2 m_prev_position = null;
    private Vector2 m_prev_scale = null;
    private Vector2 m_prev_dimenstions = null;
    private float m_prev_rotation = 0.0f;
    

    @Override
    public ComponentType get_type() {
        return ComponentType.TRANSFORM;
    }
    //public float m_rotation = 0;

    public Transform()
    {
        m_transform = new Matrix();
        m_position = new Vector2(0,0);
        m_scale = new Vector2(1, 1);
        m_dimenstions = new Vector2(0, 0);
        m_rotation = 0.0f;
        
        m_prev_position = new Vector2(0,0);
        m_prev_scale = new Vector2(1, 1);
        m_prev_dimenstions = new Vector2(0, 0);
        m_prev_rotation = 0.0f;
    }

    public Transform(Transform p_other)
    {
        m_transform = new Matrix(p_other.m_transform);

        m_position = new Vector2(p_other.m_position);
        m_scale = new Vector2(p_other.m_scale);
        m_rotation = p_other.m_rotation;
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
    
    final public void set_prev_position(float x, float y)
     {
         m_prev_position.m_x = x;
         m_prev_position.m_y = y;
     }
     final public void set_prev_scale(float w, float h)
     {
         m_prev_scale.m_x = w;
         m_prev_scale.m_y = h;
     }
     final public void set_prev_dimensions(float w, float h)
     {
         m_prev_dimenstions.m_x = w;
         m_prev_dimenstions.m_y = h;
     }
     final public void set_prev_rotation(float p_degrees)
     {
         m_prev_rotation = p_degrees;
     }
    
    final public Vector2 get_prev_position()
    {
        return m_prev_position;
    }
    final public Vector2 get_prev_scale()
    {
        return m_prev_scale;
    }
    final public float get_prev_rotation()
    {
        return m_prev_rotation;
    }
    final public Vector2 get_prev_dimensions()
    {
        return m_prev_dimenstions;
    }

    final public Matrix get_matrix()
    {
        float origin_x = m_dimenstions.m_x.floatValue() * 0.5f;
        float origin_y = m_dimenstions.m_x.floatValue() * 0.5f;
        
        m_transform.reset();
        m_transform.setRotate(m_rotation, origin_x, origin_y);
        m_transform.postTranslate(m_position.m_x.floatValue() - m_dimenstions.m_x.floatValue() * 0.5f,
                                  m_position.m_y.floatValue() - m_dimenstions.m_y.floatValue() * 0.5f);
        m_transform.preScale(m_scale.m_x.floatValue(), m_scale.m_y.floatValue(), origin_x, origin_y);

        return m_transform;
    }
}
