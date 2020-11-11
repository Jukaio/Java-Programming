package com.jukaio.asteroids;

import android.opengl.GLES20;

public class Asteroid extends GLEntity
{
    enum Size
    {
        small(0.5f),
        medium(1.0f),
        big(1.5f);
        
        private float m_float;
        Size(float p_float)
        {
            m_float = p_float;
        }
        
        float as_float()
        {
            return m_float;
        }
    }

    private static final float MAX_VEL = 0.1f;
    private static final float MIN_VEL = -0.1f;
    private int m_score;
    private Size m_size;
    
    public Size get_size_type()
    {
        return m_size;
    }

    public Asteroid(final float p_x, final float p_y, float p_w, float p_h, int points, Size p_size, int p_score)
    {
        if(points < 3){ points = 3; } //triangles or more, please. :)
        set_position(p_x, p_y);
        m_size = p_size;
        m_active = false;
        
        float scaled_w = p_w * p_size.as_float();
        float scaled_h = p_h * p_size.as_float();
        m_score =  (int)(p_score * p_size.as_float());
        
        set_dimensions(scaled_w, scaled_h);
        
        set_velocity(Utilities.between(MIN_VEL*2, MAX_VEL*2),
                     Utilities.between(MIN_VEL*2, MAX_VEL*2));
        //_velR = Utilities.between(MIN_VEL*4, MAX_VEL*4);
        final double radius = scaled_w * 0.5;
        final float[] verts = Mesh.generate_line_polygon(points, radius);
        m_mesh = new Mesh(verts, GLES20.GL_LINES);
        m_mesh.set_width_and_height(scaled_w, scaled_h);
    }
    
    @Override
    Type get_type()
    {
        return Type.ASTEROID;
    }
    
    @Override
    public void update(double dt) {
        super.update(dt);
        set_rotation(get_rotation() + 1);
    }
    
    @Override
    public void on_collision(GLEntity that)
    {
        switch(that.get_type())
        {
            case INVALID:
                break;
            case ASTEROID:
                break;
            case BORDER:
                break;
            case BULLET:
                m_active = false;
                break;
            case PLAYER:
                m_active = false;
                break;
            case FLAME:
                break;
            case STAR:
                break;
            case HUD:
                break;
            case TEXT:
                break;
        }
    }
}