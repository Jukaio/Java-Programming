package com.jukaio.jumpandrun.components;

import android.graphics.Paint;

import com.jukaio.jumpandrun.entity.Entity;
import com.jukaio.jumpandrun.input.InputManager;
import com.jukaio.jumpandrun.Jukebox;
import com.jukaio.jumpandrun.SoundID;
import com.jukaio.jumpandrun.Viewport;
import com.jukaio.jumpandrun.XML;

import org.w3c.dom.Element;

public class PlayerControllerComponent extends Component
{
    private KineticComponent        m_kinematic         = null;
    private JumpComponent           m_jump              = null;
    private GroundSensorsComponent  m_ground_sensors    = null;
    private float                   m_jump_timer        = 0.0f;
    private float                   m_speed_x           = 0.0f;
    private float                   m_speed_y           = 0.0f;
    private float                   m_jump_force        = 0.0f;
    private float                   m_jump_cooldown     = 0.0f;
    private float                   spawn_x             = 0.0f;
    private float                   spawn_y             = 0.0f;
    private int                     previous_collision  = 0;
    
    public PlayerControllerComponent(Entity p_entity, Element p_data)
    {
        super(p_entity);
        m_speed_x = XML.parse_float(p_data, "acceleration_x");
        m_speed_y = XML.parse_float(p_data, "acceleration_y");
        m_jump_force = XML.parse_float(p_data, "jump_force");
        m_jump_cooldown = XML.parse_float(p_data, "jump_cooldown");
        spawn_x = get_entity().get_position().m_x.floatValue();
        spawn_y = get_entity().get_position().m_y.floatValue();
    }
    
    @Override
    public ComponentType get_type()
    {
        return ComponentType.PLAYER_CONTROLLER;
    }
    
    @Override
    public void start()
    {
        get_entity().set_active(true);
        m_kinematic = get_entity().get_component(ComponentType.KINEMATIC);
        m_jump = get_entity().get_component(ComponentType.JUMP);
        m_ground_sensors = get_entity().get_component(ComponentType.GROUND_SENSORS);
        get_entity().set_position(spawn_x, spawn_y);
    }
    
    @Override
    public void pre_update(float p_dt)
    {
        if(previous_collision == 0 && m_ground_sensors.m_collisions.size() != 0)
        {
            m_jump_timer = m_jump_cooldown;
        }
        else if (InputManager.is_jump() && m_ground_sensors.m_collisions.size() != 0 && m_jump_timer <= 0.0f)
        {
            m_jump.add_force(m_jump_force);
        }
        else if(m_jump_timer > 0.0f)
        {
            m_jump_timer -= p_dt;
        }
    
        float accel_x = m_kinematic.get_acceleration().m_x.floatValue();
        float accel_y = m_kinematic.get_acceleration().m_x.floatValue();
              accel_x += InputManager.get_horizontal() * m_speed_x;
        m_kinematic.set_acceleration(accel_x,
                                     accel_y);
        
        previous_collision = m_ground_sensors.m_collisions.size();
    }
    
    @Override
    public void update(float p_dt)
    {
    
    }
    
    @Override
    public void late_update(float p_dt)
    {
        //Viewport.m_active.look_at(get_entity());
        Viewport.m_active.look_at(get_entity().get_position().m_x.floatValue(), 176);
    }
    
    @Override
    public void render(Viewport p_viewport, Paint p_paint)
    {

    }
    
    @Override
    protected void destroy()
    {
        m_kinematic         = null;
        m_jump              = null;
        m_ground_sensors    = null;
    }
    
    @Override
    public void on_collision(Entity p_other)
    {
        switch (p_other.get_type())
        {
            case LETHAL:
                get_entity().set_position(spawn_x, spawn_y);
                Jukebox.play(SoundID.GET_HIT, Jukebox.MAX_VOLUME, 0);
                break;
        }
    }
}
