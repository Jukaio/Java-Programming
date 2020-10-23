package com.jukaio.jumpandrun.components;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.jukaio.jumpandrun.Entity;
import com.jukaio.jumpandrun.XML;

import org.w3c.dom.Element;

public class GetHitComponent extends Component
{
    float m_recovery = 0.0f;
    float m_recovery_timer = 0.0f;
    float m_flick_rate = 0.0f;
    float m_flick_timer = 0.0f;
    
    private BitmapComponent m_bitmap = null;

    public GetHitComponent(Entity p_entity, Element p_data)
    {
        super(p_entity);
        m_recovery = XML.parse_float(p_data, "recovery");
        m_flick_rate = XML.parse_float(p_data, "flicker");
    }
    
    @Override
    public ComponentType get_type()
    {
        return ComponentType.GET_HIT;
    }
    
    @Override
    public void start()
    {
        m_bitmap = get_entity().get_component(ComponentType.BITMAP);
    }
    
    @Override
    public void pre_update(float p_dt)
    {
    
    }
    
    @Override
    public void update(float p_dt)
    {
        if(m_recovery_timer > 0.0f)
        {
            if (m_flick_timer < 0.0f)
            {
                m_bitmap.set_colour(m_bitmap.get_colour() ^ Color.WHITE);
                m_flick_timer = m_flick_rate;
            }
            m_recovery_timer -= p_dt;
            m_flick_timer -= p_dt;
            
        }
        else if(m_recovery_timer < 0.0f)
        {
            m_bitmap.set_colour(Color.WHITE);
            m_recovery_timer = 0.0f;
        }
        else if(m_recovery_timer == 0.0f && m_bitmap.get_colour() != Color.WHITE)
        {
            m_bitmap.set_colour(Color.WHITE);
        }
        
        
    }
    
    @Override
    public void late_update(float p_dt)
    {
    
    }
    
    @Override
    public void render(Canvas p_canvas, Paint p_paint)
    {
    
    }
    
    @Override
    public void on_collision(Entity p_other)
    {
        switch(p_other.get_type())
        {
            case LETHAL:
                m_recovery_timer = m_recovery;
                break;
        }
    }
}
