package com.jukaio.jumpandrun.components;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.jukaio.jumpandrun.Entity;
import com.jukaio.jumpandrun.InputManager;
import com.jukaio.jumpandrun.XML;

import org.w3c.dom.Element;

public class InputComponent extends Component
{
    KineticComponent m_kinematic = null;
    JumpComponent m_jump = null;
    GroundSensorsComponent m_ground_sensors = null;
    
    float m_speed_x = 0.0f;
    float m_speed_y = 0.0f;
    float m_jump_force = 0.0f;
    
    public InputComponent(Entity p_entity, Element p_data)
    {
        super(p_entity);
        m_speed_x = XML.parse_float(p_data, "acceleration_x");
        m_speed_y = XML.parse_float(p_data, "acceleration_y");
        m_jump_force = XML.parse_float(p_data, "jump_force");
    }
    
    @Override
    public ComponentType get_type()
    {
        return ComponentType.INPUT;
    }
    
    @Override
    public void start()
    {
        m_kinematic = get_entity().get_component(ComponentType.KINEMATIC);
        m_jump = get_entity().get_component(ComponentType.JUMP);
        m_ground_sensors = get_entity().get_component(ComponentType.GROUND_SENSORS);
    }
    
    @Override
    public void pre_update(float p_dt)
    {
        if (InputManager.is_jump() && m_ground_sensors.m_collisions.size() != 0)
        {
            m_jump.add_force(m_jump_force);
        }
    
        float accel_x = m_kinematic.get_acceleration().m_x.floatValue();
        float accel_y = m_kinematic.get_acceleration().m_x.floatValue();
              accel_x += InputManager.get_horizontal() * m_speed_x;
        m_kinematic.set_acceleration(accel_x,
                                     accel_y);
        
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
