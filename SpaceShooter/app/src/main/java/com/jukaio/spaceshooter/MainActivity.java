package com.jukaio.spaceshooter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button start_button = findViewById(R.id.start_button);
        start_button.setOnClickListener(this);
        
        final TextView high_score = findViewById(R.id.high_score);
        SharedPreferences prefs = getSharedPreferences(Game.PREFS,
                                                       Context.MODE_PRIVATE);
        int highest_score = prefs.getInt(Game.HIGH_SCORE, 0);
        
        String string = getResources().getString(R.string.zero_filling);
        String to_draw = string.substring(0, string.length() - Integer.toString(highest_score).length());
        high_score.setText(getString(R.string.high_score_text_main_act) + " " + to_draw + highest_score);
    }

    @Override
    public void onClick(View view)
    {
        final Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
        finish();
    }
}