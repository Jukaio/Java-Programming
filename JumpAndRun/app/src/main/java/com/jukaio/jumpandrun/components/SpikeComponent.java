package com.jukaio.jumpandrun.components;

import android.graphics.Paint;

import com.jukaio.jumpandrun.entity.Entity;
import com.jukaio.jumpandrun.Viewport;

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
    public void render(Viewport p_viewport, Paint p_paint)
    {
    
    }
    
    @Override
    protected void destroy()
    {
    
    }
}
