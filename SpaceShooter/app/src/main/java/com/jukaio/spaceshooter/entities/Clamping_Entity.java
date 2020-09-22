package com.jukaio.spaceshooter.entities;

import android.graphics.Rect;
import android.graphics.RectF;

import com.jukaio.spaceshooter.Vector2;

public abstract class Clamping_Entity extends Entity
{
    public Clamping_Entity(boolean p_active)
    {
        super(p_active);
    }

    public Clamping_Entity(Clamping_Entity p_other)
    {
        super(p_other);
    }
    
    @Override
    public void destroy()
    {
        super.destroy();
    }
    
    @Override
    public void update(float p_dt)
    {
        set_position(clamp_position_around(get_rectangle(),
                                           get_position(),
                                           get_dimensions(),
                                           m_game.m_window_size));
        super.update(p_dt);
    }
    
    private static Vector2 clamp_position_around(RectF p_entity_rect, Vector2 p_position, Vector2 p_dimension, Rect p_bounds)
       {
           if(p_entity_rect.bottom > p_bounds.bottom)
               p_position.m_y = p_bounds.bottom - (p_dimension.m_y * 0.5f);
           else if(p_entity_rect.top < 0)
               p_position.m_y = 0 + (p_dimension.m_y * 0.5f);
           if(p_entity_rect.right > p_bounds.right)
               p_position.m_x = p_bounds.right - (p_dimension.m_x * 0.5f);
           else if(p_entity_rect.left < 0)
               p_position.m_x = 0 + (p_dimension.m_x * 0.5f);
           return p_position;
       }
}
