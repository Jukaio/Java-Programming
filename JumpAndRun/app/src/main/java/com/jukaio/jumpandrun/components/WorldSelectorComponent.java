package com.jukaio.jumpandrun.components;

import android.graphics.Paint;

import com.jukaio.jumpandrun.entity.Entity;
import com.jukaio.jumpandrun.MusicPlayer;
import com.jukaio.jumpandrun.Viewport;
import com.jukaio.jumpandrun.world.WorldManager;

public class WorldSelectorComponent extends Component
{
    private WorldManager            m_world_manager = null;
    private ResourceCountComponent  m_counter       = null;
    
    public WorldSelectorComponent(Entity p_entity, WorldManager p_world_manager)
    {
        super(p_entity);
        m_world_manager = p_world_manager;
    }
    
    @Override
    public ComponentType get_type()
    {
        return ComponentType.WORLD_SELECTOR;
    }
    
    @Override
    public void start()
    {
        m_counter = get_entity().get_component(ComponentType.RESOURCE_COUNT);
    }
    
    @Override
    public void pre_update(float p_dt)
    {
    
    }
    
    @Override
    public void update(float p_dt)
    {
        if(m_counter.get_current_lives() <= 0)
        {
            m_counter.reset_health();
            m_world_manager.set_current_world(0);
            m_world_manager.start();
            MusicPlayer.stop();
            MusicPlayer.set_track(0); // security if not enough music is loaded
            MusicPlayer.play();
        }
    
        if(m_counter.get_count() <= 0)
        {
            int current = m_world_manager.get_current_world_index();
            int count = m_world_manager.get_world_count();
            int next = (current + 1) % count;
            
            m_world_manager.set_current_world(next);
            m_world_manager.start();
    
            MusicPlayer.stop();
            MusicPlayer.set_track(next % MusicPlayer.get_track_count()); // security if not enough music is loaded
            MusicPlayer.play();
        }
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
        m_world_manager = null;
        m_counter       = null;
    }
}
