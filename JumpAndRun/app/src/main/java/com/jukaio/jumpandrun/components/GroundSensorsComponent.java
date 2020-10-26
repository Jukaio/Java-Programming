package com.jukaio.jumpandrun.components;

import android.graphics.Color;
import android.graphics.Paint;

import com.jukaio.jumpandrun.entity.Entity;
import com.jukaio.jumpandrun.Game;
import com.jukaio.jumpandrun.Viewport;
import com.jukaio.jumpandrun.world.World;
import com.jukaio.jumpandrun.XML;
import com.jukaio.jumpandrun.extramath.Formulas;
import com.jukaio.jumpandrun.extramath.Line;

import org.w3c.dom.Element;

import java.util.ArrayList;

public class GroundSensorsComponent extends Component
{
    public boolean  m_active        = true;
    public Line     m_ground_left   = new Line();
    public Line     m_ground_center = new Line();
    public Line     m_ground_right  = new Line();
    private float   m_width         = 0.0f;
    private float   m_height        = 0.0f;
    private float   m_offset_x      = 0.0f;
    private float   m_offset_y      = 0.0f;
    
    public ArrayList<Line> m_collisions = new ArrayList<>();
    
    KineticComponent m_kinematic = null;
    
    public GroundSensorsComponent(Entity p_entity, World p_world, Element p_data)
    {
        super(p_entity);
        m_width = p_world.m_tile_set.get_tile_size().m_x.floatValue();
        m_height = p_world.m_tile_set.get_tile_size().m_y.floatValue();
        m_offset_x = XML.parse_float(p_data,
                                     "offset_x");
        m_offset_y = XML.parse_float(p_data,
                                     "offset_y");
    }
    
    @Override
    public ComponentType get_type()
    {
        return ComponentType.GROUND_SENSORS;
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
        update_position(this);
    }
    
    @Override
    public void render(Viewport p_viewport, Paint p_paint)
    {
        if(Game.DEBUG_ON)
        {
            int prev_color = p_paint.getColor();
    
            p_paint.setColor(Color.YELLOW);
    
            p_viewport.draw_line(m_ground_left,
                                p_paint);
            p_viewport.draw_line(m_ground_right,
                                p_paint);
            p_viewport.draw_line(m_ground_center,
                                p_paint);
    
            p_paint.setColor(prev_color);
        }
    }
    
    @Override
    protected void destroy()
    {
        m_ground_left   = null;
        m_ground_center = null;
        m_ground_right  = null;
    }
    
    public static void update_position(GroundSensorsComponent p_sensor)
    {
        float rotation = p_sensor.get_entity().get_rotation() * Formulas.D2R;
        float cos_angle = (float) Math.cos(rotation);
        float sin_angle = (float) Math.sin(rotation);
    
        float vel_x = p_sensor.m_kinematic.get_velocity().m_x.floatValue();
        float vel_y = p_sensor.m_kinematic.get_velocity().m_y.floatValue();
    
        final float x = p_sensor.get_entity().get_position().m_x.floatValue() + vel_x;
        final float y = p_sensor.get_entity().get_position().m_y.floatValue() + vel_y;
        final float half_w = p_sensor.m_width * 0.5f;
        final float half_h = p_sensor.m_height * 0.5f;
    
        p_sensor.m_ground_left.m_start.x = (int) (x + p_sensor.m_offset_x - half_w);
        p_sensor.m_ground_left.m_start.y = (int) (y + p_sensor.m_offset_y);
        p_sensor.m_ground_left.m_end.x = (int) (x + p_sensor.m_offset_x - half_w);
        p_sensor.m_ground_left.m_end.y = (int) (y + half_h + p_sensor.m_offset_y);
    
        p_sensor.m_ground_center.m_start.x = (int) (x);
        p_sensor.m_ground_center.m_start.y = (int) (y + p_sensor.m_offset_x);
        p_sensor.m_ground_center.m_end.x = (int) (x);
        p_sensor.m_ground_center.m_end.y = (int) (y + half_h + p_sensor.m_offset_y);
    
        p_sensor.m_ground_right.m_start.x = (int) (x + half_w - p_sensor.m_offset_x);
        p_sensor.m_ground_right.m_start.y = (int) (y + p_sensor.m_offset_y);
        p_sensor.m_ground_right.m_end.x = (int) (x + half_w - p_sensor.m_offset_x);
        p_sensor.m_ground_right.m_end.y = (int) (y + half_h + p_sensor.m_offset_y);
    /*
        Formulas.rotate_line(p_sensor.m_ground_center,
                             x,
                             y,
                             cos_angle,
                             sin_angle);
        Formulas.rotate_line(p_sensor.m_ground_left,
                             x,
                             y,
                             cos_angle,
                             sin_angle);
        Formulas.rotate_line(p_sensor.m_ground_right,
                             x,
                             y,
                             cos_angle,
                             sin_angle);
                             */
    }
    
}
