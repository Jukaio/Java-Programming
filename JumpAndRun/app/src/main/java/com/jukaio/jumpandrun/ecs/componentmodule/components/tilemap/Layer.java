package com.jukaio.jumpandrun.ecs.componentmodule.components.tilemapcomponents;

import com.jukaio.jumpandrun.extramath.Vector2;

import java.util.ArrayList;


public class Layer
{
    public enum LayerType
    {
        Background,
        Static,
        HUD
    }

    public int m_id;
    public String m_name;
    public LayerType m_type;
    public Vector2 m_dimensions;
    public ArrayList<LayerTile> m_tiles;
}