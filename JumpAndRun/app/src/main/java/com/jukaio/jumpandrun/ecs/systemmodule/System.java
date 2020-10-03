package com.jukaio.jumpandrun.ecs.systemmodule;

import com.jukaio.jumpandrun.ecs.ECS;
import com.jukaio.jumpandrun.ecs.entitymodule.Entity;

import java.util.ArrayList;

public abstract class System
{
    public abstract SystemType get_type();

    abstract public int get_signature();
    final public void register(ECS p_ecs)
    {
        p_ecs.register_system(this, get_signature());
    }
    final public void init_system(ECS p_ecs, ArrayList<Entity> p_entities)
    {
        register(p_ecs);
        init(p_ecs, p_entities);
    }
    abstract protected void init(ECS p_ecs, ArrayList<Entity> p_entities);
    abstract public void update(ECS p_ecs, ArrayList<Entity> p_entities);
    abstract public void render(ECS p_ecs, ArrayList<Entity> p_entities);
}
