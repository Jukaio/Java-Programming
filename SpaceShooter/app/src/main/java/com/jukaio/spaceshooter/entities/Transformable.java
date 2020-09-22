package com.jukaio.spaceshooter.entities;

import android.graphics.RectF;
import android.security.identity.IdentityCredentialStore;

import com.jukaio.spaceshooter.Vector2;

public abstract class Transformable
{
    private RectF m_rectangle = new RectF(0, 0, 0, 0);
    private Vector2 m_position = new Vector2(0, 0);
    private Vector2 m_dimension = new Vector2(0, 0);
    private Vector2 m_velocity = new Vector2(0, 0);
    
    public Transformable()
    {
    }
    
    public Transformable(final Transformable p_other)
    {
        set_position(p_other.m_position);
        set_dimensions(m_dimension = p_other.m_dimension);
        set_velocity(p_other.m_velocity);
    }
    
    public void destroy()
    {
        m_rectangle = null;
        m_position  = null;
        m_dimension = null;
        m_velocity  = null;
    }
    
    private static void refresh_rect(RectF p_rect,
                                     Vector2 p_position,
                                     Vector2 p_dimension)
    {
        p_rect.left = p_position.m_x - (p_dimension.m_x * 0.5f);
        p_rect.right = p_position.m_x + (p_dimension.m_x * 0.5f);
        p_rect.top = p_position.m_y - (p_dimension.m_y * 0.5f);
        p_rect.bottom = p_position.m_y + (p_dimension.m_y * 0.5f);
    }
    
    public final void set_position(Vector2 p_position)
    {
        m_position = p_position;
        refresh_rect(m_rectangle,
                     p_position,
                     m_dimension);
    }
    
    public final void set_dimensions(Vector2 p_dimension)
    {
        m_dimension = p_dimension;
        refresh_rect(m_rectangle,
                     m_position,
                     p_dimension);
        
    }
    
    public final void set_velocity(Vector2 p_velocity)
    {
        m_velocity = p_velocity;
    }
    
    public final Vector2 get_position()
    {
        return m_position;
    }
    
    public final Vector2 get_dimensions()
    {
        return m_dimension;
    }
    
    public final Vector2 get_velocity()
    {
        return m_velocity;
    }
    
    public final RectF get_rectangle()
    {
        return m_rectangle;
    }
}
