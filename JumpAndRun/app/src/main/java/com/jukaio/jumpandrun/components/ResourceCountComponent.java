package com.jukaio.jumpandrun.components;

import android.graphics.Paint;

import com.jukaio.jumpandrun.Entity;
import com.jukaio.jumpandrun.EntityType;
import com.jukaio.jumpandrun.HUD;
import com.jukaio.jumpandrun.Jukebox;
import com.jukaio.jumpandrun.SoundID;
import com.jukaio.jumpandrun.Viewport;
import com.jukaio.jumpandrun.World;
import com.jukaio.jumpandrun.XML;

import org.w3c.dom.Element;

public class CoinCountComponent extends Component
{
    private World                   m_world     = null;
    private HUD.TextElement         m_display   = null;
    private int                     m_count     = 0;
    private String                  m_base_text = "";
    
    public int get_count()
    {
        return m_count;
    }
    
    public CoinCountComponent(Entity p_entity, World p_world, Element p_data)
    {
        super(p_entity);
        m_world = p_world;
        
        m_base_text = p_data.getAttribute("base_text");
        m_display = new HUD.TextElement(m_base_text,
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
        m_count = 0;
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
    public void render(Viewport p_viewport, Paint p_paint)
    {
    
    }
    
    @Override
    protected void destroy()
    {
        m_world = null;
        m_display = null;
    }
    
    @Override
    public void on_collision(Entity p_other)
    {
        switch (p_other.get_type())
        {
            case COLLECT:
                m_count--;
                if(m_count <= 0)
                {
                    get_entity().set_position(0, 0);
                }
                Jukebox.play(SoundID.GET_COIN, Jukebox.MAX_VOLUME, 0);
                break;
        }
    }
}
