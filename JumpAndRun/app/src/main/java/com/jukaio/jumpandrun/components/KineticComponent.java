package com.jukaio.jumpandrun.components;

import android.graphics.Paint;

import com.jukaio.jumpandrun.entity.Entity;
import com.jukaio.jumpandrun.Viewport;
import com.jukaio.jumpandrun.XML;
import com.jukaio.jumpandrun.extramath.Vector2;

import org.w3c.dom.Element;

public class KineticComponent extends Component
{
    private Vector2     m_acceleration  = new Vector2();
    private Vector2     m_velocity      = new Vector2();
    private Vector2     m_maximum_speed = new Vector2();
    private float       m_damp          = 0.0f;
    
    public Vector2 get_max_speed()
    {
        return m_maximum_speed;
    }
    
    public void set_max_speed(float p_x, float p_y)
    {
        m_maximum_speed.m_x = p_x;
        m_maximum_speed.m_y = p_y;
    }
    
    public Vector2 get_acceleration()
    {
        return m_acceleration;
    }
    
    public void set_acceleration(float p_x, float p_y)
    {
        m_acceleration.m_x = p_x;
        m_acceleration.m_y = p_y;
    }
    
    public void set_velocity(float p_x, float p_y)
    {
        m_velocity.m_x = p_x;
        m_velocity.m_y = p_y;
    }
    
    public Vector2 get_velocity()
    {
        return m_velocity;
    }
    
    public KineticComponent(Entity p_entity, Element p_data)
    {
        super(p_entity);
        m_velocity.m_x = 0.0f;
        m_velocity.m_y = 0.0f;
        
        m_maximum_speed.m_x = XML.parse_float(p_data,
                                              "max_speed_x");
        m_maximum_speed.m_y = XML.parse_float(p_data,
                                              "max_speed_y");
        m_damp = XML.parse_float(p_data,
                                 "damp");
    }
    
    @Override
    public ComponentType get_type()
    {
        return ComponentType.KINEMATIC;
    }
    
    @Override
    public void start()
    {
    
    }
    
    @Override
    public void pre_update(float p_dt)
    {
        m_acceleration.m_x = 0.0f;
        m_acceleration.m_y = 0.0f;
    }
    
    @Override
    public void update(float p_dt)
    {
    
    }
    
    @Override
    public void late_update(float p_dt)
    {
        update_velocity(this);
    
    }
    
    @Override
    public void render(Viewport p_viewport, Paint p_paint)
    {
    
    }
    
    @Override
    protected void destroy()
    {
        m_acceleration  = null;
        m_velocity      = null;
        m_maximum_speed = null;
    }
    
    private static final double EPSILON = 0.0001;
    
    private static void update_velocity(KineticComponent p_kinematic)
    {
        float accel_x = p_kinematic.get_acceleration().m_x.floatValue();
        float accel_y = p_kinematic.get_acceleration().m_y.floatValue();
    
        float max_speed_x = p_kinematic.get_max_speed().m_x.floatValue();
        float max_speed_y = p_kinematic.get_max_speed().m_y.floatValue();
    
        float current_vel_x = p_kinematic.get_velocity().m_x.floatValue();
        float current_vel_y = p_kinematic.get_velocity().m_y.floatValue();
    
        float to_apply_x = current_vel_x;
        float to_apply_y = current_vel_y;
    
        float next_vel_x = current_vel_x + accel_x;
        float next_vel_y = current_vel_y + accel_y;
    
        float direction_x = (next_vel_x <= 0 + EPSILON && next_vel_x >= 0 - EPSILON) ?
                0.0f :
                next_vel_x / Math.abs(next_vel_x);
        float direction_y = (next_vel_y <= 0 + EPSILON && next_vel_y >= 0 - EPSILON) ?
                0.0f :
                next_vel_y / Math.abs(next_vel_y);
    
        next_vel_x = next_vel_x / (1 + p_kinematic.m_damp);
        next_vel_y = next_vel_y / (1 + p_kinematic.m_damp);
    
        to_apply_x = (Math.abs(next_vel_x) > max_speed_x) ? direction_x * max_speed_x : next_vel_x;
        to_apply_y = (Math.abs(next_vel_y) > max_speed_y) ? direction_y * max_speed_y : next_vel_y;
    
        if (to_apply_x < 0.001 &&
                to_apply_x > -0.001)
        {
            to_apply_x = 0;
        }
        if (to_apply_y < 0.001 &&
                to_apply_y > -0.001)
        {
            to_apply_y = 0;
        }
    
    
        p_kinematic.set_velocity(to_apply_x,
                                 to_apply_y);
    }
}
