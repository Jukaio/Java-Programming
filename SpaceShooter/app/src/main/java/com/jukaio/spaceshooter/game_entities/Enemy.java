package com.jukaio.spaceshooter.game_entities;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.jukaio.spaceshooter.Vector2;
import com.jukaio.spaceshooter.component.Bitmap_Component;
import com.jukaio.spaceshooter.entities.Entity;

public abstract class Enemy extends Entity
{
    private static final int BASE_COLOR = 0xFFFFFFFF;
    
    Vector2 m_spawn_position = Vector2.ZERO;
    
    boolean m_moving = false;
    float target_length = 0.0f;
    float length_travelled = 0.0f;
    public float m_speed = 0.0f;
    Vector2 m_target = Vector2.ZERO;
    
    @Override
    protected void on_disable()
    {
        if(m_moving) m_game.update_grid(m_target, false);
        else m_game.update_grid(get_position(), false);
    }
    
    public Enemy(Enemy p_other)
    {
        super(p_other);
        m_spawn_position = p_other.m_spawn_position;
        m_speed = p_other.m_speed;
        m_moving = false;
        target_length = 0.0f;
        length_travelled = 0.0f;
    }
    public void reset()
    {
        set_active(false);
        set_position(m_spawn_position);
        m_moving = false;
        target_length = 0.0f;
        length_travelled = 0.0f;
    }
    
    void move_to(Vector2 p_target)
    {
        m_target = new Vector2(p_target);
        p_target.m_x -= get_position().m_x;
        p_target.m_y -= get_position().m_y;
 
        target_length = (float) Math.sqrt(Math.pow(p_target.m_x, 2) + Math.pow(p_target.m_y, 2));
        
        p_target.m_x /= target_length;
        p_target.m_y /= target_length;
        
        p_target.m_x *= m_speed;
        p_target.m_y *= m_speed;
        
        set_velocity(p_target);
        m_moving = true;
    }

    public Enemy(boolean p_active, Vector2 p_position, int p_bitmap_id, float p_scale_ratio, float p_speed)
    {
        super(p_active);
        add_component(new Bitmap_Component(m_game, this, p_bitmap_id, p_scale_ratio, BASE_COLOR));
        set_position(p_position);
        m_spawn_position = p_position;
        m_speed = p_speed;
    }
    
    @Override
    public void update(float p_dt)
    {
        super.update(p_dt);
        if(m_moving)
        {
            length_travelled += m_speed;
            if (length_travelled > target_length)
            {
                m_moving = false;
                length_travelled = 0;
                set_velocity(Vector2.ZERO);
            }
        }
    }
    

    
    @Override
    public void render(Canvas p_canvas,
                       Paint p_paint)
    {
        super.render(p_canvas, p_paint);
    }
    

    
    @Override
    public void destroy()
    {
        super.destroy();
        m_spawn_position = null;
        m_target = null;
    }
    
}
