package com.jukaio.jumpandrun.ecs.systemmodule;

import com.jukaio.jumpandrun.ecs.ECS;
import com.jukaio.jumpandrun.ecs.componentmodule.components.ComponentType;
import com.jukaio.jumpandrun.ecs.componentmodule.components.tilemap.SingleTile;
import com.jukaio.jumpandrun.ecs.componentmodule.components.tilemap.Tile;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.RenderCanvas;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.SharedComponentType;
import com.jukaio.jumpandrun.ecs.componentmodule.components.tilemap.Tileset;
import com.jukaio.jumpandrun.ecs.componentmodule.components.Transform;
import com.jukaio.jumpandrun.ecs.entitymodule.Entity;


import java.util.ArrayList;

public class SingleTileRenderer extends System
{
    @Override
    public SystemType get_type() {
        return SystemType.SINGLE_TILE_RENDERER;
    }

    @Override
    public int get_signature() {
        return ComponentType.SINGLE_TILE.as_bitmask() |
               ComponentType.TRANSFORM.as_bitmask() |
               ComponentType.TILE_SET.as_bitmask() |
               (SharedComponentType.RENDER_CANVAS.as_bitmask() << ComponentType.values().length);
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
        final int signature = p_ecs.get_system_signature(this);
        for(Entity entity : p_entities)
        {
            final int entity_signature = p_ecs.get_entity_signature(entity) & signature;
            if(entity_signature == signature)
            {
                SingleTile single_tile = p_ecs.get_component(entity, ComponentType.SINGLE_TILE);
                Transform transform = p_ecs.get_component(entity, ComponentType.TRANSFORM);
                Tileset tileset = p_ecs.get_component(entity, ComponentType.TILE_SET);
                RenderCanvas render = p_ecs.get_shared_component(SharedComponentType.RENDER_CANVAS);

                Tile tile = tileset.m_tiles.get(single_tile.m_tile_id);

                render.m_canvas.drawBitmap(tile.m_bitmap, transform.get_matrix(), render.m_paint);
            }
        }
    }
}
