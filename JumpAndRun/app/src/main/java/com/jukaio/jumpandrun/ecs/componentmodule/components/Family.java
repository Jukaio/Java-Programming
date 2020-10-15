package com.jukaio.jumpandrun.ecs.componentmodule.components;

import com.jukaio.jumpandrun.ecs.entitymodule.Entity;

import java.util.ArrayList;

public class Family extends Component
{
    public Entity m_parent;
    public ArrayList<Entity> m_children = new ArrayList<>();
    
    @Override
    public ComponentType get_type()
    {
        return ComponentType.FAMILY;
    }
}
