package com.jukaio.spaceshooter.design;

import com.jukaio.spaceshooter.Vector2;

public class Player_Data
{
    public float m_accel;
    public float m_deaccel;
    public float m_max_vel;
    public float m_scale;
    public Vector2 m_start_position;
    
    public float m_recovery_time;
    public float m_flicker_rate;
    
    public int m_max_hp;
    
    public class Weapon
    {
        public float m_fire_rate;
        public float m_reload_speed;
        
        public class Ammo_Clip
        {
            public int m_size;
            
            public class Bullet
            {
                public float m_direction_x;
                public float m_direction_y;
                public float m_speed;
                public float m_scale;
            }
            public Bullet m_bullet = new Bullet();
        }
        public Ammo_Clip m_ammo_clip = new Ammo_Clip();
    }
    public class Dual_Gun
    {
        public float m_fire_rate;
        public float m_reload_speed;
        public float m_duration;
        
        public class Ammo_Clip
        {
            public int m_size;
    
            public class Bullet
            {
                public float m_direction_x;
                public float m_direction_y;
                public float m_speed;
                public float m_scale;
            }
    
            public Dual_Gun.Ammo_Clip.Bullet m_bullet = new Dual_Gun.Ammo_Clip.Bullet();
        }
    
        public Dual_Gun.Ammo_Clip m_ammo_clip = new Dual_Gun.Ammo_Clip();
    }
    public Weapon m_weapon = new Weapon();
    public Dual_Gun m_dual_gun = new Dual_Gun();
}
