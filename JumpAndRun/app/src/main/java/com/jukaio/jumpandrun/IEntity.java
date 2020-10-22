package com.jukaio.jumpandrun;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.jukaio.jumpandrun.components.Component;

public interface IEntity
{
    void start();
    void update(float p_dt);
    void render(Canvas p_canvas, Paint p_paint);
}
