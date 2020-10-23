package com.jukaio.jumpandrun;

public abstract class InputDevice
{
    public abstract void on_start();
    public abstract void on_stop();
    public abstract void on_pause();
    public abstract void on_resume();
    public abstract void on_destroy();
    
    public abstract float get_horizontal();
    public abstract boolean get_jump();
}
