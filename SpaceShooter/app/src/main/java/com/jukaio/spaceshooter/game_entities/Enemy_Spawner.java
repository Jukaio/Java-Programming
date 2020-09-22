package com.jukaio.spaceshooter.game_entities;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.jukaio.spaceshooter.Game;
import com.jukaio.spaceshooter.Grid;
import com.jukaio.spaceshooter.Vector2;
import com.jukaio.spaceshooter.design.Config;
import com.jukaio.spaceshooter.entities.Entity;

import java.util.ArrayList;

public class Enemy_Spawner extends Entity
{
    //m_enemies
    ArrayList<ArrayList<Enemy>> m_pools = null;
    ArrayList<Integer> m_pool_indexer = null;
    
    ArrayList<Integer> m_open_first_row_indices = null;
    private Grid m_grid = null;
    
    float m_spawn_timer = Config.SPAWNER_SPAWN_RATE;
    float m_move_timer = Config.SPANWER_MOVE_RATE;
    
    // Switch for what to spawn
    int spawn = 1;
    int invert = 1;
    
    public void update_grid(Vector2 p_position, boolean p_value)
    {
        m_grid.set_unit_at_from_world(p_position, p_value);
    }
    
    public Enemy_Spawner(boolean p_active)
    {
        super(p_active);
        m_pools = new ArrayList<ArrayList<Enemy>>();
        m_pool_indexer = new ArrayList<Integer>();
        
        m_open_first_row_indices = new ArrayList<Integer>();
        
        int count_x = m_game.m_window_size.right / Config.GRID_CELL_SIZE;
        int count_y = m_game.m_window_size.bottom / Config.GRID_CELL_SIZE;
        
        
        m_grid = new Grid(count_x,
                          count_y,
                          Config.GRID_CELL_SIZE,
                          Config.GRID_CELL_SIZE,
                          new Vector2((m_game.m_window_size.right % Config.GRID_CELL_SIZE) / 2,
                                      (m_game.m_window_size.bottom % Config.GRID_CELL_SIZE) / 2));
        m_game = m_game;
    }
    
    
    Enemy entity_factory(Enemy p_template)
    {
        switch (p_template.get_type())
        {
            case UNKNOWN:
                return null;
            case UFO:
                return new Ufo((Ufo) p_template);
            case HUNTER:
                return new Hunter((Hunter) p_template);
        }
        return null;
    }
    
    final public void add_pool(Enemy p_template, int p_count)
    {
        int pool_index = m_pools.size();
        m_pools.add(new ArrayList<Enemy>());
        m_pool_indexer.add(0);
        for (int i = 0; i < p_count; i++)
        {
            m_pools.get(pool_index).add(entity_factory(p_template));
        }
    }
    
    @Override
    protected void on_enable()
    {
    
    }
    
    @Override
    public void update(float p_dt)
    {
        for (int pool = 0; pool < m_pools.size(); pool++)
        {
            for (int unit = 0; unit < m_pools.get(pool).size(); unit++)
            {
                m_pools.get(pool).get(unit).update(p_dt);
            }
        }
        
        m_open_first_row_indices = get_open_indices(m_open_first_row_indices, m_grid);
        if(m_open_first_row_indices.size() > 0 && m_spawn_timer < 0)
        {
            m_spawn_timer = Config.SPAWNER_SPAWN_RATE;
            int index_x =  m_open_first_row_indices.get(m_game.m_rand.nextInt(m_open_first_row_indices.size()));
            spawn_unit_at(spawn, index_x, 0, new Vector2(0, -Config.GRID_CELL_SIZE));
            spawn = spawn ^ invert;
        }
        
        if(m_move_timer < 0)
        {
            Enemy temp = null;
            for (int pool = 0; pool < m_pools.size(); pool++)
            {
                for (int unit = 0; unit < m_pools.get(pool).size(); unit++)
                {
                    temp = m_pools.get(pool).get(unit);
                    if (temp.get_active() &&
                        !temp.m_moving &&
                        m_grid.world_to_grid_y(temp.get_position().m_y) < m_grid.get_grid_height() - 1 &&
                        !m_grid.has_unit_at(m_grid.world_to_grid_x(temp.get_position().m_x),
                                           m_grid.world_to_grid_y(temp.get_position().m_y) + 1))
                    {
                        int x = m_grid.world_to_grid_x(temp.get_position().m_x);
                        int y = m_grid.world_to_grid_y(temp.get_position().m_y);
                        
                        m_grid.set_unit_at(x, y, false);
                        m_pools.get(pool).get(unit).move_to(m_grid.grid_to_world(x, y + 1));
                        m_grid.set_unit_at(x, y + 1, true);
                    }
                }
            }
            m_move_timer = Config.SPANWER_MOVE_RATE;
        }
        
        m_spawn_timer -= p_dt;
        m_move_timer -= p_dt;
    }
    
    void spawn_unit_at(int p_pool_index, int x, int y, Vector2 p_offset)
    {
        m_grid.set_unit_at(x, y, true);
        Vector2 position = m_grid.grid_to_world(x, y);
        
        Enemy temp = get_inactive_unit(p_pool_index);
        if(temp != null)
        {
            Vector2 spawn = new Vector2(position.m_x + p_offset.m_x,
                                        position.m_y + p_offset.m_y);
    
            temp.set_position(spawn);
            temp.move_to(position);
            temp.set_active(true);
        }
    }
    
    Enemy get_inactive_unit(int p_pool_index)
    {
        Enemy to_return = null;
        if(!m_pools.get(p_pool_index).get(m_pool_indexer.get(p_pool_index)).get_active())
        {
            to_return = m_pools.get(p_pool_index).get(m_pool_indexer.get(p_pool_index));
            m_pool_indexer.set(p_pool_index, m_pool_indexer.get(p_pool_index) + 1);
            if(m_pool_indexer.get(p_pool_index) >= m_pools.get(p_pool_index).size())
                m_pool_indexer.set(p_pool_index, 0);
        }
        return to_return;
    }
    
    static ArrayList<Integer> get_open_indices(ArrayList<Integer> p_container, Grid p_grid)
    {
        p_container.clear();
        for(int i = 0; i < p_grid.get_grid_width(); i++)
        {
            if(!p_grid.has_unit_at(i, 0))
            {
                p_container.add(i);
            }
        }
        return p_container;
    }
    
    @Override
    public void render(Canvas p_canvas, Paint p_paint)
    {
        for (int pool = 0; pool < m_pools.size(); pool++)
        {
            for (int unit = 0; unit < m_pools.get(pool).size(); unit++)
            {
                m_pools.get(pool).get(unit).render(p_canvas,
                                                       p_paint);
            }
        }
    }
    
    @Override
    public void on_collision(Entity p_other)
    {
        super.on_collision(p_other);
    }
    
    public void check_collisions(Player p_player)
    {
        for (int pool = 0; pool < m_pools.size(); pool++)
        {
            for (int unit = 0; unit < m_pools.get(pool).size(); unit++)
            {
                // Checks all collisions
                if (m_pools.get(pool).get(unit).is_colliding(p_player))
                {
                    m_pools.get(pool).get(unit).on_collision(p_player);
                    p_player.on_collision(m_pools.get(pool).get(unit));
                }
                if (m_pools.get(pool).get(unit).get_active() && p_player.get_active())
                {
                    p_player.is_colliding(m_pools.get(pool).get(unit)); // if double collisions make player return false
                }
            }
        }
    }
    
    @Override
    public boolean is_colliding(Entity other)
    {
        return false;
    }
    
    @Override
    protected void on_disable()
    {
    
    }
    
    @Override
    public void destroy()
    {
        for (int pool = 0; pool < m_pools.size(); pool++)
        {
            for (int unit = 0; unit < m_pools.get(pool).size(); unit++)
            {
                m_pools.get(pool).get(unit).destroy();
            }
            m_pools.get(pool).clear();
            m_pools.set(pool, null);
        }
        m_open_first_row_indices.clear();
        m_pools.clear();
        m_pool_indexer.clear();
        m_open_first_row_indices = null;
        m_pools = null;
        m_pool_indexer = null;
        m_grid = null;
    }
    
    @Override
    public void reset()
    {
        for (int pool = 0; pool < m_pools.size(); pool++)
        {
            for (int unit = 0; unit < m_pools.get(pool).size(); unit++)
            {
                m_pools.get(pool).get(unit).reset();
            }
            m_pool_indexer.set(pool, 0);
        }
        m_grid.reset();
    }
}
