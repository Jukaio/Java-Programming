package com.jukaio.jumpandrun.ecs.componentmodule.components.tilemap;

import com.jukaio.jumpandrun.ecs.componentmodule.components.Component;
import com.jukaio.jumpandrun.ecs.componentmodule.components.ComponentType;

public class SingleTile extends Component
{
    @Override
    public ComponentType get_type()
    {
        return ComponentType.SINGLE_TILE;
    }
    
    public int m_tile_id;
}
