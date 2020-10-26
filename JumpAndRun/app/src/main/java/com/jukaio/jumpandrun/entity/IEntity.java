package com.jukaio.jumpandrun.entity;

import android.graphics.Paint;

import com.jukaio.jumpandrun.Viewport;

public interface IEntity
{
    void start();
    void update(float p_dt);
    void render(Viewport p_viewport, Paint p_paint);
    void destroy();
}
