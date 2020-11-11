package com.jukaio.jumpandrun;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jukaio.jumpandrun.input.InputDevice;
import com.jukaio.jumpandrun.input.InputManager;
import com.jukaio.jumpandrun.world.WorldManager;

import java.io.IOException;

public class Game extends SurfaceView implements Runnable, SurfaceHolder.Callback
{
    private final static String TAG                 = "GAME";
    public static final boolean DEBUG_ON            = false;
    
    private class Renderer
    {
        public SurfaceHolder m_holder;
        public Canvas m_canvas;
        public Paint m_paint;
    }
    private Viewport            m_viewport          = null;
    private Thread              m_game_thread       = null;
    private volatile boolean    m_running           = false;
    private Renderer            m_renderer          = null;
    private InputManager        m_input_manager     = new InputManager();
    private WorldManager        m_world_manager     = new WorldManager();
    private Jukebox             m_jukebox           = new Jukebox();
    private MusicPlayer         m_music             = new MusicPlayer();
    
    public int m_width = 1280;
    public int m_height = 720;
    
    public Game(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "Game Constructor");
        initialise();
    }

    public Game(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d(TAG, "Game Constructor");
        initialise();
    }

    public Game(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Log.d(TAG, "Game Constructor");
        initialise();
    }

    public Game(Context context)
    {
        super(context);
        Log.d(TAG, "Game Constructor");
        initialise();
    }

    void initialise()
    {
        //Entity.GAME = this;

        create_renderer();
        //SoundID.LEVEL_ONE = m_jukebox.load_sound(getContext(), "SpaceBG.ogg");
        SoundID.GET_COIN = m_jukebox.load_sound(getContext(), "coin.wav");
        SoundID.GET_HIT = m_jukebox.load_sound(getContext(), "damage.wav");
        
        MusicID.LEVEL_ONE = m_music.load_music(getContext(), "level_one.wav");
        MusicID.LEVEL_TWO = m_music.load_music(getContext(), "level_two.wav");
        MusicID.LEVEL_THREE = m_music.load_music(getContext(), "level_three.wav");
        
        MusicPlayer.set_track(MusicID.LEVEL_ONE);
        MusicPlayer.play();
    
        String[] paths = null;
        try
        {
            paths = getContext().getAssets().list("levels");
        }
        catch (IOException p_e)
        {
            p_e.printStackTrace();
            throw new AssertionError("'levels' folder not found");
        }
        
        if(paths.length == 0)
        {
            throw new AssertionError("No levels found in level folder");
        }
        
        for(String file : paths)
        {
            m_world_manager.create_world("levels/"+file, getContext());
        }
        
        m_viewport = new Viewport(m_width, m_height, 0.0f, 184.0f);
        
        m_world_manager.set_current_world(0);
        m_world_manager.start();

    }

    private boolean lock_canvas(Renderer p_renderer)
    {
        if(!p_renderer.m_holder.getSurface().isValid())
            return false;

        p_renderer.m_canvas = p_renderer.m_holder.lockCanvas();
        
        return (p_renderer.m_canvas != null);
    }

    private void create_renderer()
    {
        m_renderer = new Renderer();
        m_renderer.m_holder = getHolder();
        m_renderer.m_holder.addCallback(this);
        m_renderer.m_paint = new Paint();
    }
    
    public void add_controls(InputDevice p_controls)
    {
        m_input_manager.add_device(p_controls);
    }

    @Override
    public void run()
    {
    
        double time_point = System.nanoTime();
        double delta_time = System.nanoTime() - time_point;
        final double ns = 1.0 / 1000000000.0;
        
        while(m_running)
        {
            delta_time = System.nanoTime() - time_point;
            time_point = System.nanoTime();
            
            update((float)(delta_time * ns));
            render();
        }
    }

    private void update(float p_dt)
    {
        m_world_manager.update(p_dt);
    }
    
    

    private void render()
    {
        if(!lock_canvas(m_renderer))
            return;
        m_renderer.m_canvas.drawColor(Color.BLACK);
        m_viewport.set_canvas(m_renderer.m_canvas);
        
        m_world_manager.render(m_viewport, m_renderer.m_paint);

        m_renderer.m_holder.unlockCanvasAndPost(m_renderer.m_canvas);
    }
    
    public void onStart()
    {
        Log.d(TAG, "onStart");
    
    }
    
    public void onResume()
    {
        Log.d(TAG, "onResume");
        m_running = true;
        m_game_thread = new Thread(this);
    }
    
    public void onPause()
    {
        Log.d(TAG, "onPause");
        m_running = false;
        while(m_game_thread.getState() != Thread.State.TERMINATED)
        {
            try
            {
                m_game_thread.join();
                return;
            }
            catch (InterruptedException p_e)
            {
                Log.d(TAG, Log.getStackTraceString(p_e.getCause()));
            }
        }
        
    }
    
    public void onStop()
    {
        Log.d(TAG, "onStop");
    
    }
    
    public void onDestroy()
    {
        Log.d(TAG, "onDestroy");
        m_renderer.m_holder.removeCallback(this);
        m_world_manager.destroy();
        m_game_thread       = null;
        m_input_manager     = null;
        m_world_manager     = null;
        m_jukebox           = null;
        m_music             = null;
        m_renderer.m_holder = null;
        m_renderer.m_canvas = null;
        m_renderer.m_paint  = null;
    }
    
    
    @Override
    public void surfaceCreated(@NonNull final SurfaceHolder p_surfaceHolder)
    {
        Log.d(TAG, "surfaceCreated");
        if(m_game_thread != null &&
           m_running)
        {
            Log.d(TAG, "Thread started");
            m_game_thread.start();
        }
    }
    
    @Override
    public void surfaceChanged(@NonNull final SurfaceHolder p_surfaceHolder,
                               final int p_format,
                               final int p_width,
                               final int p_height)
    {
        Log.d(TAG, "surfaceChanged");
        
        m_renderer.m_holder.setFixedSize(m_width, m_height);
        m_viewport.look_at(0, 0);
    }
    
    @Override
    public void surfaceDestroyed(@NonNull final SurfaceHolder p_surfaceHolder)
    {
        Log.d(TAG, "surfaceDestroyed");
    }
    
}
