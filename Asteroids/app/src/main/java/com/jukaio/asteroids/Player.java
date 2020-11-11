package com.jukaio.asteroids;

import android.graphics.PointF;
import android.opengl.GLES20;

public class Player extends GLEntity
{
    private static final String TAG = "Player";

    static final float ROTATION_VELOCITY = 360f; //TODO: game play values!
    static final float THRUST = 0.6f;
    static final float DRAG = 0.95f;
    public static final float TIME_BETWEEN_SHOTS = 0.25f; //seconds. TODO: game play setting!
    private float m_bullet_cooldown = 0;
    
    private int m_max_hp = 0;
    private int m_hp = 0;
    private Flame m_flame = null;

    public int get_current_hp()
    {
        return m_hp;
    }

    public Player(final float p_x, final float p_y, final float p_w, final float p_h, final int p_max_hp)
    {
        super();
        set_position(p_x, p_y);
        float vertices[] =
        { // in counterclockwise order:
                0.0f,  0.5f, 0.0f, 	// top
                -0.5f, -0.5f, 0.0f,	// bottom left
                0.5f, -0.5f, 0.0f  	// bottom right
        };
        m_max_hp = p_max_hp;
        m_hp = m_max_hp;
        m_mesh = new Mesh(vertices, GLES20.GL_TRIANGLES);
        m_mesh.set_width_and_height(p_w, p_h);
        m_mesh.flip_y();
        
        set_scale(10.0f, 10.0f); //render at 20x the size
        set_dimensions(p_w * 10.0f, p_h * 10.0f);
        
        m_flame = new Flame(this);
    }

    @Override
    public void render(float[] viewportMatrix)
    {

        super.render(viewportMatrix);

        m_flame.render(viewportMatrix);
    }
    
    @Override
    Type get_type()
    {
        return Type.PLAYER;
    }
    
    @Override
    public void update(double dt)
    {
        set_rotation((float)(get_rotation() + (dt*ROTATION_VELOCITY) * s_game.m_inputs.m_horizontal_factor));
        if(s_game.m_inputs.pressed_B())
        {
            final float theta = (float) (get_rotation() * Utilities.TO_RAD);
            
            set_velocity(get_velocity().m_x + (float)Math.sin(theta) * THRUST,
                         get_velocity().m_y - (float)Math.cos(theta) * THRUST);
        }
        set_velocity(get_velocity().m_x * DRAG,
                     get_velocity().m_y * DRAG);

        m_bullet_cooldown -= dt;
        if(s_game.m_inputs.just_pressed_A() && m_bullet_cooldown <= 0)
        {
            setColors(1, 0, 1, 1);
            if(s_game.maybe_fire_bullet(this))
            {
                m_bullet_cooldown = TIME_BETWEEN_SHOTS;
            }
        }
        else
        {
            setColors(1.0f, 1, 1,1);
        }
        
        super.update(dt);
        m_flame.update(dt);
    }

    @Override
    public boolean isColliding(final GLEntity that)
    {
        if(!areBoundingSpheresOverlapping(this, that))
        {
            return false;
        }
        final PointF[] shipHull = getPointList();
        final PointF[] asteroidHull = that.getPointList();
        if(CollisionDetection.polygonVsPolygon(shipHull, asteroidHull))
        {
            return true;
        }
        return CollisionDetection.polygonVsPoint(asteroidHull, get_position().m_x, get_position().m_y); //finally, check if we're inside the asteroid
    }
    
    @Override
    public void on_collision(GLEntity that)
    {
        switch(that.get_type())
        {
            case INVALID:
                break;
            case ASTEROID:
                    m_hp--;
                    Jukebox.play(SoundID.GET_HIT, Jukebox.MAX_VOLUME, 0);
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
}