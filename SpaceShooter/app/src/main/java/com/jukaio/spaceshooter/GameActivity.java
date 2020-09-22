package com.jukaio.spaceshooter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

public class GameActivity extends AppCompatActivity {

    Game m_game;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

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
    protected void onStop()
    {
        super.onStop();
        m_game.onStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        m_game.onDestroy();
        m_game = null;
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        m_game.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        m_game.onResume();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        m_game.onRestart();
    }
}