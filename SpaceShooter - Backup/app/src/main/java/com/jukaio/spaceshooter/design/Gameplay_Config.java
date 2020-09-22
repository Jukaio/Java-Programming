package com.jukaio.spaceshooter.design;

import com.jukaio.spaceshooter.Game;
import com.jukaio.spaceshooter.Vector2;

public class Gameplay_Config
{
    static public Game GAME;

    public static Player_Data PLAYER_DATA = new Player_Data();
    public static Ufo_Data UFO_DATA = new Ufo_Data();
    public static Hunter_Data HUNTER_DATA = new Hunter_Data();
    
    // Technically doing it like this is completely redundant, but I want to receive the
    // window size. It's more convenient
    private static void assign_player_data()
    {
        // Player
        PLAYER_DATA.m_accel = 1.5f;
        PLAYER_DATA.m_deaccel = 1.0f;
        PLAYER_DATA.m_max_vel = 15.0f;
        PLAYER_DATA.m_scale = 2.0f;
        PLAYER_DATA.m_max_hp = 4;
        PLAYER_DATA.m_recovery_time = 1.0f;
        PLAYER_DATA.m_flicker_rate = 0.1f;
        PLAYER_DATA.m_start_position = new Vector2(GAME.m_window_size.right * 0.5f,
                                                   GAME.m_window_size.bottom - 150);
        
        // Weapon
        PLAYER_DATA.m_weapon.m_fire_rate = 0.4f;
        PLAYER_DATA.m_weapon.m_reload_speed = 0.8f;
        
        // Ammo Clip
        PLAYER_DATA.m_weapon.m_ammo_clip.m_size = 20;
        
        // Bullet
        PLAYER_DATA.m_weapon.m_ammo_clip.m_bullet.m_direction_x = 0.0f;
        PLAYER_DATA.m_weapon.m_ammo_clip.m_bullet.m_direction_y = -1.0f;
        PLAYER_DATA.m_weapon.m_ammo_clip.m_bullet.m_scale = 1.5f;
        PLAYER_DATA.m_weapon.m_ammo_clip.m_bullet.m_speed = 15.0f;
        
        ///////////////////////////////////////////////////////////////
        // Dual Gun
        PLAYER_DATA.m_dual_gun.m_fire_rate = 0.2f;
        PLAYER_DATA.m_dual_gun.m_reload_speed = 1.0f;
        PLAYER_DATA.m_dual_gun.m_duration = 10.0f;
        
        // Ammo Clip
        PLAYER_DATA.m_dual_gun.m_ammo_clip.m_size = 20;
        
        // Bullet
        PLAYER_DATA.m_dual_gun.m_ammo_clip.m_bullet.m_direction_x = 0.0f;
        PLAYER_DATA.m_dual_gun.m_ammo_clip.m_bullet.m_direction_y = -1.0f;
        PLAYER_DATA.m_dual_gun.m_ammo_clip.m_bullet.m_scale = 1.5f;
        PLAYER_DATA.m_dual_gun.m_ammo_clip.m_bullet.m_speed = 17.5f;
    }
    
private static void assign_hunter_data()
    {
        // Hunter
        HUNTER_DATA.m_pool_size = 25;
        HUNTER_DATA.m_speed = 2.0f;
        HUNTER_DATA.m_scale = 1.5f;
        HUNTER_DATA.m_max_hp = 2;
        HUNTER_DATA.m_points = 50;

        // Weapon
        HUNTER_DATA.m_weapon.m_fire_rate = 0.4f;
        HUNTER_DATA.m_weapon.m_reload_speed = 0.8f;
        HUNTER_DATA.m_weapon.m_trigger_rate = 2.0f;

        // Ammo Clip
        HUNTER_DATA.m_weapon.m_ammo_clip.m_size = 3;

        // Bullet
        HUNTER_DATA.m_weapon.m_ammo_clip.m_bullet.m_direction_x = 0.0f;
        HUNTER_DATA.m_weapon.m_ammo_clip.m_bullet.m_direction_y = 1.0f;
        HUNTER_DATA.m_weapon.m_ammo_clip.m_bullet.m_scale = 1.5f;
        HUNTER_DATA.m_weapon.m_ammo_clip.m_bullet.m_speed = 15.0f;
    }
    
    private static void assign_ufo_data()
    {
        UFO_DATA.m_pool_size = 25;
        UFO_DATA.m_speed = 2.0f;
        UFO_DATA.m_scale = 1.5f;
        UFO_DATA.m_max_hp = 2;
        UFO_DATA.m_points = 25;
    }
    
    public static void assign_data()
    {
        assign_player_data();
        assign_hunter_data();
        assign_ufo_data();
    }
    
}
