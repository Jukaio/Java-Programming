package com.jukaio.jumpandrun.ecs.worldmodule;

import android.content.Context;

import com.jukaio.jumpandrun.ecs.ECS;
import com.jukaio.jumpandrun.ecs.entitymodule.Entity;

import java.util.ArrayList;

public class World
{
    protected World(ECS p_ecs, Context p_context)
    {
        m_entities = new ArrayList<>();
    }

    public ArrayList<Entity> m_entities;
}
