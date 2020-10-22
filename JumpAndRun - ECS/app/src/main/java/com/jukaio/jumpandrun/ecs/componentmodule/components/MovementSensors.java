package com.jukaio.jumpandrun.ecs.componentmodule.components;

import android.graphics.PointF;

import com.jukaio.jumpandrun.extramath.Line;

import java.util.ArrayList;

public class MovementSensors extends Component
{
    @Override
    public ComponentType get_type()
    {
        return ComponentType.MOVEMENT_SENSOR;
    }
    // in
    public float m_offset_y;
    public float m_offset_x;
    
    // out
    public Line m_ground_left = new Line();
    public Line m_ground_center = new Line();
    public Line m_ground_right = new Line();
    
    public Line m_wall = new Line();
    
    public ArrayList<Line> m_all_collisions = new ArrayList<>();
    public ArrayList<Line> m_wall_collisions = new ArrayList<>();
    public ArrayList<Line> m_ground_collisions = new ArrayList<>();
    
    public boolean m_on_ground = false;
    public boolean m_right_collided = false;
    public boolean m_left_collided = false;
    public boolean m_center_collided = false;
    public boolean m_wall_collided = false;
    
    
}
