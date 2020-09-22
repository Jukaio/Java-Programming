package com.jukaio.spaceshooter.component;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.jukaio.spaceshooter.R;
import com.jukaio.spaceshooter.Vector2;
import com.jukaio.spaceshooter.design.Player_Data;
import com.jukaio.spaceshooter.entities.Entity;
import com.jukaio.spaceshooter.game_entities.Ammo_Clip;
import com.jukaio.spaceshooter.game_entities.Bullet;
import com.jukaio.spaceshooter.game_entities.Dual_Gun;
import com.jukaio.spaceshooter.game_entities.Entity_Type;
import com.jukaio.spaceshooter.game_entities.Player;
import com.jukaio.spaceshooter.game_entities.Weapon;

public class Weapons_Component extends Component
{
    Weapon m_weapon = null;
    Dual_Gun m_dual_gun = null;
    
    // If weapons are not supposed to be power-ups it can get exchanged with other forms of input
    float m_power_up_timer = 0;
    Player m_casted_entity = null;

    public Weapons_Component(Player p_entity, Player_Data p_data)
    {
        super(p_entity, "DUAL_GUN");
        
        if(p_entity.get_type() != Entity_Type.PLAYER)
            throw new AssertionError("Weapons_Component only compatible with Player");
        m_casted_entity = (Player)get_entity();
        
        init_base_weapon(p_data.m_weapon);
        init_dual_gun(p_data.m_dual_gun);
        p_entity.m_current_weaapon = m_weapon;
    }
    
    void init_base_weapon(Player_Data.Weapon p_data)
    {
        Ammo_Clip ammo_clip = new Ammo_Clip(true,
                                            p_data.m_ammo_clip.m_size,
                                            new Bullet(false,
                                                       R.drawable.base_laser,
                                                       p_data.m_ammo_clip.m_bullet.m_scale,
                                                       new Vector2(p_data.m_ammo_clip.m_bullet.m_direction_x,
                                                                   p_data.m_ammo_clip.m_bullet.m_direction_y),
                                                       p_data.m_ammo_clip.m_bullet.m_speed));
        m_weapon = new Weapon(true,
                              ammo_clip,
                              p_data.m_fire_rate,
                              p_data.m_reload_speed,
                              Vector2.ZERO);
        m_weapon.set_parent(m_casted_entity);
    }
    
    void init_dual_gun(Player_Data.Dual_Gun p_data)
    {
        Ammo_Clip other_ammo = new Ammo_Clip(true,
                                            p_data.m_ammo_clip.m_size,
                                            new Bullet(false,
                                                       R.drawable.dual_gun_laser,
                                                       p_data.m_ammo_clip.m_bullet.m_scale,
                                                       new Vector2(p_data.m_ammo_clip.m_bullet.m_direction_x,
                                                                   p_data.m_ammo_clip.m_bullet.m_direction_y),
                                                       p_data.m_ammo_clip.m_bullet.m_speed));
        m_dual_gun = new Dual_Gun(true,
                                other_ammo,
                              p_data.m_fire_rate,
                              p_data.m_reload_speed,
                              new Vector2(get_entity().get_dimensions().m_x * 0.25f, 0.0f));
        m_dual_gun.set_parent(m_casted_entity);
    }
    
    public void activate_dual_gun(float p_duration)
    {
        m_casted_entity.m_current_weaapon.reset();
        m_casted_entity.m_current_weaapon = m_dual_gun;
        m_casted_entity.m_current_weaapon.reset();
        m_power_up_timer = p_duration;
    }
    
    public void activate_basic()
    {
        m_casted_entity.m_current_weaapon.reset();
        m_casted_entity.m_current_weaapon = m_weapon;
        m_casted_entity.m_current_weaapon.reset();
    }
    
    @Override
    public void update(float p_dt)
    {
        if(m_power_up_timer != 0)
        {
            if(m_power_up_timer > 0)
                m_power_up_timer -= p_dt;
            else
            {
                m_power_up_timer = 0;
                activate_basic();
            }
        }
    }
    
    
    @Override
    public void render(Entity p_entity, Canvas p_canvas, Paint p_paint)
    {

    }
}
