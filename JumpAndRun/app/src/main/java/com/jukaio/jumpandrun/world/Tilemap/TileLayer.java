package com.jukaio.jumpandrun.world.Tilemap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.jukaio.jumpandrun.extramath.Line;
import com.jukaio.jumpandrun.extramath.Vector2;
import com.jukaio.jumpandrun.world.tileset.TileSet;
import com.jukaio.jumpandrun.collision.TileCollider;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class TileLayer
{
    int m_id;
    String m_name;
    public Vector2 m_dimensions;
    TileSet m_tile_set;
    boolean m_has_collider;

    ArrayList<Rect> m_collders = null;
    ArrayList<Line> m_lines = null;


    private ArrayList<ArrayList<Tile>> m_tile_list;
    ArrayList<Vector2> m_chunks;

    public TileLayer(Element p_data_element, TileSet p_set)
    {
        m_id = Integer.parseInt(p_data_element.getAttribute("id"));
        m_name = p_data_element.getAttribute("name");
        m_dimensions = new Vector2(Integer.parseInt(p_data_element.getAttribute("width")),
                                   Integer.parseInt(p_data_element.getAttribute("height")));

        m_tile_list = new ArrayList<ArrayList<Tile>>();
        NodeList children = p_data_element.getChildNodes();
        m_tile_set = p_set;

        for(int i = 0; i < children.getLength(); i++)
        {
            if(children.item(i).getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element)children.item(i);
                if(element.getNodeName().equals("data"))
                {
                    read_buffer(element.getFirstChild().getNodeValue());
                }
                else if(element.getNodeName().equals("properties"))
                {
                    read_buffer(element.getFirstChild().getNodeValue());
                    for(int j = 0; j < element.getChildNodes().getLength(); j++)
                    {
                        if(element.getChildNodes().item(j).getNodeType() == Node.ELEMENT_NODE)
                        {
                            Element property = (Element) element.getChildNodes().item(j);
                            if(property.getAttribute("name").equals("Collider"))
                                m_has_collider = Boolean.parseBoolean(property.getAttribute("value"));
                        }
                    }
                }
            }
        }

        if(m_has_collider) create_collider();
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

    void create_collider()
    {
        m_collders = new ArrayList<>();
        m_lines = new ArrayList<>();

        final int w = m_tile_set.m_tile_dimensions.m_x.intValue();
        final int h = m_tile_set.m_tile_dimensions.m_y.intValue();

        for(int y = 0; y < m_tile_list.size(); y++)
        {
            for(int x = 0; x < m_tile_list.get(y).size(); x++)
            {
                Tile tile = m_tile_list.get(y).get(x);
                if(tile.m_id != 0)
                {
                    m_collders.add(new Rect(x * w,
                                            y * h,
                                           (x * w) + w,
                                         (y * h) + h));


                    int flag = tile.tile_flag;
                    int first_index = tile.tile_flag & TileCollider.START_POINT_IDX;

                    ArrayList<Point> points = new ArrayList<>();
                    for(int i = 0; i < 4; i++)
                    {
                        final int wrapped_index = (i + first_index) % 4;
                        final int index = flag & (1 << (wrapped_index + 2));
                        switch (index)
                        {
                            case(TileCollider.TOP_LEFT):     points.add(new Point(x * w, y * h));             break;
                            case(TileCollider.TOP_RIGHT):    points.add(new Point((x * w) + w, y * h));       break;
                            case(TileCollider.BOTTOM_RIGHT): points.add(new Point((x * w) + w, (y * h) + h)); break;
                            case(TileCollider.BOTTOM_LEFT):  points.add(new Point(x * w, (y * h) + h));       break;
                        }
                    }
                    Point prev_p = null;
                    for(Point p : points)
                    {
                        if(prev_p != null)
                        {
                            m_lines.add(new Line(prev_p.x, prev_p.y, p.x, p.y));
                        }
                        prev_p = p;
                    }
                }
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
                Tile tile = m_tile_list.get(y).get(x);
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

        if(m_collders != null)
        {
            int prev_colour = p_paint.getColor();
            /*
            p_paint.setColor(Color.GREEN);
            p_paint.setStyle(Paint.Style.STROKE);
            for(Rect r : m_collders)
            {
                p_canvas.drawRect(r, p_paint);
            }*/

            p_paint.setStyle(Paint.Style.FILL_AND_STROKE);

            p_paint.setColor(Color.BLUE);
            for(Line l : m_lines)
            {
                p_canvas.drawLine(l.m_start.x, l.m_start.y, l.m_end.x, l.m_end.y, p_paint);
            }

            p_paint.setColor(prev_colour);

        }
    }
}
