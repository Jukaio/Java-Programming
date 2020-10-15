package com.jukaio.jumpandrun.ecs.systemmodule;

import android.graphics.Color;
import android.graphics.Paint;

import com.jukaio.jumpandrun.ecs.ECS;
import com.jukaio.jumpandrun.ecs.componentmodule.components.ComponentType;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.RenderCanvas;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.SharedComponentType;
import com.jukaio.jumpandrun.ecs.componentmodule.components.tilemap.Grid;
import com.jukaio.jumpandrun.ecs.entitymodule.Entity;

import java.util.ArrayList;

public class GridRenderer extends System
{
    @Override
    public SystemType get_type() {
        return SystemType.GRID_RENDERER;
    }

    @Override
    public int get_signature() {
        return 0;
    }

    @Override
    public void init(ECS p_ecs, ArrayList<Entity> p_entities) {

    }

    @Override
    public void update(ECS p_ecs, ArrayList<Entity> p_entities)
    {

    }

    @Override
    public void render(ECS p_ecs, ArrayList<Entity> p_entities)
    {
        final int system_signature = p_ecs.get_system_signature(get_type());
        for(Entity entity : p_entities)
        {
            if((p_ecs.get_entity_signature(entity) & system_signature) == system_signature)
            {
                Grid grid = p_ecs.get_component(entity, ComponentType.GRID);
                RenderCanvas render = p_ecs.get_shared_component(SharedComponentType.RENDER_CANVAS);

                float tile_width = grid.m_tile_dimensions.m_x.floatValue();
                float tile_height = grid.m_tile_dimensions.m_y.floatValue();
                render.m_paint.setColor(Color.GREEN);
                render.m_paint.setStyle(Paint.Style.STROKE);
                for(int y = 0; y < grid.m_dimensions.m_y.intValue(); y++)
                {
                    for(int x = 0; x < grid.m_dimensions.m_x.intValue(); x++)
                    {
                        float pos_x = x * tile_width;
                        float pos_y = y * tile_height;
                        render.m_canvas.drawRect(pos_x, pos_y, pos_x + tile_width, pos_y + tile_height, render.m_paint);
                    }
                }

            }
        }
    }
}
