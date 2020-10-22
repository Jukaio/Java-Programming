package com.jukaio.jumpandrun;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import org.w3c.dom.Element;

public class InputComponent extends Component
{
    KineticComponent m_kinematic = null;
    float m_speed = 0.0f;
    protected InputComponent(Entity p_entity, Element p_data)
    {
        super(p_entity);
        m_kinematic = get_entity().get_component(ComponentType.KINEMATIC);
        m_speed = XML.parse_float(p_data, "acceleration_x");
    }
    
    @Override
    public ComponentType get_type()
    {
        return ComponentType.INPUT;
    }
    
    @Override
    public void update(float p_dt)
    {
        if (InputManager.is_jump())
        {
            Log.d("Jump",
                  "Works");
        }
    
        float accel_x = m_kinematic.get_acceleration().m_x.floatValue();
        float accel_y = m_kinematic.get_acceleration().m_x.floatValue();
              accel_x += InputManager.get_horizontal() * m_speed;
              accel_y += 0.0f;
        m_kinematic.set_acceleration(accel_x,
                                     accel_y);
    
    
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
