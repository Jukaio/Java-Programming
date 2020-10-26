package com.jukaio.jumpandrun.ecs.systemmodule;

import android.graphics.Color;
import android.graphics.Point;

import com.jukaio.jumpandrun.collision.CollisionDetection;
import com.jukaio.jumpandrun.ecs.ECS;
import com.jukaio.jumpandrun.ecs.componentmodule.components.Component;
import com.jukaio.jumpandrun.ecs.componentmodule.components.ComponentType;
import com.jukaio.jumpandrun.ecs.componentmodule.components.MovementSensors;
import com.jukaio.jumpandrun.ecs.componentmodule.components.RectangleCollider;
import com.jukaio.jumpandrun.ecs.componentmodule.components.physics.Kinematic;
import com.jukaio.jumpandrun.ecs.componentmodule.components.tilemap.TileMapCollider;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.Gravity;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.RenderCanvas;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.SharedComponentType;
import com.jukaio.jumpandrun.ecs.componentmodule.components.Transform;
import com.jukaio.jumpandrun.ecs.entitymodule.Entity;
import com.jukaio.jumpandrun.extramath.Formulas;
import com.jukaio.jumpandrun.extramath.Line;

import java.util.ArrayList;

public class MovementSensorsTilemapCollision extends System
{
    @Override
    public SystemType get_type() {
        return SystemType.MOVEMENT_SENSORS_TILEMAP_COLLISION;
    }

    @Override
    public int get_signature() {
        return ComponentType.TRANSFORM.as_bitmask() |
               ComponentType.MOVEMENT_SENSOR.as_bitmask() |
               ComponentType.KINEMATICS.as_bitmask() |
               (SharedComponentType.GRAVITY.as_bitmask() << ComponentType.values().length);
    }

    @Override
    public void init(ECS p_ecs, ArrayList<Entity> p_entities)
    {

    }

    Line to_draw = new Line();
    Point to_draw_circle = new Point(20, 20);
    @Override
    public void update(ECS p_ecs, ArrayList<Entity> p_entities)
    {
        final int system_signature = p_ecs.get_system_signature(this);
        for(Entity entity : p_entities)
        {
            if((p_ecs.get_entity_signature(entity) & system_signature) == system_signature)
            {
                Gravity gravity = p_ecs.get_shared_component(SharedComponentType.GRAVITY);
                Transform transform = p_ecs.get_component(entity,
                                                          ComponentType.TRANSFORM);
                MovementSensors sensors = p_ecs.get_component(entity,
                                                              ComponentType.MOVEMENT_SENSOR);
                Kinematic kinematic = p_ecs.get_component(entity,
                                                          ComponentType.KINEMATICS);

                for (Component other_component : p_ecs.get_all_components_of_type(ComponentType.TILE_MAP_COLLIDER))
                {
                    if (other_component != null)
                    {
                        TileMapCollider tile_map_collider = (TileMapCollider) other_component;
                        get_segment_collisions(tile_map_collider, sensors);
                        break;
                    }
                }
                
                if(sensors.m_all_collisions.size() != 0)
                {
                    float position_x = transform.get_prev_position().m_x.floatValue();
                    float position_y = transform.get_prev_position().m_y.floatValue();
                    to_draw_circle.x = (int) position_x;
                    to_draw_circle.y = (int) position_y;
                    
                    // Find closest line to player
                    Line collision_line = closest_line(position_x, position_y, sensors.m_all_collisions);
    
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
                    float gravity_x = gravity.m_direction.m_x.floatValue();
                    float gravity_y = gravity.m_direction.m_y.floatValue();
                    float line_gravity_dot = dot_product(gravity_x,
                                                         gravity_y,
                                                         line_inner_perpendicular_dir_x,
                                                         line_inner_perpendicular_dir_y);
                    float line_gravity_cross = cross_product(gravity_x,
                                                             gravity_y,
                                                             line_inner_perpendicular_dir_x,
                                                             line_inner_perpendicular_dir_y);
                                                             
                    float angle = Formulas.angleatan2(local_line_x,
                                                      local_line_y);
                    float cos_angle = (float) Math.cos(-angle);
                    float sin_angle = (float) Math.sin(-angle);
                    float next_dir_x = (cos_angle * gravity_x) - (sin_angle * gravity_y);
                    float next_dir_y = (sin_angle * gravity_x) + (cos_angle * gravity_y);
                    float kinetic_velocity_dot = (next_dir_x * kinematic.get_velocity().m_x.floatValue()) +
                                                 (next_dir_y * kinematic.get_velocity().m_y.floatValue());
                                                 
                    // TODO: Speed down if kinetic velocity is in direction of new dir x
                    float gravity_activator = (kinematic.get_velocity().m_x.floatValue() != 0.0f) ? 0 : 1;
                    float final_redistributed_velocity = kinetic_velocity_dot; // Replace 0 with gravity_activator for slope effect
    
                    // Clamp to line
                    float width = transform.get_dimensions().m_x.floatValue();
                    float height = transform.get_dimensions().m_y.floatValue();
                    float point_x = position_x - collision_line.m_start.x;
                    float point_y = position_y - collision_line.m_start.y;
                    float line_x = collision_line.m_end.x - collision_line.m_start.x;
                    float line_y = collision_line.m_end.y - collision_line.m_start.y;
                    float line_dir_x = line_x / line_length;
                    float line_dir_y = line_y / line_length;
                    float player_line_dot = dot_product(point_x, point_y, line_dir_x, line_dir_y);
                    float clamp_to_line_x = (line_dir_x * player_line_dot) + collision_line.m_start.x;
                    float clamp_to_line_y = (line_dir_y * player_line_dot) + collision_line.m_start.y;
                    float some_length = Formulas.length(clamp_to_line_x, clamp_to_line_y);
                    
                    to_draw.m_end.x = (int) clamp_to_line_x ;
                    to_draw.m_end.y = (int) clamp_to_line_y ;
                    clamp_to_line_x = (clamp_to_line_x - (line_inner_perpendicular_dir_x * width * 0.5f));
                    clamp_to_line_y = (clamp_to_line_y - (line_inner_perpendicular_dir_y * height * 0.5f));
    
                    // Apply changes
                    float to_apply_x = (next_dir_x * final_redistributed_velocity) + clamp_to_line_x;
                    float to_apply_y = (next_dir_y * final_redistributed_velocity) + clamp_to_line_y;
                    float to_apply_angle = -Formulas.angleatan2(line_inner_perpendicular_dir_x,
                                                                line_inner_perpendicular_dir_y) * Formulas.R2D;
                    to_draw.m_start.x = (int) position_x;
                    to_draw.m_start.y = (int) position_y;
                    if(line_gravity_dot != 0.0f)
                    {
                        transform.set_rotation(to_apply_angle);
                        transform.set_position(to_apply_x,
                                               to_apply_y);
                    }
                    else
                    {
                        transform.set_rotation(angle);
                        transform.set_position(position_x,
                                               position_y);
                    }
                }
            }
        }
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
    
    
    private void get_segment_collisions(TileMapCollider p_tile_map_collider, MovementSensors p_sensors)
    {
        p_sensors.m_all_collisions.clear();
        p_sensors.m_wall_collisions.clear();
        p_sensors.m_ground_collisions.clear();
        p_sensors.m_on_ground = false;
        
        for (Line line : p_tile_map_collider.m_lines)
        {
            p_sensors.m_left_collided = CollisionDetection.LineLine(p_sensors.m_ground_left,
                                                              line);
            p_sensors.m_right_collided = CollisionDetection.LineLine(p_sensors.m_ground_right,
                                                               line);
            p_sensors.m_center_collided = CollisionDetection.LineLine(p_sensors.m_ground_center,
                                                               line);
            p_sensors.m_wall_collided = CollisionDetection.LineLine(p_sensors.m_wall,
                                                               line);
            // TODO: Handle wall colision correctly - Wall has a fucking normal of (abs(1) | 0)
            if (p_sensors.m_right_collided ||
                p_sensors.m_center_collided ||
                p_sensors.m_left_collided  ||
                p_sensors.m_wall_collided)
            {
                if (p_sensors.m_right_collided ||
                    p_sensors.m_center_collided ||
                    p_sensors.m_left_collided)
                {
                    p_sensors.m_ground_collisions.add(line);
                }
                else if(p_sensors.m_wall_collided)
                {
                    p_sensors.m_wall_collisions.add(line);
                    
                }
                p_sensors.m_on_ground = true;
                p_sensors.m_all_collisions.add(line);
            }
        }
    }
    
    
    @Override
    public void render(ECS p_ecs, ArrayList<Entity> p_entities)
    {
        RenderCanvas renderer = p_ecs.get_shared_component(SharedComponentType.RENDER_CANVAS);
        int color = renderer.m_paint.getColor();
        renderer.m_paint.setColor(Color.GREEN);
        renderer.m_paint.setStrokeWidth(5.0f);
        if(to_draw != null)
            renderer.m_canvas.drawLine(to_draw.m_start.x, to_draw.m_start.y, to_draw.m_end.x, to_draw.m_end.y, renderer.m_paint);
        renderer.m_canvas.drawCircle(to_draw_circle.x, to_draw_circle.y, 2.0f, renderer.m_paint);
        renderer.m_paint.setStrokeWidth(1.0f);
        renderer.m_paint.setColor(color);
    }

}
