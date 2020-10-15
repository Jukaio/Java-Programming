package com.jukaio.jumpandrun.ecs.componentmodule.components.physics;

import com.jukaio.jumpandrun.ecs.componentmodule.components.Component;
import com.jukaio.jumpandrun.ecs.componentmodule.components.ComponentType;
import com.jukaio.jumpandrun.extramath.Vector2;

public class Kinematic extends Component
{
    @Override
    public ComponentType get_type()
    {
        return ComponentType.KINEMATICS;
    }
    
    private Vector2 m_speed = new Vector2();
    private Vector2 m_velocity = new Vector2();
    private Vector2 m_acceleration = new Vector2();
    
    public void set_acceleration(float p_x, float p_y)
    {
        m_acceleration.m_x = p_x;
        m_acceleration.m_y = p_y;
    }
    
    public Vector2 get_acceleration()
    {
        return m_acceleration;
    }
    
    public void set_velocity(float p_x, float p_y)
    {
        m_velocity.m_x = p_x;
        m_velocity.m_y = p_y;
    }
    
    public void set_speed(float p_speed_x, float p_speed_y)
    {
        m_speed.m_x = p_speed_x;
        m_speed.m_y = p_speed_y;
    }
    
    public Vector2 get_speed()
    {
        return m_speed;
    }
    
    public void add_velocity(float p_x, float p_y)
    {
        m_velocity.m_x = m_velocity.m_x.floatValue() + p_x;
        m_velocity.m_y = m_velocity.m_y.floatValue() + p_y;
    }
    
    public Vector2 get_velocity()
    {
        return m_velocity;
    }
}
