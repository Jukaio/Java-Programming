package com.jukaio.spaceshooter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public abstract class Utility
{
    public static Bitmap load_bitmap(Game p_game, int p_id)
    {
        Bitmap temp = BitmapFactory.decodeResource(p_game.getContext().getResources(),
                                                   p_id);
        if(temp == null)
            throw new AssertionError("BitMapFactory in Player Constructor: Bitmap error");
        return temp;
    }
    
    public static Bitmap resize_bitmap(Bitmap p_bitmap, float p_scale_ratio)
    {
        if(p_scale_ratio == 1.0f)
            return p_bitmap;
            
        Bitmap resized = Bitmap.createScaledBitmap(p_bitmap,
                                                   (int)(p_bitmap.getWidth() * p_scale_ratio),
                                                   (int)(p_bitmap.getHeight() * p_scale_ratio),
                                                   true);
        if(resized == null)
            throw new AssertionError("BitMapFactory in Player Constructor: Bitmap error");
            
        p_bitmap.recycle();
        return resized;
    }
}
