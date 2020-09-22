package com.jukaio.spaceshooter.game_entities;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.jukaio.spaceshooter.Jukebox;
import com.jukaio.spaceshooter.Vector2;
import com.jukaio.spaceshooter.entities.Entity;
import com.jukaio.spaceshooter.Sound_ID;

import java.util.Vector;

public class Weapon extends Entity
{
    Entity m_parent = null;
    Ammo_Clip m_ammo_clip = null;
    
    private float m_shoot_timer = 0.0f;
    private float m_fire_rate = 0.0f;
    
    private float m_reload_timer = 0.0f;
    private float m_reload_speed = 0.0f;
    
    Vector2 m_offset = null;
    private Vector2 m_shoot_position = null;
    
    public Weapon(Weapon p_other)
    {
        super(p_other);
        m_ammo_clip = new Ammo_Clip(p_other.m_ammo_clip);
        m_fire_rate = p_other.m_fire_rate;
        m_reload_speed = p_other.m_reload_speed;
        m_parent = p_other.m_parent;
        m_shoot_timer= m_fire_rate;
        m_reload_timer = m_reload_speed;
        m_offset = new Vector2(p_other.m_offset);
        m_shoot_position = new Vector2(0, 0);
    }
    
    public Weapon(boolean p_active, Ammo_Clip p_ammo_clip, float p_fire_rate, float p_reload_speed, Vector2 p_offset)
    {
        super(p_active);
        m_ammo_clip = p_ammo_clip;
        m_fire_rate = p_fire_rate;
        m_shoot_timer= m_fire_rate;
        m_reload_speed = p_reload_speed;
        m_reload_timer = m_reload_speed;
        m_offset = p_offset;
        m_shoot_position = new Vector2(0, 0);
        }
    
    public void set_parent(Entity p_parent)
    {
        m_parent = p_parent;
    }
    
    public boolean trigger(float p_dt)
    {
        return m_game.m_shoot;
    }
    
    @Override
    public void update(float p_dt)
    {
        if(get_active())
        {
            if (m_parent != null && m_parent.get_active())
            {
                if (!m_ammo_clip.is_empty())
                {
                    if (trigger(p_dt) && m_shoot_timer <= 0.0f)
                    {
                        m_game.play_sounnd(Sound_ID.PLAYER_BASE_LASER,
                                           Jukebox.MAX_VOLUME,
                                           0);
                        m_shoot_timer = m_fire_rate;
                        
                        m_shoot_position.m_x = m_parent.get_position().m_x + m_offset.m_x;
                        m_shoot_position.m_y = m_parent.get_position().m_y + m_offset.m_y;
                        m_ammo_clip.grab_bullet().shoot(m_shoot_position);
                    } else if (m_shoot_timer > 0.0f)
                    {
                        m_shoot_timer -= p_dt;
                    }
                } else if (m_reload_timer <= 0.0f)
                {
                    m_ammo_clip.reload();
                    m_shoot_timer = 0.0f;
                    m_reload_timer = m_reload_speed;
                } else if (m_reload_timer > 0.0f)
                {
                    m_reload_timer -= p_dt;
                }
            }
    
            m_ammo_clip.update(p_dt);
        }
    }
    
    @Override
    public void render(Canvas p_canvas, Paint p_paint)
    {
        m_ammo_clip.render(p_canvas, p_paint);
    }
    
    @Override
    public boolean is_colliding(Entity other)
    {
        m_ammo_clip.is_colliding(other);
        return false;
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
    public void destroy()
    {
        super.destroy();
        m_ammo_clip.destroy();
        m_parent = null;
        m_ammo_clip = null;
        m_offset = null;
        m_shoot_position = null;
        
    }
    
    @Override
    public void reset()
    {
        m_ammo_clip.reset();
        m_shoot_timer= m_fire_rate;
        m_reload_timer = m_reload_speed;
    }
}
