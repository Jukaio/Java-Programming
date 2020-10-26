package com.jukaio.jumpandrun;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayer
{
    public final static float DEFAULT_MUSIC_VOLUME = 1.0f;
    
    private static ArrayList<MediaPlayer> m_music_list = null;
    private static MediaPlayer m_current = null;
    
    public static int get_track_count()
    {
        return m_music_list.size();
    }
    
    public MusicPlayer()
    {
        m_music_list = new ArrayList<>();
    }
    
    public int load_music(final Context p_context, String p_path)
    {
        try{
           MediaPlayer temp = new MediaPlayer();
           AssetFileDescriptor afd = p_context
   				.getAssets().openFd(p_path);
            temp.setDataSource(
                   afd.getFileDescriptor(),
                   afd.getStartOffset(),
                   afd.getLength());
            temp.setLooping(true);
            temp.setVolume(DEFAULT_MUSIC_VOLUME, DEFAULT_MUSIC_VOLUME);
            temp.prepare();
            m_music_list.add(temp);
            return m_music_list.size() - 1;
       }
       catch(IOException e)
       {
       }

        return -1;
    }
    
    public static void set_track(int p_track)
    {
        if(p_track < m_music_list.size())
            m_current = m_music_list.get(p_track);
    }
    
    private static void restart()
    {
        if(m_current != null)
            m_current.seekTo(0);
    }
    
    public static void play()
    {
        if(m_current != null)
            m_current.start();
    }
    
    public static void stop()
    {
        if(m_current != null)
        {
            m_current.pause();
            restart();
        }
    }
    
    void destroy()
    {
    
    }
}
