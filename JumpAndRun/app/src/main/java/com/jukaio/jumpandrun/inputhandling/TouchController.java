package com.jukaio.jumpandrun.inputhandling;

import android.view.MotionEvent;
import android.view.View;

import com.jukaio.jumpandrun.R;

public class TouchController extends InputManager implements View.OnTouchListener
{
    public TouchController(View p_view)
    {
        p_view.findViewById(R.id.keypad_up).setOnTouchListener(this);
        p_view.findViewById(R.id.keypad_down).setOnTouchListener(this);
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
            case R.id.keypad_up:
            m_vertical = (p_is_action_down) ? m_vertical - 1 : m_vertical + 1;
            break;
            
            case R.id.keypad_down:
            m_vertical = (p_is_action_down) ? m_vertical + 1 : m_vertical - 1;
            break;
            
            case R.id.keypad_left:
            m_horizontal = (p_is_action_down) ? m_horizontal - 1 : m_horizontal + 1;
            break;
            
            case R.id.keypad_right:
            m_horizontal = (p_is_action_down) ? m_horizontal + 1 : m_horizontal - 1;
            break;
            
            case R.id.keypad_jump:
            m_jump = p_is_action_down;
            break;
        }
    }
    
    @Override
    public void onStart()
    {
    
    }
    
    @Override
    public void onStop()
    {
    
    }
    
    @Override
    public void onPause()
    {
    
    }
    
    @Override
    public void onResume()
    {
    
    }
}
