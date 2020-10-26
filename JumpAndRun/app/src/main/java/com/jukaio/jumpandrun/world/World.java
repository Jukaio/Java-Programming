package com.jukaio.jumpandrun.world;

import com.jukaio.jumpandrun.CollisionAABB;
import com.jukaio.jumpandrun.entity.Entity;
import com.jukaio.jumpandrun.HUD;

import java.util.ArrayList;

public class World
{
    public HUD m_hud = new HUD();
    public CollisionAABB m_collisionAABB = new CollisionAABB();
    public TileMap m_tile_map = new TileMap();
    public ArrayList<Entity> m_entities = new ArrayList<Entity>();
    public TileSet m_tile_set = new TileSet();
}
