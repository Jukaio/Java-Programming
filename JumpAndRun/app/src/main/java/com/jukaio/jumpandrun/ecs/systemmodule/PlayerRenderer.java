package com.jukaio.jumpandrun.ecs.systemmodule;

import com.jukaio.jumpandrun.ecs.ECS;
import com.jukaio.jumpandrun.ecs.componentmodule.components.ComponentType;
import com.jukaio.jumpandrun.ecs.componentmodule.components.Player;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.RenderCanvas;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.SharedComponentType;
import com.jukaio.jumpandrun.ecs.componentmodule.components.Tileset;
import com.jukaio.jumpandrun.ecs.componentmodule.components.Transform;
import com.jukaio.jumpandrun.ecs.entitymodule.Entity;
import com.jukaio.jumpandrun.world.Tilemap.Tile;

import java.util.ArrayList;

public class PlayerRenderer extends System
{
    @Override
    public SystemType get_type() {
        return SystemType.PLAYER;
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
        final int signature = p_ecs.get_system_signature(this);
        for(Entity entity : p_entities)
        {
            final int entity_signature = p_ecs.get_entity_signature(entity) & signature;
            if(entity_signature == signature)
            {
                Player player = p_ecs.get_component(entity, ComponentType.PLAYER);
                Transform transform = p_ecs.get_component(entity, ComponentType.TRANSFORM);
                Tileset tileset = p_ecs.get_component(entity, ComponentType.TILE_SET);
                RenderCanvas render = p_ecs.get_shared_component(SharedComponentType.RENDER_CANVAS);

                Tile tile = tileset.m_tiles.get(player.m_tile_id);

                render.m_canvas.drawBitmap(tile.m_bitmap, transform.get_matrix(), render.m_paint);
            }
        }
    }
}
