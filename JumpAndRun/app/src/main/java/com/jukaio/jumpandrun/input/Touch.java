package com.jukaio.jumpandrun;

import android.view.MotionEvent;

public class Touch extends InputDevice
{
    Game m_game = null;

    boolean m_move_left = false;
    boolean m_move_right = false;
    boolean m_jump = false;

    public void set_game(Game p_game)
    {
        m_game = p_game;
    }

    @Override
    public void on_start()
    {
    
    }
    
    @Override
    public void on_stop()
    {
    
    }
    
    @Override
    public void on_pause()
    {
    
    }
    
    @Override
    public void on_resume()
    {
    
    }
    
    @Override
    public void on_destroy()
    {
    
    }
    
    @Override
    public float get_horizontal()
    {
        if(m_move_left && !m_move_right)
            return -1;
        else if(m_move_right && !m_move_left)
            return 1;
        return 0;
    }
    
    @Override
    public boolean get_jump()
    {
        return m_jump;
    }
    
    public boolean onTouchEvent(MotionEvent event)
    {
        int pointer_index = event.getActionIndex();
        float point_x = event.getX(pointer_index);
        float point_y = event.getY(pointer_index);
        
        switch (event.getActionMasked())
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                set_inputs(point_x,
                           point_y,
                           true);
                break;
    
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_CANCEL:
                m_move_left = false;
                m_move_right = false;
                m_jump = false;
                for (int i = 0; i < event.getPointerCount(); i++)
                {
                    point_x = event.getX(i);
                    point_y = event.getY(i);
                    set_inputs(point_x,
                               point_y,
                               true);
                }
                break;
    
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                set_inputs(point_x,
                           point_y,
                           false);
                break;
        }
        return true;
    }
    
    private void set_inputs(float p_point_x, float p_point_y,
                                   boolean p_set_to)
    {
        if (p_point_y > (m_game.m_height * m_game.m_zoom) * 0.25f)
        {
            if (p_point_x > (m_game.m_width * m_game.m_zoom) * 0.5f)
                m_move_right = p_set_to;
            else
                m_move_left = p_set_to;
        } else
            m_jump = p_set_to;
    }
}
