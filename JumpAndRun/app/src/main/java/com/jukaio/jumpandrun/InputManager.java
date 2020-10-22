package com.jukaio.jumpandrun;

public abstract class InputManager {
    private static float m_vertical = 0.0f;
    private static float m_horizontal = 0.0f;
    private static boolean m_jump = false;

    public abstract void onStart();
    public abstract void onStop();
    public abstract void onPause();
    public abstract void onResume();
    
    public static float get_vertical()
    {
        return m_vertical;
    }
    
    protected void set_vertical(float p_vertical)
    {
        m_vertical = p_vertical;
    }
    
    public static float get_horizontal()
    {
        return m_horizontal;
    }
    
    protected void set_horizontal(float p_horizontal)
    {
        m_horizontal = p_horizontal;
    }
    
    public static boolean is_jump()
    {
        return m_jump;
    }
    
    protected void set_jump(boolean p_jump)
    {
        m_jump = p_jump;
    }
}