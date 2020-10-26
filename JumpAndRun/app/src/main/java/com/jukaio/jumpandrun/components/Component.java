package com.jukaio.jumpandrun.components;

import android.graphics.Paint;

import com.jukaio.jumpandrun.entity.Entity;
import com.jukaio.jumpandrun.Viewport;

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
    
    public Entity get_entity()
    {
        return m_entity;
    }
    public abstract ComponentType get_type();
    
    public void on_collision(Entity p_other)
    {
    
    }
    
    abstract public void start();
    abstract public void pre_update(float p_dt);
    abstract public void update(float p_dt);
    abstract public void late_update(float p_dt);
    abstract public void render(Viewport p_viewport, Paint p_paint);
    
    final public void clean()
    {
        destroy();
        m_entity = null;
    }
    
    abstract protected void destroy();
}
