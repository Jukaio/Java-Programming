package com.jukaio.jumpandrun.world;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.util.Log;

import com.jukaio.jumpandrun.entity.Entity;
import com.jukaio.jumpandrun.entity.EntityType;
import com.jukaio.jumpandrun.entity.EntityXML;
import com.jukaio.jumpandrun.Game;
import com.jukaio.jumpandrun.entity.IEntity;
import com.jukaio.jumpandrun.Viewport;
import com.jukaio.jumpandrun.XML;
import com.jukaio.jumpandrun.extramath.Line;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class WorldManager implements IEntity
{
    private ArrayList<World> m_worlds = new ArrayList<World>();
    private World m_current = null;
    private int m_world_index = -1;
    
    public int get_current_world_index()
    {
        return m_world_index;
    }
    
    public int get_world_count()
    {
        return m_worlds.size();
    }
    
    public void set_current_world(int p_index)
    {
        m_world_index = p_index;
        m_current = m_worlds.get(p_index);
        Grid grid = m_current.m_tile_map.get_grid();
        
        float x = 0; // No need for an offset on the left side - a little colour makes it pretty
        float y = 0;
        
        float w = grid.get_dimensions().m_x.floatValue() * grid.get_tile_size().m_x.floatValue();
        float h = grid.get_dimensions().m_y.floatValue() * grid.get_tile_size().m_y.floatValue();
        
        Viewport.m_active.set_bounds(x,
                                     y,
                                     w,
                                     h);
        
        if (Game.DEBUG_ON)
        {
            Log.d("WorldManager",
                  Integer.toString(p_index) + " LOADED!");
        }
    }
    
    public void create_world(String p_source, Context p_context)
    {
        World world = new World();
        TileMap tile_map = world.m_tile_map;
        TileSet tile_set = world.m_tile_set;
    
        Document document = XML.get_document(p_source,
                                             p_context);
        Node map_node = document.getFirstChild();
        tile_map.set_grid_dimensions(Integer.parseInt(map_node.getAttributes().getNamedItem("width").getNodeValue()),
                                     Integer.parseInt(map_node.getAttributes().getNamedItem("height").getNodeValue()));
        tile_map.set_tile_size(Integer.parseInt(map_node.getAttributes().getNamedItem("tilewidth").getNodeValue()),
                               Integer.parseInt(map_node.getAttributes().getNamedItem("tileheight").getNodeValue()));
        tile_map.set_color(Color.parseColor((map_node.getAttributes().getNamedItem("backgroundcolor").getNodeValue())));
    
    
        NodeList node_list = document.getDocumentElement().getChildNodes();
        for (int i = 0; i < node_list.getLength(); i++)
        {
            Node node = node_list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element) node;
                if (element.getNodeName().equals("tileset"))
                {
                    String tileset = element.getAttribute("source");
                    file_to_tileset(tile_set,
                                    tileset,
                                    p_context);
                } else if (element.getNodeName().equals("properties"))
                {
                    NodeList property_children = element.getChildNodes();
                    for (int j = 0; j < property_children.getLength(); j++)
                    {
                        if (property_children.item(j).getNodeType() == Node.ELEMENT_NODE)
                        {
                            Element property_child_element = (Element) property_children.item(j);
                            if (property_child_element.getNodeName().equals("property"))
                            {
                                if (property_child_element.getAttribute("name").equals("colour"))
                                    tile_map.set_color(Color.parseColor((property_child_element.getAttribute("value"))));
                            }
                        }
                    }
                } else if (element.getNodeName().equals("layer"))
                {
                    tile_map.add_layer(create_layer(element));
                }
    
            }
        }
    
    
        create_collider(tile_map,
                        tile_set);
        create_dynamic_entities(world,
                                tile_set,
                                p_context);
        m_worlds.add(world);
    }
    
    @Override
    public void start()
    {
        for (Entity e : m_current.m_entities)
            e.start();
    }
    
    
    @Override
    public void update(float p_dt)
    {
        m_current.m_collisionAABB.check_collisions();
    
        for (Entity e : m_current.m_entities)
            if (e.get_active()) e.pre_update(p_dt);
        for (Entity e : m_current.m_entities)
            if (e.get_active()) e.update(p_dt);
        for (Entity e : m_current.m_entities)
            if (e.get_active()) e.late_update(p_dt);
    }
    
    @Override
    public void render(Viewport p_viewport, Paint p_paint)
    {
        Grid grid = m_current.m_tile_map.get_grid();
        TileMap tile_map = m_current.m_tile_map;
        TileSet tile_set = m_current.m_tile_set;
        
        float tile_width = grid.get_tile_size().m_x.floatValue();
        float tile_height = grid.get_tile_size().m_y.floatValue();
    
        p_viewport.draw_colour(tile_map.get_color());
    
    
        for (int i = 0; i < tile_map.layer_count(); i++)
        {
            TileMap.Layer layer = tile_map.get_layer(i);
            
            switch (layer.get_type())
            {
                case ERROR:
                    throw new AssertionError("ERROR IN LAYER RENDER");
    
                case BACKGROUND:
                case STATIC:
                    for (int y = 0; y < grid.get_dimensions().m_y.intValue(); y++)
                    {
                        for (int x = 0; x < grid.get_dimensions().m_x.intValue(); x++)
                        {
                            int to_draw_id = layer.get_tile(((y * grid.get_dimensions().m_x.intValue()) + x));
                            if (to_draw_id != 0)
                            {
                                TileSet.Tile to_draw = tile_set.get_tile_at(to_draw_id);
                                if (p_viewport.in_view(x * tile_width,
                                                       y * tile_height,
                                                       tile_width,
                                                       +tile_height))
                                    p_viewport.draw_bitmap(to_draw.m_bitmap,
                                                           x * tile_width,
                                                           y * tile_height,
                                                           p_paint);
                            }
                        }
                    }
                    break;
                case DYNAMIC:
                    for (Entity e : m_current.m_entities)
                        if (p_viewport.in_view(e))
                            if (e.get_active()) e.render(p_viewport,
                                                         p_paint);
                    break;
            }
        }
        m_current.m_hud.render(p_viewport,
                               p_paint);
        
        // <-- DEBUG ->> //
        if (Game.DEBUG_ON)
        {
            p_paint.setStyle(Paint.Style.FILL_AND_STROKE);
            p_paint.setColor(Color.RED);
            for (int i = 0; i < tile_map.m_collider.m_tiles.length; i++)
            {
                if (tile_map.m_collider.m_tiles[i] != null)
                {
                    for (int j = 0; j < tile_map.m_collider.m_tiles[i].size(); j++)
                    {
                        Line line = tile_map.m_collider.m_tiles[i].get(j);
                        if (line != null)
                            p_viewport.draw_line(line,
                                                 p_paint);
    
                    }
                }
            }
            p_viewport.debug(p_paint);
        }
    }
    
    @Override
    public void destroy()
    {
        for(World w : m_worlds)
            for(Entity e : w.m_entities)
                e.destroy();
    }
    
    final public void clean()
    {
    
    }
    
    static void create_collider(TileMap p_tile_map, TileSet p_set)
    {
        Grid grid = p_tile_map.get_grid();
        TileMap.Layer layer = p_tile_map.get_layer(TileMap.Layer.LayerType.STATIC);
        
        if (layer == null)
            return;
        
        p_tile_map.m_collider.m_tiles = new ArrayList[layer.get_dimensions().m_x.intValue() * layer.get_dimensions().m_y.intValue()];
        
        final int w = grid.get_tile_size().m_x.intValue();
        final int h = grid.get_tile_size().m_y.intValue();
        
        for (int y = 0; y < layer.get_dimensions().m_y.intValue(); y++)
        {
            for (int x = 0; x < layer.get_dimensions().m_x.intValue(); x++)
            {
                int layer_index = y * layer.get_dimensions().m_x.intValue() + x;
                int tile_id = layer.get_tile((layer_index));
                TileSet.Tile tile = p_set.get_tile_at(tile_id);
                
                if (tile.m_id != 0)
                {
                    p_tile_map.m_collider.m_tiles[layer_index] = new ArrayList<>();
                    int flag = tile.m_flag;
                    int first_index = tile.m_flag & TileMap.CollisionFlags.START_POINT_IDX;
                    
                    ArrayList<Point> points = new ArrayList<>();
                    for (int i = 0; i < 4; i++)
                    {
                        final int wrapped_index = (i + first_index) % 4;
                        final int index = flag & (1 << (wrapped_index + 2));
                        switch (index)
                        {
                            case (TileMap.CollisionFlags.TOP_LEFT):
                                points.add(new Point(x * w,
                                                     y * h));
                                break;
                            case (TileMap.CollisionFlags.TOP_RIGHT):
                                points.add(new Point((x * w) + w,
                                                     y * h));
                                break;
                            case (TileMap.CollisionFlags.BOTTOM_RIGHT):
                                points.add(new Point((x * w) + w,
                                                     (y * h) + h));
                                break;
                            case (TileMap.CollisionFlags.BOTTOM_LEFT):
                                points.add(new Point(x * w,
                                                     (y * h) + h));
                                break;
                        }
                    }
                    Point prev_p = null;
                    for (Point p : points)
                    {
                        if (prev_p != null)
                        {
                            p_tile_map.m_collider.m_tiles[layer_index].add(new Line(prev_p.x,
                                                                                    prev_p.y,
                                                                                    p.x,
                                                                                    p.y));
                            //p_tile_map.add_line_to_collider(new Line(prev_p.x, prev_p.y, p.x, p.y));
                        }
                        prev_p = p;
                    }
                }
            }
        }
    }
    
    private void create_dynamic_entities(World p_world, TileSet p_set, Context p_context)
    {
        Grid grid = p_world.m_tile_map.get_grid();
        TileMap.Layer layer = p_world.m_tile_map.get_layer(TileMap.Layer.LayerType.DYNAMIC);
        
        if (layer == null)
            return;
    
        final int w = grid.get_tile_size().m_x.intValue();
        final int h = grid.get_tile_size().m_y.intValue();
        
        EntityXML.Params parameters = new EntityXML.Params();
        
        for (int y = 0; y < layer.get_dimensions().m_y.intValue(); y++)
        {
            for (int x = 0; x < layer.get_dimensions().m_x.intValue(); x++)
            {
                int tile_id = layer.get_tile((y * layer.get_dimensions().m_x.intValue() + x));
                TileSet.Tile tile = p_set.get_tile_at(tile_id);
    
    
                if (tile.m_type != null)
                {
                    EntityType type_enum = EntityType.valueOf(tile.m_type.toUpperCase());
                    
                    parameters.m_world_manager = this;
                    parameters.m_world = p_world;
                    parameters.m_bitmap = tile.m_bitmap;
                    parameters.m_source = tile.m_type.toLowerCase() + ".xml";
                    parameters.m_type = type_enum;
                    parameters.m_x = x * w;
                    parameters.m_y = y * h;
    
                    p_world.m_entities.add(new EntityXML(true,
                                                         parameters,
                                                         p_context));
                }
            }
        }
    }
    
    public static TileMap.Layer create_layer(Element p_data_element)
    {
        TileMap.Layer layer = null;
        
        int width = Integer.parseInt(p_data_element.getAttribute("width"));
        int height = Integer.parseInt(p_data_element.getAttribute("height"));
        
        NodeList children = p_data_element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++)
        {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element) children.item(i);
                if (element.getNodeName().equals("properties"))
                {
                    read_buffer(layer,
                                element.getFirstChild().getNodeValue());
                    for (int j = 0; j < element.getChildNodes().getLength(); j++)
                    {
                        if (element.getChildNodes().item(j).getNodeType() == Node.ELEMENT_NODE)
                        {
                            Element property = (Element) element.getChildNodes().item(j);
                            if (property.getAttribute("name").equals("LayerType"))
                            {
                                TileMap.Layer.LayerType type = TileMap.Layer.LayerType.ERROR;
                                switch (Integer.parseInt((property.getAttribute("value"))))
                                {
                                    case 0:
                                        type = TileMap.Layer.LayerType.BACKGROUND;
                                        break;
                                    case 1:
                                        type = TileMap.Layer.LayerType.STATIC;
                                        break;
                                    case 2:
                                        type = TileMap.Layer.LayerType.DYNAMIC;
                                        break;
                                    case 3:
                                        type = TileMap.Layer.LayerType.HUD;
                                        break;
                                }
                                layer = new TileMap.Layer(type,
                                                          width,
                                                          height);
                            }
                        }
                    }
                } else if (element.getNodeName().equals("data"))
                {
                    read_buffer(layer,
                                element.getFirstChild().getNodeValue());
                }
            }
        }
        return layer;
    }
    
    private static void read_buffer(TileMap.Layer p_layer, String p_buffer)
    {
        String number = new String("");
        
        int count = 0;
        
        int start = 0;
        char scan = p_buffer.charAt(0);
        if (scan == '\n')
            start++;
        for (int i = start; i < p_buffer.length(); i++)
        {
            scan = p_buffer.charAt(i);
            if (scan == '\n' &&
                    i != p_buffer.length() - 1)
            {
                continue;
            } else if (scan == ',')
            {
                int num = Integer.parseInt(number);
                p_layer.set_tile_at(count,
                                    num);
                number = "";
                count++;
            } else
            {
                number += scan;
            }
        }
    }
    
    private static void file_to_tileset(@NonNull TileSet p_set, String p_filename,
                                        Context p_context)
    {
        p_set.add_tile(0,
                       0,
                       null,
                       null); // ADD NULL TILE
    
        Document document = XML.get_document(p_filename,
                                             p_context);
    
        Node tile_set = document.getFirstChild();
        p_set.set_tile_size(Integer.parseInt(tile_set.getAttributes().getNamedItem("tilewidth").getNodeValue()),
                            Integer.parseInt(tile_set.getAttributes().getNamedItem("tileheight").getNodeValue()));
        p_set.get_dimensions().m_x = Integer.parseInt(tile_set.getAttributes().getNamedItem("columns").getNodeValue());
    
        NodeList node_list = document.getDocumentElement().getChildNodes();
        read_xml_children(p_set,
                          node_list,
                          p_context);
    }
    
    private static void read_xml_children(TileSet p_set, NodeList p_nodes, Context p_context)
    {
        for (int i = 0; i < p_nodes.getLength(); i++)
        {
            Node node = p_nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                // Fix tileset parsing
                Element element = (Element) node;
                if (element.getNodeName().equals("image"))
                {
                    read_image_data(p_set,
                                    element,
                                    p_context);
                } else if (element.getNodeName().equals("tile"))
                {
                    read_tile_data(p_set,
                                   element);
                }
            }
        }
    }
    
    private static void read_image_data(TileSet p_set, Element p_element, Context p_context)
    {
        int height = Integer.parseInt(p_element.getAttribute("height"));
        String source = p_element.getAttribute("source");
        
        InputStream bitmap_stream = null;
        try
        {
            bitmap_stream = p_context.getResources().getAssets().open(source);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        p_set.set_bitmap(BitmapFactory.decodeStream(bitmap_stream));
        p_set.get_dimensions().m_y = (height / p_set.get_tile_size().m_y.intValue());
    }
    
    private static void read_tile_data(TileSet p_set, Element p_element)
    {
        NodeList property_children = p_element.getChildNodes();
        int tile_flag = read_tile_properties_to_flag(property_children);
        
        int id = Integer.parseInt(p_element.getAttribute("id"));
        String type = p_element.getAttribute("type");
        int x = id % p_set.get_dimensions().m_x.intValue();
        int y = id / p_set.get_dimensions().m_x.intValue();
        
        int width = p_set.get_tile_size().m_x.intValue();
        int height = p_set.get_tile_size().m_y.intValue();
        
        Bitmap bitmap = Bitmap.createBitmap(p_set.get_bitmap(),
                                            x * width,
                                            y * height,
                                            width,
                                            height);
        p_set.add_tile(id,
                       tile_flag,
                       bitmap,
                       type);
    }
    
    private static int read_tile_properties_to_flag(NodeList p_property_children)
    {
        int tile_flag = 0;
        for (int j = 0; j < p_property_children.getLength(); j++)
        {
            Node property_node = p_property_children.item(j);
            if (property_node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element tile_element = (Element) property_node;
                NodeList tile_children = tile_element.getChildNodes();
                for (int k = 0; k < tile_children.getLength(); k++)
                {
                    Node tile_child = tile_children.item(k);
                    if (tile_child.getNodeType() == Node.ELEMENT_NODE)
                    {
                        Element tile_child_element = (Element) tile_child;
                        if (tile_child_element.getAttribute("name").equals("point_flags"))
                        {
                            tile_flag |= (Integer.parseInt(tile_child_element.getAttribute("value")) << 2);
                        } else if (tile_child_element.getAttribute("name").equals("first_corner"))
                        {
                            tile_flag |= Integer.parseInt(tile_child_element.getAttribute("value"));
                        }
                    }
                }
                
            }
        }
        return tile_flag;
    }
}
