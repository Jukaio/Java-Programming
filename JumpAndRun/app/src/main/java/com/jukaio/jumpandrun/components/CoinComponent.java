package com.jukaio.jumpandrun.components;

import android.graphics.Paint;

import com.jukaio.jumpandrun.entity.Entity;
import com.jukaio.jumpandrun.Viewport;
import com.jukaio.jumpandrun.XML;

import org.w3c.dom.Element;

public class CoinComponent extends Component
{
    private GroundSensorsComponent  m_ground_sensors    = null;
    private JumpComponent           m_jump              = null;
    private float m_bob_force = 0.0f;
    
    public CoinComponent(Entity p_entity, Element p_data)
    {
        super(p_entity);
        m_bob_force = XML.parse_float(p_data, "bob_force");
        get_entity().set_position(get_entity().get_position().m_x.floatValue() + XML.parse_float(p_data, "offset_x"),
                                  get_entity().get_position().m_y.floatValue() + XML.parse_float(p_data, "offset_y"));
    }
    
    @Override
    public ComponentType get_type()
    {
        return ComponentType.COIN;
    }
    
    @Override
    public void start()
    {
        get_entity().set_active(true);
        m_ground_sensors = get_entity().get_component(ComponentType.GROUND_SENSORS);
        m_jump = get_entity().get_component(ComponentType.JUMP);
    }
    
    @Override
    public void pre_update(float p_dt)
    {
    
    }
    
    @Override
    public void update(float p_dt)
    {
        if(m_ground_sensors.m_collisions.size() != 0)
            m_jump.add_force(m_bob_force);
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
        m_ground_sensors = null;
        m_jump = null;
    }
    
    @Override
    public void on_collision(Entity p_other)
    {
        switch(p_other.get_type())
        {
            case PLAYER:
                get_entity().set_active(false);
                break;
        }
    }
}
