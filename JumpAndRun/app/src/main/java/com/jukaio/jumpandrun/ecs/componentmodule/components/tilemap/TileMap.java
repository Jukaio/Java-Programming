package com.jukaio.jumpandrun.ecs.componentmodule.components.tilemapcomponents;


import com.jukaio.jumpandrun.ecs.componentmodule.components.Component;
import com.jukaio.jumpandrun.ecs.componentmodule.components.ComponentType;

import java.util.ArrayList;

public class TileMap extends Component
{
    @Override
    public ComponentType get_type() {
        return ComponentType.TILE_MAP;
    }

    public int m_colour;
    public ArrayList<Layer> m_layers;

}
