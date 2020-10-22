package com.jukaio.jumpandrun.ecs.systemmodule;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.NonNull;

import com.jukaio.jumpandrun.ecs.ECS;
import com.jukaio.jumpandrun.ecs.componentmodule.components.ComponentType;
import com.jukaio.jumpandrun.ecs.componentmodule.components.tilemap.Tile;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.RenderCanvas;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.SharedComponentType;
import com.jukaio.jumpandrun.ecs.componentmodule.components.tilemap.Grid;
import com.jukaio.jumpandrun.ecs.componentmodule.components.AssetLoaderXML;
import com.jukaio.jumpandrun.ecs.componentmodule.components.tilemap.Layer;
import com.jukaio.jumpandrun.ecs.componentmodule.components.tilemap.LayerTile;
import com.jukaio.jumpandrun.ecs.componentmodule.components.tilemap.TileMap;
import com.jukaio.jumpandrun.ecs.componentmodule.components.tilemap.Tileset;
import com.jukaio.jumpandrun.ecs.componentmodule.components.tilemap.TileMapCollider;
import com.jukaio.jumpandrun.ecs.entitymodule.Entity;
import com.jukaio.jumpandrun.extramath.Line;
import com.jukaio.jumpandrun.extramath.Vector2;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class TileMapCore extends System
{

    @Override
    public SystemType get_type() {
        return SystemType.TILE_MAP;
    }

    @Override
    public int get_signature() {
        return ComponentType.SOURCE_XML.as_bitmask() |
               ComponentType.GRID.as_bitmask() |
               ComponentType.TILE_MAP.as_bitmask() |
               ComponentType.TILE_SET.as_bitmask()  |
               ComponentType.TILE_MAP_COLLIDER.as_bitmask() |
               (SharedComponentType.RENDER_CANVAS.as_bitmask() << ComponentType.values().length);
    }

    @Override
    public void init(ECS p_ecs, ArrayList<Entity> p_entities)
    {
        final int system_signature = p_ecs.get_system_signature(get_type());
        for(Entity entity : p_entities)
        {
            if((p_ecs.get_entity_signature(entity) & system_signature) == system_signature)
            {
                AssetLoaderXML source = p_ecs.get_component(entity, ComponentType.SOURCE_XML);
                Grid grid = p_ecs.get_component(entity, ComponentType.GRID);
                TileMap tile_map = p_ecs.get_component(entity, ComponentType.TILE_MAP);
                Tileset tile_set = p_ecs.get_component(entity, ComponentType.TILE_SET);
                TileMapCollider collider = p_ecs.get_component(entity, ComponentType.TILE_MAP_COLLIDER);

                inti_components(tile_map, tile_set, grid, source);
                for(Layer layer : tile_map.m_layers)
                {
                    if(layer.m_type == Layer.LayerType.Static)
                        create_collider(collider, tile_set, layer, grid);
                }
            }
        }
    }

    @Override
    public void update(ECS p_ecs, ArrayList<Entity> p_entities)
    {

    }

    @Override
    public void render(ECS p_ecs, ArrayList<Entity> p_entities)
    {
        final int system_signature = p_ecs.get_system_signature(get_type());
        for(Entity entity : p_entities)
        {
            if((p_ecs.get_entity_signature(entity) & system_signature) == system_signature)
            {
                Grid grid = p_ecs.get_component(entity, ComponentType.GRID);
                TileMap tile_map = p_ecs.get_component(entity, ComponentType.TILE_MAP);
                Tileset tile_set = p_ecs.get_component(entity, ComponentType.TILE_SET);
                RenderCanvas render = p_ecs.get_shared_component(SharedComponentType.RENDER_CANVAS);
                TileMapCollider collider = p_ecs.get_component(entity, ComponentType.TILE_MAP_COLLIDER);

                float tile_width = grid.m_tile_dimensions.m_x.floatValue();
                float tile_height = grid.m_tile_dimensions.m_y.floatValue();

                for(Layer layer : tile_map.m_layers)
                {
                    for(int y = 0; y < grid.m_dimensions.m_y.intValue(); y++)
                    {
                        for(int x = 0; x < grid.m_dimensions.m_x.intValue(); x++)
                        {
                            int to_draw_id = layer.m_tiles.get((y * grid.m_dimensions.m_x.intValue()) + x).m_id;
                            if(to_draw_id != 0)
                            {
                                Tile to_draw = get_tile_from_set(tile_set, to_draw_id);
                                render.m_canvas.drawBitmap(to_draw.m_bitmap, x * tile_width, y * tile_height, render.m_paint);
                            }
                        }
                    }
                }

                render.m_paint.setStyle(Paint.Style.FILL_AND_STROKE);
                render.m_paint.setColor(Color.BLUE);
                for(Line l : collider.m_lines)
                {
                    render.m_canvas.drawLine(l.m_start.x, l.m_start.y, l.m_end.x, l.m_end.y, render.m_paint);
                }
            }
        }
    }

    void create_collider(TileMapCollider p_collider, Tileset p_set, Layer p_layer, Grid p_grid)
    {
        p_collider.m_lines = new ArrayList<>();

        final int w = p_grid.m_tile_dimensions.m_x.intValue();
        final int h = p_grid.m_tile_dimensions.m_y.intValue();

        for(int y = 0; y < p_layer.m_dimensions.m_y.intValue(); y++)
        {
            for(int x = 0; x < p_layer.m_dimensions.m_x.intValue(); x++)
            {
                int tile_id = p_layer.m_tiles.get((y * p_layer.m_dimensions.m_x.intValue() + x)).m_id;
                Tile tile = p_set.m_tiles.get(tile_id);
                if(tile.m_id != 0)
                {
                    int flag = tile.tile_flag;
                    int first_index = tile.tile_flag & TileMapCollider.START_POINT_IDX;

                    ArrayList<Point> points = new ArrayList<>();
                    for(int i = 0; i < 4; i++)
                    {
                        final int wrapped_index = (i + first_index) % 4;
                        final int index = flag & (1 << (wrapped_index + 2));
                        switch (index)
                        {
                            case(TileMapCollider.TOP_LEFT):     points.add(new Point(x * w, y * h));             break;
                            case(TileMapCollider.TOP_RIGHT):    points.add(new Point((x * w) + w, y * h));       break;
                            case(TileMapCollider.BOTTOM_RIGHT): points.add(new Point((x * w) + w, (y * h) + h)); break;
                            case(TileMapCollider.BOTTOM_LEFT):  points.add(new Point(x * w, (y * h) + h));       break;
                        }
                    }
                    Point prev_p = null;
                    for(Point p : points)
                    {
                        if(prev_p != null)
                        {
                            p_collider.m_lines.add(new Line(prev_p.x, prev_p.y, p.x, p.y));
                        }
                        prev_p = p;
                    }
                }
            }
        }
    }


    private Document get_document(String p_source, Context p_context)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document document = null;
        try
        {
            builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(p_context.getResources().getAssets().open(p_source));
            document = builder.parse(is);
        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }
        catch (SAXException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return document;
    }

    public void inti_components(@NonNull TileMap p_map, @NonNull Tileset p_set, @NonNull Grid p_grid, @NonNull AssetLoaderXML p_source_xml)
    {
        p_map.m_layers = new ArrayList<Layer>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try
        {
            Document document = get_document(p_source_xml.m_source, p_source_xml.m_context);
            Node map_node = document.getFirstChild();
            // TODO: Deserialize function : i.e. Deserialize(Element element, String name) - DeserializeInt as wrapper for that
            p_grid.m_dimensions = new Vector2(Integer.parseInt(map_node.getAttributes().getNamedItem("width").getNodeValue()),
                    Integer.parseInt(map_node.getAttributes().getNamedItem("height").getNodeValue()));

            p_grid.m_tile_dimensions = new Vector2(Integer.parseInt(map_node.getAttributes().getNamedItem("tilewidth").getNodeValue()),
                    Integer.parseInt(map_node.getAttributes().getNamedItem("tileheight").getNodeValue()));

            NodeList node_list = document.getDocumentElement().getChildNodes();
            for(int i = 0; i < node_list.getLength(); i++)
            {
                Node node = node_list.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element element = (Element)node;
                    if(element.getNodeName().equals("tileset"))
                    {
                        String tileset = element.getAttribute("source");
                        file_to_tileset(p_set, tileset, p_source_xml.m_context);
                    }
                    else if(element.getNodeName().equals("properties"))
                    {
                        NodeList property_children = element.getChildNodes();
                        for(int j = 0; j < property_children.getLength(); j++)
                        {
                            if(property_children.item(j).getNodeType() == Node.ELEMENT_NODE)
                            {
                                Element property_child_element = (Element) property_children.item(j);
                                if(property_child_element.getNodeName().equals("property"))
                                {
                                    if(property_child_element.getAttribute("name").equals("colour"))
                                        p_map.m_colour = Color.parseColor((property_child_element.getAttribute("value")));
                                }
                            }
                        }
                    }
                    else if(element.getNodeName().equals("layer"))
                    {
                        Layer layer = create_layer(element);
                        p_map.m_layers.add(layer);
                    }

                }
            }
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        catch (DOMException e)
        {
            e.printStackTrace();
        }
    }

    public Layer create_layer(Element p_data_element)
    {
        Layer layer = new Layer();

        layer.m_id = Integer.parseInt(p_data_element.getAttribute("id"));
        layer.m_name = p_data_element.getAttribute("name");
        layer.m_dimensions = new Vector2(Integer.parseInt(p_data_element.getAttribute("width")),
                                         Integer.parseInt(p_data_element.getAttribute("height")));

        layer.m_tiles = new ArrayList<LayerTile>();
        NodeList children = p_data_element.getChildNodes();
        for(int i = 0; i < children.getLength(); i++)
        {
            if(children.item(i).getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element)children.item(i);
                if(element.getNodeName().equals("data"))
                {
                    read_buffer(layer, element.getFirstChild().getNodeValue());
                }
                else if(element.getNodeName().equals("properties"))
                {
                    read_buffer(layer, element.getFirstChild().getNodeValue());
                    for(int j = 0; j < element.getChildNodes().getLength(); j++)
                    {
                        if(element.getChildNodes().item(j).getNodeType() == Node.ELEMENT_NODE)
                        {
                            Element property = (Element) element.getChildNodes().item(j);
                            if(property.getAttribute("name").equals("LayerType"))
                            {
                                switch (Integer.parseInt((property.getAttribute("value"))))
                                {
                                    case 0: layer.m_type = Layer.LayerType.Background; break;
                                    case 1: layer.m_type = Layer.LayerType.Static; break;
                                    case 2: layer.m_type = Layer.LayerType.HUD; break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return layer;
    }

    private void read_buffer(Layer p_layer, String p_buffer)
    {
        String number = new String("");

        int start = 0;
        char scan = p_buffer.charAt(0);
        if(scan == '\n')
            start++;
        for(int i = start; i < p_buffer.length(); i++)
        {
            scan = p_buffer.charAt(i);
            if(scan == '\n' &&
               i != p_buffer.length() - 1)
            {
                continue;
            }
            else if(scan == ',')
            {
                int num = Integer.parseInt(number);
                LayerTile layer_tile = new LayerTile();
                layer_tile.m_id = num;
                p_layer.m_tiles.add(layer_tile);
                number = "";
            }
            else
            {
                number += scan;
            }
        }
    }

    static public Tile get_tile_from_set(Tileset p_set, int p_index)
    {
        return p_set.m_tiles.get(p_index);
    }

    static private void file_to_tileset(@NonNull Tileset p_set, String p_filename, Context p_context)
    {
        p_set.m_set_dimensions = new Vector2();
        p_set.m_tile_dimensions = new Vector2();
        p_set.m_tiles = new ArrayList<>();
        Tile to_add = new Tile();
        to_add.m_bitmap = null;
        to_add.tile_flag = 0;
        to_add.m_id = 0;
        p_set.m_tiles.add(to_add); // ADD NULL TILE

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try
        {
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(p_context.getResources().getAssets().open(p_filename));
            Document document = builder.parse(is);

            Node tile_set = document.getFirstChild();
            p_set.m_tile_dimensions = new Vector2(Integer.parseInt(tile_set.getAttributes().getNamedItem("tilewidth").getNodeValue()),
                    Integer.parseInt(tile_set.getAttributes().getNamedItem("tileheight").getNodeValue()));
            p_set.m_tile_count = Integer.parseInt(tile_set.getAttributes().getNamedItem("tilecount").getNodeValue());
            p_set.m_set_dimensions.m_x = Integer.parseInt(tile_set.getAttributes().getNamedItem("columns").getNodeValue());

            NodeList node_list = document.getDocumentElement().getChildNodes();
            read_xml_children(p_set, node_list, p_context);
        }
        catch (SAXException e)
        {
            e.printStackTrace();
        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void read_xml_children(Tileset p_set, NodeList p_nodes, Context p_context)
    {
        for(int i = 0; i < p_nodes.getLength(); i++)
        {
            Node node = p_nodes.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE)
            {
                // Fix tileset parsing
                Element element = (Element)node;
                if(element.getNodeName().equals("image"))
                {
                    read_image_data(p_set, element, p_context);
                }
                else if(element.getNodeName().equals("tile"))
                {
                    read_tile_data(p_set, element);
                }
            }
        }
    }

    private static void read_image_data(Tileset p_set, Element p_element, Context p_context)
    {
        int height = Integer.parseInt(p_element.getAttribute("height"));
        String source = p_element.getAttribute("source");

        InputStream bitmap_stream = null;
        try
        {
            bitmap_stream = p_context.getResources().getAssets().open(source);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        p_set.m_bitmap = BitmapFactory.decodeStream(bitmap_stream);
        p_set.m_set_dimensions.m_y = (height / p_set.m_tile_dimensions.m_y.intValue());
    }

    private static void read_tile_data(Tileset p_set, Element p_element)
    {
        NodeList property_children = p_element.getChildNodes();
        int tile_flag = read_tile_properties_to_flag(property_children);

        int id = Integer.parseInt(p_element.getAttribute("id"));
        String type = p_element.getAttribute("type");
        int x = id % p_set.m_set_dimensions.m_x.intValue();
        int y = id / p_set.m_set_dimensions.m_x.intValue();

        int width = p_set.m_tile_dimensions.m_x.intValue();
        int height = p_set.m_tile_dimensions.m_y.intValue();

        Bitmap bitmap = Bitmap.createBitmap(p_set.m_bitmap,
                x * p_set.m_tile_dimensions.m_x.intValue(),
                y * p_set.m_tile_dimensions.m_y.intValue(),
                p_set.m_tile_dimensions.m_x.intValue(),
                p_set.m_tile_dimensions.m_y.intValue());
                
        Tile temp = new Tile();
        temp.m_id = id;
        temp.m_bitmap = bitmap;
        temp.tile_flag = tile_flag;
        p_set.m_tiles.add(temp);
    }

    private static int read_tile_properties_to_flag(NodeList p_property_children)
    {
        int tile_flag = 0;
        for(int j = 0; j < p_property_children.getLength(); j++)
        {
            Node property_node = p_property_children.item(j);
            if(property_node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element tile_element = (Element)property_node;
                NodeList tile_children = tile_element.getChildNodes();
                for(int k = 0; k < tile_children.getLength(); k++)
                {
                    Node tile_child = tile_children.item(k);
                    if(tile_child.getNodeType() == Node.ELEMENT_NODE)
                    {
                        Element tile_child_element = (Element)tile_child;
                        if(tile_child_element.getAttribute("name").equals("point_flags"))
                        {
                            tile_flag |= (Integer.parseInt(tile_child_element.getAttribute("value")) << 2);
                        }
                        else if(tile_child_element.getAttribute("name").equals("first_corner"))
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
