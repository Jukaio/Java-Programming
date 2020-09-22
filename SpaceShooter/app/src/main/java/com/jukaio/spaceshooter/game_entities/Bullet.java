package com.jukaio.spaceshooter.game_entities;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.jukaio.spaceshooter.Jukebox;
import com.jukaio.spaceshooter.Vector2;
import com.jukaio.spaceshooter.component.Bitmap_Component;
import com.jukaio.spaceshooter.entities.Entity;
import com.jukaio.spaceshooter.Sound_ID;

public class Bullet extends Entity
{
    static final int BASE_COLOR = 0xFFFFFFFF;
    Vector2 m_step = new Vector2();

    public Bullet(boolean p_active, int p_bitmap_id, float p_bitmap_scale, Vector2 p_direction, float p_speed)
    {
        super(p_active);
        set_type(Entity_Type.BULLET);
        add_component(new Bitmap_Component(m_game, this, p_bitmap_id, p_bitmap_scale, BASE_COLOR));
        
        m_step.m_x = p_direction.m_x * p_speed;
        m_step.m_y = p_direction.m_y * p_speed;
        set_velocity(m_step);
    }
    
    public Bullet(final Bullet p_other)
    {
        super(p_other);
        m_step = p_other.m_step;
    }
    
    public void shoot(Vector2 p_from)
    {
        set_position(p_from);
        set_active(true);
    }
    
    @Override
    public void on_collision(Entity p_other)
    {
        super.on_collision(p_other);
        switch (p_other.get_type())
        {
            case UNKNOWN:
                break;
            case PLAYER:
                m_game.play_sounnd(Sound_ID.PLAYER_GET_HIT,
                                   Jukebox.MAX_VOLUME, 0);
                set_active(false);
                break;
            case BULLET:
                break;
            case UFO:
            case HUNTER:
                m_game.play_sounnd(Sound_ID.ENEMY_GET_HIT, Jukebox.MAX_VOLUME, 0);
                set_active(false);
                break;
        }
    }
    
    @Override
    protected void on_enable()
    {
    
    }
    
    @Override
    protected void on_disable()
    {
    
    }
    
    @Override
    public void update(float p_dt)
    {
        if(out_of_bounds(get_rectangle(), m_game.m_window_size))
            set_active(false);
        super.update(p_dt);
    }
    
    @Override
    public void render(Canvas p_canvas,
                Paint p_paint)
    {
        super.render(p_canvas, p_paint);
    }
    
    @Override
    public void destroy()
    {
        super.destroy();
        m_step = null;
    }
    
    private static boolean out_of_bounds(RectF p_entity_rect, Rect p_bounds)
    {
        if(p_entity_rect.top > p_bounds.bottom)
            return true;
        else if(p_entity_rect.bottom < 0)
            return true;
        if(p_entity_rect.left > p_bounds.right)
            return true;
        else return p_entity_rect.right < 0;
    }
    
    @Override
    public void reset()
    {
    
    }
}
