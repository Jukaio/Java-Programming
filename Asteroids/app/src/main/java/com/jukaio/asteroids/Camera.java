package com.jukaio.asteroids;

import android.graphics.Point;
import android.graphics.PointF;
import android.opengl.Matrix;

public class Camera
{
    //setup a projection matrix by passing in the range of the game world that will be mapped by OpenGL to the screen.
    static final int m_offset = 0;
    static final float m_left = 0;
    static float m_right = 0;
    static float m_bottom = 0;
    static final float m_top = 0;
    static final float m_near = 0f;
    static final float m_far = 1f;


    private final PointF m_look_at = new PointF(0f, 0f);
    private int m_pixels_per_meter_x; //viewport "density"
    private int m_pixels_per_meter_y;
    private int m_screen_width; //resolution
    private int m_screen_height;
    private int m_screen_center_x; //center screen
    private int m_screen_center_y;
    private final static float BUFFER = 2f; //overdraw, to avoid visual gaps

    public Camera(final int p_screen_width, final int p_screen_height, final float p_meters_to_show_x, final float p_meters_to_show_y) {
        m_screen_width = p_screen_width;
        m_screen_height = p_screen_height;
        m_screen_center_y = m_screen_width / 2;
        m_screen_center_x = m_screen_height / 2;
        m_look_at.x = 0.0f;
        m_look_at.y = 0.0f;
        setMetersToShow(p_meters_to_show_x, p_meters_to_show_y);
    }

    private void setMetersToShow(float p_meters_to_show_x, float p_meters_to_show_y) {
        if (p_meters_to_show_x <= 0f && p_meters_to_show_y <= 0f)
            throw new IllegalArgumentException("One of the dimensions must be provided!");
        m_right = p_meters_to_show_x;
        m_bottom = p_meters_to_show_y;
        if (p_meters_to_show_x == 0f || p_meters_to_show_y == 0f) {
            if (p_meters_to_show_y > 0f) {
                m_right = ((float) m_screen_width / m_screen_height) * p_meters_to_show_y;
            } else {
                m_bottom = ((float) m_screen_height / m_screen_width) * p_meters_to_show_x;
            }
        }
        m_pixels_per_meter_x = (int) (m_screen_width / m_right);
        m_pixels_per_meter_y = (int) (m_screen_height / m_bottom);
    }

    public void lookAt(final float x, final float y) {
            m_look_at.x = x;
            m_look_at.y = y;
    }

    public void lookAt(final GLEntity obj)
    {
        lookAt(obj.center_x(), obj.center_y());
    }

    public void lookAt(final PointF pos)
    {
        lookAt(pos.x, pos.y);
    }

    public void worldToScreen(final float worldPosX, final float worldPosY, Point screenPos) {
        screenPos.x = (int) (m_screen_center_y - ((m_look_at.x - worldPosX) * m_pixels_per_meter_x));
        screenPos.y = (int) (m_screen_center_x - ((m_look_at.y - worldPosY) * m_pixels_per_meter_y));
    }

    public void worldToScreen(final PointF worldPos, Point screenPos) {
        worldToScreen(worldPos.x, worldPos.y, screenPos);
    }


    public void worldToScreen(final GLEntity e, final Point screenPos) {
        worldToScreen(e.get_position().m_x, e.get_position().m_y, screenPos);
    }

    public void get_projection_matrix(float[] out_viewport_matrix)
    {
        Matrix.orthoM(out_viewport_matrix,
                      m_offset,
                      m_left + m_look_at.x,
                      m_right + m_look_at.x,
                      m_bottom + m_look_at.y,
                      m_top + m_look_at.y,
                      m_near,
                      m_far);
    }
}
