package com.jukaio.jumpandrun.ecs.systemmodule;

import com.jukaio.jumpandrun.ecs.ECS;
import com.jukaio.jumpandrun.ecs.componentmodule.components.ComponentType;
import com.jukaio.jumpandrun.ecs.componentmodule.components.RectangleCollider;
import com.jukaio.jumpandrun.ecs.componentmodule.components.tilemap.Tileset;
import com.jukaio.jumpandrun.ecs.componentmodule.components.Transform;

import com.jukaio.jumpandrun.ecs.entitymodule.Entity;


import java.util.ArrayList;

public class UpdateColliderPosition extends System
{
    @Override
    public SystemType get_type()
    {
        return SystemType.UPDATE_COLLIDER_POSITION;
    }
    
    @Override
    public int get_signature()
    {
        return ComponentType.TRANSFORM.as_bitmask() |
               ComponentType.RECTANGLE_COLLIDER.as_bitmask() |
               ComponentType.TILE_SET.as_bitmask();
    }
    
    @Override
    protected void init(ECS p_ecs, ArrayList<Entity> p_entities)
    {

    }
    
    @Override
    public void update(ECS p_ecs, ArrayList<Entity> p_entities)
    {
        final int system_signature = p_ecs.get_system_signature(this);
           for(Entity entity : p_entities)
           {
               if((p_ecs.get_entity_signature(entity) & system_signature) == system_signature)
               {
                   RectangleCollider collider = p_ecs.get_component(entity, ComponentType.RECTANGLE_COLLIDER);
                   Transform transform = p_ecs.get_component(entity, ComponentType.TRANSFORM);
                   Tileset set = p_ecs.get_component(entity, ComponentType.TILE_SET);
                   
                   float pos_x = transform.get_position().m_x.floatValue();
                   float pos_y = transform.get_position().m_y.floatValue();
                   float half_x = transform.get_dimensions().m_x.floatValue() * 0.5f;
                   float half_y = transform.get_dimensions().m_y.floatValue() * 0.5f;
                   
                   collider.m_bounds.left = pos_x - half_x;
                   collider.m_bounds.top = pos_y - half_y;
                   collider.m_bounds.right = pos_x + half_x;
                   collider.m_bounds.bottom = pos_y + half_y;
               }
           }
    }
    
    @Override
    public void render(ECS p_ecs, ArrayList<Entity> p_entities)
    {
    
    }
}
