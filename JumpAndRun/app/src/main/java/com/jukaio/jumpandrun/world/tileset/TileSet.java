package com.jukaio.jumpandrun.world.tileset;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jukaio.jumpandrun.extramath.Vector2;
import com.jukaio.jumpandrun.world.Tilemap.Tile;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class TileSet
{
    public Bitmap m_bitmap;
    public Vector2 m_tile_dimensions;
    Vector2 m_set_dimensions;
    int m_tile_count;

    private ArrayList<Tile> m_set = null;

    public Tile get(int p_index)
    {
        return m_set.get(p_index);
    }

    private static HashMap<String, TileSet> s_tile_set_cache = new HashMap<String, TileSet>();

    public static TileSet create(Context context, String p_filename)
    {
        if(s_tile_set_cache.containsKey(p_filename))
            return s_tile_set_cache.get(p_filename);
        TileSet temp = new TileSet(context, p_filename);
        s_tile_set_cache.put(p_filename, temp);
        return temp;
    }
    public static void clear_cache()
    {
        s_tile_set_cache.clear();
    }

    private TileSet(Context context, String p_filename)
    {
        m_set_dimensions = new Vector2();
        m_tile_dimensions = new Vector2();
        m_set = new ArrayList<Tile>();
        m_set.add(new Tile(0, null, 0)); // ADD NULL TILE

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try
        {
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(context.getResources().getAssets().open(p_filename));
            Document document = builder.parse(is);

            Node tile_set = document.getFirstChild();
            m_tile_dimensions = new Vector2(Integer.parseInt(tile_set.getAttributes().getNamedItem("tilewidth").getNodeValue()),
                                            Integer.parseInt(tile_set.getAttributes().getNamedItem("tileheight").getNodeValue()));
            m_tile_count = Integer.parseInt(tile_set.getAttributes().getNamedItem("tilecount").getNodeValue());
            m_set_dimensions.m_x = Integer.parseInt(tile_set.getAttributes().getNamedItem("columns").getNodeValue());

            NodeList node_list = document.getDocumentElement().getChildNodes();
            for(int i = 0; i < node_list.getLength(); i++)
            {
                Node node = node_list.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE)
                {
                    // Fix tileset parsing
                    Element element = (Element)node;
                    if(element.getNodeName().equals("image"))
                    {
                        int height = Integer.parseInt(element.getAttribute("height"));
                        String source = element.getAttribute("source");

                        InputStream bitmap_stream = context.getResources().getAssets().open(source);
                        m_bitmap = BitmapFactory.decodeStream(bitmap_stream);
                        m_set_dimensions.m_y = (height / m_tile_dimensions.m_y.intValue());
                    }
                    else if(element.getNodeName().equals("tile"))
                    {
                        NodeList property_children = element.getChildNodes();
                        int tile_flag = 0;
                        for(int j = 0; j < property_children.getLength(); j++)
                        {
                            Node property_node = property_children.item(j);
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

                        int id = Integer.parseInt(element.getAttribute("id"));
                        String type = element.getAttribute("type");
                        int x = id % m_set_dimensions.m_x.intValue();
                        int y = id / m_set_dimensions.m_x.intValue();

                        int width = m_tile_dimensions.m_x.intValue();
                        int height = m_tile_dimensions.m_y.intValue();

                        Bitmap bitmap = Bitmap.createBitmap(m_bitmap,
                                x * m_tile_dimensions.m_x.intValue(),
                                y * m_tile_dimensions.m_y.intValue(),
                                m_tile_dimensions.m_x.intValue(),
                                m_tile_dimensions.m_y.intValue());
                        Tile temp = new Tile(id, bitmap, tile_flag);
                        m_set.add(temp);
                    }
                }
            }
        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } catch (SAXException e)
        {
            e.printStackTrace();
        }

        /*// TODO: Optimise Bitmap creation
        m_set.add(new Tile(0, null));
        for(int y = 0; y < m_set_dimensions.m_y.intValue(); y++)
        {
            for(int x = 0; x < m_set_dimensions.m_x.intValue(); x++)
            {
                Bitmap temp = Bitmap.createBitmap(m_bitmap,
                                                  x * m_tile_dimensions.m_x.intValue(),
                                                  y * m_tile_dimensions.m_y.intValue(),
                                                  m_tile_dimensions.m_x.intValue(),
                                                  m_tile_dimensions.m_y.intValue());
                m_set.add(new Tile(y * m_set_dimensions.m_x.intValue() + x, temp));
            }
        }*/
    }
}
