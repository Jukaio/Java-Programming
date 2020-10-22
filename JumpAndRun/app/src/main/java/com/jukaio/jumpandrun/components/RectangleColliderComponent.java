package com.jukaio.jumpandrun.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.jukaio.jumpandrun.Entity;
import com.jukaio.jumpandrun.extramath.Formulas;

public class RectangleColliderComponent extends Component
{
    private RectF m_rectangle_collider = new RectF();
    private KineticComponent m_kinematic = null;
    
    public RectangleColliderComponent(Entity p_entity)
    {
        super(p_entity);
    }
    
    @Override
    public ComponentType get_type()
    {
        return null;
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
        update_collider(this);
    }
    
    @Override
    public void late_update(float p_dt)
    {
    
    }
    
    @Override
    public void render(Canvas p_canvas, Paint p_paint)
    {
        p_paint.setColor(Color.RED);
        p_canvas.drawRect(m_rectangle_collider, p_paint);
        
    }
    
    public static void update_collider(RectangleColliderComponent p_collider)
    {
        float rotation = p_collider.get_entity().get_rotation() * Formulas.D2R;
        float cos_angle = (float) Math.cos(rotation);
        float sin_angle = (float) Math.sin(rotation);
    
        float vel_x = p_collider.m_kinematic.get_velocity().m_x.floatValue();
        float vel_y = p_collider.m_kinematic.get_velocity().m_y.floatValue();
        final float x = p_collider.get_entity().get_position().m_x.floatValue() + vel_x;
        final float y = p_collider.get_entity().get_position().m_y.floatValue() + vel_y;
        final float half_w = p_collider.get_entity().get_dimensions().m_y.floatValue() * 0.5f;
        final float half_h = p_collider.get_entity().get_dimensions().m_y.floatValue() * 0.5f;
        
        p_collider.m_rectangle_collider.top = y - half_h;
        p_collider.m_rectangle_collider.bottom = y + half_h;
        p_collider.m_rectangle_collider.left = x - half_w;
        p_collider.m_rectangle_collider.right = x + half_w;
        
        
    }
}
