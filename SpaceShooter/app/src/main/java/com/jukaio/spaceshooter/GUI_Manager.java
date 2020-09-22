package com.jukaio.spaceshooter;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.jukaio.spaceshooter.entities.Entity;

import java.util.ArrayList;

public class GUI_Manager extends Entity
{
    ArrayList<GUI> m_guis = null;
    GUI m_active_gui = null;
    private Game_State m_previous_state = null;
    
    public GUI_Manager()
    {
        super(true);
        m_guis = new ArrayList<GUI>();
    }
    
    public void add_GUI(GUI p_gui)
    {
        m_guis.add(p_gui);
    }
    
    public boolean set_GUI(Game_State p_type)
    {
        for (GUI gui : m_guis)
        {
            if(gui.m_type == p_type)
            {
                m_active_gui = gui;
                return true;
            }
        }
        return false;
    }
    
    @Override
    protected void on_enable()
    {
    
    }
    
    @Override
    protected void on_disable()
    {
    
    }
    
    @Override
    public void update(float p_dt)
    {
        if(m_previous_state != m_game.m_game_state)
            set_GUI(m_game.m_game_state);
        m_active_gui.update(p_dt);
        m_previous_state = m_game.m_game_state;
    }
    
    @Override
    public void render(Canvas p_canvas, Paint p_paint)
    {
        m_active_gui.render(p_canvas, p_paint);
    }
    
    @Override
    public void destroy()
    {
        super.destroy();
        for(GUI g : m_guis)
            g.destroy();
        m_guis.clear();
        m_active_gui = null;
        m_previous_state = null;
        m_guis = null;
    }
    
    @Override
    public void reset()
    {
        for (GUI g : m_guis)
        {
            g.reset();
        }
    }
}
