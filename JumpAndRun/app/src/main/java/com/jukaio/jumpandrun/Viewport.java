package com.jukaio.jumpandrun;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import com.jukaio.jumpandrun.entity.Entity;
import com.jukaio.jumpandrun.extramath.Line;
import com.jukaio.jumpandrun.extramath.Vector2;

public class Viewport
{
    public static Viewport m_active = null;

    private final PointF m_look_at = new PointF(0f, 0f);
    private Vector2 m_pixels_per_meter = null;
    private Vector2 m_screen_size = null;
    private Vector2 m_screen_center = null;
    private Vector2 m_meters_to_show = null;
    private Vector2 m_half_distance = null;
    private RectF m_bounds = new RectF();
    Matrix m_matrix = new Matrix();
    private final static float BUFFER = 0f; //overdraw, to avoid visual gaps
    Canvas m_canvas = null;
    
    public void set_bounds(float p_x, float p_y, float p_w, float p_h)
    {
        m_bounds.bottom = p_h;
        m_bounds.top = p_y;
        m_bounds.left = p_x;
        m_bounds.right = p_w;
    }


    public Viewport(final int p_screen_width, final int p_screen_height, final float metersToShowX, final float metersToShowY)
    {
        m_active = this;
    
        m_screen_size = new Vector2(p_screen_width, p_screen_height);
        m_screen_center = new Vector2(p_screen_width * 0.5f, p_screen_height * 0.5f);
        m_pixels_per_meter = new Vector2();
        m_meters_to_show = new Vector2();
        m_half_distance = new Vector2();
        m_look_at.x = 0.0f;
        m_look_at.y = 0.0f;
      
        set_projection_size(metersToShowX, metersToShowY);
    }
    
    public void set_canvas(Canvas p_canvas)
    {
        m_canvas = p_canvas;
    }

    public void set_projection_size(float metersToShowX, float metersToShowY) {
        if (metersToShowX <= 0f && metersToShowY <= 0f)
            throw new IllegalArgumentException("One of the dimensions must be provided!");
        
        m_meters_to_show.m_x = metersToShowX;
        m_meters_to_show.m_y = metersToShowY;
        
        if (metersToShowX == 0f || metersToShowY == 0f)
        {
            if (metersToShowY > 0f)
            {
                m_meters_to_show.m_x = (m_screen_size.m_x.floatValue() / m_screen_size.m_y.floatValue()) * metersToShowY;
            } else {
                m_meters_to_show.m_y = (m_screen_size.m_y.floatValue() / m_screen_size.m_x.floatValue()) * metersToShowX;
            }
        }
        m_half_distance.m_x = (m_meters_to_show.m_x.floatValue() + BUFFER) * 0.5f;
        m_half_distance.m_y = (m_meters_to_show.m_y.floatValue() + BUFFER) * 0.5f;
        m_pixels_per_meter.m_x = (m_screen_size.m_x.floatValue() / m_meters_to_show.m_x.floatValue());
        m_pixels_per_meter.m_y = (m_screen_size.m_y.floatValue() / m_meters_to_show.m_y.floatValue());
    }

    public Vector2 get_pixel_per_meter()
    {
        return m_pixels_per_meter;
    }


    public void look_at(final float x, final float y)
    {
        m_look_at.x = x;
        m_look_at.y = y;
        clamp_look_at(x, y);
    }
    
    //This clamp function prioritises the top left corner, avoid awkward repositioning
    private void clamp_look_at(float p_x, float p_y)
    {
        float w = m_half_distance.m_x.floatValue();
        float h = m_half_distance.m_y.floatValue();
        
        if(p_x + w > m_bounds.right)
           m_look_at.x = m_bounds.right - w;
        if(m_look_at.x - w < m_bounds.left)
           m_look_at.x = m_bounds.left + w;
           
        if(p_y + h > m_bounds.bottom)
            m_look_at.y = m_bounds.bottom - h;
        if(m_look_at.y  - h < m_bounds.top)
            m_look_at.y = m_bounds.top + h;
    }

    public void look_at(final Entity obj)
    {
        float x = obj.get_position().m_x.floatValue();
        float y = obj.get_position().m_y.floatValue();
        
        look_at(x, y);
    }

    public void look_at(final PointF pos) {
        look_at(pos.x, pos.y);
    }

    public void world_to_screen(final float worldPosX, final float worldPosY, Point screenPos)
    {
        screenPos.x = (int) (m_screen_center.m_x.floatValue() - ((m_look_at.x - worldPosX) * m_pixels_per_meter.m_x.floatValue()));
        screenPos.y = (int) (m_screen_center.m_y.floatValue() - ((m_look_at.y - worldPosY) * m_pixels_per_meter.m_y.floatValue()));
    }

    public void world_to_screen(final PointF worldPos, Point screenPos) {
        world_to_screen(worldPos.x, worldPos.y, screenPos);
    }

    public void world_to_screen(final Entity e, final Point screenPos)
    {
        float x = e.get_position().m_x.floatValue();
        float y = e.get_position().m_y.floatValue();
        
        world_to_screen(x, y, screenPos);
    }

    public boolean in_view(final Entity e)
    {
        float x = e.get_position().m_x.floatValue();
        float y = e.get_position().m_y.floatValue();
    
        float width = e.get_dimensions().m_x.floatValue();
        float height = e.get_dimensions().m_y.floatValue();
    
        float half_x = m_half_distance.m_x.floatValue();
        float half_y = m_half_distance.m_y.floatValue();
    
    
        final float maxX = (m_look_at.x + half_x);
        final float minX = (m_look_at.x - half_x) - width;
        final float maxY = (m_look_at.y + half_y);
        final float minY = (m_look_at.y - half_y) - height;
        
        return (x > minX && x < maxX)
                && (y > minY && y < maxY);
    }

    public boolean in_view(final RectF bounds)
    {
        float half_x = m_half_distance.m_x.floatValue();
        float half_y = m_half_distance.m_y.floatValue();
        
        final float right = (m_look_at.x + half_x);
        final float left = (m_look_at.x - half_x);
        final float bottom = (m_look_at.y + half_y);
        final float top = (m_look_at.y - half_y);
        return (bounds.left < right && bounds.right > left)
                && (bounds.top < bottom && bounds.bottom > top);
    }
    
    public boolean in_view(final float x, final float y, final float w, final float h)
    {
        float half_x = m_half_distance.m_x.floatValue();
        float half_y = m_half_distance.m_y.floatValue();
        
        final float right = (m_look_at.x + half_x);
        final float left = (m_look_at.x - half_x);
        final float bottom = (m_look_at.y + half_y);
        final float top = (m_look_at.y - half_y);
        return (x < right && x + w > left)
                && (y < bottom && y + h > top);
    }
    
    public void draw_bitmap(Bitmap m_bitmap, Matrix p_matrix, Paint p_paint)
    {
        m_matrix.reset();
        m_matrix.set(p_matrix);
        m_matrix.postTranslate(-m_look_at.x + m_half_distance.m_x.floatValue(), -m_look_at.y + m_half_distance.m_y.floatValue());
        m_matrix.postScale(m_pixels_per_meter.m_x.floatValue(), m_pixels_per_meter.m_y.floatValue());
        m_canvas.drawBitmap(m_bitmap, m_matrix, p_paint);
    }
    
    public void draw_bitmap(Bitmap m_bitmap, float x, float y, Paint p_paint)
    {
        m_matrix.reset();
        m_matrix.setTranslate(x, y);
        m_matrix.postTranslate(-m_look_at.x + m_half_distance.m_x.floatValue(), -m_look_at.y + m_half_distance.m_y.floatValue());
        m_matrix.postScale(m_pixels_per_meter.m_x.floatValue(), m_pixels_per_meter.m_y.floatValue());
        m_canvas.drawBitmap(m_bitmap, m_matrix, p_paint);
        //m_matrix.postScale(m_meters_to_show.m_x.floatValue(), m_meters_to_show.m_y.floatValue());
    }
    
    public void draw_line(Line p_line, Paint p_paint)
    {
        m_canvas.drawLine(p_line.m_start.x,
                          p_line.m_start.y,
                          p_line.m_end.x,
                          p_line.m_end.y,
                          p_paint);
    }
    
    public void draw_colour(int p_colour)
    {
        m_canvas.drawColor(p_colour);
    }
    
    public void draw_text(String p_text, float x, float y, Paint p_paint)
    {
        m_canvas.drawText(p_text, x, y, p_paint);
    }
    public void draw_rect(Rect p_rect, Paint p_paint)
    {
        m_canvas.drawRect(p_rect, p_paint);
    }
    
    public void draw_rect(RectF p_rect, Paint p_paint)
    {
        m_canvas.drawRect(p_rect, p_paint);
    }
    
    public void draw_rect(float x, float y, float w, float h, Paint p_paint)
    {
        m_canvas.drawRect(x, y, x + w, y + h, p_paint);
    }
    
    public void debug(Paint p_paint)
    {
        if (Game.DEBUG_ON)
        {
            float w = m_screen_size.m_x.floatValue() / 2;
            float h = m_screen_size.m_y.floatValue() / 2;
            float x = m_screen_center.m_x.floatValue();
            float y = m_screen_center.m_y.floatValue();
            float meter_x = m_half_distance.m_x.floatValue();
            float meter_y = m_half_distance.m_y.floatValue();
            float show_x = m_meters_to_show.m_x.floatValue();
            float show_y = m_meters_to_show.m_y.floatValue();
            //m_half_distance = null;
            p_paint.setStyle(Paint.Style.STROKE);
            p_paint.setColor(Color.CYAN); // Draw camera
            m_canvas.drawRect(m_look_at.x - meter_x,
                              m_look_at.y - meter_y,
                              m_look_at.x + meter_x,
                              m_look_at.y + meter_y,
                              p_paint);
            p_paint.setColor(Color.GREEN); // Draw the point
            m_canvas.drawRect(m_look_at.x - (show_x * meter_x) + 4,
                              m_look_at.y - (show_y * meter_y) + 4,
                              m_look_at.x + (show_x * meter_x) - 4,
                              m_look_at.y + (show_y * meter_y) - 4,
                              p_paint);
            p_paint.setStyle(Paint.Style.FILL_AND_STROKE);
        }
    }
}

