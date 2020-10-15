package com.jukaio.jumpandrun.ecs.componentmodule.components.tilemapcomponents;

import com.jukaio.jumpandrun.ecs.componentmodule.components.Component;
import com.jukaio.jumpandrun.ecs.componentmodule.components.ComponentType;
import com.jukaio.jumpandrun.extramath.Line;

import java.util.ArrayList;

public class TileMapCollider extends Component
{
    public final static int START_POINT_IDX    = 3;     // 0000 0011
    public final static int TOP_LEFT_START     = 0;
    public final static int TOP_RIGHT_START    = 1;
    public final static int BOTTOM_LEFT_START  = 2;
    public final static int BOTTOM_RIGHT_START = 3;

    public final static int TOP_LEFT           = 1 << 2;// 0000 0100
    public final static int TOP_RIGHT          = 1 << 3;// 0000 1000
    public final static int BOTTOM_RIGHT       = 1 << 4;// 0001 0000
    public final static int BOTTOM_LEFT        = 1 << 5;// 0010 0000

    public ArrayList<Line> m_lines;

    @Override
    public ComponentType get_type() {
        return ComponentType.TILE_MAP_COLLIDER;
    }
}
