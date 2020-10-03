package com.jukaio.jumpandrun;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jukaio.jumpandrun.ecs.ECS;
import com.jukaio.jumpandrun.ecs.WorldType;
import com.jukaio.jumpandrun.ecs.componentmodule.components.Component;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.Gravity;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.RenderCanvas;
import com.jukaio.jumpandrun.ecs.componentmodule.components.SourceXML;
import com.jukaio.jumpandrun.ecs.componentmodule.components.Tileset;
import com.jukaio.jumpandrun.ecs.componentmodule.components.tilemapcomponents.Grid;
import com.jukaio.jumpandrun.ecs.componentmodule.components.tilemapcomponents.TileMap;
import com.jukaio.jumpandrun.ecs.componentmodule.components.tilemapcomponents.TileMapCollider;
import com.jukaio.jumpandrun.ecs.entitymodule.Entity;
import com.jukaio.jumpandrun.ecs.systemmodule.TileMapRenderer;
import com.jukaio.jumpandrun.extramath.Vector2;

public class Game extends SurfaceView implements Runnable, SurfaceHolder.Callback
{
    private final static String TAG                 = "GAME";
    private Thread              m_game_thread       = null;
    private volatile boolean    m_running           = false;

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

        create_worlds();
        construct_world();

        add_systems();

        m_ecs.init();
        m_ecs.set_world_active(WorldType.LEVEL_ONE);
    }

    private void create_shared_components()
    {
        create_renderer();
        create_gravity();
    }

    private void create_worlds()
    {
        m_ecs.create_world(WorldType.LEVEL_ONE); // Create a world
    }
    private void construct_world()
    {
        create_tilemap();
    }
    private void create_tilemap()
    {
        // Gather a free entity
        Entity entity = m_ecs.create_entity();

        // Create a pack of components - In this case 5 components
        Component[] components = new Component[5];
        SourceXML source = new SourceXML();
        source.m_source = "tilemap.xml";
        source.m_context = getContext();
        components[0] = source;
        components[1] = new TileMap();
        components[2]= new Tileset();
        components[3] = new Grid();
        components[4] = new TileMapCollider();

        // Register the components
        m_ecs.register_components(m_ecs.components_to_signature(components));

        // Associate components with entity
        m_ecs.add_components(entity, components);

        // Place entity in world
        m_ecs.place_entity(entity, WorldType.LEVEL_ONE);
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
        gravity.m_drag = 0.9f;
        m_ecs.register_shared_component(gravity);
    }
    void add_systems()
    {
        m_ecs.add_system(new TileMapRenderer());
    }

    @Override
    public void run()
    {
        while(m_running)
        {
            update();
            render();
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
