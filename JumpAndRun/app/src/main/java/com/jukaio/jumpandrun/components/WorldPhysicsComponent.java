package com.jukaio.jumpandrun.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.jukaio.jumpandrun.Entity;
import com.jukaio.jumpandrun.World;
import com.jukaio.jumpandrun.collision.CollisionDetection;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.RenderCanvas;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.SharedComponentType;
import com.jukaio.jumpandrun.extramath.Formulas;
import com.jukaio.jumpandrun.extramath.Line;

import java.util.ArrayList;

public class WorldCollisionComponent extends Component
{
    private GroundSensorsComponent m_ground_sensors = null;
    private WallSensorsComponent m_wall_sensors = null;
    private World.TileMap m_tile_map = null;
    private KineticComponent m_kinematic = null;
    private RecorderComponent m_recorder = null;
    
    public ArrayList<Line> m_ground_collisions = new ArrayList<>();
    
    public boolean is_colliding()
    {
        return m_ground_collisions.size() != 0;
    }
    
    public WorldCollisionComponent(Entity p_entity, World p_world)
    {
        super(p_entity);
        m_ground_sensors = get_entity().get_component(ComponentType.GROUND_SENSORS);
        m_wall_sensors = get_entity().get_component(ComponentType.WALL_SENSORS);
        m_kinematic = get_entity().get_component(ComponentType.KINEMATIC);
        m_recorder = get_entity().get_component(ComponentType.RECORDER);
        
        m_tile_map = p_world.m_tile_map;
    }
    
    @Override
    public ComponentType get_type()
    {
        return ComponentType.WORLD_COLLISION;
    }
    
    @Override
    public void update(float p_dt)
    {

    }
    
    @Override
    public void fixed_update()
    {
    
    }
    
    @Override
    public void late_update(float p_dt)
    {
        get_segment_collisions(m_ground_collisions,
                               m_tile_map,
                               m_ground_sensors);
        lerp_to_collision_lines(p_dt);
    
    }
    
    @Override
    public void render(Canvas p_canvas, Paint p_paint)
    {
        int color = p_paint.getColor();
        p_paint.setColor(Color.GREEN);
        p_paint.setStrokeWidth(5.0f);
        p_canvas.drawCircle(to_draw_circle.x, to_draw_circle.y, 2.0f, p_paint);
        p_paint.setStrokeWidth(1.0f);
        p_paint.setColor(color);
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
    Point to_draw_circle = new Point(20, 20);
    private void lerp_to_collision_lines(float p_dt)
    {
        if (m_ground_collisions.size() != 0)
        {
            float position_x = m_recorder.get_previous_state().get_position().m_x.floatValue();
            float position_y = m_recorder.get_previous_state().get_position().m_y.floatValue();
    
            // Find closest line to player
            Line collision_line = closest_line(position_x,
                                               position_y,
                                               m_ground_collisions);
    
            // Rearrange line endings
            float local_line_x = collision_line.m_end.x - collision_line.m_start.x;
            float local_line_y = collision_line.m_end.y - collision_line.m_start.y;
            float line_length = Formulas.length(local_line_x,
                                                local_line_y);
            if (local_line_x < 0.0f)
            {
                local_line_x = -local_line_x;
                local_line_y = -local_line_y;
            }
    
            // Redistribute force
            float line_inner_perpendicular_dir_x = -local_line_y / line_length;
            float line_inner_perpendicular_dir_y = local_line_x / line_length;
            float gravity_x = 0;
            float gravity_y = 1;
            float line_gravity_dot = dot_product(0,
                                                 1,
                                                 line_inner_perpendicular_dir_x,
                                                 line_inner_perpendicular_dir_y);
            float angle = Formulas.angleatan2(local_line_x,
                                              local_line_y);
            float cos_angle = (float) Math.cos(-angle);
            float sin_angle = (float) Math.sin(-angle);
            float next_dir_x = (cos_angle * gravity_x) - (sin_angle * gravity_y);
            float next_dir_y = (sin_angle * gravity_x) + (cos_angle * gravity_y);
            float kinetic_velocity_dot = (next_dir_x * m_kinematic.get_velocity().m_x.floatValue()) +
                                         (next_dir_y * m_kinematic.get_velocity().m_y.floatValue());
    
            // TODO: Speed down if kinetic velocity is in direction of new dir x
            float gravity_activator = (m_kinematic.get_velocity().m_x.floatValue() != 0.0f) ? 0 : 1;
            float final_redistributed_velocity = kinetic_velocity_dot; // Replace 0 with gravity_activator for slope effect
            final_redistributed_velocity *= p_dt;
            // Clamp to line
            float width = get_entity().get_dimensions().m_x.floatValue();
            float height = get_entity().get_dimensions().m_y.floatValue();
            float point_x = position_x - collision_line.m_start.x;
            float point_y = position_y - collision_line.m_start.y;
            float line_x = collision_line.m_end.x - collision_line.m_start.x;
            float line_y = collision_line.m_end.y - collision_line.m_start.y;
            float line_dir_x = line_x / line_length;
            float line_dir_y = line_y / line_length;
            float player_line_dot = dot_product(point_x,
                                                point_y,
                                                line_dir_x,
                                                line_dir_y);
            float clamp_to_line_x = (line_dir_x * player_line_dot) + collision_line.m_start.x;
            float clamp_to_line_y = (line_dir_y * player_line_dot) + collision_line.m_start.y;
            clamp_to_line_x = (clamp_to_line_x - (line_inner_perpendicular_dir_x * width * 0.5f));
            clamp_to_line_y = (clamp_to_line_y - (line_inner_perpendicular_dir_y * height * 0.5f));
    
            // Apply changes
            float to_apply_x = (next_dir_x * final_redistributed_velocity) + clamp_to_line_x;
            float to_apply_y = (next_dir_y * final_redistributed_velocity) + clamp_to_line_y;
            float to_apply_angle = -Formulas.angleatan2(line_inner_perpendicular_dir_x,
                                                        line_inner_perpendicular_dir_y) * Formulas.R2D;
    
            to_draw_circle.x = (int) clamp_to_line_x;
            to_draw_circle.y = (int) clamp_to_line_y;
            if (line_gravity_dot != 0.0f)
            {
                get_entity().set_rotation(to_apply_angle);
                get_entity().set_position(to_apply_x,
                                          to_apply_y);
            }
            else
            {
                get_entity().set_rotation(angle);
                get_entity().set_position(position_x,
                                          position_y);
            }
        }
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
