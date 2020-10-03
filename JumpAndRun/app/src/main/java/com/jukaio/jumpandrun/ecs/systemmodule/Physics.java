package com.jukaio.jumpandrun.ecs.systemmodule;

import com.jukaio.jumpandrun.ecs.ECS;
import com.jukaio.jumpandrun.ecs.componentmodule.components.ComponentType;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.Gravity;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.SharedComponentType;
import com.jukaio.jumpandrun.ecs.componentmodule.components.Transform;
import com.jukaio.jumpandrun.ecs.entitymodule.Entity;
import com.jukaio.jumpandrun.extramath.Vector2;

import java.util.ArrayList;

public class Physics extends System
{
    @Override
    public SystemType get_type() {
        return SystemType.PHYSICS;
    }

    @Override
    public int get_signature() {
        return 0;
    }

    @Override
    public void init(ECS p_ecs, ArrayList<Entity> p_entities)
    {

    }

    @Override
    public void update(ECS p_ecs, ArrayList<Entity> p_entities)
    {

    }

    @Override
    public void render(ECS p_ecs, ArrayList<Entity> p_entities)
    {
        final int system_signature = p_ecs.get_system_signature(this);
        for(Entity entity : p_entities)
        {
            if((p_ecs.get_entity_signature(entity) & system_signature) == system_signature)
            {
                Gravity gravity = p_ecs.get_shared_component(SharedComponentType.GRAVITY);
                Transform transform = p_ecs.get_component(entity, ComponentType.TRANSFORM);

                Vector2 position = transform.get_position();
                float add_x = (gravity.m_direction.m_x.floatValue() * gravity.m_drag);
                float add_y = (gravity.m_direction.m_y.floatValue() * gravity.m_drag);
                position.m_x = position.m_x.floatValue()  + add_x;
                position.m_y = position.m_y.floatValue()  + add_y;
                transform.set_position(position.m_x.floatValue(), position.m_y.floatValue());
            }
        }
    }

}
