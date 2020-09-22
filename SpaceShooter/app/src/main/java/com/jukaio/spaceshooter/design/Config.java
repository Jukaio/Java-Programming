package com.jukaio.spaceshooter.design;

/* Gameplay dependent entity data is getting stored and adjusted
    in the Data Configfuration Class to raise readability  */

public class Config
{
    // Input
    public static final float INPUT_BLOCK = 1.0f;
    
    // Stars
    public static final int     GAME_STAR_COUNT                     = 50;
    public static final float   GAME_STAR_BASE_SIZE                 = 5.0f;
    public static final float   GAME_STAR_BASE_SPEED                = 10.0f;
    public static final float   GAME_STAR_DERIVATION_RATIO_SIZE     = 0.4f;
    public static final float   GAME_STAR_DERIVATION_RATIO_SPEED    = 0.25f;
    public static final int     GAME_STAR_RANDOM_RANGE_FROM         = -25;
    public static final int     GAME_STAR_RANDOM_RANGE_TO           = 25;
    
    // GUI
    public static final float GUI_GAMEPLAY_TESXT_SIZE               = 92;
    public static final float GUI_GAMEPLAY_TEXT_PADDING_LEFT        = 40;
    public static final float GUI_GAMEPLAY_TEXT_PADDING_TOP         = 10;
    public static final float GUI_GAME_OVER_UPPER_TEXT              = 200;
    public static final float GUI_GAME_OVER_LOWER_TEXT              = 92;
    public static final float GUI_GAME_OVER_SCORE_TEXT              = 124;
    
    // Spawner
    public static final float SPANWER_MOVE_RATE                     = 8.0f;
    public static final float SPAWNER_SPAWN_RATE                    = 2.0f;
    
    // Grid
    public static final int GRID_CELL_SIZE                          = 192;
    
    // Power-Ups
    public static final int SPAWN_POWER_UP_TRIGGER_KILL_COUNT       = 5;
    public static final float POWER_BITMAP_SCALE                    = 1.5f;
    public static final float POWER_UP_SPEED                        = 4.0f;
    
}