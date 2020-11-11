package com.jukaio.asteroids;

import android.graphics.PointF;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Mesh
{
    private static final String TAG = "Mesh";
    // find the size of the float type, in bytes
    public static final int SIZE_OF_FLOAT = Float.SIZE / Byte.SIZE; //32bit/8bit = 4 bytes
    // number of coordinates per vertex in our meshes
    public static final int COORDS_PER_VERTEX = 3; //X, Y, Z
    // number of bytes per vertex
    public static final int VERTEX_STRIDE = COORDS_PER_VERTEX * SIZE_OF_FLOAT;
    
    public static float[] POINT = {0f, 0f, 0f};
    
    public FloatBuffer m_vertex_buffer = null;
    public int m_vertex_count = 0;
    public int m_draw_mode = GLES20.GL_TRIANGLES;
    
    public float m_width = 0f;
    public float m_height = 0f;
    public float m_depth = 0f;
    public float m_radius = 0f;
    public Point3D m_min = new Point3D();
    public Point3D m_max = new Point3D();
    
    public static final int X = 0;
    public static final int Y = 1;
    public static final int Z = 2;
    
    PointF[] m_collision_points = null;
    
    public Mesh(final float[] geometry)
    {
        init(geometry,
             GLES20.GL_TRIANGLES);
    }
    
    public Mesh(final float[] geometry, final int drawMode)
    {
        init(geometry,
             drawMode);
    }
    
    private void init(final float[] geometry, final int drawMode)
    {
        set_vertices(geometry);
        set_draw_mode(drawMode);
    }
    
    public void flip_x()
    {
        flip(X);
    }
    
    public void flip_y()
    {
        flip(Y);
    }
    
    public void flip_z()
    {
        flip(Z);
    }
    
    public void scale(final double xFactor, final double yFactor, final double zFactor)
    {
        for (int i = 0; i < m_vertex_count * COORDS_PER_VERTEX; i += COORDS_PER_VERTEX)
        {
            m_vertex_buffer.put(i + X,
                                (float) (m_vertex_buffer.get(i + X) * xFactor));
            m_vertex_buffer.put(i + Y,
                                (float) (m_vertex_buffer.get(i + Y) * yFactor));
            m_vertex_buffer.put(i + Z,
                                (float) (m_vertex_buffer.get(i + Z) * zFactor));
        }
        update_bounds();
    }
    
    public void scale(final double factor)
    {
        scale(factor,
              factor,
              factor);
    }
    
    public void scale_x(final double factor)
    {
        scale(factor,
              1.0,
              1.0);
    }
    
    public void scale_y(final double factor)
    {
        scale(1.0,
              factor,
              1.0);
    }
    
    public void scale_z(final double factor)
    {
        scale(1.0,
              1.0,
              factor);
    }
    
    public void set_width_and_height(final double w, final double h)
    {
        normalize();  //a normalized mesh is centered at [0,0] and ranges from [-1,1]
        scale(w * 0.5,
              h * 0.5,
              1.0); //meaning we now scale from the center, so *0.5 (radius)
        Utilities.require(Math.abs(w - m_width) < Float.MIN_NORMAL && Math.abs(h - m_height) < Float.MIN_NORMAL,
                          "incorrect width / height after scaling!");
    }
    
    public void flip(final int axis)
    {
        assert (axis == X || axis == Y || axis == Z);
        m_vertex_buffer.position(0);
        for (int i = 0; i < m_vertex_count; i++)
        {
            final int index = i * COORDS_PER_VERTEX + axis;
            final float invertedCoordinate = m_vertex_buffer.get(index) * -1;
            m_vertex_buffer.put(index,
                                invertedCoordinate);
        }
    }
    
    public void set_draw_mode(int drawMode)
    {
        assert (drawMode == GLES20.GL_TRIANGLES
                || drawMode == GLES20.GL_LINES
                || drawMode == GLES20.GL_POINTS);
        m_draw_mode = drawMode;
    }
    
    public void set_vertices(final float[] geometry)
    {
        // create a floating point buffer from a ByteBuffer
        m_vertex_buffer = ByteBuffer.allocateDirect(geometry.length * SIZE_OF_FLOAT)
                .order(ByteOrder.nativeOrder()) // use the device hardware's native byte order
                .asFloatBuffer();
        m_vertex_buffer.put(geometry); //add the coordinates to the FloatBuffer
        m_vertex_buffer.position(0); // set the buffer to read the first coordinate
        m_vertex_count = geometry.length / COORDS_PER_VERTEX;
        m_collision_points = new PointF[m_vertex_count];
        update_bounds();
        normalize(); //center the mesh on [0,0,0] and scale to [-1, -1, -1][1, 1, 1]
    }
    
    public void update_bounds()
    {
        float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE, minZ = Float.MAX_VALUE;
        float maxX = -Float.MAX_VALUE, maxY = -Float.MAX_VALUE, maxZ = -Float.MAX_VALUE;
        for (int i = 0; i < m_vertex_count * COORDS_PER_VERTEX; i += COORDS_PER_VERTEX)
        {
            final float x = m_vertex_buffer.get(i + X);
            final float y = m_vertex_buffer.get(i + Y);
            final float z = m_vertex_buffer.get(i + Z);
            minX = Math.min(minX,
                            x);
            minY = Math.min(minY,
                            y);
            minZ = Math.min(minZ,
                            z);
            maxX = Math.max(maxX,
                            x);
            maxY = Math.max(maxY,
                            y);
            maxZ = Math.max(maxZ,
                            z);
        }
        m_min.set(minX,
                  minY,
                  minZ);
        m_max.set(maxX,
                  maxY,
                  maxZ);
        m_width = maxX - minX;
        m_height = maxY - minY;
        m_depth = maxZ - minZ;
        m_radius = Math.max(Math.max(m_width,
                                     m_height),
                            m_depth) * 0.5f;
    }
    
    public float left()
    {
        return m_min._x;
    }
    
    public float right()
    {
        return m_max._x;
    }
    
    public float top()
    {
        return m_min._y;
    }
    
    public float bottom()
    {
        return m_max._y;
    }
    
    public float center_x()
    {
        return m_min._x + (m_width * 0.5f);
    }
    
    public float center_y()
    {
        return m_min._y + (m_height * 0.5f);
    }
    
    //scale mesh to normalized device coordinates [-1.0, 1.0]
    public void normalize()
    {
        final double inverseW = (m_width == 0.0) ? 0.0 : 1 / m_width;
        final double inverseH = (m_height == 0.0) ? 0.0 : 1 / m_height;
        final double inverseD = (m_depth == 0.0) ? 0.0 : 1 / m_depth;
        for (int i = 0; i < m_vertex_count * COORDS_PER_VERTEX; i += COORDS_PER_VERTEX)
        {
            final double dx = m_vertex_buffer.get(i + X) - m_min._x; //"d" for "delta" or "difference"
            final double dy = m_vertex_buffer.get(i + Y) - m_min._y;
            final double dz = m_vertex_buffer.get(i + Z) - m_min._z;
            final double xNorm = 2.0 * (dx * inverseW) - 1.0; //(dx * inverseW) is equivalent to (dx / _width)
            final double yNorm = 2.0 * (dy * inverseH) - 1.0; //but avoids the risk of division-by-zero.
            final double zNorm = 2.0 * (dz * inverseD) - 1.0;
            m_vertex_buffer.put(i + X,
                                (float) xNorm);
            m_vertex_buffer.put(i + Y,
                                (float) yNorm);
            m_vertex_buffer.put(i + Z,
                                (float) zNorm);
        }
        update_bounds();
        Utilities.require(m_width <= 2.0f,
                          "x-axis is out of range!");
        Utilities.require(m_height <= 2.0f,
                          "y-axis is out of range!");
        Utilities.require(m_depth <= 2.0f,
                          "z-axis is out of range!");
        Utilities.expect((m_min._x >= -1.0f && m_max._x <= 1.0f),
                         TAG,
                         "normalized x[" + m_min._x + ", " + m_max._x + "] expected x[-1.0, 1.0]");
        Utilities.expect((m_min._y >= -1.0f && m_max._y <= 1.0f),
                         TAG,
                         "normalized y[" + m_min._y + ", " + m_max._y + "] expected y[-1.0, 1.0]");
        Utilities.expect((m_min._z >= -1.0f && m_max._z <= 1.0f),
                         TAG,
                         "normalized z[" + m_min._z + ", " + m_max._z + "] expected z[-1.0, 1.0]");
    }
    
    public static float[] generate_line_polygon(final int numPoints, final double radius)
    {
        Utilities.require(numPoints > 2,
                          "a polygon requires at least 3 points.");
        final int numVerts = numPoints * 2; //we render lines, and each line requires 2 points
        final float[] verts = new float[numVerts * Mesh.COORDS_PER_VERTEX];
        double step = 2.0 * Math.PI / numPoints;
        int i = 0, point = 0;
        while (point < numPoints)
        { //generate verts on circle, 2 per point
            double theta = point * step;
            verts[i++] = (float) (Math.cos(theta) * radius); //X
            verts[i++] = (float) (Math.sin(theta) * radius); //Y
            verts[i++] = 0f;                                //Z
            point++;
            theta = point * step;
            verts[i++] = (float) (Math.cos(theta) * radius); //X
            verts[i++] = (float) (Math.sin(theta) * radius); //Y
            verts[i++] = 0f;                                 //Z
        }
        return verts;
    }
    
    public void rotate_x(final double theta)
    {
        rotate(X,
               theta);
    }
    
    public void rotate_y(final double theta)
    {
        rotate(Y,
               theta);
    }
    
    public void rotate_z(final double theta)
    {
        rotate(Z,
               theta);
    }
    
    private void rotate(final int axis, final double theta)
    {
        Utilities.require(axis == X || axis == Y || axis == Z);
        final double sinTheta = Math.sin(theta);
        final double cosTheta = Math.cos(theta);
        for (int i = 0; i < m_vertex_count * COORDS_PER_VERTEX; i += COORDS_PER_VERTEX)
        {
            final double x = m_vertex_buffer.get(i + X);
            final double y = m_vertex_buffer.get(i + Y);
            final double z = m_vertex_buffer.get(i + Z);
            if (axis == Z)
            {
                m_vertex_buffer.put(i + X,
                                    (float) (x * cosTheta - y * sinTheta));
                m_vertex_buffer.put(i + Y,
                                    (float) (y * cosTheta + x * sinTheta));
            } else if (axis == Y)
            {
                m_vertex_buffer.put(i + X,
                                    (float) (x * cosTheta - z * sinTheta));
                m_vertex_buffer.put(i + Z,
                                    (float) (z * cosTheta + x * sinTheta));
            } else if (axis == X)
            {
                m_vertex_buffer.put(i + Y,
                                    (float) (y * cosTheta - z * sinTheta));
                m_vertex_buffer.put(i + Z,
                                    (float) (z * cosTheta + y * sinTheta));
            }
        }
        update_bounds();
    }
    
    public PointF[] get_point_list(final float p_offset_x, final float p_offset_y,
                                   final float p_facing_angle_degrees)
    {
        final double sinTheta = Math.sin(p_facing_angle_degrees * Utilities.TO_RAD);
        final double cosTheta = Math.cos(p_facing_angle_degrees * Utilities.TO_RAD);
        float[] verts = new float[m_vertex_count * COORDS_PER_VERTEX];
        m_vertex_buffer.position(0);
        m_vertex_buffer.get(verts);
        m_vertex_buffer.position(0);
        int index = 0;
        for (int i = 0; i < m_vertex_count * COORDS_PER_VERTEX; i += COORDS_PER_VERTEX)
        {
            final float x = verts[i + X];
            final float y = verts[i + Y];
            final float rotatedX = (float) (x * cosTheta - y * sinTheta) + p_offset_x;
            final float rotatedY = (float) (y * cosTheta + x * sinTheta) + p_offset_y;
            //final float z = verts[i + Z];
            m_collision_points[index++] = new PointF(rotatedX,
                                                     rotatedY);  //TODO: DANGER! We're creating new objects, make a pool instead!
        }
        return m_collision_points;
    }
    
    
}