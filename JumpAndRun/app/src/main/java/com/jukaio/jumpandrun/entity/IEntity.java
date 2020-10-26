package com.jukaio.jumpandrun;

import android.graphics.Paint;

public interface IEntity
{
    void start();
    void update(float p_dt);
    void render(Viewport p_viewport, Paint p_paint);
    void destroy();
}
