package com.jukaio.jumpandrun;

import com.jukaio.jumpandrun.components.RectangleColliderComponent;

import java.util.ArrayList;

public class CollisionAABB
{
    public void push_back_collider(RectangleColliderComponent p_collider)
    {
        m_rectangle_components.add(p_collider);
    }
    
    public void check_collisions()
    {
        RectangleColliderComponent lhs;
        RectangleColliderComponent rhs;
        for(int i = 0; i < m_rectangle_components.size() - 1; i++)
        {
            lhs = m_rectangle_components.get(i);
            if(lhs.get_entity().get_active())
            {
                for (int j = i + 1; j < m_rectangle_components.size(); j++)
                {
                    rhs = m_rectangle_components.get(j);
                    if(rhs.get_entity().get_active())
                    {
                        if (lhs.AABB(rhs))
                        {
                            lhs.get_entity().on_collision(rhs.get_entity());
                            rhs.get_entity().on_collision(lhs.get_entity());
                        }
                    }
                }
            }
        }
    }
    
    private ArrayList<RectangleColliderComponent> m_rectangle_components = new ArrayList<>();
}
