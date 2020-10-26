package com.jukaio.jumpandrun;

import android.graphics.PointF;
import android.graphics.RectF;

import com.jukaio.jumpandrun.extramath.Formulas;
import com.jukaio.jumpandrun.extramath.Line;

import java.util.ArrayList;

public class CollisionDetection
{
    public static boolean PointLine(float p_point_x, float p_point_y,
                                    float p_line_start_x, float p_line_start_y,
                                    float p_line_end_x, float p_line_end_y)
    {
        final float distance_start = Formulas.distance(p_point_x, p_point_y, p_line_start_x, p_line_start_y);
        final float distance_end = Formulas.distance(p_point_x, p_point_y, p_line_end_x, p_line_end_y);
        final float line_length = Formulas.distance(p_line_start_x, p_line_start_y, p_line_end_x, p_line_end_y);
        final float correction = 1.0f;
        
        return (distance_start + distance_end >= line_length - correction &&
                distance_start + distance_end <= line_length + correction);
    }
    
    private static Line m_lhs = new Line();
    private static Line m_rhs = new Line();
    public static boolean LineLine(int p_lhs_s_x, int p_lhs_s_y, int p_lhs_e_x, int p_lhs_e_y,
                                   int p_rhs_s_x, int p_rhs_s_y, int p_rhs_e_x, int p_rhs_e_y)
    {
        m_lhs.m_start.x = p_lhs_s_x;
        m_lhs.m_start.y = p_lhs_s_y;
        m_lhs.m_end.x = p_lhs_e_x;
        m_lhs.m_end.y = p_lhs_e_y;
        
        m_rhs.m_start.x = p_rhs_s_x;
        m_rhs.m_start.y = p_rhs_s_y;
        m_rhs.m_end.x = p_rhs_e_x;
        m_rhs.m_end.y = p_rhs_e_y;
        
        return LineLine(m_lhs, m_rhs);
    }
    public static boolean LineLine(int p_lhs_s_x, int p_lhs_s_y, int p_lhs_e_x, int p_lhs_e_y,
                                   Line p_rhs)
    {
        m_lhs.m_start.x = p_lhs_s_x;
        m_lhs.m_start.y = p_lhs_s_y;
        m_lhs.m_end.x = p_lhs_e_x;
        m_lhs.m_end.y = p_lhs_e_y;
        
        return LineLine(m_lhs, p_rhs);
    }
    
    public static boolean LineLine(Line p_lhs, Line p_rhs)
    {
        final float distance_rhs_a = (p_rhs.m_end.x - p_rhs.m_start.x) * (p_lhs.m_start.y - p_rhs.m_start.y);
        final float distance_rhs_b = (p_rhs.m_end.y - p_rhs.m_start.y) * (p_lhs.m_start.x - p_rhs.m_start.x);
        
        final float distance_lhs_a = (p_lhs.m_end.x - p_lhs.m_start.x) * (p_lhs.m_start.y - p_rhs.m_start.y);
        final float distance_lhs_b = (p_lhs.m_end.y - p_lhs.m_start.y) * (p_lhs.m_start.x - p_rhs.m_start.x);
        
        
        final float distance_length_a = (p_rhs.m_end.y - p_rhs.m_start.y) * (p_lhs.m_end.x - p_lhs.m_start.x);
        final float distance_length_b = (p_rhs.m_end.x - p_rhs.m_start.x) * (p_lhs.m_end.y - p_lhs.m_start.y);
        
        final float distance_rhs_full = (distance_rhs_a - distance_rhs_b) / (distance_length_a - distance_length_b);
        final float distance_lhs_full = (distance_lhs_a - distance_lhs_b) / (distance_length_a - distance_length_b);
        
        return (distance_rhs_full >= 0.0f &&
                distance_rhs_full <= 1.0f &&
                distance_lhs_full >= 0.0f &&
                distance_lhs_full <= 1.0f);
    }
    
    public static boolean LineLine(Line p_lhs, Line p_rhs, PointF p_collision_points)
    {
        final float distance_rhs_a = (p_rhs.m_end.x - p_rhs.m_start.x) * (p_lhs.m_start.y - p_rhs.m_start.y);
        final float distance_rhs_b = (p_rhs.m_end.y - p_rhs.m_start.y) * (p_lhs.m_start.x - p_rhs.m_start.x);
        
        final float distance_lhs_a = (p_lhs.m_end.x - p_lhs.m_start.x) * (p_lhs.m_start.y - p_rhs.m_start.y);
        final float distance_lhs_b = (p_lhs.m_end.y - p_lhs.m_start.y) * (p_lhs.m_start.x - p_rhs.m_start.x);
        
        
        final float distance_length_a = (p_rhs.m_end.y - p_rhs.m_start.y) * (p_lhs.m_end.x - p_lhs.m_start.x);
        final float distance_length_b = (p_rhs.m_end.x - p_rhs.m_start.x) * (p_lhs.m_end.y - p_lhs.m_start.y);
        
        final float distance_rhs_full = (distance_rhs_a - distance_rhs_b) / (distance_length_a - distance_length_b);
        final float distance_lhs_full = (distance_lhs_a - distance_lhs_b) / (distance_length_a - distance_length_b);
        
        if (distance_rhs_full >= 0.0f &&
            distance_rhs_full <= 1.0f &&
            distance_lhs_full >= 0.0f &&
            distance_lhs_full <= 1.0f)
        {
            p_collision_points.x = p_lhs.m_start.x + (distance_rhs_full * (p_lhs.m_end.x - p_lhs.m_start.x));
            p_collision_points.y = p_lhs.m_start.y + (distance_rhs_full * (p_lhs.m_end.y - p_lhs.m_start.y));
            return true;
        }
        return false;
    }
    
    private static Line[] temp_lines = new Line[4];
    public static boolean LineRect(RectF p_rect, Line p_line)
    {
        final int left = (int) p_rect.left;
        final int right = (int) p_rect.right;
        final int top = (int) p_rect.top;
        final int bottom = (int) p_rect.bottom;
        
        temp_lines[0] = new Line(left, top, right, top);
        temp_lines[1] = new Line(right, top, right, bottom);
        temp_lines[2] = new Line(right, bottom, left, bottom);
        temp_lines[3] = new Line(left, bottom, left, top);
    
        for(Line line : temp_lines)
            if(LineLine(line, p_line))
                return true;
        return false;
    }
}
