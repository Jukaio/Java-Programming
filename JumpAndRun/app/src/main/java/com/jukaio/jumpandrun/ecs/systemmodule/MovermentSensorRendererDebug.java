package com.jukaio.jumpandrun.ecs.systemmodule;

import android.graphics.Color;

import com.jukaio.jumpandrun.ecs.ECS;
import com.jukaio.jumpandrun.ecs.componentmodule.components.ComponentType;
import com.jukaio.jumpandrun.ecs.componentmodule.components.MovementSensors;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.RenderCanvas;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.SharedComponentType;
import com.jukaio.jumpandrun.ecs.entitymodule.Entity;

import java.util.ArrayList;

public class MovermentSensorRendererDebug extends System
{
    @Override
    public SystemType get_type()
    {
        return SystemType.MOVEMENT_SENSOR_RENDERER_DEBUG;
    }
    
    @Override
    public int get_signature()
    {
        return ComponentType.MOVEMENT_SENSOR.as_bitmask() |
               (SharedComponentType.RENDER_CANVAS.as_bitmask() << ComponentType.values().length);
    }

    
    @Override
    protected void init(ECS p_ecs, ArrayList<Entity> p_entities)
    {
    
    }
    
    @Override
    public void update(ECS p_ecs, ArrayList<Entity> p_entities)
    {
    
    }
    
    @Override
    public void render(ECS p_ecs, ArrayList<Entity> p_entities)
    {
        final int system_signature = p_ecs.get_system_signature(get_type());
        for (Entity entity : p_entities)
        {
            if ((p_ecs.get_entity_signature(entity) & system_signature) == system_signature)
            {
                MovementSensors sensors = p_ecs.get_component(entity,
                                                              ComponentType.MOVEMENT_SENSOR);
                RenderCanvas renderer = p_ecs.get_shared_component(SharedComponentType.RENDER_CANVAS);
            
                int prev_color = renderer.m_paint.getColor();
            
                renderer.m_paint.setColor(Color.YELLOW);
                
                renderer.m_canvas.drawLine(sensors.m_ground_left.m_start.x,
                                           sensors.m_ground_left.m_start.y,
                                           sensors.m_ground_left.m_end.x,
                                           sensors.m_ground_left.m_end.y,
                                           renderer.m_paint);
                renderer.m_canvas.drawLine(sensors.m_ground_right.m_start.x,
                                           sensors.m_ground_right.m_start.y,
                                           sensors.m_ground_right.m_end.x,
                                           sensors.m_ground_right.m_end.y,
                                           renderer.m_paint);
                                           
                renderer.m_canvas.drawLine(sensors.m_ground_center.m_start.x,
                                           sensors.m_ground_center.m_start.y,
                                           sensors.m_ground_center.m_end.x,
                                           sensors.m_ground_center.m_end.y,
                                           renderer.m_paint);
                                           
                renderer.m_canvas.drawLine(sensors.m_wall.m_start.x,
                                           sensors.m_wall.m_start.y,
                                           sensors.m_wall.m_end.x,
                                           sensors.m_wall.m_end.y,
                                           renderer.m_paint);
                renderer.m_paint.setColor(prev_color);
            }
        }
    }
}
