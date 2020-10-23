package com.jukaio.jumpandrun;

import java.util.ArrayList;

public class InputManager {
    private static ArrayList<InputDevice> m_devices = new ArrayList<>();

    public void onStart()
    {
        for(InputDevice d : m_devices)
            d.on_start();
    }
    public void onStop()
    {
        for(InputDevice d : m_devices)
            d.on_stop();
    }
    public void onPause()
    {
        for(InputDevice d : m_devices)
            d.on_pause();
    }
    public void onResume()
    {
        for(InputDevice d : m_devices)
            d.on_resume();
    }
    public void on_destroy()
    {
        for(InputDevice d : m_devices)
            d.on_destroy();
    }
    
    public void add_device(InputDevice p_device)
    {
        m_devices.add(p_device);
    }
    
    public static float get_horizontal()
    {
        float to_return = 0;
        for(InputDevice d : m_devices)
            to_return += d.get_horizontal();
        if(to_return > 0)
            return 1;
        else if(to_return < 0)
            return -1;
        return 0;
    }
    
    public static boolean is_jump()
    {
        boolean to_return = false;
        for(InputDevice d : m_devices)
            to_return |= d.get_jump();
        return to_return;
    }
}