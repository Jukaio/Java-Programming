package com.jukaio.jumpandrun;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity
{
    private Game m_game;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_game = new Game(this);
        setContentView(m_game);
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
    protected void onPause()
    {
        m_game.onPause();
        super.onPause();
    }
    
    @Override
    protected void onStop()
    {
        m_game.onStop();
        super.onStop();
    }
    
    @Override
    protected void onDestroy()
    {
        m_game.onDestroy();
        super.onDestroy();
    }
}