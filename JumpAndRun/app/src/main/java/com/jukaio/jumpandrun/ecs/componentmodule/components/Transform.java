package com.jukaio.jumpandrun.ecs.componentmodule.components;

import android.graphics.Matrix;

import com.jukaio.jumpandrun.extramath.Vector2;

public class Transform extends Component
{
    private Matrix m_transform = null;
    private Vector2 m_position = null;
    private Vector2 m_scale = null;
    private float m_rotation = 0.0f;

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
        m_rotation = 0.0f;
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
        m_transform.postTranslate(x, y);
        m_position.m_x = x;
        m_position.m_y = y;
    }
    final public void set_scale(float w, float h)
    {
        m_transform.postScale(w, h);
        m_position.m_x = w;
        m_position.m_y = h;
    }
    final public void set_rotation(float p_degrees)
    {
        m_transform.postRotate(p_degrees);
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

    final public Matrix get_matrix()
    {
        m_transform.reset();
        m_transform.setScale(m_scale.m_x.floatValue(), m_scale.m_y.floatValue());
        m_transform.postTranslate(m_position.m_x.floatValue(), m_position.m_y.floatValue());
        m_transform.postRotate(m_rotation);

        return m_transform;
    }
}
