package com.jukaio.spaceshooter.design;

public class Hunter_Data
{
    public int m_pool_size;

    public float m_scale;
    public float m_speed;
    public int m_max_hp;
    public int m_points;

    public class Weapon
    {
        public float m_fire_rate;
        public float m_reload_speed;
        public float m_trigger_rate;

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
            public Hunter_Data.Weapon.Ammo_Clip.Bullet m_bullet = new Hunter_Data.Weapon.Ammo_Clip.Bullet();
        }
        public Hunter_Data.Weapon.Ammo_Clip m_ammo_clip = new Hunter_Data.Weapon.Ammo_Clip();
    }
    public Hunter_Data.Weapon m_weapon = new Hunter_Data.Weapon();

}