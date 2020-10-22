package com.jukaio.jumpandrun;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Component
{
    private Entity m_entity = null;
    
    protected Component(Entity p_entity)
    {
        m_entity = p_entity;
    }
    protected Component(Component p_other)
    {
        m_entity = p_other.m_entity;
    }
    
    Entity get_entity()
    {
        return m_entity;
    }
    public abstract ComponentType get_type();
    
    public void pre_update(float p_dt)
    {
    
    }
    abstract public void update(float p_dt);
    abstract public void fixed_update();
    abstract public void late_update(float p_dt);
    abstract public void render(Canvas p_canvas, Paint p_paint);
    
    public void destroy()
    {
        m_entity = null;
    }
}
