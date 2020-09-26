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

import com.jukaio.jumpandrun.world.Tilemap.TileMap;

public class Game extends SurfaceView implements Runnable, SurfaceHolder.Callback
{
    private final static String TAG                 = "GAME";
    private Thread              m_game_thread       = null;
    private volatile boolean    m_running           = false;
    
    private SurfaceHolder       m_surface_holder    = null;
    private Paint               m_paint             = null;
    private Canvas              m_canvas            = null;
    
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

    TileMap m_map;

    void initialise()
    {
        m_surface_holder = getHolder();
        m_surface_holder.addCallback(this);
        int zoom = getResources().getInteger(R.integer.zoom);
        m_surface_holder.setFixedSize(getResources().getInteger(R.integer.screen_width) / zoom,
                                      getResources().getInteger(R.integer.screen_height) / zoom);

        m_map = new TileMap(getContext(), "tilemap.xml");
        m_paint = new Paint();
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

    }
    
    private void render()
    {
        if(!lock_canvas())
            return;
        m_canvas.drawColor(Color.BLACK);
    
        m_paint.setColor(Color.RED);
        m_canvas.drawLine(0, 0, getResources().getInteger(R.integer.screen_width),
                getResources().getInteger(R.integer.screen_height),  m_paint);

        m_map.draw(m_canvas, m_paint);

        m_surface_holder.unlockCanvasAndPost(m_canvas);
    }
    
    private boolean lock_canvas()
    {
        if(!m_surface_holder.getSurface().isValid())
            return false;
        m_canvas = m_surface_holder.lockCanvas();
        return (m_canvas != null);
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
