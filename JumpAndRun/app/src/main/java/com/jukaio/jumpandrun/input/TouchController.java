package com.jukaio.jumpandrun.input;

import android.view.MotionEvent;
import android.view.View;

import com.jukaio.jumpandrun.R;

public class TouchController extends InputDevice implements View.OnTouchListener
{
    private static float m_horizontal = 0.0f;
    private static boolean m_jump = false;
    
    public TouchController(View p_view)
    {
        p_view.findViewById(R.id.keypad_left).setOnTouchListener(this);
        p_view.findViewById(R.id.keypad_right).setOnTouchListener(this);
        p_view.findViewById(R.id.keypad_jump).setOnTouchListener(this);
    }
    
    @Override
    public boolean onTouch(View p_view, final MotionEvent p_event)
    {
        int action = p_event.getActionMasked();
        int id = p_view.getId();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
            change(id, true);
            break;
            case MotionEvent.ACTION_UP:
            change(id, false);
            break;
        }
        return false;
    }
    
    
    private void change(int p_id, boolean p_is_action_down)
    {
        // User started pressing a key
        switch (p_id)
        {
            case R.id.keypad_left:
                m_horizontal = ((p_is_action_down) ? m_horizontal - 1 :m_horizontal + 1);
            break;
            case R.id.keypad_right:
                m_horizontal = ((p_is_action_down) ? m_horizontal + 1 : m_horizontal - 1);
            break;
            
            case R.id.keypad_jump:
                m_jump = p_is_action_down;
            break;
        }
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
        return m_horizontal;
    }
    
    @Override
    public boolean get_jump()
    {
        return m_jump;
    }
}
