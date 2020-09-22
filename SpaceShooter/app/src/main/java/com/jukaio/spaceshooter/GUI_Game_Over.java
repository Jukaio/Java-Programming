package com.jukaio.spaceshooter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.jukaio.spaceshooter.design.Config;

public class GUI_Game_Over extends GUI
{
    public GUI_Game_Over(boolean p_active)
    {
        super(p_active, Game_State.GAME_OVER);
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
    }
    
    @Override
    public void reset()
    {
    
    }
    
    @Override
    public void render(Canvas p_canvas, Paint p_paint)
    {
        p_paint.setColor(Color.WHITE);
 
        p_paint.setTextSize(Config.GUI_GAME_OVER_UPPER_TEXT);
        p_paint.setTextAlign(Paint.Align.CENTER);
        p_canvas.drawText(m_game.getContext().getString(R.string.game_over_GUI),
                          m_game.m_window_size.right * 0.5f,
                          (m_game.m_window_size.bottom * 0.5f),
                          p_paint);
                          
        p_paint.setTextSize(Config.GUI_GAME_OVER_LOWER_TEXT);
        p_paint.setTextAlign(Paint.Align.CENTER);
        p_canvas.drawText(m_game.getContext().getString(R.string.press_start_GUI),
                          m_game.m_window_size.right * 0.5f,
                          (m_game.m_window_size.bottom * 0.5f) + Config.GUI_GAME_OVER_LOWER_TEXT,
                          p_paint);
                          
        p_paint.setTextSize(Config.GUI_GAME_OVER_SCORE_TEXT);
        p_paint.setTextAlign(Paint.Align.CENTER);
        p_canvas.drawText(Integer.toString(m_game.m_score),
                          m_game.m_window_size.right * 0.5f,
                          (m_game.m_window_size.bottom * 0.5f) +
                          Config.GUI_GAME_OVER_LOWER_TEXT +
                          Config.GUI_GAME_OVER_SCORE_TEXT,
                          p_paint);
    
    }
}
