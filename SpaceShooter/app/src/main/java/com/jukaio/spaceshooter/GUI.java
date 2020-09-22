package com.jukaio.spaceshooter;

import com.jukaio.spaceshooter.entities.Entity;

public abstract class GUI extends Entity
{
    public Game_State m_type;
    
    public GUI(boolean p_active, Game_State p_type)
    {
        super(p_active);
        m_type = p_type;
    }
    
    @Override
    public void destroy()
    {
        super.destroy();
        m_type = null;
    }
}
