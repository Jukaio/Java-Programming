package com.jukaio.jumpandrun.ecs.componentmodule.components.tilemapcomponents;

import com.jukaio.jumpandrun.ecs.componentmodule.components.Component;
import com.jukaio.jumpandrun.ecs.componentmodule.components.ComponentType;
import com.jukaio.jumpandrun.extramath.Line;

import java.util.ArrayList;

public class TileMapCollider extends Component
{
    public ArrayList<Line> m_lines;

    @Override
    public ComponentType get_type() {
        return ComponentType.TILE_MAP_COLLIDER;
    }
}
