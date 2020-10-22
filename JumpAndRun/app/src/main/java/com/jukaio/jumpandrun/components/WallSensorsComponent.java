package com.jukaio.jumpandrun;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.jukaio.jumpandrun.extramath.Line;

public class WallSensorsComponent extends Component
{
    public Line m_wall = new Line();
    public boolean m_wall_collided = false;
    
    protected WallSensorsComponent(Entity p_entity)
    {
        super(p_entity);
    }
    
    @Override
    public ComponentType get_type()
    {
        return ComponentType.WALL_SENSORS;
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
    
    }
    
    @Override
    public void render(Canvas p_canvas, Paint p_paint)
    {
    
    }
}
