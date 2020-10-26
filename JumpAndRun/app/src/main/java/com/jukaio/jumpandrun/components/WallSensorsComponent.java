package com.jukaio.jumpandrun.components;

import android.graphics.Color;
import android.graphics.Paint;

import com.jukaio.jumpandrun.entity.Entity;
import com.jukaio.jumpandrun.Game;
import com.jukaio.jumpandrun.Viewport;
import com.jukaio.jumpandrun.XML;
import com.jukaio.jumpandrun.extramath.Line;

import org.w3c.dom.Element;

import java.util.ArrayList;

public class WallSensorsComponent extends Component
{
    public Line                     m_wall = new Line();
    public Line                     m_rescue = new Line();
    public Line                     m_ceiling = new Line();
    public ArrayList<Line>          m_collisions = new ArrayList<>();
    private KineticComponent        m_kinematic = null;
    private float                   m_offset_x = 0.0f;
    private float                   m_offset_y = 0.0f;
    
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
    public void render(Viewport p_viewport, Paint p_paint)
    {
        if(Game.DEBUG_ON)
        {
            p_paint.setColor(Color.RED);
            p_viewport.draw_line(m_wall,
                                 p_paint);
            p_viewport.draw_line(m_ceiling,
                                 p_paint);
            p_viewport.draw_line(m_rescue,
                                 p_paint);
        }
    }
    
    @Override
    protected void destroy()
    {
        m_wall = null;
        m_rescue = null;
        m_ceiling = null;
        m_collisions = null;
        m_kinematic = null;
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
        p_sensor.m_ceiling.m_start.y = (int) (y - p_sensor.m_offset_y);
        p_sensor.m_ceiling.m_end.x = (int) x;
        p_sensor.m_ceiling.m_end.y = (int) (y - half_w);
        
        p_sensor.m_rescue.m_start.x = (int) (x - (half_w + p_sensor.m_offset_x) * 0.5f);
        p_sensor.m_rescue.m_start.y = (int) (y);
        p_sensor.m_rescue.m_end.x = (int) (x + (half_w - p_sensor.m_offset_x) * 0.5f);
        p_sensor.m_rescue.m_end.y = (int) (y);
    }
}
