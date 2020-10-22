package com.jukaio.jumpandrun.ecs.componentmodule.components.physics;

import com.jukaio.jumpandrun.ecs.componentmodule.components.Component;
import com.jukaio.jumpandrun.ecs.componentmodule.components.ComponentType;

public class Jump extends Component
{
    @Override
    public ComponentType get_type()
    {
        return ComponentType.JUMP;
    }
    
    private boolean m_on_ground = false;
    private boolean m_prev_on_ground = false;
    private float m_jump_force = 0.0f;
    private float m_used_force = 0.0f;

    public void set_jump_force(float p_force)
    {
        m_jump_force = p_force;
    }
    
    public float get_jump_force()
    {
        return m_jump_force;
    }
    
    public void set_current_force(float p_force)
    {
        m_used_force = p_force;
    }
    
    public float get_current_force()
    {
        return m_used_force;
    }
    
    public void set_on_ground(boolean p_on_ground)
    {
        m_on_ground = p_on_ground;
    }
    
    
    public boolean get_on_ground()
    {
        return m_on_ground;
    }
    
    
    public void set_prev_on_ground(boolean p_on_ground)
    {
        m_prev_on_ground = p_on_ground;
    }
    
    
    public boolean get_prev_on_ground()
    {
        return m_prev_on_ground;
    }
    
}
