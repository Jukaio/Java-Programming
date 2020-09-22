package com.jukaio.spaceshooter.game_entities;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.jukaio.spaceshooter.Jukebox;
import com.jukaio.spaceshooter.Vector2;
import com.jukaio.spaceshooter.design.Ufo_Data;
import com.jukaio.spaceshooter.entities.Entity;
import com.jukaio.spaceshooter.Sound_ID;

public class Ufo extends Enemy
{
    public static int POINTS = 0;
    int m_max_hp = 0;
    int m_hp = 0;
    
    public Ufo(Ufo p_other)
    {
        super(p_other);
        m_hp = p_other.m_hp;
        m_max_hp = p_other.m_max_hp;
        m_speed = p_other.m_speed;
    }
    
    
    public void reset()
    {
        super.reset();
        m_hp = m_max_hp;
    }

    public Ufo(boolean p_active, int p_bitmap_id, Ufo_Data p_data)
    {
        super(p_active, Vector2.ZERO, p_bitmap_id, p_data.m_scale, p_data.m_speed);
        set_type(Entity_Type.UFO);
        
        m_max_hp = p_data.m_max_hp;
        m_hp     = p_data.m_max_hp;
    }
    
    
    
    @Override
    protected void on_enable()
    {
    
    }
    
    @Override
    protected void on_disable()
    {
        super.on_disable();
    }
    
    @Override
    public void update(float p_dt)
    {
        if(get_active())
            super.update(p_dt);
    }

    @Override
    public void render(Canvas p_canvas,
                       Paint p_paint)
    {
        if(get_active())
            super.render(p_canvas, p_paint);
    }
    
    @Override
    public boolean is_colliding(Entity other)
    {
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
                    m_game.play_sounnd(Sound_ID.ENEMY_DEATH,
                                       Jukebox.MAX_VOLUME, 0);
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
        super.destroy();
    }
}
