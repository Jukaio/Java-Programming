package com.jukaio.jumpandrun.ecs.componentmodule.components;

import android.graphics.Bitmap;

public class Sprite extends Component
{
    @Override
    public ComponentType get_type() {
        return ComponentType.SPRITE;
    }

    public Bitmap m_bitmap;
}
