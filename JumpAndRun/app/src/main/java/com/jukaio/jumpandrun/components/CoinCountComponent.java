package com.jukaio.jumpandrun.components;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.jukaio.jumpandrun.Entity;
import com.jukaio.jumpandrun.EntityType;
import com.jukaio.jumpandrun.Jukebox;
import com.jukaio.jumpandrun.SoundID;
import com.jukaio.jumpandrun.World;
import com.jukaio.jumpandrun.XML;

import org.w3c.dom.Element;

public class CoinCountComponent extends Component
{
    public int m_count = 0;
    public World m_world = null;
    private World.HUD.TextElement m_display;
    private String m_base_text = "";
    
    public CoinCountComponent(Entity p_entity, World p_world, Element p_data)
    {
        super(p_entity);
        m_world = p_world;
        
        m_base_text = p_data.getAttribute("base_text");
        m_display = new World.HUD.TextElement(m_base_text,
                                              XML.parse_float(p_data, "x"),
                                              XML.parse_float(p_data, "y"),
                                              XML.parse_int(p_data, "size"),
                                              Paint.Align.valueOf(p_data.getAttribute("align")));
        
        m_world.m_hud.add(m_display);
    }
    
    @Override
    public ComponentType get_type()
    {
        return ComponentType.COIN_COUNT;
    }
    
    @Override
    public void start()
    {
        for(Entity e : m_world.m_entities)
        {
            if(e.get_type() == EntityType.COLLECT)
                m_count++;
        }
        m_display.set_text(m_base_text + Integer.toString(m_count));
    }
    
    @Override
    public void pre_update(float p_dt)
    {
    
    }
    
    @Override
    public void update(float p_dt)
    {
        m_display.set_text(m_base_text + Integer.toString(m_count));
    }
    
    @Override
    public void late_update(float p_dt)
    {
    
    }
    
    @Override
    public void render(Canvas p_canvas, Paint p_paint)
    {
    
    }
    
    @Override
    public void on_collision(Entity p_other)
    {
        switch (p_other.get_type())
        {
            case COLLECT:
                m_count--;
                Jukebox.play(SoundID.LEVEL_ONE, Jukebox.MAX_VOLUME, 0);
                break;
        }
    }
}
