package com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.SharedComponent;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.SharedComponentType;

public class RenderCanvas extends SharedComponent
{
    public SurfaceHolder m_holder;
    public Canvas m_canvas;
    public Paint m_paint;

    @Override
    public SharedComponentType get_type() {
        return SharedComponentType.RENDER_CANVAS;
    }
}
