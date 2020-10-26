package com.jukaio.jumpandrun;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class Gamepad extends InputDevice
{
    boolean m_pad_left = false;
    boolean m_pad_right = false;
    boolean m_jump = false;
    
    MainActivity m_activity = null;
    
    public Gamepad()
    {
    
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
        if(m_pad_left && !m_pad_right)
            return -1;
        else if(m_pad_right && !m_pad_left)
            return 1;
        return 0;
    }
    
    @Override
    public boolean get_jump()
    {
        return m_jump;
    }
    
    public boolean onKeyDown(int p_keycode, KeyEvent p_keyEvent)
    {
        switch (p_keycode)
        {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                m_pad_left = true;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                m_pad_right = true;
                break;
                
            case KeyEvent.KEYCODE_BUTTON_A:
                m_jump = true;
                break;
        }
        return false;
    }
    
    
    public boolean onKeyUp(int p_keycode, KeyEvent p_keyEvent)
    {
        switch (p_keycode)
        {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                m_pad_left = false;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                m_pad_right = false;
                break;
                
            case KeyEvent.KEYCODE_BUTTON_A:
                m_jump = false;
                break;
        }
        return false;
    }


}
