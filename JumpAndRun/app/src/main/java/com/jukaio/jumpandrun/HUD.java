package com.jukaio.jumpandrun;

import android.graphics.Color;
import android.graphics.Paint;

import com.jukaio.jumpandrun.extramath.Vector2;

import java.util.ArrayList;

public class HUD
{
    public static abstract class ElementHUD
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
        
        public abstract void draw(Viewport p_viewport, Paint p_paint);
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
        public void draw(Viewport p_viewport, Paint p_paint)
        {
            float x = get_position().m_x.floatValue();
            float y = get_position().m_y.floatValue();
            int prev_colour = p_paint.getColor();
            
            p_paint.setTextSize(m_size);
            p_paint.setTextAlign(m_align);
            
            p_paint.setColor(get_colour());
            p_viewport.draw_text(m_text, x, y, p_paint);
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
    
    public void render(Viewport p_viewport, Paint p_paint)
    {
        for(ElementHUD e : m_elements)
            e.draw(p_viewport, p_paint);
    }
    
    private ArrayList<ElementHUD> m_elements = new ArrayList<>();
}
