package com.jukaio.jumpandrun.components;

import android.graphics.Paint;

import com.jukaio.jumpandrun.entity.Entity;
import com.jukaio.jumpandrun.Viewport;

public class RecorderComponent extends Component
{
    private Entity m_previous_state = null;
    
    public Entity get_previous_state()
    {
        return m_previous_state;
    }
    
    public RecorderComponent(Entity p_entity)
    {
        super(p_entity);
        m_previous_state = new Entity(p_entity);
    }
    
    @Override
    public ComponentType get_type()
    {
        return ComponentType.RECORDER;
    }
    
    @Override
    public void start()
    {
    
    }
    
    @Override
    public void pre_update(float p_dt)
    {
        m_previous_state.set_position(get_entity().get_position().m_x.floatValue(),
                                      get_entity().get_position().m_y.floatValue());
        m_previous_state.set_dimensions(get_entity().get_dimensions().m_x.floatValue(),
                                        get_entity().get_dimensions().m_y.floatValue());
        m_previous_state.set_scale(get_entity().get_scale().m_x.floatValue(),
                                   get_entity().get_scale().m_y.floatValue());
        m_previous_state.set_rotation(get_entity().get_rotation());
        m_previous_state.set_active(get_entity().get_active());
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
        m_previous_state = null;
    }
}
