package com.jukaio.asteroids;

import android.graphics.Color;
import android.opengl.GLES20;

public class Star extends GLEntity
{
    private static Mesh m = null; //Q&D pool

    public Star(final float p_x, final float p_y)
    {
        super();
        set_position(p_x, p_y);
        m_colour[0] = Color.red(Color.YELLOW) / 255f;
        m_colour[1] = Color.green(Color.YELLOW) / 255f;
        m_colour[2] = Color.blue(Color.YELLOW) / 255f;
        m_colour[3] = 1f;
        if(m == null) {
            final float[] vertices = {0, 0, 0};
            m = new Mesh(vertices, GLES20.GL_POINTS);
        }
        m_mesh = m; //all Stars use the exact same Mesh instance.
    }
    
    @Override
    Type get_type()
    {
        return Type.STAR;
    }
}