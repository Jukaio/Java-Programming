package com.jukaio.jumpandrun.world.Tilemap;

import android.graphics.Bitmap;

public class Tile
{
    public int m_id;
    public Bitmap m_bitmap;

    public Tile(int p_id, Bitmap p_bitmap)
    {
        m_id = p_id;
        m_bitmap = p_bitmap;
    }
}
