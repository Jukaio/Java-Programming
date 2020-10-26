package com.jukaio.jumpandrun.world;

import com.jukaio.jumpandrun.extramath.Line;
import com.jukaio.jumpandrun.extramath.Vector2;

import java.util.ArrayList;

public class TileMap
{
    public static class Layer
    {
        public enum LayerType
        {
            ERROR,
            BACKGROUND,
            STATIC,
            DYNAMIC,
            HUD
        }
        
        Layer(LayerType p_type, int p_width, int p_height)
        {
            m_type = p_type;
            m_tiles = new int[p_width * p_height];
            m_dimensions.m_x = p_width;
            m_dimensions.m_y = p_height;
        }
        
        public void set_tile_at(int p_index, int p_tile)
        {
            m_tiles[p_index] = p_tile;
        }
        
        public int get_tile(int p_index)
        {
            return m_tiles[p_index];
        }
        
        void set_dimension(int p_x, int p_y)
        {
            m_dimensions.m_x = p_x;
            m_dimensions.m_y = p_y;
        }
        
        public Vector2 get_dimensions()
        {
            return m_dimensions;
        }
        
        public LayerType get_type()
        {
            return m_type;
        }
        
        private Vector2 m_dimensions = new Vector2();
        private int m_color;
        private LayerType m_type;
        private int m_tiles[] = null;
    }
    
    public static class CollisionFlags
    {
        public final static int START_POINT_IDX = 3;     // 0000 0011
        public final static int TOP_LEFT_START = 0;
        public final static int TOP_RIGHT_START = 1;
        public final static int BOTTOM_LEFT_START = 2;
        public final static int BOTTOM_RIGHT_START = 3;
        
        public final static int TOP_LEFT = 1 << 2;// 0000 0100
        public final static int TOP_RIGHT = 1 << 3;// 0000 1000
        public final static int BOTTOM_RIGHT = 1 << 4;// 0001 0000
        public final static int BOTTOM_LEFT = 1 << 5;// 0010 0000
    }
    
    public class LayerCollider
    {
        public ArrayList<Line>[] m_tiles = null;
    }
    
    
    public void set_color(int p_colour)
    {
        m_colour = p_colour;
    }
    
    public int get_color()
    {
        return m_colour;
    }
    
    public void add_layer(Layer p_layer)
    {
        m_layers.add(p_layer);
    }
    
    public Layer get_layer(int p_index)
    {
        return m_layers.get(p_index);
    }
    
    public Layer get_layer(Layer.LayerType p_type)
    {
        for (Layer layer : m_layers)
        {
            if (layer.get_type() == p_type)
                return layer;
        }
        return null;
    }
    
    public int layer_count()
    {
        return m_layers.size();
    }
    
    
    public void set_grid_dimensions(int p_x, int p_y)
    {
        m_grid.set_dimension(p_x,
                             p_y);
    }
    
    public void set_tile_size(int p_x, int p_y)
    {
        m_grid.set_tile_size(p_x,
                             p_y);
    }
    
    public Grid get_grid()
    {
        return m_grid;
    }
    
    
    private Grid m_grid = new Grid();
    private int m_colour = 0;
    private ArrayList<Layer> m_layers = new ArrayList<Layer>();
    public LayerCollider m_collider = new LayerCollider();
}