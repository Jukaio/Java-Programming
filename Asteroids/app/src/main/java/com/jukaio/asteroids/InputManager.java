package com.jukaio.asteroids;

public class InputManager {
    public float m_horizontal_factor = 0.0f;
    
    public final int CURRENT = 0;
    public final int PREVIOUS = 1;
    
    protected boolean[] m_pressing_a = { false, false }; // 0 == Current; 1 == Previous
    protected boolean[] m_pressing_b = { false, false }; // 0 == Current; 1 == Previous

    public boolean released_A()
    {
        return !m_pressing_a[CURRENT];
    }
    public boolean released_B()
    {
        return !m_pressing_b[CURRENT];
    }
    public boolean pressed_A()
    {
        return m_pressing_a[CURRENT];
    }
    public boolean pressed_B()
    {
        return m_pressing_b[CURRENT];
    }
    
    public boolean just_released_A()
    {
        return !m_pressing_a[CURRENT] && m_pressing_a[PREVIOUS];
    }
    
    public boolean just_released_B()
    {
        return !m_pressing_b[CURRENT] && m_pressing_b[PREVIOUS];
    }
    
    public boolean just_pressed_A()
    {
        return m_pressing_a[CURRENT] && !m_pressing_a[PREVIOUS];
    }
    
    public boolean just_pressed_B()
    {
        return m_pressing_b[CURRENT] && !m_pressing_b[PREVIOUS];
    }

    public final void update()
    {
        m_pressing_a[PREVIOUS] = m_pressing_a[CURRENT];
        m_pressing_b[PREVIOUS] = m_pressing_b[CURRENT];
    }
    public void onStart() {};
    public void onStop() {};
    public void onPause() {};
    public void onResume() {};
}