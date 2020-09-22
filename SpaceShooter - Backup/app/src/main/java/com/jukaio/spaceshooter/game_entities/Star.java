package com.jukaio.spaceshooter.game_entities;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.jukaio.spaceshooter.Vector2;
import com.jukaio.spaceshooter.entities.Wrapping_Entity;

public class Star extends Wrapping_Entity
{
    private float m_radius = 0.0f;
    private int m_color = 0xFFFFFFFF;


    public Star(boolean p_active, Vector2 p_position, int p_color, float p_radius, Vector2 p_direction, float p_speed)
    {
        super(p_active);
        set_position(p_position);
        m_radius = p_radius;
        set_dimensions(new Vector2(p_radius * 2.0f, p_radius * 2.0f));
        m_color = p_color;
        set_velocity(new Vector2(p_direction.m_x * p_speed,
                                 p_direction.m_y * p_speed));
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
        super.update(p_dt);
    }

    @Override
    public void render(Canvas p_canvas, Paint p_paint)
    {
        super.render(p_canvas, p_paint);
        p_paint.setColor(m_color);
        p_canvas.drawCircle(get_position().m_x, get_position().m_y, m_radius * 0.5f, p_paint);
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
