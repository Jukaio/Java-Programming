package com.jukaio.spaceshooter.game_entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.jukaio.spaceshooter.Jukebox;
import com.jukaio.spaceshooter.R;
import com.jukaio.spaceshooter.Utility;
import com.jukaio.spaceshooter.Vector2;
import com.jukaio.spaceshooter.component.Bitmap_Component;
import com.jukaio.spaceshooter.component.Weapon_Switch_Component;
import com.jukaio.spaceshooter.design.Player_Data;
import com.jukaio.spaceshooter.entities.Clamping_Entity;
import com.jukaio.spaceshooter.entities.Entity;
import com.jukaio.spaceshooter.Sound_ID;

public class Player extends Clamping_Entity
{
    private int m_max_hp = 0;
    private int m_hp = 0;

    Bitmap_Component m_bitmap_component = null;
    
    public Weapon m_current_weaapon = null;
    Weapon_Switch_Component m_weapons_component = null;
    float m_power_up_timer = 0;
    float p_power_up_duration = 0;
    
    Vector2 m_reset_position = Vector2.ZERO;
    float m_acceleration = 0.0f;
    float m_deceleration = 0.0f;
    float m_max_velocity = 0.0f;
    
    // Recovery Component
    boolean in_recovery = false;
    float m_recovery = 0.0f;
    float m_recovery_timer = 0.0f;
    float m_flick_rate = 0.0f;
    float m_flick_timer = 0.0f;
    
    // Damage Component
    static final int NUM_DAMAGE_SPRITES = 3;
    Bitmap[] m_damage_bitmaps = new Bitmap[NUM_DAMAGE_SPRITES];

    public Player(boolean p_active, int p_bitmap_id, Player_Data p_data)
    {
        super(p_active);
        set_type(Entity_Type.PLAYER);
        
        // damage bitmaps
        m_damage_bitmaps[0] = Utility.load_bitmap(m_game, R.drawable.damage_0);
        m_damage_bitmaps[0] = Utility.resize_bitmap(m_damage_bitmaps[0], p_data.m_scale);
        m_damage_bitmaps[1] = Utility.load_bitmap(m_game, R.drawable.damage_1);
        m_damage_bitmaps[1] = Utility.resize_bitmap(m_damage_bitmaps[1], p_data.m_scale);
        m_damage_bitmaps[2] = Utility.load_bitmap(m_game, R.drawable.damage_2);
        m_damage_bitmaps[2] = Utility.resize_bitmap(m_damage_bitmaps[2], p_data.m_scale);
        m_bitmap_component = new Bitmap_Component(m_game, this, p_bitmap_id, p_data.m_scale,
                                                   0xFFFFFFFF);
        m_weapons_component = new Weapon_Switch_Component(this, p_data);
        m_acceleration      = p_data.m_accel;
        m_deceleration      = p_data.m_deaccel;
        m_max_velocity      = p_data.m_max_vel;
        m_max_hp            = p_data.m_max_hp;
        m_hp                = m_max_hp;
        m_recovery          = p_data.m_recovery_time;
        m_flick_rate        = p_data.m_flicker_rate;
        p_power_up_duration = p_data.m_dual_gun.m_duration;
        
        
        add_component(m_bitmap_component); // Where to put base color? hmmm
        add_component(m_weapons_component);
        
        set_velocity(new Vector2(0, 0));
        m_reset_position = p_data.m_start_position;
        set_position(p_data.m_start_position);
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
        if(m_current_weaapon != null)
            m_current_weaapon.update(p_dt);

        if(in_recovery && m_recovery_timer < 0.0f)
        {
            in_recovery = false;
            m_bitmap_component.set_color(0xFFFFFFFF);
        }
        else if(in_recovery)
        {
            if(m_flick_timer < 0.0f)
            {
                m_bitmap_component.set_color(m_bitmap_component.get_color() ^ Color.WHITE);
                m_flick_timer = m_flick_rate;
            }
            m_recovery_timer -= p_dt;
            m_flick_timer -= p_dt;
        }
        
    
        if (m_game.m_move_right && !m_game.m_move_left)
        {
            if (get_velocity().m_x < m_max_velocity)
                set_velocity(new Vector2(get_velocity().m_x + m_acceleration,
                                         get_velocity().m_y));
        }
        else if (!m_game.m_move_right && m_game.m_move_left)
        {
            if (get_velocity().m_x > -m_max_velocity)
                set_velocity(new Vector2(get_velocity().m_x - m_acceleration,
                                         get_velocity().m_y));
        }
        else
        {
            if (get_velocity().m_x > 0)
                set_velocity(new Vector2(get_velocity().m_x - m_deceleration,
                                         get_velocity().m_y));
            else if (get_velocity().m_x < 0)
                set_velocity(new Vector2(get_velocity().m_x + m_deceleration,
                                         get_velocity().m_y));
        }
        super.update(p_dt);

        if(m_power_up_timer != 0)
        {
            if(m_power_up_timer > 0)
                m_power_up_timer -= p_dt;
            else
            {
                m_power_up_timer = 0;
                m_current_weaapon.reset();
                
            }
        }
    }

    @Override
    public void render(Canvas p_canvas, Paint p_paint)
    {
        if(m_current_weaapon != null)
            m_current_weaapon.render(p_canvas, p_paint);
        super.render(p_canvas, p_paint);
        if(m_hp < m_max_hp)
            p_canvas.drawBitmap(m_damage_bitmaps[NUM_DAMAGE_SPRITES - m_hp],
                                get_rectangle().left,
                                get_rectangle().top,
                                p_paint);
    }
    
    @Override
    public boolean is_colliding(Entity other)
    {
        m_current_weaapon.is_colliding(other);
        return super.is_colliding(other);
    }
    
    @Override
    public void on_collision(Entity p_other)
    {
        switch (p_other.get_type())
        {
            case UNKNOWN:
                break;
            case PLAYER:
                break;
            case BULLET:
                if(!in_recovery)
                {
                    m_hp--;
                    in_recovery = true;
                    m_recovery_timer = m_recovery;
                }
                if(m_hp <= 0)
                {
                    m_hp = 0; // Interpolate HP to 0 for GUI
                    m_game.play_sounnd(Sound_ID.PLAYER_DEATH, Jukebox.MAX_VOLUME, 0);
                    m_game.enter_game_over();
                    set_active(false);
                }
                break;
            case UFO:
            case HUNTER:
                m_hp -= m_hp;
                m_game.enter_game_over();
                m_game.play_sounnd(Sound_ID.PLAYER_DEATH, Jukebox.MAX_VOLUME, 0);
                set_active(false);
                break;
                
            case POWER_UP:
                on_power_up_collision(((Power_Up_Pick_Up)p_other).get_power_up_type());
                break;
        }
    }
    
    private void on_power_up_collision(Power_Up_Type p_type)
    {
        switch (p_type)
        {
            case DUAL_GUN:
                m_weapons_component.activate_dual_gun(p_power_up_duration);
                m_game.play_sounnd(Sound_ID.SWAP_TO_DUAL, Jukebox.MAX_VOLUME, 0);
                break;
                
            case HP_PLUS:
                if(m_hp < m_max_hp)
                    m_hp++;
                int test = m_game.play_sounnd(Sound_ID.HEALTH_UP, Jukebox.MAX_VOLUME, 0);
                break;
        }
    }

    @Override
    public void destroy()
    {
        super.destroy();
        m_bitmap_component = null;
        m_current_weaapon = null;
        m_weapons_component = null;
        m_reset_position = null;
        for(int i = 0; i < NUM_DAMAGE_SPRITES; i++)
        {
            m_damage_bitmaps[i].recycle();
            m_damage_bitmaps[i] = null;
        }
        m_damage_bitmaps = null;
    }
    
    @Override
    public void reset()
    {
        set_active(true);
        m_hp = m_max_hp;
        set_velocity(new Vector2(0, 0));
        set_position(m_reset_position);
        in_recovery = false;
        m_bitmap_component.set_color(Color.WHITE);
        m_weapons_component.activate_basic();
    }
}
