package com.jukaio.asteroids;

import java.util.ArrayList;
import java.util.Random;

public class AsteroidPool extends GLEntity
{
    public AsteroidPool(int p_pool_size, float p_base_x, float p_base_y, int p_max_points)
    {
        Random r = new Random();
        for(int i = 0; i < p_pool_size; i++)
        {
            Asteroid small = new Asteroid(0,
                                          0,
                                          p_base_x,
                                          p_base_y,
                                          3 + r.nextInt(p_max_points),
                                          Asteroid.Size.small,
                                          10);
            small.m_active = false;
            m_small_asteroid_pool.add(small);
    
            Asteroid medium = new Asteroid(0,
                                           0,
                                           p_base_x,
                                           p_base_y,
                                           3 + r.nextInt(p_max_points),
                                           Asteroid.Size.medium,
                                           10);
            medium.m_active = false;
            m_medium_asteroid_pool.add(medium);
    
            Asteroid big = new Asteroid(0,
                                        0,
                                        p_base_x,
                                        p_base_y,
                                        3 + r.nextInt(p_max_points),
                                        Asteroid.Size.big,
                                        10);
            big.m_active = false;
            m_big_asteroid_pool.add(big);
        }
    }
    
    private Asteroid find_inactive(ArrayList<Asteroid> p_asteroids)
    {
        for(int i = 0; i < p_asteroids.size(); i++)
        {
            if(!p_asteroids.get(i).m_active)
                return p_asteroids.get(i);
        }
        return null;
    }
    
    
    public void spawn(float p_x, float p_y)
    {
        Asteroid temp = find_inactive(m_big_asteroid_pool);
        if(temp != null)
        {
            temp.set_position(p_x, p_y);
            temp.m_active = true;
            m_active_asteroids.add(temp);
        }
    }
    
    
    @Override
    public void update(double dt)
    {
        for(int i = 0; i < m_active_asteroids.size(); i++)
        {
            if(!m_active_asteroids.get(i).m_active)
            {
                switch(m_active_asteroids.get(i).get_size_type())
                {
                    case small:
                        m_active_asteroids.remove(i);
                        i--;
                        break;
                        
                    case medium:
                        {
                            Asteroid prev = m_active_asteroids.remove(i);
        
                            Asteroid left = find_inactive(m_small_asteroid_pool);
                            left.set_position(prev.get_position().m_x - (prev.get_dimensions().m_x * 0.5f),
                                              prev.get_position().m_y);
                            left.m_active = true;
        
                            Asteroid right = find_inactive(m_small_asteroid_pool);
                            right.set_position(prev.get_position().m_x + (prev.get_dimensions().m_x * 0.5f),
                                               prev.get_position().m_y);
                            right.m_active = true;
        
                            m_active_asteroids.add(left);
                            m_active_asteroids.add(right);
                            
                            i++;
                        }
                        break;
                        
                    case big:
                        {
                            Asteroid prev = m_active_asteroids.remove(i);
        
                            Asteroid left = find_inactive(m_medium_asteroid_pool);
                            left.set_position(prev.get_position().m_x - (prev.get_dimensions().m_x * 0.5f),
                                              prev.get_position().m_y);
                            left.m_active = true;
        
                            Asteroid right = find_inactive(m_medium_asteroid_pool);
                            right.set_position(prev.get_position().m_x + (prev.get_dimensions().m_x * 0.5f),
                                               prev.get_position().m_y);
                            right.m_active = true;
        
                            m_active_asteroids.add(left);
                            m_active_asteroids.add(right);
        
                            i--;
                        }
                        break;
                }
            }
            else
            {
                m_active_asteroids.get(i).update(dt);
            }
            
        }
    }
    
    @Override
    public void render(float[] viewportMatrix)
    {
        for(int i = 0; i < m_active_asteroids.size(); i++)
        {
            m_active_asteroids.get(i).render(viewportMatrix);
        }
    }
    
    
    private ArrayList<Asteroid> m_big_asteroid_pool = new ArrayList<>();
    private ArrayList<Asteroid> m_medium_asteroid_pool = new ArrayList<>();
    private ArrayList<Asteroid> m_small_asteroid_pool = new ArrayList<>();
    
    public ArrayList<Asteroid> m_active_asteroids = new ArrayList<>();
    
    
    @Override
    Type get_type()
    {
        return Type.INVALID;
    }
}
