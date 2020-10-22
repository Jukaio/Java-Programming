package com.jukaio.jumpandrun.components;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.jukaio.jumpandrun.Entity;

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
    
    abstract public void start();
    abstract public void pre_update(float p_dt);
    abstract public void update(float p_dt);
    abstract public void late_update(float p_dt);
    abstract public void render(Canvas p_canvas, Paint p_paint);
    
    public void destroy()
    {
        m_entity = null;
    }
}
