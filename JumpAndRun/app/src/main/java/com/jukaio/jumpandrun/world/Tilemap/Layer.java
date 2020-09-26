package com.jukaio.jumpandrun.world.Tilemap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;

import com.jukaio.jumpandrun.extramath.Vector2;
import com.jukaio.jumpandrun.world.tileset.TileSet;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class Layer
{
    public Vector2 m_dimensions;
    TileSet m_tile_set;

    public Layer(Element p_data_element, TileSet p_set)
    {
        m_dimensions = new Vector2(Integer.parseInt(p_data_element.getAttribute("width")),
                                   Integer.parseInt(p_data_element.getAttribute("height")));
        m_tile_list = new ArrayList<ArrayList<Tile>>();
        NodeList children = p_data_element.getChildNodes();
        m_tile_set = p_set;
        for(int i = 0; i < children.getLength(); i++)
        {
            if(children.item(i).getNodeType() == Node.ELEMENT_NODE)
            {
                read_buffer(((Element)(children.item(i))).getFirstChild().getNodeValue());
            }
        }
    }

    private void read_buffer(String p_buffer)
    {
        String number = new String("");

        int start = 0;
        char scan = p_buffer.charAt(0);
        if(scan == '\n')
            start++;
        m_tile_list.add(new ArrayList<Tile>());
        for(int i = start; i < p_buffer.length(); i++)
        {
            scan = p_buffer.charAt(i);
            if(scan == '\n' &&
               i != p_buffer.length() - 1)
            {
                m_tile_list.add(new ArrayList<Tile>());
            }
            else if(scan == ',')
            {
                int num = Integer.parseInt(number);
                m_tile_list.get(m_tile_list.size() - 1).add(m_tile_set.get(num));
                number = "";
            }
            else
            {
                number += scan;
            }
        }
    }

    public Tile get(int x, int y)
    {
        if(x >= m_dimensions.m_x.intValue() ||
           y >= m_dimensions.m_y.intValue())
        {
            throw new AssertionError("index out of bounds");
        }
        return m_tile_list.get(y).get(x);
    }

    Rect destination_rect = new Rect();
    void draw(Canvas p_canvas, Paint p_paint)
    {
        p_paint.setAntiAlias(false);
        p_paint.setDither(true);
        p_paint.setFilterBitmap(false);
        for(int y = 0; y < m_tile_list.size(); y++)
        {
            for(int x = 0; x < m_tile_list.get(y).size(); x++)
            {
                Bitmap bitmap = m_tile_list.get(y).get(x).m_bitmap;
                if(bitmap != null)
                {
                    p_canvas.drawBitmap(bitmap,
                                  x * m_tile_set.m_tile_dimensions.m_x.intValue(),
                                  y * m_tile_set.m_tile_dimensions.m_y.intValue(),
                                   p_paint);
                }
            }
        }
    }

    private ArrayList<ArrayList<Tile>> m_tile_list;
}
