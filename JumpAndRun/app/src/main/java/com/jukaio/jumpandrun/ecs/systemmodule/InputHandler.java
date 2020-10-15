package com.jukaio.jumpandrun.ecs.systemmodule;

import android.util.Log;

import com.jukaio.jumpandrun.ecs.ECS;
import com.jukaio.jumpandrun.ecs.componentmodule.components.Component;
import com.jukaio.jumpandrun.ecs.componentmodule.components.ComponentType;
import com.jukaio.jumpandrun.ecs.componentmodule.components.physics.Jump;
import com.jukaio.jumpandrun.ecs.componentmodule.components.physics.Kinematic;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.InputController;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.SharedComponentType;
import com.jukaio.jumpandrun.ecs.entitymodule.Entity;

import java.util.ArrayList;

public class InputHandler extends System
{
    @Override
    public SystemType get_type()
    {
        return SystemType.INPUT_HANDLER;
    }
    
    @Override
    public int get_signature()
    {
        return ComponentType.KINEMATICS.as_bitmask() |
               ComponentType.JUMP.as_bitmask() |
               (SharedComponentType.INPUT_CONTROLLER.as_bitmask() << ComponentType.values().length);
    }
    
    @Override
    protected void init(ECS p_ecs, ArrayList<Entity> p_entities)
    {
    
    }
    
    @Override
    public void update(ECS p_ecs, ArrayList<Entity> p_entities)
    {
        final int system_signature = p_ecs.get_system_signature(get_type());
        for(Entity entity : p_entities)
        {
            if ((p_ecs.get_entity_signature(entity) & system_signature) == system_signature)
            {
                Kinematic kinematic = p_ecs.get_component(entity, ComponentType.KINEMATICS);
                InputController input = p_ecs.get_shared_component(SharedComponentType.INPUT_CONTROLLER);
                Jump jump = p_ecs.get_component(entity, ComponentType.JUMP);
                
                
                if(input.m_horizontal != 0.0f)
                {
                    kinematic.set_acceleration(input.m_horizontal,  0);
                }
                else
                {
                    kinematic.set_acceleration(0,  0);
                    kinematic.set_velocity(0, kinematic.get_velocity().m_y.floatValue());
                }
                
                if(!input.m_prev_jump && input.m_jump && jump.get_on_ground())
                {
                    jump.set_current_force(jump.get_jump_force());
                }
            }
        }
    }
    
    @Override
    public void render(ECS p_ecs, ArrayList<Entity> p_entities)
    {
    
    }
}
