package com.jukaio.asteroids;

import android.opengl.Matrix;

import java.util.ArrayList;

public class HUD extends GLEntity {
    int FPS = 0;
    int HP = 0;
    int LVL = 0;
    Text fpsText;
    Text hpText;
    Text lvlText;

    private ArrayList<Text> texts = new ArrayList<Text>();

    final String s1 = s_game.getContext().getString(R.string.fps);
    final String s2 = s_game.getContext().getString(R.string.health);
    final String s3 = s_game.getContext().getString(R.string.level);
    final String s4 = s_game.getContext().getString(R.string.gameOver);
    final String s5 = s_game.getContext().getString(R.string.gameWin);
    final String s6 = s_game.getContext().getString(R.string.restart);
    
    private float[] m_hud_camera = new float[4*4];
    
    @Override
    Type get_type()
    {
        return Type.HUD;
    }
    
    public HUD() {
        fpsText = new Text(String.format(s1, FPS), 10, 10);
        texts.add(fpsText);
        hpText = new Text(String.format(s2, HP), 10, 20);
        texts.add(hpText);
        lvlText = new Text(String.format(s3, LVL), 10, 30);
        texts.add(lvlText);

        texts.add(new Text(s4, 10,40));
        texts.add(new Text(s5, 10,50));
        texts.add(new Text(s6, 10,60));
    }

    @Override
    public void update(double dt)
    {
        FPS = (int)(s_game.frame_count / s_game.frame_timer);
        HP = s_game.get_player().get_current_hp();
        LVL = s_game.m_astroid_level;
        fpsText.setString(String.format(s1,FPS));
        hpText.setString(String.format(s2,HP));
        lvlText.setString(String.format(s3,LVL));
    }

    @Override
    public void render(final float[] viewportMatrix)
    {
        Matrix.setIdentityM(m_hud_camera, 0);
        Matrix.orthoM(m_hud_camera, 0, 0, Game.WORLD_WIDTH, Game.WORLD_HEIGHT, 0, 0, 1.0f);
        
        for (int i = 0; i < texts.size(); i++) {
             texts.get(i).render(m_hud_camera);
        }
    }
}