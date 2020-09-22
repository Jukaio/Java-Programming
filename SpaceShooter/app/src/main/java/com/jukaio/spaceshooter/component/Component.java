package com.jukaio.spaceshooter.component;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.jukaio.spaceshooter.entities.Entity;

public abstract class Component
{
    private Entity m_entity;
    
    // TODO: Exchange that with enum
    public String m_component_type = "UNKNOWN";
    Component(Entity p_entity, String p_component_type_tag)
    {
        m_component_type = p_component_type_tag;
        m_entity = p_entity;
    }
    Component(Component p_other)
    {
        m_entity = p_other.m_entity;
        m_component_type = p_other.m_component_type;
    }
    
    Entity get_entity()
    {
        return m_entity;
    }
    
    abstract public void update(float p_dt);
    abstract public void render(Entity p_entity, Canvas p_canvas, Paint p_paint);
    
    public void destroy()
    {
        m_entity = null;
        m_component_type = null;
    }
}
