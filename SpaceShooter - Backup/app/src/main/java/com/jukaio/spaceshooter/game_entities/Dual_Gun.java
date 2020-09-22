package com.jukaio.spaceshooter.game_entities;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.jukaio.spaceshooter.R;
import com.jukaio.spaceshooter.Vector2;
import com.jukaio.spaceshooter.design.Player_Data;
import com.jukaio.spaceshooter.entities.Entity;

import java.util.Vector;

public class Dual_Gun extends Weapon
{
    Weapon m_second_weapon = null;
    
    public Dual_Gun(boolean p_active, Ammo_Clip p_ammo_clip, float p_fire_rate,
                    float p_reload_speed,
                    Vector2 p_offset)
    {
        super(p_active,
              p_ammo_clip,
              p_fire_rate,
              p_reload_speed,
              p_offset);
              
        m_second_weapon = new Weapon(this);
        m_second_weapon.m_offset = new Vector2(-m_offset.m_x, 0);
    }
    
    @Override
    public void set_parent(Entity p_parent)
    {
        super.set_parent(p_parent);
        m_second_weapon.m_parent = p_parent;
    }
    
    @Override
    public void update(float p_dt)
    {
        super.update(p_dt);
        m_second_weapon.update(p_dt);
    }
    
    @Override
    public void render(Canvas p_canvas, Paint p_paint)
    {
        super.render(p_canvas, p_paint);
        m_second_weapon.render(p_canvas, p_paint);
    }
    
    @Override
    public boolean is_colliding(Entity other)
    {
        m_second_weapon.is_colliding(other);
        return super.is_colliding(other);
    }
    
    @Override
    public void on_collision(Entity p_other)
    {
        super.on_collision(p_other);
        m_second_weapon.on_collision(p_other);
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
    
    }
    
    @Override
    public void reset()
    {
    
    }
}
