package com.jukaio.spaceshooter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

public class Grid
{
    private Vector2 m_offset    = Vector2.ZERO;
    private int m_grid_width    = 0;
    private int m_grid_height   = 0;
    private int m_tile_width    = 0;
    private int m_tile_height   = 0;
    ArrayList<Tile> m_tiles     = null;
    
    public Grid(int p_grid_width, int p_grid_height, int p_tile_width, int p_tile_height, Vector2 p_offset)
    {
        m_grid_width = p_grid_width;
        m_grid_height = p_grid_height;
        
        m_tile_width = p_tile_width;
        m_tile_height = p_tile_height;
        
        m_offset = p_offset;
        
        m_tiles = new ArrayList<Tile>(m_grid_width* m_grid_height);
        for(int x = 0; x < m_grid_width; x++)
        {
            for (int y = 0; y < m_grid_height; y++)
            {
                m_tiles.add(new Tile());
            }
        }
    }
    
    public void reset()
    {
        for(int x = 0; x < m_grid_width; x++)
        {
            for (int y = 0; y < m_grid_height; y++)
            {
                m_tiles.get(y * m_grid_width + x).has_unit = false;
            }
        }
    }
    
    public void set_unit_at(int x, int y, boolean p_value)
    {
        m_tiles.get(y * get_grid_width() + x).has_unit = p_value;
    }
    
    public boolean has_unit_at(int x, int y)
    {
        return m_tiles.get(y * get_grid_width() + x).has_unit;
    }
    
    public Vector2 grid_to_world(int x, int y)
    {
        return new Vector2(x * m_tile_width + (m_tile_width * 0.5f) + m_offset.m_x,
                           y * m_tile_height + (m_tile_height * 0.5f) + m_offset.m_y);
    }
   
    public int world_to_grid_x(float p_x)
    {
        return (int)((((p_x - m_offset.m_x) - m_tile_width * 0.5f)) / m_tile_width);
    }
    
    public int world_to_grid_y(float p_y)
    {
        return (int)((((p_y - m_offset.m_y) - m_tile_height * 0.5f)) / m_tile_height);
    }
    
    public void set_unit_at_from_world(Vector2 p_position, boolean p_value)
    {
        int x = (int)(((p_position.m_x - m_offset.m_x) - m_tile_width * 0.5f));
        int y = (int)(((p_position.m_y - m_offset.m_y) - m_tile_height * 0.5f));
        x /= m_tile_width;
        y /= m_tile_height;
        m_tiles.get(y * get_grid_width() + x).has_unit = p_value;
    }
    
    // Debug function
    public void draw(Canvas p_canvas, Paint p_paint)
    {
        for(int x = 0; x < m_grid_width; x++)
        {
            for (int y = 0; y < m_grid_height; y++)
            {
                p_paint.setColor(Color.WHITE);
                p_paint.setStyle(Paint.Style.STROKE);
                p_paint.setStrokeWidth(2);
                
                p_canvas.drawRect(x * m_tile_width + m_offset.m_x, y * m_tile_height + m_offset.m_y,
                                  x * m_tile_width + m_tile_width + m_offset.m_x, y * m_tile_height + m_tile_height + m_offset.m_y, p_paint);
                                  
                p_paint.setStyle(Paint.Style.FILL);
                p_paint.setStrokeWidth(0);
                
                p_paint.setTextAlign(Paint.Align.CENTER);
                p_paint.setTextSize(24);
                p_canvas.drawText(x + " " + y, x * m_tile_width + (m_tile_width * 0.5f) + m_offset.m_x, y * m_tile_height + (m_tile_height * 0.5f) + m_offset.m_y, p_paint);
                
                p_paint.setColor(Color.BLUE);
                if(m_tiles.get(y * m_grid_width + x).has_unit)
                {
                    p_canvas.drawRect(x * m_tile_width + m_offset.m_x, y * m_tile_height + m_offset.m_y,
                                      x * m_tile_width + (m_tile_width * 0.25f) + m_offset.m_x , y * m_tile_height + (m_tile_height * 0.25f) + m_offset.m_y , p_paint);
                                      
                }
            }
        }
    }
    
    public int get_grid_width()
    {
        return m_grid_width;
    }
    public int get_grid_height()
    {
        return m_grid_height;
    }
    
    public final void clean()
    {
        m_tiles.clear();
        m_offset = null;
        m_tiles = null;
    }
  
}
