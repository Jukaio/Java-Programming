package com.jukaio.jumpandrun.world.Tilemap;

import android.graphics.Bitmap;

import com.jukaio.jumpandrun.collision.LineCollider;
import com.jukaio.jumpandrun.extramath.Line;
import com.jukaio.jumpandrun.extramath.Vector2;

import java.util.ArrayList;

public class Tile
{
    public int m_id;
    public int tile_flag;
    public Bitmap m_bitmap;

    public Tile(int p_id, Bitmap p_bitmap, int p_tile_flag)
    {
        m_id = p_id;
        m_bitmap = p_bitmap;
        tile_flag = p_tile_flag;
    }
}
