package com.jukaio.jumpandrun.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.jukaio.jumpandrun.Entity;
import com.jukaio.jumpandrun.World;
import com.jukaio.jumpandrun.XML;
import com.jukaio.jumpandrun.collision.CollisionDetection;
import com.jukaio.jumpandrun.extramath.Formulas;
import com.jukaio.jumpandrun.extramath.Line;

import org.w3c.dom.Element;

import java.util.ArrayList;

public class WorldPhysicsComponent extends Component
{
    private GroundSensorsComponent m_ground_sensors = null;
    private WallSensorsComponent m_wall_sensors = null;
    private World.TileMap m_tile_map = null;
    private KineticComponent m_kinematic = null;
    
    float m_previous_x = 0.0f;
    float m_previous_y = 0.0f;
    float m_gravity = 0.0f;

    public WorldPhysicsComponent(Entity p_entity, World p_world, Element p_data)
    {
        super(p_entity);
        m_gravity = XML.parse_float(p_data, "gravity");
        
        m_tile_map = p_world.m_tile_map;
    }
    
    @Override
    public ComponentType get_type()
    {
        return ComponentType.WORLD_PHYSICS;
    }
    
    @Override
    public void start()
    {
        m_ground_sensors = get_entity().get_component(ComponentType.GROUND_SENSORS);
        m_wall_sensors = get_entity().get_component(ComponentType.WALL_SENSORS);
        m_kinematic = get_entity().get_component(ComponentType.KINEMATIC);
    }
    
    @Override
    public void pre_update(float p_dt)
    {
        m_previous_x = get_entity().get_position().m_x.floatValue();
        m_previous_y = get_entity().get_position().m_y.floatValue();
    }
    
    @Override
    public void update(float p_dt)
    {

    }

    
    @Override
    public void late_update(float p_dt)
    {
        float x = get_entity().get_position().m_x.floatValue();
        float y = get_entity().get_position().m_y.floatValue();
    
        float vel_x = m_kinematic.get_velocity().m_x.floatValue();
        float vel_y = m_kinematic.get_velocity().m_y.floatValue();
    
        x += vel_x;
        y += vel_y;
        get_entity().set_position(x,
                                  y + m_gravity);
    
        if(vel_x != 0.0f)
         {
             float flip_scale_x = vel_x;
             get_entity().set_scale(Math.abs(flip_scale_x) / flip_scale_x, 1);
         }
    
        get_segment_collisions(m_ground_sensors.m_ground_collisions,
                               m_tile_map,
                               m_ground_sensors);
        get_segment_collisions(m_wall_sensors.m_collisions,
                               m_tile_map,
                               m_wall_sensors);
        if (m_wall_sensors.m_collisions.size() == 0)
        {
            if (m_ground_sensors.m_ground_collisions.size() != 0 && m_ground_sensors.m_active)
                lerp_to_collision_lines();
        }
        else
        {
            if(m_ground_sensors.m_ground_collisions.size() != 0)
            {
                Line closest_wall = closest_line(x, y, m_wall_sensors.m_collisions);
                float difference = x - closest_wall.m_start.x;
                get_entity().set_position(m_previous_x + (difference * 0.5f),
                                          m_previous_y);
            }
            else
                get_entity().set_position(m_previous_x, m_previous_y + m_gravity);
            m_kinematic.set_velocity(0, 0);
        }
    }
    
    @Override
    public void render(Canvas p_canvas, Paint p_paint)
    {
        int color = p_paint.getColor();
        p_paint.setColor(Color.GREEN);
        p_paint.setStrokeWidth(5.0f);
        //p_canvas.drawCircle(to_draw_circle.x, to_draw_circle.y, 2.0f, p_paint);
        
        for(Line line : m_wall_sensors.m_collisions)
        {
            //p_canvas.drawLine(line.m_start.x, line.m_start.y, line.m_end.x, line.m_end.y, p_paint);
        }
        
        
        p_paint.setStrokeWidth(1.0f);
        p_paint.setColor(color);
    }
    
    private void handle_wall_collisions()
    {
        Line collision = m_wall_sensors.m_collisions.get(0);
        float mid_x = (collision.m_start.x + collision.m_end.x) * 0.5f;
        float mid_y = (collision.m_start.x + collision.m_end.x) * 0.5f;
        
        float x = get_entity().get_position().m_x.floatValue();
        float y = get_entity().get_position().m_y.floatValue();
    }
    
    private static void get_segment_collisions(ArrayList<Line> out_collisions,
                                               World.TileMap p_tile_map,
                                               GroundSensorsComponent p_sensor)
    {
        out_collisions.clear();
    
        for (int i = 0; i < p_tile_map.line_count_in_collider(); i++)
        {
            Line line = p_tile_map.get_line_from_collider(i);
            boolean left_collided = CollisionDetection.LineLine(p_sensor.m_ground_left,
                                                                line);
            boolean right_collided = CollisionDetection.LineLine(p_sensor.m_ground_right,
                                                                 line);
            boolean center_collided = CollisionDetection.LineLine(p_sensor.m_ground_center,
                                                                  line);
    
            // TODO: Handle wall colision correctly - Wall has a fucking normal of (abs(1) | 0)
            if (left_collided ||
                center_collided ||
                right_collided)
            {
                out_collisions.add(line);
            }
        }
    }
    
    private static void get_segment_collisions(ArrayList<Line> out_collisions,
                                               World.TileMap p_tile_map,
                                               WallSensorsComponent p_sensor)
        {
            out_collisions.clear();
        
            for (int i = 0; i < p_tile_map.line_count_in_collider(); i++)
            {
                Line line = p_tile_map.get_line_from_collider(i);
                boolean collided = CollisionDetection.LineLine(p_sensor.m_wall,
                                                                    line);
                if (collided)
                {
                    out_collisions.add(line);
                }
            }
        }
    
    Point to_draw_circle = new Point(20, 20);
    private void lerp_to_collision_lines()
    {
    
        // Find closest line to player
        Line collision_line = closest_line(m_previous_x,
                                           m_previous_y,
                                           m_ground_sensors.m_ground_collisions);
    
        // Rearrange line endings
        float line_x = collision_line.m_end.x - collision_line.m_start.x;
        float line_y = collision_line.m_end.y - collision_line.m_start.y;
        float line_length = Formulas.length(line_x,
                                            line_y);
        float line_dir_x = line_x / line_length;
        float line_dir_y = line_y / line_length;
        
        if (line_x < 0.0f)
        {
            line_x = -line_x;
            line_y = -line_y;
        }
    
    
        // Redistribute force
        float line_inner_perpendicular_dir_x = -line_y / line_length;
        float line_inner_perpendicular_dir_y = line_x / line_length;
        float gravity_x = 0;
        float gravity_y = 1;
        
        float angle = Formulas.angleatan2(line_x,
                                          line_y);
        float cos_angle = (float) Math.cos(-angle);
        float sin_angle = (float) Math.sin(-angle);
        float next_dir_x = (cos_angle * gravity_x) - (sin_angle * gravity_y);
        float next_dir_y = (sin_angle * gravity_x) + (cos_angle * gravity_y);
        float kinetic_velocity_dot = (next_dir_x * m_kinematic.get_velocity().m_x.floatValue()) +
                                     (next_dir_y * m_kinematic.get_velocity().m_y.floatValue());
    
        // TODO: Speed down if kinetic velocity is in direction of new dir x
        float gravity_activator = (m_kinematic.get_velocity().m_x.floatValue() != 0.0f) ? 0 : 1;
        float final_redistributed_velocity = kinetic_velocity_dot; // Replace 0 with gravity_activator for slope effect
        
        // Clamp to line
        float width = get_entity().get_dimensions().m_x.floatValue();
        float height = get_entity().get_dimensions().m_y.floatValue();
        float point_x = m_previous_x - collision_line.m_start.x;
        float point_y = m_previous_y - collision_line.m_start.y;
        float player_line_dot = dot_product(point_x,
                                            point_y,
                                            line_dir_x,
                                            line_dir_y);
        float clamp_to_line_x = (line_dir_x * player_line_dot) + collision_line.m_start.x;
        float clamp_to_line_y = (line_dir_y * player_line_dot) + collision_line.m_start.y;
        clamp_to_line_x = (clamp_to_line_x - (line_inner_perpendicular_dir_x * width * 0.5f));
        clamp_to_line_y = (clamp_to_line_y - (line_inner_perpendicular_dir_y * height * 0.5f));
        
        //to_draw_circle.x = (int) (clamp_to_line_x);
        //to_draw_circle.y = (int) (clamp_to_line_y);
    
        // Apply changes
        float to_apply_x = (next_dir_x * final_redistributed_velocity) + clamp_to_line_x;
        float to_apply_y = (next_dir_y * final_redistributed_velocity) + clamp_to_line_y;
        float to_apply_angle = -Formulas.angleatan2(line_inner_perpendicular_dir_x,
                                                    line_inner_perpendicular_dir_y) * Formulas.R2D;
    
    
        get_entity().set_rotation(to_apply_angle);
        get_entity().set_position(to_apply_x,
                                  to_apply_y);
    
    
    }
    
    private Line closest_line(float p_pos_x, float p_pos_y, ArrayList<Line> p_lines)
    {
        Line to_return = p_lines.get(0);
        float prev_mid_x = (to_return.m_end.x + to_return.m_start.x) * 0.5f;
        float prev_mid_y = (to_return.m_end.y + to_return.m_start.y) * 0.5f;
        float prev_distance = Formulas.distance(p_pos_x,
                                                p_pos_y,
                                                prev_mid_x,
                                                prev_mid_y);
        for (int i = 1; i < p_lines.size(); i++)
        {
            Line line = p_lines.get(i);
        
            float mid_x = (line.m_end.x + line.m_start.x) * 0.5f;
            float mid_y = (line.m_end.y + line.m_start.y) * 0.5f;
            float distance = Formulas.distance(p_pos_x,
                                               p_pos_y,
                                               mid_x,
                                               mid_y);
            if (distance < prev_distance)
            {
                prev_distance = distance;
                to_return = line;
            }
        }
        return to_return;
    }
    
    private float dot_product(float p_lhs_x, float p_lhs_y, float p_rhs_x, float p_rhs_y)
    {
        return (p_lhs_x * p_rhs_x +
                p_lhs_y * p_rhs_y);
    }
    
    private float cross_product(float p_lhs_x, float p_lhs_y, float p_rhs_x, float p_rhs_y)
    {
        return (p_lhs_x * p_rhs_y -
                p_lhs_y * p_rhs_x);
    }
}
