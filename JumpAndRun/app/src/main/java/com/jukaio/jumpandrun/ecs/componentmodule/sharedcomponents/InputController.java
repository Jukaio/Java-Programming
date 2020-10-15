package com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents;

import com.jukaio.jumpandrun.ecs.componentmodule.components.Component;
import com.jukaio.jumpandrun.ecs.componentmodule.components.ComponentType;

public class InputController extends SharedComponent
{
    
    @Override
    public SharedComponentType get_type()
    {
        return SharedComponentType.INPUT_CONTROLLER;
    }
    
    public float m_horizontal = 0.0f;
    public float m_vertical = 0.0f;
    public boolean m_jump = false;
    
    public float m_prev_horizontal = 0.0f;
    public float m_prev_vertical = 0.0f;
    public boolean m_prev_jump = false;
}
