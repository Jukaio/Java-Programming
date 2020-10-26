package com.jukaio.jumpandrun.components;

import android.graphics.Color;
import android.graphics.Paint;

import com.jukaio.jumpandrun.CollisionDetection;
import com.jukaio.jumpandrun.entity.Entity;
import com.jukaio.jumpandrun.Game;
import com.jukaio.jumpandrun.world.TileMap;
import com.jukaio.jumpandrun.Viewport;
import com.jukaio.jumpandrun.world.World;
import com.jukaio.jumpandrun.XML;
import com.jukaio.jumpandrun.extramath.Formulas;
import com.jukaio.jumpandrun.extramath.Line;

import org.w3c.dom.Element;

import java.util.ArrayList;

public class WorldPhysicsComponent extends Component
{
    private GroundSensorsComponent  m_ground_sensors    = null;
    private WallSensorsComponent    m_wall_sensors      = null;
    private TileMap                 m_tile_map          = null;
    private KineticComponent        m_kinematic         = null;
    private float                   m_previous_x        = 0.0f;
    private float                   m_previous_y        = 0.0f;
    private float                   m_gravity           = 0.0f;
    private int                     m_detection_range_x = 0;
    private int                     m_detection_range_y = 0;
    ArrayList<Line>                 m_checked           = new ArrayList<>();

    public WorldPhysicsComponent(Entity p_entity, World p_world, Element p_data)
    {
        super(p_entity);
        m_gravity = XML.parse_float(p_data, "gravity");
        m_detection_range_x = XML.parse_int(p_data, "detection_range_x");
        m_detection_range_y = XML.parse_int(p_data, "detection_range_y");
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
                                  y + m_gravity); // TODO: change back to m_gravity
    
        if(vel_x != 0.0f)
         {
             float flip_scale_x = vel_x;
             get_entity().set_scale(Math.abs(flip_scale_x) / flip_scale_x, 1);
         }
    
        get_ground_collisions(m_ground_sensors.m_collisions);
        get_wall_collisions(m_wall_sensors.m_collisions);
        
        if (m_wall_sensors.m_collisions.size() == 0)
        {
            if (m_ground_sensors.m_collisions.size() != 0 && m_ground_sensors.m_active)
                lerp_to_collision_lines();
        }
        
        else
        {
            Line closest_wall = closest_line(x, y, m_wall_sensors.m_collisions);
            float difference_x = x - closest_wall.m_start.x;
            float difference_y = y - closest_wall.m_start.y;
            difference_x = (difference_x != 0.0f) ? difference_x / Math.abs(difference_x) : 0.0f;
            difference_y = (difference_y != 0.0f) ? difference_y / Math.abs(difference_y) : 0.0f;
            
            if(m_ground_sensors.m_collisions.size() != 0)
            {
                get_entity().set_position(m_previous_x + (difference_x),
                                          y);
            }
            else
            {
                get_entity().set_position(m_previous_x + (difference_x),
                                          m_previous_y + m_gravity + difference_y);
            }
            m_kinematic.set_velocity(0, 0);
        }
    }

    @Override
    public void render(Viewport p_viewport, Paint p_paint)
    {
        if(Game.DEBUG_ON)
        {
            int color = p_paint.getColor();
            p_paint.setColor(Color.BLUE);
            p_paint.setStrokeWidth(5.0f);

    
            p_paint.setColor(Color.GREEN);
            for (Line line : m_checked)
            {
                p_viewport.draw_line(line,
                                     p_paint);
            }
            p_paint.setStrokeWidth(1.0f);

            p_paint.setColor(color);
        }
    }
    
    @Override
    protected void destroy()
    {
        m_ground_sensors    = null;
        m_wall_sensors      = null;
        m_tile_map          = null;
        m_kinematic         = null;
        m_checked           = null;
        
    }
    
    
    private void get_ground_collisions(ArrayList<Line> out_collisions)
    {
        m_checked.clear();
        out_collisions.clear();
        int map_width = m_tile_map.get_grid().get_dimensions().m_x.intValue();
        int map_height = m_tile_map.get_grid().get_dimensions().m_y.intValue();
    
        int x = get_entity().get_position().m_x.intValue() / m_tile_map.get_grid().get_tile_size().m_x.intValue();
        int y = get_entity().get_position().m_y.intValue() / m_tile_map.get_grid().get_tile_size().m_y.intValue();

        for (int index_y = Math.max(y - m_detection_range_y,
                                    0); index_y < Math.min(y + m_detection_range_y,
                                                           map_height); index_y++)
        {
            for (int index_x = Math.max(x - m_detection_range_x,
                                        0); index_x < Math.min(x + m_detection_range_x,
                                                               map_width); index_x++)
            {
                int index = (index_y * map_width) + index_x;
                if (m_tile_map.m_collider.m_tiles[index] != null)
                {
                    for (Line line : m_tile_map.m_collider.m_tiles[index])
                    {
                        boolean left_collided = CollisionDetection.LineLine(m_ground_sensors.m_ground_left,
                                                                            line);
                        boolean right_collided = CollisionDetection.LineLine(m_ground_sensors.m_ground_right,
                                                                             line);
                        boolean center_collided = CollisionDetection.LineLine(m_ground_sensors.m_ground_center,
                                                                              line);
                        m_checked.add(line);
                        // TODO: Handle wall colision correctly - Wall has a fucking normal of (abs(1) | 0)
                        if (left_collided ||
                                center_collided ||
                                right_collided)
                        {
                            out_collisions.add(line);
                        }
                    }
                }
            }
        }
    }
    
    private void get_wall_collisions(ArrayList<Line> out_collisions)
    {
        out_collisions.clear();
        int map_width = m_tile_map.get_grid().get_dimensions().m_x.intValue();
        int map_height = m_tile_map.get_grid().get_dimensions().m_y.intValue();
    
        int x = get_entity().get_position().m_x.intValue() / m_tile_map.get_grid().get_tile_size().m_x.intValue();
        int y = get_entity().get_position().m_y.intValue() / m_tile_map.get_grid().get_tile_size().m_y.intValue();
    
    
        for (int index_y = Math.max(y - m_detection_range_y,
                                    0); index_y < Math.min(y + m_detection_range_y,
                                                           map_height); index_y++)
        {
            for (int index_x = Math.max(x - m_detection_range_x,
                                        0); index_x < Math.min(x + m_detection_range_x,
                                                               map_width); index_x++)
            {
                int index = (index_y * map_width) + index_x;
                if (m_tile_map.m_collider.m_tiles[index] != null)
                {
                    for (Line line : m_tile_map.m_collider.m_tiles[index])
                    {
    
                        boolean wall_collided = CollisionDetection.LineLine(m_wall_sensors.m_wall,
                                                                            line);
                        boolean ceiling_collided = CollisionDetection.LineLine(m_wall_sensors.m_ceiling,
                                                                               line);
                        boolean rescue_collided = CollisionDetection.LineLine(m_wall_sensors.m_rescue,
                                                                                                       line);
                        m_checked.add(line);
                        // TODO: Handle wall colision correctly - Wall has a fucking normal of (abs(1) | 0)
                        if (wall_collided || ceiling_collided || rescue_collided)
                        {
                            out_collisions.add(line);
                        }
                    }
                }
            }
        }
    }
    
    private void lerp_to_collision_lines()
    {
    
        // Find closest line to player
        Line collision_line = closest_line(m_previous_x,
                                           m_previous_y,
                                           m_ground_sensors.m_collisions);
    
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
        
        
        float vel_length = Formulas.length(m_kinematic.get_velocity().m_x.floatValue(),
                                           m_kinematic.get_velocity().m_y.floatValue());
        float vel_dir_x = (vel_length != 0.0f) ? m_kinematic.get_velocity().m_x.floatValue() / vel_length : 0.0f;
        float vel_dir_y = (vel_length != 0.0f) ? m_kinematic.get_velocity().m_y.floatValue() / vel_length : 0.0f;
        
        float vel_angle = Formulas.angleatan2(line_y, line_x);
        cos_angle = (float) Math.cos(vel_angle);
        sin_angle = (float) Math.sin(vel_angle);
        vel_dir_x = (cos_angle * vel_dir_x) - (sin_angle * vel_dir_y);
        vel_dir_y = (sin_angle * vel_dir_x) + (cos_angle * vel_dir_y);
       
        // Clamp to line
        float width = get_entity().get_dimensions().m_x.floatValue();
        float height = get_entity().get_dimensions().m_y.floatValue();
        
        float point_x = (m_previous_x + (vel_dir_x * vel_length)) - collision_line.m_start.x;
        float point_y = (m_previous_y + (vel_dir_y * vel_length)) - collision_line.m_start.y;
        float player_line_dot = dot_product(point_x,
                                            point_y,
                                            line_dir_x,
                                            line_dir_y);
        float clamp_to_line_x = (line_dir_x * player_line_dot) + collision_line.m_start.x;
        float clamp_to_line_y = (line_dir_y * player_line_dot) + collision_line.m_start.y;
        clamp_to_line_x = (clamp_to_line_x - (line_inner_perpendicular_dir_x * (width * 0.5f)));
        clamp_to_line_y = (clamp_to_line_y - (line_inner_perpendicular_dir_y * (height * 0.5f)));
        
    
        // Apply changes
        float to_apply_angle = -Formulas.angleatan2(line_inner_perpendicular_dir_x,
                                                    line_inner_perpendicular_dir_y) * Formulas.R2D;
        get_entity().set_rotation(to_apply_angle);
        get_entity().set_position(clamp_to_line_x,
                                  clamp_to_line_y);
    
    
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
