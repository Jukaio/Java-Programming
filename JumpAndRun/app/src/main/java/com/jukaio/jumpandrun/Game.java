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

public class Game extends SurfaceView implements Runnable, SurfaceHolder.Callback
{
    private class Renderer
    {
        public SurfaceHolder m_holder;
        public Canvas m_canvas;
        public Paint m_paint;
    }

    private final static String TAG                 = "GAME";
    private Thread              m_game_thread       = null;
    private volatile boolean    m_running           = false;
    private Renderer            m_renderer          = null;
    

    private InputManager m_input_manager = null;
    private WorldManager m_world_manager = new WorldManager();

    int m_width = 0;
    int m_height = 0;

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


        create_shared_components();
        m_world_manager.create_world("level_two.xml", getContext());

    }

    private void create_shared_components()
    {
        create_renderer();
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
        int zoom = getResources().getInteger(R.integer.zoom);
        m_renderer.m_holder.setFixedSize(getResources().getInteger(R.integer.screen_width) / zoom,
                getResources().getInteger(R.integer.screen_height) / zoom);
    }
    
    public void set_controls(InputManager p_controls)
    {
        m_input_manager = p_controls;
    }

    @Override
    public void run()
    {
        m_world_manager.start();
    
        double time_point = System.nanoTime();
        double delta_time = System.nanoTime() - time_point;
        final double ns = 1.0 / 1000000000.0;
        
        while(m_running)
        {
            delta_time = System.nanoTime() - time_point;
            time_point = System.nanoTime();
            
            update((float)(delta_time * ns));
            Log.d("DELTA", Double.toString(delta_time * ns));
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

        m_world_manager.render(m_renderer.m_canvas, m_renderer.m_paint);



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
        m_game_thread = null;
    }
    
    
    @Override
    public void surfaceCreated(@NonNull final SurfaceHolder p_surfaceHolder)
    {
        Log.d(TAG, "surfaceCreated");
    }
    
    @Override
    public void surfaceChanged(@NonNull final SurfaceHolder p_surfaceHolder,
                               final int p_format,
                               final int p_width,
                               final int p_height)
    {
        Log.d(TAG, "surfaceChanged");
        
        m_width = p_width;
        m_height = p_height;
        //m_surface_holder.setFixedSize(m_width, m_height);
        
        if(m_game_thread != null &&
           m_running)
        {
            Log.d(TAG, "Thread started");
            m_game_thread.start();
        }
    }
    
    @Override
    public void surfaceDestroyed(@NonNull final SurfaceHolder p_surfaceHolder)
    {
        Log.d(TAG, "surfaceDestroyed");
    
    }
}
