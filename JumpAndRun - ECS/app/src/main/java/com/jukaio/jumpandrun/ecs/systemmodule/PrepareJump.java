package com.jukaio.jumpandrun.ecs.systemmodule;

import com.jukaio.jumpandrun.ecs.ECS;
import com.jukaio.jumpandrun.ecs.componentmodule.components.ComponentType;
import com.jukaio.jumpandrun.ecs.componentmodule.components.MovementSensors;
import com.jukaio.jumpandrun.ecs.componentmodule.components.physics.Jump;
import com.jukaio.jumpandrun.ecs.componentmodule.components.physics.Kinematic;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.InputController;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.SharedComponentType;
import com.jukaio.jumpandrun.ecs.entitymodule.Entity;

import java.util.ArrayList;

public class PrepareJump extends System
{
    @Override
    public SystemType get_type()
    {
        return SystemType.PREPARE_JUMP;
    }
    
    @Override
    public int get_signature()
    {
        return ComponentType.MOVEMENT_SENSOR.as_bitmask() |
                ComponentType.JUMP.as_bitmask() |
                ComponentType.KINEMATICS.as_bitmask();
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
                MovementSensors sensors = p_ecs.get_component(entity,
                                                              ComponentType.MOVEMENT_SENSOR);
                Jump jump = p_ecs.get_component(entity,
                                                ComponentType.JUMP);
                jump.set_prev_on_ground(jump.get_prev_on_ground());
                jump.set_on_ground(sensors.m_on_ground);
            }
        }
    }
    

    
    @Override
    public void render(ECS p_ecs, ArrayList<Entity> p_entities)
    {
    
    }
}
