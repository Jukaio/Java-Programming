package com.jukaio.jumpandrun.entity;

import android.graphics.Paint;

import com.jukaio.jumpandrun.Viewport;
import com.jukaio.jumpandrun.components.Component;
import com.jukaio.jumpandrun.components.ComponentType;

import java.util.ArrayList;

public class Entity extends Transform implements IEntity
{
    static final String TAG = "ENTITY";
    private boolean m_active = false;
    ArrayList<Component> m_components = new ArrayList<>();
    EntityType m_type;
    
    public void set_type(EntityType p_type)
    {
        m_type = p_type;
    }
    
    public EntityType get_type()
    {
        return m_type;
    }
    
    public Entity(boolean p_active, EntityType p_type)
    {
        super();
        m_active = p_active;
        m_type = p_type;
    }
    public Entity(final Entity p_other)
    {
        super(p_other);
        m_active = p_other.m_active;
        m_components = p_other.m_components;
        m_type = p_other.m_type;
    }
    
    public void add_component(Component p_component)
    {
        m_components.add(p_component);
    }
    public Component get_component(int p_index)
    {
        return m_components.get(p_index);
    }
    public <T> T get_component(ComponentType p_type)
    {
        for(Component c : m_components)
            if(c.get_type() == p_type)
                return (T) c;
        return null;
    }
    
    final public void set_active(final boolean p_active)
    {
        if(!m_active != !p_active)
        {
            if(p_active)
                on_enable();
            else
                on_disable();
            m_active = p_active;
        }
    }
    final public boolean get_active()
    {
        return m_active;
    }
    
    protected void on_enable()
    {
    
    }
    protected void on_disable()
    {
    
    }
    
    final public void on_collision(Entity p_other)
    {
        for(Component c : m_components)
            c.on_collision(p_other);
    }
    
    final public void pre_update(float p_dt)
    {
        for(Component c : m_components)
            c.pre_update(p_dt);
    }
    
    @Override
    public void start()
    {
        for(Component c : m_components)
            c.start();
    }
    
    final public void update(float p_dt)
    {
        for(Component c : m_components)
            c.update(p_dt);
    }
    final public void late_update(float p_dt)
    {
        for(Component c : m_components)
            c.late_update(p_dt);
    }
    final public void render(final Viewport p_viewport, final Paint p_paint)
    {
        for(Component c : m_components)
            c.render(p_viewport, p_paint);
    }
    
    @Override
    public void destroy()
    {
        for(Component c : m_components)
            c.clean();
    }
    
}
