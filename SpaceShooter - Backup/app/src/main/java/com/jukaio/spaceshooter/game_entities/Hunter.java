package com.jukaio.spaceshooter.game_entities;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.jukaio.spaceshooter.Jukebox;
import com.jukaio.spaceshooter.R;
import com.jukaio.spaceshooter.Vector2;
import com.jukaio.spaceshooter.design.Hunter_Data;
import com.jukaio.spaceshooter.entities.Entity;
import com.jukaio.spaceshooter.Sound_ID;

public class Hunter extends Enemy
{
    public static int POINTS = 0;
    static final int MAX_HP = 2;
    
    int m_hp = 0;
    int m_max_hp = 0;
    
    Enemy_Weapon m_weapon = null;
    
    public Hunter(Hunter p_other)
    {
        super(p_other);
        m_weapon = new Enemy_Weapon(p_other.m_weapon);
        m_weapon.set_parent(this);
        
        m_hp = p_other.m_hp;
        m_max_hp = p_other.m_max_hp;
        m_speed = p_other.m_speed;
    }
    
    public void reset()
    {
        super.reset();
        m_weapon.reset();
        m_hp = m_max_hp;
    }

    public Hunter(boolean p_active, int p_bitmap_id, Hunter_Data p_data)
    {
        super(p_active, Vector2.ZERO, p_bitmap_id, p_data.m_scale, p_data.m_speed);
        
        set_type(Entity_Type.HUNTER);
        
        m_hp = p_data.m_max_hp;
        m_max_hp = p_data.m_max_hp;

        init_weapon(p_data.m_weapon);
    }
    
    void init_weapon(Hunter_Data.Weapon p_data)
    {
        Ammo_Clip ammo_clip = new Ammo_Clip(true,
                                            p_data.m_ammo_clip.m_size,
                                            new Bullet(false,
                                                       R.drawable.enemy_base_laser,
                                                       p_data.m_ammo_clip.m_bullet.m_scale,
                                                       new Vector2(p_data.m_ammo_clip.m_bullet.m_direction_x,
                                                                   p_data.m_ammo_clip.m_bullet.m_direction_y),
                                                       p_data.m_ammo_clip.m_bullet.m_speed));
        m_weapon = new Enemy_Weapon(true,
                                    ammo_clip,
                                    p_data.m_fire_rate,
                                    p_data.m_reload_speed,
                                    p_data.m_trigger_rate);
        m_weapon.set_parent(this);
    }
    
    @Override
    protected void on_disable()
    {
        super.on_disable();
    }
    
    @Override
    public void update(float p_dt)
    {
        m_weapon.update(p_dt);
        if(get_active())
            super.update(p_dt);
    }
    
    @Override
    public void render(Canvas p_canvas,
                       Paint p_paint)
    {
        m_weapon.render(p_canvas, p_paint);
        if(get_active())
            super.render(p_canvas, p_paint);
    }
    
    @Override
    protected void on_enable()
    {
    
    }
    
    @Override
    public boolean is_colliding(Entity other)
    {
        m_weapon.is_colliding(other);
        return super.is_colliding(other);
    }
    
    @Override
    public void on_collision(Entity p_other)
    {
        switch (p_other.get_type())
        {
            case UNKNOWN:
                break;
            case PLAYER:
                break;
            case BULLET:
                m_hp--;
                if(m_hp <= 0)
                {
                    m_hp = 0; // Interpolate HP to 0 for GUI
                    m_game.add_score(POINTS);
                    m_game.play_sounnd(Sound_ID.ENEMY_DEATH, Jukebox.MAX_VOLUME, 0);
                    set_active(false);
                    m_game.increase_kill_count(get_position());
                }
                break;
            case UFO:
                break;
        }
    }
    
    @Override
    public void destroy()
    {
    
    }
}
