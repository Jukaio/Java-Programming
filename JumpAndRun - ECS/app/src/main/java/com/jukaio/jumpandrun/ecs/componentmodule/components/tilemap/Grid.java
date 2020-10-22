package com.jukaio.jumpandrun.ecs.componentmodule.components.tilemap;

import com.jukaio.jumpandrun.ecs.componentmodule.components.Component;
import com.jukaio.jumpandrun.ecs.componentmodule.components.ComponentType;
import com.jukaio.jumpandrun.extramath.Vector2;

public class Grid extends Component
{
    @Override
    public ComponentType get_type() {
        return ComponentType.GRID;
    }

    public Vector2 m_dimensions;
    public Vector2 m_tile_dimensions;
}
