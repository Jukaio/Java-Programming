package com.jukaio.jumpandrun.world;

import android.graphics.Bitmap;

import com.jukaio.jumpandrun.extramath.Vector2;

import java.util.ArrayList;

public class TileSet
{
    public static class Tile
    {
        private Tile()
        {
            m_id = -1;
            m_flag = 0;
            m_bitmap = null;
            m_type = null;
        }
        
        private Tile(int p_id, int p_flag, Bitmap p_bitmap, String p_type)
        {
            m_id = p_id;
            m_flag = p_flag;
            m_bitmap = p_bitmap;
            m_type = p_type;
        }
 
        public String m_type;
        public int m_id;
        public int m_flag;
        public Bitmap m_bitmap = null;
    }
    
    public Bitmap get_bitmap()
    {
        return m_bitmap;
    }

    public void set_bitmap(Bitmap p_bitmap)
    {
        m_bitmap = p_bitmap;
    }
    
    public Vector2 get_tile_size()
    {
        return m_tile_size;
    }

    public void set_tile_size(int p_x, int p_y)
    {
        m_tile_size.m_x = p_x;
        m_tile_size.m_y = p_y;
    }

    public Vector2 get_dimensions()
    {
        return m_set_dimensions;
    }

    public void set_dimensions(int p_x, int p_y)
    {
        m_set_dimensions.m_x = p_x;
        m_set_dimensions.m_y = p_y;
    }
    
    public Tile get_tile_at(int p_index)
    {
        return m_tiles.get(p_index);
    }
    
    public void add_tile(int p_tile, int p_flag, Bitmap p_bitmap, String p_type)
    {
        m_tiles.add(new Tile(p_tile, p_flag, p_bitmap, p_type));
    }
    
    private Bitmap m_bitmap = null;
    private Vector2 m_tile_size = new Vector2();
    private Vector2 m_set_dimensions = new Vector2();
    private ArrayList<Tile> m_tiles = new ArrayList<Tile>();
}
