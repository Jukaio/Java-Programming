package com.jukaio.jumpandrun.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.jukaio.jumpandrun.Entity;
import com.jukaio.jumpandrun.XML;
import com.jukaio.jumpandrun.extramath.Line;

import org.w3c.dom.Element;

import java.util.ArrayList;

public class WallSensorsComponent extends Component
{
    public Line m_wall = new Line();
    public Line m_ceiling = new Line();
    public ArrayList<Line> m_collisions = new ArrayList<>();
    KineticComponent m_kinematic = null;
    float m_offset_x = 0.0f;
    float m_offset_y = 0.0f;
    
    public WallSensorsComponent(Entity p_entity, Element p_data)
    {
        super(p_entity);
        m_offset_x = XML.parse_float(p_data,
                                     "offset_x");
        m_offset_y = XML.parse_float(p_data,
                                     "offset_y");
    }
    
    @Override
    public ComponentType get_type()
    {
        return ComponentType.WALL_SENSORS;
    }
    
    @Override
    public void start()
    {
        m_kinematic = get_entity().get_component(ComponentType.KINEMATIC);
    }
    
    @Override
    public void pre_update(float p_dt)
    {
    
    }
    
    @Override
    public void update(float p_dt)
    {
        update_position(this);
    }
    
    @Override
    public void late_update(float p_dt)
    {
    
    }
    
    @Override
    public void render(Canvas p_canvas, Paint p_paint)
    {
        p_paint.setColor(Color.RED);
        p_canvas.drawLine(m_wall.m_start.x,
                          m_wall.m_start.y,
                          m_wall.m_end.x,
                          m_wall.m_end.y,
                          p_paint);
        p_canvas.drawLine(m_ceiling.m_start.x,
                          m_ceiling.m_start.y,
                          m_ceiling.m_end.x,
                          m_ceiling.m_end.y,
                          p_paint);
    }
    
    public static void update_position(WallSensorsComponent p_sensor)
    {
        float vel_x = p_sensor.m_kinematic.get_velocity().m_x.floatValue();
        float vel_y = p_sensor.m_kinematic.get_velocity().m_y.floatValue();
    
        final float x = p_sensor.get_entity().get_position().m_x.floatValue() + vel_x;
        final float y = p_sensor.get_entity().get_position().m_y.floatValue() + vel_y;
        final float half_w = p_sensor.get_entity().get_dimensions().m_x.floatValue() * 0.5f;
        
        p_sensor.m_wall.m_start.x = (int) (x - half_w + p_sensor.m_offset_x);
        p_sensor.m_wall.m_start.y = (int) (y - p_sensor.m_offset_y);
        p_sensor.m_wall.m_end.x = (int) (x + half_w - p_sensor.m_offset_x);
        p_sensor.m_wall.m_end.y = (int) (y - p_sensor.m_offset_y);
        
        p_sensor.m_ceiling.m_start.x = (int) x;
        p_sensor.m_ceiling.m_start.y = (int) y;
        p_sensor.m_ceiling.m_end.x = (int) x;
        p_sensor.m_ceiling.m_end.y = (int) (y - half_w);
    }
}
