package com.jukaio.jumpandrun.ecs.systemmodule;

import android.util.Log;

import com.jukaio.jumpandrun.ecs.ECS;
import com.jukaio.jumpandrun.ecs.componentmodule.components.ComponentType;
import com.jukaio.jumpandrun.ecs.componentmodule.components.MovementSensors;
import com.jukaio.jumpandrun.ecs.componentmodule.components.physics.Jump;
import com.jukaio.jumpandrun.ecs.componentmodule.components.physics.Kinematic;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.SharedComponentType;
import com.jukaio.jumpandrun.ecs.entitymodule.Entity;
import com.jukaio.jumpandrun.extramath.Formulas;

import java.util.ArrayList;

public class Jumping extends System
{
    @Override
    public SystemType get_type()
    {
        return SystemType.JUMP;
    }
    
    @Override
     public int get_signature()
     {
         return  ComponentType.JUMP.as_bitmask() |
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
                 Kinematic kinematic = p_ecs.get_component(entity,
                                                           ComponentType.KINEMATICS);
                 Jump jump = p_ecs.get_component(entity,
                                                 ComponentType.JUMP);
    
                 kinematic.set_acceleration(kinematic.get_acceleration().m_x.floatValue(),
                                            -jump.get_current_force());
                 jump.set_current_force(Math.max(0,
                                                 jump.get_current_force() - (((jump.get_current_force() * 0.1f) + 0.1f))));
                 if(jump.get_current_force() == 0.0f && kinematic.get_acceleration().m_y.floatValue() == 0.0f)
                    kinematic.set_velocity(kinematic.get_velocity().m_x.floatValue(), 0.0f);
             }
         }
     }
    
    @Override
    public void render(ECS p_ecs, ArrayList<Entity> p_entities)
    {
    
    }
}
