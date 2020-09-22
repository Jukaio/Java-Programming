package com.jukaio.spaceshooter.game_entities;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.jukaio.spaceshooter.Jukebox;
import com.jukaio.spaceshooter.Sound_ID;
import com.jukaio.spaceshooter.entities.Entity;

public interface IWeapon
{
    public void set_parent(Entity p_parent);
    public boolean trigger(float p_dt);
    public void update(float p_dt);
    public void render(Canvas p_canvas, Paint p_paint);
    public boolean is_colliding(Entity other);
    public void reset();

}
