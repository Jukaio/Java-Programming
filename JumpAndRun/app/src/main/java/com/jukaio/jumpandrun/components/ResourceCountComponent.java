package com.jukaio.jumpandrun.components;

import android.graphics.Paint;

import com.jukaio.jumpandrun.entity.Entity;
import com.jukaio.jumpandrun.entity.EntityType;
import com.jukaio.jumpandrun.HUD;
import com.jukaio.jumpandrun.Jukebox;
import com.jukaio.jumpandrun.SoundID;
import com.jukaio.jumpandrun.Viewport;
import com.jukaio.jumpandrun.world.World;
import com.jukaio.jumpandrun.XML;

import org.w3c.dom.Element;

public class ResourceCountComponent extends Component
{
    private World                   m_world             = null;
    private HUD.TextElement         m_display_coins     = null;
    private HUD.TextElement         m_display_health    = null;
    private int                     m_count             = 0;
    private String                  m_base_text_coins   = "";
    private String                  m_base_text_health  = "";
    
    private static int              m_max_lives         = 0;
    private static int              m_current_lives     = 0;
    
    public int get_count()
    {
        return m_count;
    }
    public int get_current_lives() { return m_current_lives; }
    
    public void reset_health()
    {
        m_current_lives = m_max_lives;
    }
    
    public ResourceCountComponent(Entity p_entity, World p_world, Element p_data)
    {
        super(p_entity);
        m_world = p_world;
        m_max_lives = XML.parse_int(p_data, "max_lives");
        m_base_text_coins = p_data.getAttribute("base_text_coins");
        m_base_text_health = p_data.getAttribute("base_text_health");
        float pos_x = XML.parse_float(p_data, "x");
        float pos_y = XML.parse_float(p_data, "y");
        float size = XML.parse_float(p_data, "size");
        Paint.Align align = Paint.Align.valueOf(p_data.getAttribute("align"));
        m_display_coins = new HUD.TextElement(m_base_text_coins,
                                              pos_x,
                                              pos_y,
                                              (int)size,
                                              align);
        m_display_health = new HUD.TextElement(m_base_text_health,
                                               pos_x,
                                               pos_y + size,
                                               (int)size,
                                               align);
        m_world.m_hud.add(m_display_coins);
        m_world.m_hud.add(m_display_health);
        m_current_lives = m_max_lives;
    }
    
    @Override
    public ComponentType get_type()
    {
        return ComponentType.RESOURCE_COUNT;
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
        m_display_coins.set_text(m_base_text_coins + Integer.toString(m_count));
    }
    
    @Override
    public void pre_update(float p_dt)
    {

    }
    
    @Override
    public void update(float p_dt)
    {
        m_display_coins.set_text(m_base_text_coins + Integer.toString(m_count));
        m_display_health.set_text(m_base_text_health + Integer.toString(m_current_lives));
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
        m_display_coins = null;
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
                
            case LETHAL:
                m_current_lives--;
                break;
        }
    }
}
