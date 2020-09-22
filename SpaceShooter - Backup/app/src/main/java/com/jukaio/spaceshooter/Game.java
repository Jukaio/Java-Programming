package com.jukaio.spaceshooter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;

import com.jukaio.spaceshooter.design.Config;
import com.jukaio.spaceshooter.design.Gameplay_Config;
import com.jukaio.spaceshooter.game_entities.Enemy_Spawner;
import com.jukaio.spaceshooter.entities.Entity;
import com.jukaio.spaceshooter.game_entities.Player;
import com.jukaio.spaceshooter.game_entities.Power_Up_Pick_Up;
import com.jukaio.spaceshooter.game_entities.Power_Up_Type;
import com.jukaio.spaceshooter.game_entities.Star;
import com.jukaio.spaceshooter.game_entities.Hunter;
import com.jukaio.spaceshooter.game_entities.Ufo;

import java.util.ArrayList;
import java.util.Random;

// Font: https://www.fontspace.com/darrell-flood
// Sounds: https://gamesupply.itch.io/

public class Game extends android.view.SurfaceView implements Runnable
{
    // Constants
    public static final String  TAG                 = "Game";
    public static final String  PREFS               = "com.jukaio.spaceshooter";
    public static final String  HIGH_SCORE          = "high_score";
    private static final double ns                  = 1.0 / 1000000000.0;
    
    // Game
    private Thread              m_game_thread       = null;
    private ArrayList<Entity>   m_entities          = null;
    public Game_State           m_game_state        = null;
    private GUI_Manager         m_gui_manager       = null;
    private Player              m_player            = null;
    private Jukebox             m_jukebox           = null;
    private Enemy_Spawner       m_spawner           = null;
    private int                 m_high_score        = 0;
    public int                  m_score             = 0;
    private volatile boolean    m_is_running        = false;
    
    private int                 m_kill_count        = 0;
    Power_Up_Pick_Up[]          m_pick_ups          = null;
    
    // Rendering
    private SurfaceHolder       m_holder            = null;
    SurfaceHolder.Callback      m_callback          = null;
    private Canvas              m_canvas            = null;
    private Paint               m_paint             = null;
    
    // Shared Data
    SharedPreferences           m_prefs             = null;
    SharedPreferences.Editor    m_editor            = null;
    
    // Input
    public boolean              m_move_left         = false;
    public boolean              m_move_right        = false;
    public boolean              m_shoot             = false;
    private float               m_input_block       = 0;
    
    // Others
    public Rect                 m_window_size       = null;
    public Random               m_rand              = null;
    private boolean             m_initialised      = false;
    
    @RequiresApi(api = Build.VERSION_CODES.R)
    public Game(Context context)
    {
        super(context);
        Entity.m_game = this;
        Gameplay_Config.GAME = this;
        
        setup_surface_holder();
        measure_window_Size(context);
        Gameplay_Config.assign_data(); // TODO: Instead of the method, XML Class
        
        initialise(context);
        create_star_field();
        construct();
        load_assets(context);
        order_other_entities();

        m_game_state = Game_State.GAMEPLAY;
        m_gui_manager.set_GUI(Game_State.GAMEPLAY);
        // TODO: CHANGE POWERUP STUFF
    }
    
    private void setup_surface_holder()
    {
        m_holder = getHolder();
        m_callback = new SurfaceHolder.Callback()
        {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder p_surfaceHolder)
            {
                m_initialised = true;
            }
        
            @Override
            public void surfaceChanged(@NonNull SurfaceHolder p_surfaceHolder, int p_i, int p_i1,
                                       int p_i2)
            {
            
            }
        
            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder p_surfaceHolder)
            {
                m_initialised = false;
            }
        };
    
        m_holder.addCallback(m_callback);
    }
    
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void measure_window_Size(Context p_context)
    {
        DisplayMetrics dm   = new DisplayMetrics();
        p_context.getDisplay().getMetrics(dm);
    
        m_window_size       = new Rect();
        m_window_size.left = 0;
        m_window_size.top = 0;
        m_window_size.right = dm.widthPixels;
        m_window_size.bottom = dm.heightPixels;
    }
    
    private void initialise(Context p_context)
    {
        m_paint         = new Paint();
        m_rand          = new Random();
        m_entities      = new ArrayList<Entity>();
        m_jukebox       = new Jukebox();
        m_gui_manager   = new GUI_Manager();
        m_prefs         = p_context.getSharedPreferences(PREFS,
                                                         Context.MODE_PRIVATE);
        m_editor        = m_prefs.edit();
        m_high_score    = m_prefs.getInt(HIGH_SCORE, 0);
        m_pick_ups      = new Power_Up_Pick_Up[Power_Up_Type.values().length];
    }
    
    private void create_star_field()
    {
        final int FULL_BYTE     = Byte.MAX_VALUE + Math.abs(Byte.MIN_VALUE);
        final int RAND_BOUNDS   = Config.GAME_STAR_RANDOM_RANGE_TO +
                                  Math.abs(Config.GAME_STAR_RANDOM_RANGE_FROM);
        for (int i = 0; i < Config.GAME_STAR_COUNT; i++)
        {
            int scale_rand = m_rand.nextInt(RAND_BOUNDS) + Config.GAME_STAR_RANDOM_RANGE_FROM;
            m_entities.add(new Star(true,
                                    new Vector2(m_rand.nextInt(m_window_size.right),
                                                m_rand.nextInt(m_window_size.bottom)),
                                    Color.argb(FULL_BYTE,
                                               m_rand.nextInt(FULL_BYTE),
                                               m_rand.nextInt(FULL_BYTE),
                                               m_rand.nextInt(FULL_BYTE)),
                                    (scale_rand * Config.GAME_STAR_DERIVATION_RATIO_SIZE) + Config.GAME_STAR_BASE_SIZE,
                                    Vector2.DOWN,
                                    (scale_rand * Config.GAME_STAR_DERIVATION_RATIO_SPEED) + Config.GAME_STAR_BASE_SPEED));
        }
    }
    
    private void construct()
    {
        m_player = new Player(true,
                              R.drawable.ship,
                              Gameplay_Config.PLAYER_DATA);
        m_spawner = new Enemy_Spawner(true);
        Hunter.POINTS = Gameplay_Config.HUNTER_DATA.m_points;
    
        m_spawner.add_pool(new Hunter(false,
                                      R.drawable.hunter_brown,
                                      Gameplay_Config.HUNTER_DATA),
                           Gameplay_Config.HUNTER_DATA.m_pool_size);
        Ufo.POINTS = Gameplay_Config.UFO_DATA.m_points;
        m_spawner.add_pool(new Ufo(false,
                                   R.drawable.ufo_red,
                                   Gameplay_Config.UFO_DATA),
                           Gameplay_Config.UFO_DATA.m_pool_size);
        m_gui_manager.add_GUI(new GUI_Gameplay(true));
        m_gui_manager.add_GUI(new GUI_Game_Over(true));
        
        m_pick_ups[0] = new Power_Up_Pick_Up(false, Vector2.DOWN, Config.POWER_UP_SPEED, Power_Up_Type.DUAL_GUN);
        m_pick_ups[1] = new Power_Up_Pick_Up(false, Vector2.DOWN, Config.POWER_UP_SPEED, Power_Up_Type.HP_PLUS);
    }
    
    private void load_assets(Context p_context)
    {
        // Load and set font
        m_paint.setTypeface(ResourcesCompat.getFont(p_context, R.font.retronoid));
        
        // Load sound effects
        Sound_ID.PLAYER_BASE_LASER  = m_jukebox.load_sound(p_context, "player_laser_base.wav");
        Sound_ID.PLAYER_GET_HIT     = m_jukebox.load_sound(p_context, "player_get_hit.wav");
        Sound_ID.PLAYER_DEATH       = m_jukebox.load_sound(p_context, "player_death.wav");
        Sound_ID.ENEMY_DEATH        = m_jukebox.load_sound(p_context, "ufo_death.wav");
        Sound_ID.ENEMY_GET_HIT      = m_jukebox.load_sound(p_context, "ufo_get_hit.wav");
        Sound_ID.ENEMY_BASE_LASER   = m_jukebox.load_sound(p_context, "ufo_laser_base.wav");
        Sound_ID.GAME_START         = m_jukebox.load_sound(p_context, "game_start.wav");
        Sound_ID.HEALTH_UP          = m_jukebox.load_sound(p_context, "health_up.wav");;
        Sound_ID.SWAP_TO_BASE       = m_jukebox.load_sound(p_context, "swap_to_base_gun.wav");;
        Sound_ID.SWAP_TO_DUAL       = m_jukebox.load_sound(p_context, "swap_to_dual_gun.wav");
    }
    
    private void order_other_entities()
    {
        m_entities.add(m_player);
        m_entities.add(m_spawner);
        m_entities.add(m_gui_manager);
        m_entities.add(m_pick_ups[0]);
        m_entities.add(m_pick_ups[1]);
    }
    
    
    public void update_grid(Vector2 p_position, boolean p_value)
    {
        m_spawner.update_grid(p_position, p_value);
    }

    public int play_sounnd(int p_id, float p_volume, int p_loop)
    {
        return m_jukebox.play(p_id, p_volume, p_loop);
    }
    
    public void stop_sound(int p_stream_id)
    {
       m_jukebox.stop(p_stream_id);
    }
    
    public void restart_game()
    {
        for (Entity u : m_entities)
        {
            u.reset();
        }
        m_player.reset();
        
        if(m_score > m_high_score)
        {
            m_editor.putInt(HIGH_SCORE, m_score);
            m_editor.apply();
            m_high_score = m_score;
        }
        m_score = 0;
        m_game_state = Game_State.GAMEPLAY;
        m_input_block = 0.0f;
    }
    
    
    public void enter_game_over()
    {
        m_game_state = Game_State.GAME_OVER;
        m_input_block = Config.INPUT_BLOCK;
    }
    
    public void add_score(int p_score)
    {
        m_score += p_score;
    }
    
    @Override
    public void run()
    {
        double time_point = System.nanoTime();
        double delta_time = System.nanoTime() - time_point;
        
        while (m_is_running)
        {
            time_point = System.nanoTime();
    
            if(m_initialised)
            {
                update((float) (delta_time * ns));
            }
            render();
            delta_time = System.nanoTime() - time_point;
        }
    }
    
    public void increase_kill_count(Vector2 p_position)
    {
        m_kill_count++;
        if(m_kill_count > Config.SPAWN_POWER_UP_TRIGGER_KILL_COUNT)
        {
            m_kill_count = 0;
            m_pick_ups[m_rand.nextInt(2)].spawn_at(p_position);
        }
    }
    
    private void check_collisions()
    {
        m_spawner.check_collisions(m_player);
        for(Power_Up_Pick_Up power_up : m_pick_ups)
        {
            if(power_up.get_active() &&
                    power_up.is_colliding(m_player))
            {
                m_player.on_collision(power_up);
                power_up.on_collision(m_player);
            }
        }
    }
    
    private void update(float p_dt)
    {
        if(m_game_state == Game_State.GAME_OVER)
        {
            if(m_input_block <= 0.0f && (m_move_left || m_move_right || m_shoot))
            {
                restart_game();
            }
            else if(m_input_block > 0.0f)
            {
                m_input_block -= p_dt;
            }
            
            m_gui_manager.update(p_dt);
            return;
        }
            
        for (Entity e : m_entities)
        {
            if (e.get_active())
                e.update(p_dt);
        }
        check_collisions();
    }

    
    private void render()
    {
        if (!get_and_lock_canvas())
            return;
    
        m_canvas.drawColor(Color.BLACK);
    
        for (Entity e : m_entities)
        {
            if (e.get_active())
                e.render(m_canvas,
                         m_paint);
        }
        
        m_holder.unlockCanvasAndPost(m_canvas);
    }
    
   
    
    private boolean get_and_lock_canvas()
    {
        if (!m_holder.getSurface().isValid())
            return false;
        
        m_canvas = m_holder.lockCanvas();
        return (m_canvas != null);
    }
    
    private void log_message(String p_message)
    {
        Log.d(TAG, p_message);
    }
    
    protected void onStart()
    {
    
        log_message("onStart");
    }
    
    protected void onStop()
    {
        log_message("onStop");
    }
    
    protected void onDestroy()
    {
        log_message("onDestroy");
        m_game_thread = null;
        for (Entity e : m_entities)
            e.destroy();
        m_jukebox.destroy();
        Entity.m_game = null;
        Gameplay_Config.GAME = null;
    }
    
    
    protected void onPause()
    {
        log_message("onPause");
        m_is_running = false;
        try
        {
            m_game_thread.join();
        } catch (InterruptedException e)
        {
            Log.d(TAG,
                  Log.getStackTraceString(e.getCause()));
        }
    }
    
    protected void onResume()
    {
        log_message("onResume");
        m_is_running = true;
        m_game_thread = new Thread(this);
        m_game_thread.start();
    }
    
    protected void onRestart()
    {
        log_message("onRestart");
    }
    
    
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int pointer_index = event.getActionIndex();
        float point_x = event.getX(pointer_index);
        float point_y = event.getY(pointer_index);
        
        switch (event.getActionMasked())
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                set_inputs(this,
                           point_x,
                           point_y,
                           true);
                break;
    
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_CANCEL:
                m_move_left = false;
                m_move_right = false;
                m_shoot = false;
                for (int i = 0; i < event.getPointerCount(); i++)
                {
                    point_x = event.getX(i);
                    point_y = event.getY(i);
                    set_inputs(this,
                               point_x,
                               point_y,
                               true);
                }
                break;
    
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                set_inputs(this,
                           point_x,
                           point_y,
                           false);
                break;
        }
        return true;
    }
    
    private final static void set_inputs(Game p_game, float p_point_x, float p_point_y,
                                         boolean p_set_to)
    {
        if (p_point_y > p_game.m_window_size.bottom * 0.5f)
        {
            if (p_point_x > p_game.m_window_size.right * 0.5f)
                p_game.m_move_right = p_set_to;
            else
                p_game.m_move_left = p_set_to;
        } else
            p_game.m_shoot = p_set_to;
    }
    
}
