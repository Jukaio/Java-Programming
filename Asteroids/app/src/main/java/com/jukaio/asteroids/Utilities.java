package com.jukaio.asteroids;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

public abstract class Utilities {

    static Matrix matrix = new Matrix();
    private final static java.util.Random RNG = new java.util.Random();

    public static final double TO_DEG = 180.0/Math.PI;
    public static final double TO_RAD = Math.PI/180.0;

    public static Bitmap flipBitmap(final Bitmap src, final boolean horizon) {
       matrix.reset();
       final int cx = src.getWidth() / 2;
       final int cy = src.getHeight() / 2;
       if (horizon) {
           matrix.postScale(1, -1, cx, cy);
       } else {
           matrix.postScale(-1, 1, cx, cy);
       }
       return Bitmap.createBitmap(src, 0,0, src.getWidth(), src.getHeight(), matrix, true);
    }

    public static Bitmap scaleToTarget(final Bitmap src, final int targetHeight) {
        float ratio = targetHeight / (float) src.getHeight();
        int newHeight = (int) (src.getHeight() * ratio);
        int newWidth = (int) (src.getWidth() * ratio);
        return Bitmap.createScaledBitmap(src, newWidth, newHeight, true);
    }

    public static float wrap(float value, final float min, final float max) {
        if (value < min) {
            value = max;
        }
        if (value > max) {
            value = min;
        }
        return value;
    }

    public static float clamp(float value, final float min, final float max) {
        if (value > max) {
            value = max;
        }
        if (value < min) {
            value = min;
        }
        return value;
    }

    public static boolean coinFlip(){
        return RNG.nextFloat() > 0.5 ;
    }

    public static float nextFloat(){
        return RNG.nextFloat();
    }

    public static int nextInt( final int max){
        return RNG.nextInt(max);
    }

    public static int between( final int min, final int max){
        return RNG.nextInt(max-min)+min;
    }

    public static float between( final float min, final float max){
        return min+RNG.nextFloat()*(max-min);
    }

    public static void expect(final boolean condition, final String tag) {
        Utilities.expect(condition, tag, "Expectation was broken.");
    }
    public static void expect(final boolean condition, final String tag, final String message) {
        if(!condition) {
            Log.e(tag, message);
        }
    }
    public static void require(final boolean condition) {
        Utilities.require(condition, "Assertion failed!");
    }
    public static void require(final boolean condition, final String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

}
