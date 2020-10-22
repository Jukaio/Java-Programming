package com.jukaio.jumpandrun.ecs.systemmodule;

import android.util.Log;

import com.jukaio.jumpandrun.ecs.ECS;
import com.jukaio.jumpandrun.ecs.componentmodule.components.ComponentType;
import com.jukaio.jumpandrun.ecs.componentmodule.components.MovementSensors;
import com.jukaio.jumpandrun.ecs.componentmodule.components.RectangleCollider;
import com.jukaio.jumpandrun.ecs.componentmodule.components.Transform;
import com.jukaio.jumpandrun.ecs.componentmodule.components.physics.Kinematic;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.Gravity;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.SharedComponentType;
import com.jukaio.jumpandrun.ecs.entitymodule.Entity;
import com.jukaio.jumpandrun.extramath.Formulas;
import com.jukaio.jumpandrun.extramath.Vector2;

import java.util.ArrayList;

public class ApplyForces extends System
{
    @Override
    public SystemType get_type()
    {
        return SystemType.APPLY_FORCES;
    }
    
    @Override
    public int get_signature()
    {
        return ComponentType.TRANSFORM.as_bitmask() |
                ComponentType.RECTANGLE_COLLIDER.as_bitmask() |
                ComponentType.MOVEMENT_SENSOR.as_bitmask() |
                ComponentType.KINEMATICS.as_bitmask() |
                (SharedComponentType.GRAVITY.as_bitmask() << ComponentType.values().length);
    }
    
    @Override
    protected void init(ECS p_ecs, ArrayList<Entity> p_entities)
    {
    
    }
    
    @Override
    public void update(ECS p_ecs, ArrayList<Entity> p_entities)
    {
        final int system_signature = p_ecs.get_system_signature(this);
        for (Entity entity : p_entities)
        {
            if ((p_ecs.get_entity_signature(entity) & system_signature) == system_signature)
            {
                Gravity gravity = p_ecs.get_shared_component(SharedComponentType.GRAVITY);
                Transform transform = p_ecs.get_component(entity,
                                                          ComponentType.TRANSFORM);
                RectangleCollider entity_collider = p_ecs.get_component(entity,
                                                                        ComponentType.RECTANGLE_COLLIDER); // Later for entity collision
                MovementSensors sensors = p_ecs.get_component(entity,
                                                              ComponentType.MOVEMENT_SENSOR);
                Kinematic kinematic = p_ecs.get_component(entity,
                                                          ComponentType.KINEMATICS);
    
                transform.set_prev_position(transform.get_position().m_x.floatValue(),
                                            transform.get_position().m_y.floatValue());
                transform.set_prev_scale(transform.get_scale().m_x.floatValue(),
                                         transform.get_scale().m_y.floatValue());
                transform.set_prev_rotation(transform.get_rotation());
    
                update_velocity(kinematic);
                apply_force(transform,
                            kinematic);
                apply_gravity(transform,
                              gravity);
            }
        }
    }
    
    @Override
    public void render(ECS p_ecs, ArrayList<Entity> p_entities)
    {
    
    }
    
    void update_velocity(Kinematic p_kinematic)
    {
        if(Formulas.length(p_kinematic.get_acceleration().m_x.floatValue(), p_kinematic.get_acceleration().m_y.floatValue()) > 0.0f)
        {
            float accel_x = p_kinematic.get_acceleration().m_x.floatValue();
            float accel_y = p_kinematic.get_acceleration().m_y.floatValue();
            
            float max_speed_x = p_kinematic.get_speed().m_x.floatValue();
            float max_speed_y = p_kinematic.get_speed().m_y.floatValue();
            
            float current_vel_x = p_kinematic.get_velocity().m_x.floatValue();
            float current_vel_y = p_kinematic.get_velocity().m_y.floatValue();
            
            float next_vel_x = current_vel_x + accel_x;
            float next_vel_y = current_vel_y + accel_y;
            
            float to_apply_x = (Math.abs(next_vel_x) > max_speed_x) ? current_vel_x : next_vel_x;
            float to_apply_y = (Math.abs(next_vel_y) > max_speed_y) ? current_vel_y : next_vel_y;

            p_kinematic.set_velocity(to_apply_x, to_apply_y);
        }
    }
    
    void apply_force(Transform p_transform, Kinematic p_kinematic)
    {
        Vector2 position = p_transform.get_position();
        float vel_x = p_kinematic.get_velocity().m_x.floatValue();
        float vel_y = p_kinematic.get_velocity().m_y.floatValue();
        
        p_transform.set_position(position.m_x.floatValue() + vel_x,
                                 position.m_y.floatValue() + vel_y);
        if(vel_x != 0.0f)
        {
            float flip_scale_x = p_kinematic.get_velocity().m_x.floatValue();
            p_transform.set_scale(Math.abs(flip_scale_x) / flip_scale_x, 1);
        }
    }
    
    void apply_gravity(Transform p_transform, Gravity p_gravity)
    {
        Vector2 position = p_transform.get_position();
        float gravity_x = p_gravity.m_direction.m_x.floatValue() * p_gravity.m_drag;
        float gravity_y = p_gravity.m_direction.m_y.floatValue() * p_gravity.m_drag;
        
        p_transform.set_position(position.m_x.floatValue() + gravity_x,
                                 position.m_y.floatValue() + gravity_y);
    }
}
