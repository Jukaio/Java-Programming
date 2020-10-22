package com.jukaio.jumpandrun.ecs.systemmodule;

import android.graphics.Color;
import android.graphics.Paint;

import com.jukaio.jumpandrun.ecs.ECS;
import com.jukaio.jumpandrun.ecs.componentmodule.components.ComponentType;
import com.jukaio.jumpandrun.ecs.componentmodule.components.RectangleCollider;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.RenderCanvas;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.SharedComponentType;
import com.jukaio.jumpandrun.ecs.entitymodule.Entity;

import java.util.ArrayList;

public class ColliderRendererDebug extends System
{
    @Override
    public SystemType get_type()
    {
        return SystemType.COLLIDER_RENDERER_DEBUG;
    }
    
    @Override
    public int get_signature()
    {
        return ComponentType.RECTANGLE_COLLIDER.as_bitmask() |
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
                   RectangleCollider rect = p_ecs.get_component(entity, ComponentType.RECTANGLE_COLLIDER);
                   RenderCanvas renderer = p_ecs.get_shared_component(SharedComponentType.RENDER_CANVAS);
                   
                   int prev_color = renderer.m_paint.getColor();
                   Paint.Style prev_style = renderer.m_paint.getStyle();
                   renderer.m_paint.setStyle(Paint.Style.STROKE);
                   renderer.m_paint.setColor(Color.RED);
                   renderer.m_canvas.drawRect(rect.m_bounds, renderer.m_paint);
                   renderer.m_paint.setColor(prev_color);
                   renderer.m_paint.setStyle(prev_style);
               }
           }
    }
}
