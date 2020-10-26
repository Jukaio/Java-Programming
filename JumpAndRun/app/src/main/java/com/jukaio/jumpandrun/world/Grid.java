package com.jukaio.jumpandrun.world;

import com.jukaio.jumpandrun.extramath.Vector2;

public class Grid
{
    private Vector2 m_dimensions = new Vector2();
    private Vector2 m_tile_size = new Vector2();

    void set_dimension(int p_x, int p_y)
    {
        m_dimensions.m_x = p_x;
        m_dimensions.m_y = p_y;
    }
    
    void set_tile_size(int p_x, int p_y)
    {
        m_tile_size.m_x = p_x;
        m_tile_size.m_y = p_y;
    }
    
    public Vector2 get_tile_size()
    {
        return m_tile_size;
    }
    
    public Vector2 get_dimensions()
    {
        return m_dimensions;
    }
}
