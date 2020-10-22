package com.jukaio.jumpandrun;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jukaio.jumpandrun.ecs.ECS;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.InputController;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.SharedComponentType;
import com.jukaio.jumpandrun.ecs.worldmodule.LevelZero;
import com.jukaio.jumpandrun.ecs.worldmodule.WorldType;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.Gravity;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.RenderCanvas;
import com.jukaio.jumpandrun.extramath.Vector2;
import com.jukaio.jumpandrun.inputhandling.InputManager;

public class Game extends SurfaceView implements Runnable, SurfaceHolder.Callback
{
    private final static String TAG                 = "GAME";
    private Thread              m_game_thread       = null;
    private volatile boolean    m_running           = false;

    private InputManager m_input_manager = null;
    private InputController m_input_shared_component = null;


    int m_width = 0;
    int m_height = 0;

    ECS m_ecs;

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

        m_ecs = new ECS();

        create_shared_components();
        
        m_ecs.add_world(new LevelZero(m_ecs, getContext()), WorldType.LEVEL_ONE);

        m_ecs.init();
        m_ecs.set_world_active(WorldType.LEVEL_ONE);
    }

    private void create_shared_components()
    {
        create_renderer();
        create_gravity();
        create_input_controller();
    }

    private void create_renderer()
    {
        RenderCanvas renderer = new RenderCanvas();
        renderer.m_holder = getHolder();
        renderer.m_holder.addCallback(this);
        renderer.m_paint = new Paint();
        int zoom = getResources().getInteger(R.integer.zoom);
        renderer.m_holder.setFixedSize(getResources().getInteger(R.integer.screen_width) / zoom,
                getResources().getInteger(R.integer.screen_height) / zoom);
        m_ecs.register_shared_component(renderer);
    }
    private void create_gravity()
    {
        Gravity gravity = new Gravity();
        gravity.m_direction = Vector2.DOWN;
        gravity.m_drag = 3.0f;
        m_ecs.register_shared_component(gravity);
    }
    public void create_input_controller()
    {
        m_input_shared_component = new InputController();
        m_ecs.register_shared_component(m_input_shared_component);
    }
    
    public void set_controls(InputManager p_controls)
    {
        m_input_manager = p_controls;
    }

    @Override
    public void run()
    {
        while(m_running)
        {
            input();
            update();
            render();
        }
    }
    
    private void input()
    {
        if(m_input_manager != null)
        {
            m_input_shared_component.m_prev_horizontal = m_input_shared_component.m_horizontal;
            m_input_shared_component.m_prev_vertical = m_input_shared_component.m_vertical;
            m_input_shared_component.m_prev_jump = m_input_shared_component.m_jump;
        
            m_input_shared_component.m_horizontal = m_input_manager.m_horizontal;
            m_input_shared_component.m_vertical = m_input_manager.m_vertical;
            m_input_shared_component.m_jump = m_input_manager.m_jump;
        }
    }

    private void update()
    {
        m_ecs.update();
    }

    private void render()
    {
        m_ecs.render();
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
        RenderCanvas renderer = m_ecs.get_shared_component(SharedComponentType.RENDER_CANVAS);
        renderer.m_holder.removeCallback(this);
        m_ecs.destroy();
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
