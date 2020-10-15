package com.jukaio.jumpandrun.ecs.componentmodule.components;

public class InputController extends Component
{
    
    @Override
    public ComponentType get_type()
    {
        return ComponentType.INPUT_CONTROLLER;
    }
    
    public float m_horizontal = 0.0f;
    public float m_vertical = 0.0f;
    public int m_button_mask = 0;
}
