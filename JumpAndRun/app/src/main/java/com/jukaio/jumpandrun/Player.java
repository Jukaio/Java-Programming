package com.jukaio.jumpandrun;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Player extends Entity
{
    Bitmap m_bitmap = null;
    
    public Player(int p_x, int p_y, Bitmap p_bitmap, String p_source, Context p_context)
    {
        super(true, EntityType.PLAYER);
        set_position(p_x, p_y);
        set_rotation(0.0f);
        set_scale(1, 1);
        Document doc = XML.get_document(p_source, p_context);
        Node first = doc.getFirstChild();
        int w = XML.parse_int(first, "width");
        int h = XML.parse_int(first, "height");
        set_dimensions(w, h);
        m_bitmap = p_bitmap;
    
        NodeList children = first.getChildNodes();
        for(int i = 0; i < children.getLength(); i++)
        {
            Node node = children.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE)
            {
            
            }
        }
    }
    
    @Override
    public EntityType get_type()
    {
        return null;
    }
}
