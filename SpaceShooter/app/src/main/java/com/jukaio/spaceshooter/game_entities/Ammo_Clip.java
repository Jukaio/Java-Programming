package com.jukaio.spaceshooter.game_entities;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.jukaio.spaceshooter.Vector2;
import com.jukaio.spaceshooter.entities.Entity;

import java.util.ArrayList;
import java.util.Collections;

public class Ammo_Clip extends Entity
{
    private int m_size = 0;
    private ArrayList<Bullet> m_bullets = null;
    
    public Ammo_Clip(Ammo_Clip p_other)
        {
            super(p_other);
            m_size = p_other.m_size;
            
            m_bullets = new ArrayList<Bullet>();
            for (Bullet bullet : p_other.m_bullets)
            {
                m_bullets.add(new Bullet(bullet));
            }
        }
    
    public Ammo_Clip(boolean p_active, int p_size, Bullet p_template)
    {
        super(p_active);
        
        m_bullets = new ArrayList<Bullet>();
        for(int i = 0; i < p_size; i++)
        {
            m_bullets.add(new Bullet(p_template));
        }
        m_size = p_size;
    }
    
    final void reload()
    {
        m_size = m_bullets.size();
    }
    
    public boolean is_empty()
    {
        return m_size <= 0;
    }
    
    final Bullet grab_bullet()
    {
        if(m_size > 0)
        {
            m_size -= 1;
            return m_bullets.get(m_size);
        }
        return null;
    }
    
    @Override
    protected void on_enable()
    {
    
    }
    
    @Override
    protected void on_disable()
    {
    
    }
    
    @Override
    public void update(float p_dt)
    {
        for(Bullet b : m_bullets)
        {
            if(b.get_active())
                b.update(p_dt);
        }
    }
    
    @Override
    public void render(Canvas p_canvas,
                       Paint p_paint)
    {
        for(Bullet b : m_bullets)
        {
            if(b.get_active())
                b.render(p_canvas, p_paint);
        }
    }
    
    @Override
    public void destroy()
    {
        super.destroy();
        for(Bullet b : m_bullets)
            b.destroy();
        m_bullets.clear();
    }
    
    @Override
    public boolean is_colliding(Entity other)
    {
        for(Bullet b : m_bullets)
        {
            if(b.get_active())
            {
                if (b.is_colliding(other))
                {
                    b.on_collision(other);
                    other.on_collision(b);
                }
            }
        }
        return false;
    }
    
    @Override
    public void reset()
    {
        for(Bullet b : m_bullets)
        {
            if (b.get_active())
            {
                b.set_active(false);
                b.reset();
            }
        }
    }
}
