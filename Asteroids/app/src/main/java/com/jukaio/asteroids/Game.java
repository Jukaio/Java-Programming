package com.jukaio.asteroids;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Game extends GLSurfaceView  implements GLSurfaceView.Renderer
{

    private static final int BG_COLOR = Color.rgb(135, 200, 230);

    private static int STAR_COUNT = 100;
    private ArrayList<Star> m_stars = new ArrayList<>();
    private Player m_player;
    private Border m_border;

    public int m_astroid_level = 0;
    //private ArrayList<Asteroid> m_asteroids = new ArrayList<>();

    private static final int BULLET_COUNT = (int)(Bullet.TIME_TO_LIVE/Player.TIME_BETWEEN_SHOTS)+1;
    private ArrayList<Bullet> m_bullets = new ArrayList<>();

    private HUD m_hud = null;
    
    private MusicPlayer m_music_player;
    private Jukebox m_sounds;
    
    AsteroidPool m_asteroids = null;

    Random m_random = new Random();

    //camera stuff
    Camera m_camera = null;
    static float WORLD_WIDTH = 160f; //all dimensions are in meters
    static float WORLD_HEIGHT = 90f;
    static float METERS_TO_SHOW_X = 160f; //160m x 90m, the entire game world in view
    static float METERS_TO_SHOW_Y = 90f; //TODO: calculate to match screen aspect ratio
    // Create the projection Matrix. This is used to project the scene onto a 2D viewport.
    private float[] m_viewport_matrix = new float[4*4];

    public static long SECOND_IN_NANOSECONDS = 1000000000;
    public static long MILLISECOND_IN_NANOSECONDS = 1000000;
    public static float NANOSECONDS_TO_MILLISECONDS = 1.0f / MILLISECOND_IN_NANOSECONDS;
    public static float NANOSECONDS_TO_SECONDS = 1.0f / SECOND_IN_NANOSECONDS;

    public InputManager m_inputs = new InputManager(); //empty but valid default

    public Game(Context context) {
        super(context);
        init();
    }

    public Game(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Player get_player()
    {
        return m_player;
    }

    void init() {
        GLEntity.s_game = this;
        setEGLContextClientVersion(2); //select OpenGL ES 2.0
        setPreserveEGLContextOnPause(true); //context *may* be preserved and thus *may* avoid slow reloads when switching apps.
        // we always re-create the OpenGL context in onSurfaceCreated, so we're safe either way.

        m_music_player = new MusicPlayer();
        m_sounds = new Jukebox();

        SoundID.SHOOT = m_sounds.load_sound(getContext(), "player_shoot.wav");
        SoundID.GET_HIT = m_sounds.load_sound(getContext(), "player_get_hit.wav");
        SoundID.DEATH = m_sounds.load_sound(getContext(), "player_death.wav");
        
        MusicID.MUSIC = m_music_player.load_music(getContext(), "music.wav");
        
        MusicPlayer.set_track(MusicID.MUSIC);
        MusicPlayer.play();
        
        m_hud = new HUD();
        setRenderer(this);
    }

    public void setControls(final InputManager input)
    {
        m_inputs = input;
    }
    
    private void spawn_next_wave()
    {
        m_astroid_level++;
        for(int i = 0; i < m_astroid_level; i++)
         {
             m_asteroids.spawn(m_random.nextInt((int)WORLD_WIDTH), m_random.nextInt((int)WORLD_HEIGHT));
         }
    }

    @Override
    public void onSurfaceCreated(final GL10 gl10, final EGLConfig eglConfig)
    {
        float red = Color.red(BG_COLOR) / 255f;
        float green = Color.green(BG_COLOR) / 255f;
        float blue = Color.blue(BG_COLOR) / 255f;
        float alpha = 1f;
        GLManager.buildProgram(getContext(), "vertex.txt", "fragment.txt"); //compile, link and upload our GL program
        //GLES20.glClearColor(BG_COLOR[0], BG_COLOR[1], BG_COLOR[2], BG_COLOR[3]); //set clear color
        GLES20.glClearColor(red, green, blue, alpha);

        Random r = m_random;
        
        for(int i = 0; i < STAR_COUNT; i++)
        {
            m_stars.add(new Star(r.nextInt((int)WORLD_WIDTH), r.nextInt((int)WORLD_HEIGHT)));
        }

        m_asteroids = new AsteroidPool(20, 12, 12, 12);
        spawn_next_wave();
        
        for(int i = 0; i < BULLET_COUNT; i++)
        {
            m_bullets.add(new Bullet());
        }

        m_player = new Player(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 1.0f, 1.0f, 3);
        m_border = new Border(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, WORLD_WIDTH, WORLD_HEIGHT);
        
        m_camera = new Camera((int)WORLD_WIDTH, (int)WORLD_HEIGHT, METERS_TO_SHOW_X, 0);
    }

    @Override
    public void onSurfaceChanged(final GL10 gl10, final int width, final int height)
    {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(final GL10 unused)
    {
        update(); //TODO: move updates away from the render thread...
        render();
    }

    //trying a fixed time-step with accumulator, courtesy of
//   https://gafferongames.com/post/fix_your_timestep/
    final double dt = 1.0 / 60.0;
    double accumulator = 0.0;
    double currentTime = System.nanoTime()*NANOSECONDS_TO_SECONDS;

    double frames_per_second_timer = 0.0;
    long frame_count = 0; // should not exceed for a veeery long time
    double frame_timer = 0;
    
    private void update()
    {
        final double newTime = System.nanoTime() * NANOSECONDS_TO_SECONDS;
        final double frameTime = newTime - currentTime;
        currentTime = newTime;
        
        if(frame_timer > 1.0)
        {
            frame_timer = 0;
            frame_count = 0;
        }
        frame_timer += dt;
        
        accumulator += frameTime;
        while(accumulator >= dt)
        {
            m_asteroids.update(dt);
            
            for(final Bullet b : m_bullets)
            {
                if(b.m_active)
                    b.update(dt);
            }
            m_player.update(dt);
            m_hud.update(dt);
            collisionDetection();
            
            accumulator -= dt;
            m_inputs.update();
        }
        
        if(m_asteroids.m_active_asteroids.size() <= 0)
        {
            spawn_next_wave();
        }
        m_camera.lookAt(m_player.center_x() - (WORLD_WIDTH * 0.5f), m_player.center_y() - (WORLD_HEIGHT * 0.5f));
    }

    private void render()
    {
        frame_count++;
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT); //clear buffer to background color
        //setup a projection matrix by passing in the range of the game world that will be mapped by OpenGL to the screen.
        //TODO: encapsulate this in a Camera-class, let it "position" itself relative to an entity
        m_camera.get_projection_matrix(m_viewport_matrix);

        m_border.render(m_viewport_matrix);
        m_asteroids.render(m_viewport_matrix);
        
        for(final Star s : m_stars){
            s.render(m_viewport_matrix);
        }
        m_player.render(m_viewport_matrix);

        for(final Bullet b : m_bullets)
        {
            if(b.m_active)
            {
                b.render(m_viewport_matrix);
            }
        }
        
        m_hud.render(m_viewport_matrix);
    }

    public boolean maybe_fire_bullet(final GLEntity source){
        for(final Bullet b : m_bullets) {
            if(!b.m_active)
            {
                b.fire_from(source);
                return true;
            }
        }
        return false;
    }

    private void collisionDetection()
    {
    
        for(final Bullet b : m_bullets)
        {
            if(b.is_inactive()){ continue; } //skip dead bullets
            for(final Asteroid a : m_asteroids.m_active_asteroids)
            {
                if(b.isColliding(a))
                {
                   if(a.is_inactive()){continue;}
                    b.on_collision(a); //notify each entity so they can decide what to do
                    a.on_collision(b);
                }
            }
        }
        for(final Asteroid a : m_asteroids.m_active_asteroids)
        {
            if(a.is_inactive()){continue;}
            if(m_player.isColliding(a))
            {
                m_player.on_collision(a);
                a.on_collision(m_player);
            }
        }
        
     
    }


}
