package com.jukaio.asteroids;

import android.opengl.GLES20;

public class Border extends GLEntity {
    public Border(final float p_x, final float p_y, final float p_w, final float p_h)
    {
        super();
        set_position(p_x, p_y);
        set_dimensions(p_w - 1, p_h - 1);
        setColors(1f, 0f, 0f, 1f); //RED for visibility
        m_mesh = new Mesh(Mesh.generate_line_polygon(4, 10.0), GLES20.GL_LINES);
        m_mesh.rotate_z(45* Utilities.TO_RAD);
        m_mesh.set_width_and_height(p_w, p_h); //will automatically normalize the mesh!
    }
    
    @Override
    Type get_type()
    {
        return Type.BORDER;
    }
}