package com.jukaio.jumpandrun.components;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.jukaio.jumpandrun.Entity;
import com.jukaio.jumpandrun.XML;

import org.w3c.dom.Element;

public class SpikeComponent extends Component
{
    public SpikeComponent(Entity p_entity, Element p_data)
    {
        super(p_entity);
        
    }
    
    @Override
    public ComponentType get_type()
    {
        return ComponentType.SPIKE_COMPONENT;
    }
    
    @Override
    public void start()
    {
    
    }
    
    @Override
    public void pre_update(float p_dt)
    {
    
    }
    
    @Override
    public void update(float p_dt)
    {
    
    }
    
    @Override
    public void late_update(float p_dt)
    {
    
    }
    
    @Override
    public void render(Canvas p_canvas, Paint p_paint)
    {
    
    }
}
