package com.jukaio.jumpandrun.ecs.componentmodule.components;

import android.graphics.Bitmap;

import com.jukaio.jumpandrun.extramath.Vector2;
import com.jukaio.jumpandrun.world.Tilemap.Tile;

import java.util.ArrayList;

public class Tileset extends Component
{
    public Bitmap m_bitmap;
    public Vector2 m_tile_dimensions;
    public Vector2 m_set_dimensions;
    public int m_tile_count;
    public ArrayList<Tile> m_tiles = null;

    @Override
    public ComponentType get_type() {
        return ComponentType.TILE_SET;
    }
}
