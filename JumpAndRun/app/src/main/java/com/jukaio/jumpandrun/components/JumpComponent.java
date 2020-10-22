package com.jukaio.jumpandrun.components;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.jukaio.jumpandrun.Entity;

public class JumpComponent extends Component
{
    private KineticComponent m_kinematic = null;
    private GroundSensorsComponent m_ground_sensors = null;
    private float m_current_force = 0.0f;

    public void add_force(float p_force)
    {
        m_current_force += p_force;
    }

    public JumpComponent(Entity p_entity)
    {
        super(p_entity);
        m_current_force = 0.0f;
    }
    
    @Override
    public ComponentType get_type()
    {
        return ComponentType.JUMP;
    }
    
    @Override
    public void start()
    {
        m_kinematic = get_entity().get_component(ComponentType.KINEMATIC);
        m_ground_sensors = get_entity().get_component(ComponentType.GROUND_SENSORS);
    }
    
    @Override
    public void pre_update(float p_dt)
    {
    
    }
    
    @Override
    public void update(float p_dt)
    {
        m_kinematic.set_acceleration(m_kinematic.get_acceleration().m_x.floatValue(),
                                   -m_current_force);
        //m_current_force = m_current_force / (1 + 0.25f);
        if(m_current_force > 1.0f)
            m_ground_sensors.m_active = false;
        else
            m_ground_sensors.m_active = true;
            
        m_current_force = (Math.max(0, m_current_force - (((m_current_force / (1 + 0.1f))))));

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
