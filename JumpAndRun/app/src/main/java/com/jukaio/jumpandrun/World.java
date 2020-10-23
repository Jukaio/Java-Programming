package com.jukaio.jumpandrun;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.jukaio.jumpandrun.components.RectangleColliderComponent;
import com.jukaio.jumpandrun.extramath.Line;
import com.jukaio.jumpandrun.extramath.Vector2;

import java.util.ArrayList;



public class World
{
    public static class Grid
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
    
    public static class HUD
    {
        public abstract static class ElementHUD
        {
            private Vector2 m_position = new Vector2();
            private int m_colour = Color.WHITE;
            
            final public void set_position(float x, float y)
            {
                m_position.m_x = x;
                m_position.m_y = y;
            }
            final public Vector2 get_position()
            {
                return m_position;
            }
            final public void set_colour(int p_colour)
            {
                m_colour = p_colour;
            }
            final public int get_colour()
            {
                return m_colour;
            }
            
            public abstract void draw(Canvas p_canvas, Paint p_paint);
        }
        
        public static class TextElement extends ElementHUD
        {
            private String m_text = "";
            private Paint.Align m_align = Paint.Align.LEFT;
            private int m_size = 0;
            
            public void set_text(String p_text)
            {
                m_text = p_text;
            }
            
            public TextElement(String p_text, float p_x, float p_y, int p_size, Paint.Align p_align)
            {
                m_text = p_text;
                set_position(p_x, p_y);
                m_align = p_align;
                m_size = p_size;
            }
            
            @Override
            public void draw(Canvas p_canvas, Paint p_paint)
            {
                float x = get_position().m_x.floatValue();
                float y = get_position().m_y.floatValue();
                int prev_colour = p_paint.getColor();
                
                p_paint.setTextSize(m_size);
                p_paint.setTextAlign(m_align);
                
                p_paint.setColor(get_colour());
                p_canvas.drawText(m_text, x, y, p_paint);
                p_paint.setColor(prev_colour);
            }
        }
        
        public void add(ElementHUD p_element)
        {
            m_elements.add(p_element);
        }
        
        public void remove(ElementHUD p_element)
        {
            m_elements.remove(p_element);
        }
        
        public void render(Canvas p_canvas, Paint p_paint)
        {
            for(ElementHUD e : m_elements)
                e.draw(p_canvas, p_paint);
        }
        
        private ArrayList<ElementHUD> m_elements = new ArrayList<>();
    }
    
    public static class CollisionAABB
    {
        public void push_back_collider(RectangleColliderComponent p_collider)
        {
            m_rectangle_components.add(p_collider);
        }
        
        public void check_collisions()
        {
            RectangleColliderComponent lhs;
            RectangleColliderComponent rhs;
            for(int i = 0; i < m_rectangle_components.size() - 1; i++)
            {
                lhs = m_rectangle_components.get(i);
                if(lhs.get_entity().get_active())
                {
                    for (int j = i + 1; j < m_rectangle_components.size(); j++)
                    {
                        rhs = m_rectangle_components.get(j);
                        if(rhs.get_entity().get_active())
                        {
                            if (lhs.AABB(rhs))
                            {
                                lhs.get_entity().on_collision(rhs.get_entity());
                                rhs.get_entity().on_collision(lhs.get_entity());
                            }
                        }
                    }
                }
            }
        }
        
        private ArrayList<RectangleColliderComponent> m_rectangle_components = new ArrayList<>();
    }
    
    public static class TileSet
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
    
    public static class TileMap
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
                return  m_type;
            }
    
            private Vector2 m_dimensions = new Vector2();
            private int m_color;
            private LayerType m_type;
            private int m_tiles[] = null;
        }
    
        public static class CollisionFlags
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
        }
        
        public static class LayerCollider
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
            for(Layer layer : m_layers)
            {
                if(layer.get_type() == p_type)
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
            m_grid.set_dimension(p_x, p_y);
        }
        
        public void set_tile_size(int p_x, int p_y)
        {
            m_grid.set_tile_size(p_x, p_y);
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
    
    public HUD m_hud = new HUD();
    public CollisionAABB m_collisionAABB = new CollisionAABB();
    public TileMap m_tile_map = new TileMap();
    public ArrayList<Entity> m_entities = new ArrayList<Entity>();
    public TileSet m_tile_set = new TileSet();
}
