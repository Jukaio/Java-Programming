package com.jukaio.jumpandrun.components;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.jukaio.jumpandrun.entity.Entity;
import com.jukaio.jumpandrun.Game;
import com.jukaio.jumpandrun.Viewport;
import com.jukaio.jumpandrun.world.World;
import com.jukaio.jumpandrun.XML;

import org.w3c.dom.Element;

public class RectangleColliderComponent extends Component
{
    private RectF       m_rectangle_collider    = new RectF();
    private float       offset_x                = 0.0f;
    private float       offset_y                = 0.0f;
    private float       shrink_x                = 0.0f;
    private float       shrink_y                = 0.0f;
    
    public RectangleColliderComponent(Entity p_entity, World p_world, Element p_data)
    {
        super(p_entity);
        p_world.m_collisionAABB.push_back_collider(this);
        offset_x = XML.parse_float(p_data, "offset_x");
        offset_y = XML.parse_float(p_data, "offset_y");
        shrink_x = XML.parse_float(p_data, "shrink_x");
        shrink_y = XML.parse_float(p_data, "shrink_y");
    }
    
    @Override
    public ComponentType get_type()
    {
        return ComponentType.RECTANGLE_COLLIDER;
    }
    
    @Override
    public void start()
    {
        
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
    public void render(Viewport p_viewport, Paint p_paint)
    {
        if(Game.DEBUG_ON)
        {
            p_paint.setColor(Color.CYAN);
            p_paint.setStyle(Paint.Style.STROKE);
            p_viewport.draw_rect(m_rectangle_collider,
                                 p_paint);
            p_paint.setStyle(Paint.Style.FILL_AND_STROKE);
        }
        
    }
    
    @Override
    protected void destroy()
    {
        m_rectangle_collider    = null;
    }
    
    public static void update_collider(RectangleColliderComponent p_collider)
    {
        final float x = p_collider.get_entity().get_position().m_x.floatValue();
        final float y = p_collider.get_entity().get_position().m_y.floatValue();
        final float half_w = p_collider.get_entity().get_dimensions().m_y.floatValue() * 0.5f;
        final float half_h = p_collider.get_entity().get_dimensions().m_y.floatValue() * 0.5f;
        
        p_collider.m_rectangle_collider.top = y - half_h + p_collider.offset_x + p_collider.shrink_y;
        p_collider.m_rectangle_collider.bottom = y + half_h + p_collider.offset_y - p_collider.shrink_y;
        p_collider.m_rectangle_collider.left = x - half_w + p_collider.offset_x + p_collider.shrink_x;
        p_collider.m_rectangle_collider.right = x + half_w + p_collider.offset_y - p_collider.shrink_x;
        
        
    }
    
    public boolean AABB(RectangleColliderComponent p_other)
    {
        return m_rectangle_collider.intersect(p_other.m_rectangle_collider);
    }
}
