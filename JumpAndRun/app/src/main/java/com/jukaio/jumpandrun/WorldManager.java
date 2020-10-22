package com.jukaio.jumpandrun;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.NonNull;

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
    
    public void create_world(String p_source, Context p_context)
    {
        World world = new World();
        World.TileMap tile_map = world.m_tile_map;
        World.TileSet tile_set = world.m_tile_set;
    
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
        m_current = world; //TODO remove this
    }
    
    @Override
    public void start()
    {
        for(Entity e : m_current.m_entities)
            e.start();
    }
    
    @Override
    public void update(float p_dt)
    {
        for(Entity e : m_current.m_entities)
            e.pre_update(p_dt);
        for(Entity e : m_current.m_entities)
            e.update(p_dt);
        for(Entity e : m_current.m_entities)
            e.late_update(p_dt);
    }
    
    @Override
    public void render(Canvas p_canvas, Paint p_paint)
    {
        World.Grid grid = m_current.m_tile_map.get_grid();
        World.TileMap tile_map = m_current.m_tile_map;
        World.TileSet tile_set = m_current.m_tile_set;
        
        float tile_width = grid.get_tile_size().m_x.floatValue();
        float tile_height = grid.get_tile_size().m_y.floatValue();
    
        p_canvas.drawColor(tile_map.get_color());
    
    
        for (int i = 0; i < tile_map.layer_count(); i++)
        {
            World.TileMap.Layer layer = tile_map.get_layer(i);
            
            switch(layer.get_type())
            {
                case ERROR:
                    throw new AssertionError("ERROR IN LAYER RENDER");
                    
                case BACKGROUND:
                case STATIC:
                case HUD:
                    for (int y = 0; y < grid.get_dimensions().m_y.intValue(); y++)
                    {
                        for (int x = 0; x < grid.get_dimensions().m_x.intValue(); x++)
                        {
                            int to_draw_id = layer.get_tile(((y * grid.get_dimensions().m_x.intValue()) + x));
                            if (to_draw_id != 0)
                            {
                                World.TileSet.Tile to_draw = tile_set.get_tile_at(to_draw_id);
                                p_canvas.drawBitmap(to_draw.m_bitmap,
                                                    x * tile_width,
                                                    y * tile_height,
                                                    p_paint);
                            }
                        }
                    }
                    break;
                case DYNAMIC:
                    for(Entity e : m_current.m_entities)
                        e.render(p_canvas, p_paint);
                    break;
            }
            
        }
    
        p_paint.setStyle(Paint.Style.FILL_AND_STROKE);
        p_paint.setColor(Color.RED);
        for (int i = 0; i < tile_map.line_count_in_collider(); i++)
        {
            Line l = tile_map.get_line_from_collider(i);
            p_canvas.drawLine(l.m_start.x,
                                     l.m_start.y,
                                     l.m_end.x,
                                     l.m_end.y,
                                     p_paint);
        }
    }
    
    
    static void create_collider(World.TileMap p_tile_map, World.TileSet p_set)
    {
        World.Grid grid = p_tile_map.get_grid();
        World.TileMap.Layer layer = p_tile_map.get_layer(World.TileMap.Layer.LayerType.STATIC);
        
        if(layer == null)
            return;
        
        final int w = grid.get_tile_size().m_x.intValue();
        final int h = grid.get_tile_size().m_y.intValue();
        
        for(int y = 0; y < layer.get_dimensions().m_y.intValue(); y++)
        {
            for(int x = 0; x < layer.get_dimensions().m_x.intValue(); x++)
            {
                int tile_id = layer.get_tile((y * layer.get_dimensions().m_x.intValue() + x));
                World.TileSet.Tile tile = p_set.get_tile_at(tile_id);
                
                if(tile.m_id != 0)
                {
                    int flag = tile.m_flag;
                    int first_index = tile.m_flag & World.TileMap.StaticCollider.START_POINT_IDX;

                    ArrayList<Point> points = new ArrayList<>();
                    for(int i = 0; i < 4; i++)
                    {
                        final int wrapped_index = (i + first_index) % 4;
                        final int index = flag & (1 << (wrapped_index + 2));
                        switch (index)
                        {
                            case(World.TileMap.StaticCollider.TOP_LEFT):     points.add(new Point(x * w, y * h));             break;
                            case(World.TileMap.StaticCollider.TOP_RIGHT):    points.add(new Point((x * w) + w, y * h));       break;
                            case(World.TileMap.StaticCollider.BOTTOM_RIGHT): points.add(new Point((x * w) + w, (y * h) + h)); break;
                            case(World.TileMap.StaticCollider.BOTTOM_LEFT):  points.add(new Point(x * w, (y * h) + h));       break;
                        }
                    }
                    Point prev_p = null;
                    for(Point p : points)
                    {
                        if(prev_p != null)
                        {
                            p_tile_map.add_line_to_collider(new Line(prev_p.x, prev_p.y, p.x, p.y));
                        }
                        prev_p = p;
                    }
                }
            }
        }
    }
    
    private static void create_dynamic_entities(World p_world, World.TileSet p_set, Context p_context)
    {
        World.Grid grid = p_world.m_tile_map.get_grid();
        World.TileMap.Layer layer = p_world.m_tile_map.get_layer(World.TileMap.Layer.LayerType.DYNAMIC);
        
        if(layer == null)
            return;
            
        final int w = grid.get_tile_size().m_x.intValue();
        final int h = grid.get_tile_size().m_y.intValue();
        
        EntityXML.Params parameters = new EntityXML.Params();
        
        for(int y = 0; y < layer.get_dimensions().m_y.intValue(); y++)
        {
            for (int x = 0; x < layer.get_dimensions().m_x.intValue(); x++)
            {
                int tile_id = layer.get_tile((y * layer.get_dimensions().m_x.intValue() + x));
                World.TileSet.Tile tile = p_set.get_tile_at(tile_id);
                
                
                
                if(tile.m_type != null)
                {
                    EntityType type_enum = EntityType.valueOf(tile.m_type.toUpperCase());
                    
                    parameters.m_world = p_world;
                    parameters.m_bitmap = tile.m_bitmap;
                    parameters.m_source = tile.m_type.toLowerCase()+".xml";
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
    
    public static World.TileMap.Layer create_layer(Element p_data_element)
    {
        World.TileMap.Layer layer = null;
        
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
                                World.TileMap.Layer.LayerType type = World.TileMap.Layer.LayerType.ERROR;
                                switch (Integer.parseInt((property.getAttribute("value"))))
                                {
                                    case 0:
                                        type = World.TileMap.Layer.LayerType.BACKGROUND;
                                        break;
                                    case 1:
                                        type = World.TileMap.Layer.LayerType.STATIC;
                                        break;
                                    case 2:
                                        type = World.TileMap.Layer.LayerType.DYNAMIC;
                                        break;
                                    case 3:
                                        type = World.TileMap.Layer.LayerType.HUD;
                                        break;
                                }
                                layer = new World.TileMap.Layer(type, width, height);
                            }
                        }
                    }
                }
                else if (element.getNodeName().equals("data"))
                {
                    read_buffer(layer,
                                element.getFirstChild().getNodeValue());
                }
            }
        }
        return layer;
    }
    
    private static void read_buffer(World.TileMap.Layer p_layer, String p_buffer)
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
            }
            else if (scan == ',')
            {
                int num = Integer.parseInt(number);
                p_layer.set_tile_at(count, num);
                number = "";
                count++;
            } else
            {
                number += scan;
            }
        }
    }
    
    private static void file_to_tileset(@NonNull World.TileSet p_set, String p_filename,
                                        Context p_context)
    {
        p_set.add_tile(0,
                       0,
                       null,
                       null); // ADD NULL TILE
    
        Document document = XML.get_document(p_filename, p_context);
    
        Node tile_set = document.getFirstChild();
        p_set.set_tile_size(Integer.parseInt(tile_set.getAttributes().getNamedItem("tilewidth").getNodeValue()),
                            Integer.parseInt(tile_set.getAttributes().getNamedItem("tileheight").getNodeValue()));
        p_set.get_dimensions().m_x = Integer.parseInt(tile_set.getAttributes().getNamedItem("columns").getNodeValue());
    
        NodeList node_list = document.getDocumentElement().getChildNodes();
        read_xml_children(p_set,
                          node_list,
                          p_context);
    }
    
    private static void read_xml_children(World.TileSet p_set, NodeList p_nodes, Context p_context)
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
                }
                else if (element.getNodeName().equals("tile"))
                {
                    read_tile_data(p_set,
                                   element);
                }
            }
        }
    }
    
    private static void read_image_data(World.TileSet p_set, Element p_element, Context p_context)
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
    
    private static void read_tile_data(World.TileSet p_set, Element p_element)
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
        p_set.add_tile(id, tile_flag, bitmap, type);
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
