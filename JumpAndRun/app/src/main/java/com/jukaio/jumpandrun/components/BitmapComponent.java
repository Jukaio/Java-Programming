package com.jukaio.jumpandrun.components;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.jukaio.jumpandrun.Entity;

public class BitmapComponent extends Component
{
    Bitmap m_bitmap = null;

    public BitmapComponent(Entity p_entity, Bitmap p_bitmap)
    {
        super(p_entity);
        m_bitmap = p_bitmap;
    }

    @Override
    public ComponentType get_type()
    {
        return ComponentType.BITMAP;
    }
    
    @Override
    public void start()
    {
    
    }
    
    @Override
    public void pre_update(float p_dt)
    {
    
    }
    
    @Override
    public void update(float p_dt)
    {
    
    }
    
    
    @Override
    public void late_update(float p_dt)
    {
    
    }
    
    @Override
    public void render(Canvas p_canvas, Paint p_paint)
    {
        p_canvas.drawBitmap(m_bitmap, get_entity().get_matrix(), p_paint);
    }
}
