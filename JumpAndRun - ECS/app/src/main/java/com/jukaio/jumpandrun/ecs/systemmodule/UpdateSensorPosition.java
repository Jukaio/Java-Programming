package com.jukaio.jumpandrun.ecs.systemmodule;

import com.jukaio.jumpandrun.ecs.ECS;
import com.jukaio.jumpandrun.ecs.componentmodule.components.ComponentType;
import com.jukaio.jumpandrun.ecs.componentmodule.components.MovementSensors;
import com.jukaio.jumpandrun.ecs.componentmodule.components.Transform;
import com.jukaio.jumpandrun.ecs.componentmodule.components.tilemap.Tileset;
import com.jukaio.jumpandrun.ecs.entitymodule.Entity;
import com.jukaio.jumpandrun.extramath.Formulas;
import com.jukaio.jumpandrun.extramath.Line;

import java.util.ArrayList;

public class UpdateSensorPosition extends System
{
    @Override
    public SystemType get_type()
    {
        return SystemType.MOVEMENT_SENSOR_RENDERER_DEBUG;
    }
    
    @Override
    public int get_signature()
    {
        return ComponentType.TRANSFORM.as_bitmask() |
               ComponentType.MOVEMENT_SENSOR.as_bitmask() |
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
                           MovementSensors sensors = p_ecs.get_component(entity,
                                                                         ComponentType.MOVEMENT_SENSOR);
                           Transform transform = p_ecs.get_component(entity,
                                                                     ComponentType.TRANSFORM);
                           Tileset set = p_ecs.get_component(entity,
                                                             ComponentType.TILE_SET);
    
                           float rotation = transform.get_rotation() * Formulas.D2R;
                           float cos_angle = (float) Math.cos(rotation);
                           float sin_angle = (float) Math.sin(rotation);
                           
                           final float offset_y = sensors.m_offset_y;
                           final float offset_x = sensors.m_offset_x;
                           final float x = transform.get_position().m_x.floatValue();
                           final float y = transform.get_position().m_y.floatValue();
                           final float width = set.m_tile_dimensions.m_x.floatValue();
                           final float height = set.m_tile_dimensions.m_y.floatValue();
                           final float half_w = set.m_tile_dimensions.m_x.floatValue() * 0.5f;
                           final float half_h = set.m_tile_dimensions.m_y.floatValue() * 0.5f;
    

                           float ground_start_x = (cos_angle * (x - x)) - (sin_angle * ((y - half_h) - y));
                           float ground_start_y = (sin_angle * (x - x)) + (cos_angle * ((y - half_h) - y));
                           float ground_end_x = (cos_angle * (x - x)) - (sin_angle * ((y + half_h) - y));
                           float ground_end_y = (sin_angle * (x - x)) + (cos_angle * ((y + half_h) - y));
                           

                           
                           sensors.m_ground_left.m_start.x = (int) (x + offset_x - half_w);
                           sensors.m_ground_left.m_start.y = (int) (y + offset_y - half_h);
                           sensors.m_ground_left.m_end.x = (int) (x + offset_x - half_w);
                           sensors.m_ground_left.m_end.y = (int) (y + half_h + offset_y);
    
                           sensors.m_ground_center.m_start.x = (int) (x);
                           sensors.m_ground_center.m_start.y = (int) (y - half_h + offset_x);
                           sensors.m_ground_center.m_end.x = (int) (x);
                           sensors.m_ground_center.m_end.y = (int) (y + half_h + offset_y);
    
                           sensors.m_ground_right.m_start.x = (int) (x + half_w - offset_x);
                           sensors.m_ground_right.m_start.y = (int) (y - half_h + offset_y);
                           sensors.m_ground_right.m_end.x = (int) (x + half_w - offset_x);
                           sensors.m_ground_right.m_end.y = (int) (y + half_h + offset_y);
    
                           sensors.m_wall.m_start.x = (int) (x - half_w + 2);
                           sensors.m_wall.m_start.y = (int) (y - (half_w * 0.5f));
                           sensors.m_wall.m_end.x = (int) (x + half_w - 2);
                           sensors.m_wall.m_end.y = (int) (y - (half_w * 0.5f));
                           
                           rotate_line(sensors.m_ground_center, x, y, cos_angle, sin_angle);
                           rotate_line(sensors.m_ground_left, x, y, cos_angle, sin_angle);
                           rotate_line(sensors.m_ground_right, x, y, cos_angle, sin_angle);
                       }
                   }
    }
    
    private void rotate_line(Line p_line, float p_local_x, float p_local_y, float p_cos, float p_sin)
    {
        float start_x = p_line.m_start.x;
        float start_y = p_line.m_start.y;
        float end_x = p_line.m_end.x;
        float end_y = p_line.m_end.y;

        
        p_line.m_start.x = (int)((p_cos * (start_x - p_local_x)) - (p_sin * (start_y - p_local_y)) + p_local_x);
        p_line.m_start.y = (int)((p_sin * (start_x - p_local_x)) + (p_cos * (start_y - p_local_y)) + p_local_y);
        p_line.m_end.x = (int)((p_cos * (end_x - p_local_x)) - (p_sin * (end_y - p_local_y)) + p_local_x);
        p_line.m_end.y = (int)((p_sin * (end_x - p_local_x)) + (p_cos * (end_y - p_local_y)) + p_local_y);
    }
    
    @Override
    public void render(ECS p_ecs, ArrayList<Entity> p_entities)
    {
    
    }
}
