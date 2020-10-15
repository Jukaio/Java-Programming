package com.jukaio.jumpandrun.inputhandling;

public abstract class InputManager {
    public float m_vertical = 0.0f;
    public float m_horizontal = 0.0f;
    public boolean m_jump = false;

    public abstract void onStart();
    public abstract void onStop();
    public abstract void onPause();
    public abstract void onResume();
}