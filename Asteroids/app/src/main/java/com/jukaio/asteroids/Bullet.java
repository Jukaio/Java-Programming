package com.jukaio.asteroids;

import android.graphics.PointF;
import android.opengl.GLES20;
import android.util.Log;

public class Bullet extends GLEntity
{
    private static Mesh BULLET_MESH = new Mesh(Mesh.POINT, GLES20.GL_POINTS); //Q&D pool, Mesh.POINT is just [0,0,0] float array
    private static final float TO_RADIANS = (float)Math.PI/180.0f;
    private static final float SPEED = 1.2f; //TODO: game play settings
    public static final float TIME_TO_LIVE = 1.0f; //seconds

    public float m_time_to_live = TIME_TO_LIVE;
    
    @Override
    Type get_type()
    {
        return Type.BULLET;
    }
    
    public Bullet()
    {
        setColors(1, 0, 0, 1);
        m_mesh = BULLET_MESH; //all bullets use the exact same mesh
        m_active = false;
    }

    public void fire_from(GLEntity source)
    {
        m_active = true;
        final float theta = source.get_rotation()*TO_RADIANS;
        set_position(source.center_x()  + (float)Math.sin(theta) * (source.get_dimensions().m_y * 0.5f) ,
                     source.center_y() - (float)Math.cos(theta) * (source.get_dimensions().m_y * 0.5f));
        Log.d("Position: ", Float.toString(source.center_x()) + " - - " + Float.toString(source.center_y()));
        set_velocity(source.get_velocity().m_x + (float)Math.sin(theta) * SPEED,
                     source.get_velocity().m_y - (float)Math.cos(theta) * SPEED);
        Jukebox.play(SoundID.SHOOT, Jukebox.MAX_VOLUME, 0);
        m_time_to_live = TIME_TO_LIVE;
    }
    
    @Override
    public void update(double dt) {
        if(m_time_to_live > 0)
        {
            m_time_to_live -= dt;
            super.update(dt);
        }
        else
        {
            m_active = false;
        }
    }
    @Override
    public void render(final float[] viewportMatrix){
        if(m_time_to_live > 0)
        {
            super.render(viewportMatrix);
        }
    }
    
    @Override
    public void on_collision(GLEntity that)
    {
        switch (that.get_type())
        {
            case INVALID:
                break;
            case ASTEROID:
                m_active = false;
                break;
            case BORDER:
                break;
            case BULLET:
                break;
            case PLAYER:
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
    
    @Override
    public boolean isColliding(final GLEntity that){
        if(!areBoundingSpheresOverlapping(this, that)){ //quick rejection
            return false;
        }
        final PointF[] asteroidVerts = that.getPointList();
        return CollisionDetection.polygonVsPoint(asteroidVerts, get_position().m_x, get_position().m_y);
    }
}