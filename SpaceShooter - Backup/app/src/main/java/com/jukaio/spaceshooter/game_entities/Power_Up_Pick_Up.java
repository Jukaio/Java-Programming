package com.jukaio.spaceshooter.game_entities;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.jukaio.spaceshooter.R;
import com.jukaio.spaceshooter.Vector2;
import com.jukaio.spaceshooter.component.Bitmap_Component;
import com.jukaio.spaceshooter.design.Config;
import com.jukaio.spaceshooter.entities.Entity;

public class Power_Up_Pick_Up extends Entity
{
    private Power_Up_Type m_power_up_type;
    Bitmap_Component m_bitmap_component = null;

    public Power_Up_Pick_Up(boolean p_active, Vector2 p_direction, float p_speed, Power_Up_Type p_power_up_type)
    {
        super(p_active);
        set_type(Entity_Type.POWER_UP);
        m_power_up_type = p_power_up_type;
        
        int to_draw = -1;
        switch (m_power_up_type)
        {
            case DUAL_GUN: to_draw = R.drawable.powerup_dual_gun; break;
            case HP_PLUS: to_draw = R.drawable.health_up; break;
            default: throw new AssertionError("NO POWERUP DRAWABLE FOR POWERUP TYPE");
        }
        
        m_bitmap_component = new Bitmap_Component(m_game,
                                                  this,
                                                  to_draw,
                                                  Config.POWER_BITMAP_SCALE,
                                                  Color.WHITE);
        set_velocity(new Vector2(p_direction.m_x * p_speed, p_direction.m_y * p_speed));
        add_component(m_bitmap_component);
    }
    
    Power_Up_Type get_power_up_type()
    {
        return m_power_up_type;
    }
    
    public void spawn_at(Vector2 p_spawn_position)
    {
        set_active(true);
        set_position(p_spawn_position);
    }
    
    @Override
    public void update(float p_dt)
    {
        super.update(p_dt);
        if(get_rectangle().top > m_game.m_window_size.bottom)
            set_active(false);
    }
    
    @Override
    public void render(Canvas p_canvas, Paint p_paint)
    {
        super.render(p_canvas,
                     p_paint);
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
    
    @Override
    public void on_collision(Entity p_other)
    {
        switch(p_other.get_type())
        {
            case PLAYER:
                set_active(false);
                break;
        }
    }
}
