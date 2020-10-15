package com.jukaio.jumpandrun.extramath;

public class Formulas
{
    public static float distance(float p_lhs_x, float p_lhs_y, float p_rhs_x, float p_rhs_y)
    {
        return (float) Math.sqrt(Math.pow(p_rhs_x - p_lhs_x, 2) +
                                 Math.pow(p_rhs_y - p_lhs_y, 2));
    }
    
    public static float length(float p_x, float p_y)
    {
        return distance(0, 0, p_x, p_y);
    }
    
    public static final float R2D = (float) (360.0 / (Math.PI * 2.0));
    public static final float D2R = (float) ((Math.PI * 2.0) / 360.0);
    private static final float base_x = 0.0f;
    private static final float base_y = -1.0f;
    public static float angle(float p_x, float p_y)
    {
        return (float) (R2D * Math.acos(((p_x * base_x) + (p_y * base_y)) /
                          (float) (Math.sqrt(Math.pow(p_x, 2.0) + Math.pow(p_y, 2.0))) *
                          (float) (Math.sqrt(Math.pow(base_x, 2.0) + Math.pow(base_y, 2.0)))));
    }
    
    public static float lerp(float a, float b, float fraction)
    {
        return a + fraction * (b - a);
    }
    
    public static float angleatan2(float p_x, float p_y)
    {
        float to_return = (float) (Math.atan2(p_x, p_y));
        return to_return;
    }
    
    public static float clamp_range(float p_x, float p_offset, float p_min, float p_max)
    {
        if(p_x - p_offset < p_min)
            return p_min;
        else if(p_x + p_offset > p_max)
            return p_max;
        return p_x;
    }
    
    public static float clamp(float p_x, float p_min, float p_max)
    {
        if(p_x < p_min)
            return p_min;
        else if(p_x > p_max)
            return p_max;
        return p_x;
    }
}
