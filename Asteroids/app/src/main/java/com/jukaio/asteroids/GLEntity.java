package com.jukaio.asteroids;

import android.graphics.PointF;
import android.opengl.Matrix;

import java.util.Objects;

public abstract class GLEntity extends Transform
{
    public enum Type
    {
        INVALID,
        ASTEROID,
        BORDER,
        BULLET,
        PLAYER,
        FLAME,
        STAR,
        HUD,
        TEXT,
        SAUCER
    }

    public static Game s_game = null; //shared ref, managed by the Game-class!
    Mesh m_mesh = null;
    float m_colour[] = { 1.0f, 1.0f, 1.0f, 1.0f }; //default white
    private Vector2 m_velocity = new Vector2(0.0f, 0.0f);


    public static final float[] m_model_matrix = new float[4*4];
    public static final float[] m_viewport_model_matrix = new float[4*4];
    public static final float[] m_rotation_viewport_model_matrix = new float[4*4];

    public boolean m_active = true;
    public boolean is_inactive(){
        return !m_active;
    }

    abstract Type get_type();

    final public void set_velocity(float p_x, float p_y)
    {
        m_velocity.m_x = p_x;
        m_velocity.m_y = p_y;
    }
    
    final public Vector2 get_velocity()
    {
        return m_velocity;
    }

    public GLEntity()
    {
        super();
    }

    public void update(final double dt) {
        Vector2 position = get_position();
        set_position(position.m_x + m_velocity.m_x, position.m_y + m_velocity.m_y);

        if(left() > Game.WORLD_WIDTH)
        {
            setRight(0);
        }else if(right() < 0)
        {
            setLeft(Game.WORLD_WIDTH);
        }
        if(top() > Game.WORLD_HEIGHT)
        {
            setBottom(0);
        }else if(bottom() < 0)
        {
            setTop(Game.WORLD_HEIGHT);
        }
    }
    public float left()
    {
        return get_position().m_x + m_mesh.left();
    }
    public  float right()
    {
        return get_position().m_x + m_mesh.right();
    }
    
    public void setLeft(final float leftEdgePosition)
    {
        Vector2 position = get_position();
        set_position(leftEdgePosition - m_mesh.left(), position.m_y);
    }
    public void setRight(final float rightEdgePosition)
    {
        Vector2 position = get_position();
        set_position(rightEdgePosition - m_mesh.left(), position.m_y);
    }
    
    public float top()
    {
        return get_position().m_y + m_mesh.top();
    }
    public float bottom()
    {
        return get_position().m_y + m_mesh.bottom();
    }
    public void setTop(final float topEdgePosition)
    {
        Vector2 position = get_position();
        set_position(position.m_x, topEdgePosition - m_mesh.top());
    }
    public void setBottom(final float bottomEdgePosition)
    {
        Vector2 position = get_position();
        set_position(position.m_x, bottomEdgePosition - m_mesh.top());
    }

    public void render(final float[] viewportMatrix){
        final int OFFSET = 0;
        
        //reset the model matrix and then translate (move) it into world space
        Matrix.setIdentityM(m_model_matrix, OFFSET); //reset model matrix
        Matrix.translateM(m_model_matrix, OFFSET, get_position().m_x, get_position().m_y, get_z());
        //viewportMatrix * modelMatrix combines into the viewportModelMatrix
        //NOTE: projection matrix on the left side and the model matrix on the right side.
        Matrix.multiplyMM(m_viewport_model_matrix, OFFSET, viewportMatrix, OFFSET,
                          m_model_matrix, OFFSET);
        //apply a rotation around the Z-axis to our modelMatrix. Rotation is in degrees.
        Matrix.setRotateM(m_model_matrix, OFFSET, get_rotation(), 0, 0, 1.0f);
        //apply scaling to our modelMatrix, on the x and y axis only.
        Matrix.scaleM(m_model_matrix, OFFSET, get_scale().m_x, get_scale().m_y, 1f);
        //finally, multiply the rotated & scaled model matrix into the model-viewport matrix
        //creating the final rotationViewportModelMatrix that we pass on to OpenGL
        Matrix.multiplyMM(m_rotation_viewport_model_matrix, OFFSET,
                          m_viewport_model_matrix, OFFSET,
                          m_model_matrix, OFFSET);

        GLManager.draw(m_mesh,
                       m_rotation_viewport_model_matrix,
                       m_colour);
    }

    public void on_collision(final GLEntity that) {
    
    }

    public boolean isColliding(final GLEntity that) {
        if (this == that) {
            throw new AssertionError("isColliding: You shouldn't test Entities against themselves!");
        }
        return GLEntity.isAABBOverlapping(this, that);
    }

    public float center_x()
    {
        return get_position().m_x;
    }
    public float center_y() {
        return get_position().m_y;
    }
    public float radius() {
        //use the longest side to calculate radius
        return (get_dimensions().m_x > get_dimensions().m_y) ? get_dimensions().m_x  *  0.5f : get_dimensions().m_y * 0.5f;
    }

    //axis-aligned intersection test
//returns true on intersection, and sets the least intersecting axis in the "overlap" output parameter
    static final PointF overlap = new PointF( 0 , 0 ); //Q&D PointF pool for collision detection. Assumes single threading.
    @SuppressWarnings("UnusedReturnValue")
    static boolean getOverlap(final GLEntity a, final GLEntity b, final PointF overlap) {
        overlap.x = 0.0f;
        overlap.y = 0.0f;
        final float centerDeltaX = a.center_x() - b.center_x();
        final float halfWidths = (a.get_dimensions().m_x + b.get_dimensions().m_x) * 0.5f;
        float dx = Math.abs(centerDeltaX); //cache the abs, we need it twice

        if (dx > halfWidths) return false ; //no overlap on x == no collision

        final float centerDeltaY = a.center_y() - b.center_y();
        final float halfHeights = (a.get_dimensions().m_y + b.get_dimensions().m_y) * 0.5f;
        float dy = Math.abs(centerDeltaY);

        if (dy > halfHeights) return false ; //no overlap on y == no collision

        dx = halfWidths - dx; //overlap on x
        dy = halfHeights - dy; //overlap on y
        if (dy < dx) {
            overlap.y = (centerDeltaY < 0 ) ? -dy : dy;
        } else if (dy > dx) {
            overlap.x = (centerDeltaX < 0 ) ? -dx : dx;
        } else {
            overlap.x = (centerDeltaX < 0 ) ? -dx : dx;
            overlap.y = (centerDeltaY < 0 ) ? -dy : dy;
        }
        return true ;
    }
    //Some good reading on bounding-box intersection tests:
//https://gamedev.stackexchange.com/questions/586/what-is-the-fastest-way-to-work-out-2d-bounding-box-intersection
    static boolean isAABBOverlapping(final GLEntity a, final GLEntity b) {
        return !(a.right() <= b.left()
                || b.right() <= a.left()
                || a.bottom() <= b.top()
                || b.bottom() <= a.top());
    }

    public void setColors(final float[] colors){
        Objects.requireNonNull(colors);
        assert(colors.length >= 4);
        setColors(colors[0], colors[1], colors[2], colors[3]);
    }
    public void setColors(final float r, final float g, final float b, final float a){
        m_colour[0] = r; //red
        m_colour[1] = g; //green
        m_colour[2] = b; //blue
        m_colour[3] = a; //alpha (transparency)
    }

    static boolean areBoundingSpheresOverlapping(final GLEntity a, final GLEntity b) {
        final float dx = a.center_x()-b.center_x(); //delta x
        final float dy = a.center_y()-b.center_y();
        final float distanceSq = (dx*dx + dy*dy);
        final float minDistance = a.radius() + b.radius();
        final float minDistanceSq = minDistance*minDistance;
        return distanceSq < minDistanceSq;
    }

    public PointF[] getPointList(){
        return m_mesh.get_point_list(get_position().m_x, get_position().m_y, get_rotation());
    }
}