package com.jukaio.jumpandrun.world.Tilemap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.jukaio.jumpandrun.extramath.Vector2;
import com.jukaio.jumpandrun.world.tileset.TileSet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class TileMap
{
    TileSet m_tile_set;
    int m_colour;
    Vector2 m_map_dimensions;
    Vector2 m_tile_dimensions;
    ArrayList<TileLayer> m_layers;

    // p_filename = "tilemap.xml"
    public TileMap(Context context, String p_filename)
    {
        m_map_dimensions = new Vector2();
        m_tile_dimensions = new Vector2();
        m_layers = new ArrayList<TileLayer>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try
        {
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(context.getResources().getAssets().open(p_filename));
            Document document = builder.parse(is);

            Node map_node = document.getFirstChild();
            // TODO: Deserialize function : i.e. Deserialize(Element element, String name) - DeserializeInt as wrapper for that
            m_map_dimensions = new Vector2(Integer.parseInt(map_node.getAttributes().getNamedItem("width").getNodeValue()),
                                           Integer.parseInt(map_node.getAttributes().getNamedItem("height").getNodeValue()));

            m_tile_dimensions = new Vector2(Integer.parseInt(map_node.getAttributes().getNamedItem("tilewidth").getNodeValue()),
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
                        m_tile_set = TileSet.create(context, tileset);
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
                                        m_colour = Color.parseColor((property_child_element.getAttribute("value")));
                                }
                            }
                        }
                    }
                    else if(element.getNodeName().equals("layer"))
                    {
                        m_layers.add(new TileLayer(element, m_tile_set));
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

    }

    public void draw(Canvas p_canvas, Paint p_paint)
    {
        p_canvas.drawColor(m_colour);
        for (TileLayer layer : m_layers)
        {
            layer.draw(p_canvas, p_paint);
        }
    }
}
