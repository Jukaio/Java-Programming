package com.jukaio.spaceshooter.entities;

import android.graphics.Rect;
import android.graphics.RectF;

import com.jukaio.spaceshooter.Vector2;

public abstract class Wrapping_Entity extends Entity
{
    public Wrapping_Entity(boolean p_active)
    {
        super(p_active);
    }

    public Wrapping_Entity(Wrapping_Entity p_other)
    {
        super(p_other);
    }

    @Override
    public void update(float p_dt)
    {
        set_position(wrap_position_around(get_rectangle(),
                                          get_position(),
                                          get_dimensions(),
                                          m_game.m_window_size));
        super.update(p_dt);
    }
    
    private static Vector2 wrap_position_around(RectF p_entity_rect, Vector2 p_position, Vector2 p_dimension, Rect p_bounds)
    {
        if(p_entity_rect.top > p_bounds.bottom)
            p_position.m_y = 0 - (p_dimension.m_y * 0.5f);
        else if(p_entity_rect.bottom < 0)
            p_position.m_y = p_bounds.bottom + (p_dimension.m_y * 0.5f);
        if(p_entity_rect.left > p_bounds.right)
            p_position.m_x = 0 - (p_dimension.m_x * 0.5f);
        else if(p_entity_rect.right < 0)
            p_position.m_x = p_bounds.right + (p_dimension.m_x * 0.5f);
            
        return p_position;
    }
}
