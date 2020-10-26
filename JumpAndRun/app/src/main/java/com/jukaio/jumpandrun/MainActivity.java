package com.jukaio.jumpandrun;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.jukaio.jumpandrun.input.Gamepad;
import com.jukaio.jumpandrun.input.Touch;
import com.jukaio.jumpandrun.input.TouchController;

public class MainActivity extends AppCompatActivity
{
    private Game m_game;
    
    private Gamepad m_gamepad = new Gamepad();
    private Touch m_touch = new Touch();
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_game = findViewById(R.id.game);
        
        
        m_touch.set_game(m_game);
        
        m_game.add_controls(new TouchController(findViewById(R.id.touchControl)));
        m_game.add_controls(m_gamepad);
        m_game.add_controls(m_touch);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        m_game.onStart();
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        m_game.onResume();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        m_touch.onTouchEvent(event);
        return true;
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        m_gamepad.onKeyDown(keyCode, event);
        return false;
    }
    
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        m_gamepad.onKeyUp(keyCode, event);
        return false;
    }
    

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if(!hasWindowFocus)
            return;

        View decor = getWindow().getDecorView();
        int visibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                         View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                         View.SYSTEM_UI_FLAG_FULLSCREEN;
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            visibility |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        else
        {
            visibility |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                          View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                          View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        decor.setSystemUiVisibility(visibility);
    }
    
    @Override
    protected void onPause()
    {
        super.onPause();
        m_game.onPause();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        m_game.onStop();
    }
    
    @Override
    protected void onDestroy()
    {
        m_game.onDestroy();
        super.onDestroy();
    }
}