package com.jukaio.spaceshooter.component;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.jukaio.spaceshooter.Game;
import com.jukaio.spaceshooter.Utility;
import com.jukaio.spaceshooter.Vector2;
import com.jukaio.spaceshooter.entities.Entity;

public class Bitmap_Component extends Component
{
    private Bitmap m_bitmap = null;
    private int m_color = 0xFFFFFFFF;
    
    final public int get_color()
    {
        return m_color;
    }
    
    final public void set_color(int p_color)
    {
        m_color = p_color;
    }
    
    public Bitmap_Component(Bitmap_Component p_other)
    {
        super(p_other);
        m_bitmap = p_other.m_bitmap;
        m_color = p_other.m_color;
        
    }
    
    public Bitmap_Component(Game p_game, Entity p_entity, int p_bitmap_id, float p_scale_ratio, int p_color)
    {
        super(p_entity, "BITMAP");
        m_color = p_color;
        m_bitmap = Utility.load_bitmap(p_game, p_bitmap_id);
        m_bitmap = Utility.resize_bitmap(m_bitmap, p_scale_ratio);
        get_entity().set_dimensions(new Vector2(m_bitmap.getWidth(), m_bitmap.getHeight()));
    }
    
    @Override
    public void update(float p_dt)
    {
    
    }
    
    @Override
    public void render(Entity p_entity,
                       Canvas p_canvas,
                       Paint p_paint)
    {
        p_paint.setColor(m_color);
        p_canvas.drawBitmap(m_bitmap, p_entity.get_rectangle().left, p_entity.get_rectangle().top, p_paint);
        
    }
    
    @Override
    public void destroy()
    {
        super.destroy();
        if(m_bitmap != null)
        {
            m_bitmap.recycle();
            m_bitmap = null;
        }
    }
}
