package com.jukaio.asteroids;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.SoundPool;

import java.io.IOException;

public class Jukebox
{
    private static SoundPool m_sound_pool = null;
    private static final int MAX_STREAMS = 5;
    public static final float MAX_VOLUME = 1.0f;
    private static final int PRIORITY = 0;
    public static final float STANDARD_RATE = 1.0f;

    Jukebox()
    {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN)
                .build();
    
        m_sound_pool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .setMaxStreams(MAX_STREAMS)
                .build();
        m_sound_pool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener()
        {
            @Override
            public void onLoadComplete(SoundPool p_soundPool, int p_i, int p_i1)
            {

            }
        });
    }
    
    public int load_sound(final Context p_context, String p_path)
    {
        int to_return = -1;
        try
        {
            AssetManager asset_manager = p_context.getAssets();
            AssetFileDescriptor descriptor;
            descriptor = asset_manager.openFd(p_path);
            to_return = m_sound_pool.load(descriptor, 1);
        }
        catch (IOException p_e)
        {
            p_e.printStackTrace();
        }
        return to_return;
    }
    
    public static int play(final int p_id, float p_volume, int p_loop)
    {
        if(p_id >= 0)
        {
            return m_sound_pool.play(p_id,
                                p_volume,
                                p_volume,
                                PRIORITY,
                                p_loop,
                                STANDARD_RATE);
        }
        return -1;
    }
    
    public static void stop(int p_stream_id)
    {
        m_sound_pool.stop(p_stream_id);
    }
    
    void destroy()
    {
        m_sound_pool.release();
        m_sound_pool = null;
    }
}