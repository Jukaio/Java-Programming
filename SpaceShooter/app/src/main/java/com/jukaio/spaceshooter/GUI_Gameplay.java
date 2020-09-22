package com.jukaio.spaceshooter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.jukaio.spaceshooter.design.Config;

public class GUI_Gameplay extends  GUI
{
    // No need to extract these string since they not "translatable" or "language dependent"
    // Those strings consist of integers
    private String m_score_text;
    private String m_to_draw;

    public GUI_Gameplay(boolean p_active)
    {
        super(p_active, Game_State.GAMEPLAY);
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
    public void destroy()
    {
        super.destroy();
        m_score_text = null;
        m_to_draw = null;
    }
    
    @Override
    public void reset()
    {
    
    }
    
    @Override
    public void update(float p_dt)
    {
        String string = m_game.getResources().getString(R.string.zero_filling);
        m_score_text = Integer.toString(m_game.m_score);
        m_to_draw = string.substring(0, string.length() - m_score_text.length());
    }
    
    @Override
    public void render(Canvas p_canvas, Paint p_paint)
    {
        p_paint.setColor(Color.WHITE);
        p_paint.setTextAlign(Paint.Align.LEFT);
        p_paint.setTextSize(Config.GUI_GAMEPLAY_TESXT_SIZE);
        
        p_canvas.drawText(m_to_draw + m_score_text,
                          Config.GUI_GAMEPLAY_TEXT_PADDING_LEFT,
                          Config.GUI_GAMEPLAY_TESXT_SIZE + Config.GUI_GAMEPLAY_TEXT_PADDING_TOP,
                          p_paint);
    }
}
