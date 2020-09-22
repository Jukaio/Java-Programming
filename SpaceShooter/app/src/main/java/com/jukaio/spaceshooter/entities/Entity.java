package com.jukaio.spaceshooter.entities;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.jukaio.spaceshooter.Game;
import com.jukaio.spaceshooter.Vector2;
import com.jukaio.spaceshooter.component.Component;
import com.jukaio.spaceshooter.game_entities.Entity_Type;

import java.util.ArrayList;

// TODO: Exchange Component ArrayList with Hashmap
// TODO: FindComponent by its Tag

public abstract class Entity extends Transformable implements IResetable
{
    static final String TAG = "Entity";
    
    private boolean m_active = false;
    private Entity_Type m_type = Entity_Type.UNKNOWN;
    public static Game m_game = null;
    private ArrayList<Component> m_components = new ArrayList<Component>();

    public Entity(boolean p_active)
    {
        m_active = p_active;
    }
    public Entity(final Entity p_other)
    {
        super(p_other);
        m_active = p_other.m_active;
        m_type = p_other.m_type;
        m_components = p_other.m_components;
    }

    final protected void set_type(Entity_Type p_type)
    {
        m_type = p_type;
    }
    final public Entity_Type get_type()
    {
        return m_type;
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
  
    public void update(float p_dt)
    {
        for(Component c : m_components)
            c.update(p_dt);
    
        Vector2 next_position = new Vector2(get_position().m_x + get_velocity().m_x,
                                            get_position().m_y + get_velocity().m_y);
        set_position(next_position);
    }
    public void render(final Canvas p_canvas, final Paint p_paint)
    {
        for(Component c : m_components)
            c.render(this, p_canvas, p_paint);
    }
    
    abstract protected void on_enable();
    abstract protected void on_disable();
    public void destroy()
    {
        super.destroy();
        m_type = null;
        m_game = null;
        for(Component c : m_components)
            c.destroy();
        m_components.clear();
        m_components = null;
    }
    
    protected void add_component(Component p_component)
    {
        m_components.add(p_component);
    }
    protected Component get_component(int p_index)
    {
        return m_components.get(p_index);
    }
    
    
    public void on_collision(Entity p_other) { Log.d(TAG, m_type.toString() + " " + p_other.get_type().toString()); }
    public boolean is_colliding(final Entity other)
    {
        if(this == other)
            throw new AssertionError("is_colliding: Don't check if an entity collides with itself");
        return get_rectangle().intersect(other.get_rectangle());
    }

}
