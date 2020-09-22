package com.jukaio.spaceshooter.game_entities;

import com.jukaio.spaceshooter.Jukebox;
import com.jukaio.spaceshooter.Vector2;
import com.jukaio.spaceshooter.entities.Entity;
import com.jukaio.spaceshooter.Sound_ID;

public class Enemy_Weapon extends Weapon
{
    float m_ai_trigger_timer = 0.0f;
    float m_ai_trigger_cooldown = 0.0f;

    public Enemy_Weapon(Enemy_Weapon p_other)
    {
        super(p_other);
        m_ai_trigger_cooldown = p_other.m_ai_trigger_cooldown;
        m_ai_trigger_timer = p_other.m_ai_trigger_timer;
    }
    
    @Override
    public void reset()
    {
        super.reset();
        m_ai_trigger_timer = m_ai_trigger_cooldown;
    }
    
    public Enemy_Weapon(boolean p_active, Ammo_Clip p_ammo_clip, float p_fire_rate,
                        float p_reload_speed, float p_ai_trigger_cooldown)
    {
        super(p_active,
              p_ammo_clip,
              p_fire_rate,
              p_reload_speed,
              Vector2.ZERO);
        m_ai_trigger_cooldown = p_ai_trigger_cooldown;
        m_ai_trigger_timer = m_ai_trigger_cooldown;
    }
    
    @Override
    public boolean trigger(float p_dt)
    {
        if(m_ai_trigger_timer < 0.0f)
        {
            Entity.m_game.play_sounnd(Sound_ID.ENEMY_BASE_LASER, Jukebox.MAX_VOLUME, 0);
            m_ai_trigger_timer = m_ai_trigger_cooldown;
           return true;
        }
        m_ai_trigger_timer -= p_dt;
        return false;
    }
}
