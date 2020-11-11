package com.jukaio.asteroids;

import android.opengl.GLES20;

public class Flame extends GLEntity {

    Player m_player;
    
    final static float SCALE_FACTOR = 1.5f;
    final static float MAX_SCALE = 2.0f;
    
    public Flame(Player p_player)
    {
        super();
        
        m_player = p_player;
        
        set_dimensions(m_player.get_dimensions().m_x * 0.5f, m_player.get_dimensions().m_y * 0.5f);
        set_scale(m_player.get_scale().m_x, m_player.get_scale().m_y);
        
        
        
        float vertices[] =
        { // in counterclockwise order:
                0.0f,  0.5f, 0.0f, 	// top
                -0.5f, -0.5f, 0.0f,	// bottom left
                0.5f, -0.5f, 0.0f  	// bottom right
        };
        
        setColors(0, 1, 0 , 1);
        m_mesh = new Mesh(vertices, GLES20.GL_TRIANGLES);
        m_mesh.set_width_and_height(get_dimensions().m_x, get_dimensions().m_y);
    }
    
    @Override
    Type get_type()
    {
        return Type.FLAME;
    }
    
    @Override
    public void update(double dt)
    {
        set_position(10, 10);
        
        set_rotation((float)(m_player.get_rotation()));
        
        float length = (float) Math.sqrt(Math.pow(m_player.get_velocity().m_x, 2) + Math.pow(m_player.get_velocity().m_y, 2)) * SCALE_FACTOR;
        if(length > get_scale().m_y) length = MAX_SCALE;
        set_scale(length, length);
        
        final float theta = (float) (m_player.get_rotation() * Utilities.TO_RAD);
        set_position(m_player.center_x() - (float)Math.sin(theta) * ((m_player.get_dimensions().m_x * 0.5f ) + (get_scale().m_x * get_dimensions().m_x * 0.5f)),
                     m_player.center_y() + (float)Math.cos(theta) * ((m_player.get_dimensions().m_y * 0.5f ) + (get_scale().m_y * get_dimensions().m_y * 0.5f)));


        //set_velocity(player.get_velocity().m_x, player.get_velocity().m_y);
    }
    
    @Override
    public void render(float[] viewportMatrix)
    {
        super.render(viewportMatrix);
    }
}
