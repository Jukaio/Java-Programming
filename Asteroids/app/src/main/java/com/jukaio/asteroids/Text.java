package com.jukaio.asteroids;

import android.opengl.Matrix;

public class Text extends GLEntity
{
    public static final GLPixelFont FONT = new GLPixelFont();
    public static float GLYPH_WIDTH = FONT.WIDTH;
    public static float GLYPH_HEIGHT = FONT.HEIGHT;
    public static float GLYPH_SPACING = GLYPH_WIDTH * 0.5f;
    public static float TEXT_SCALE = 0.75f;

    Mesh[] meshes = null;
    private float spacing = GLYPH_SPACING; //spacing between characters
    private float glyphWidth = GLYPH_WIDTH;
    private float glyphHeight = GLYPH_HEIGHT;

    public Text(final String p_string, final float p_x, final float p_y) {
        setString(p_string);
        set_position(p_x, p_y);
        setScale(TEXT_SCALE);
    }
    
    @Override
    Type get_type()
    {
        return Type.TEXT;
    }
    
    @Override
    public void render(final float[] viewportMatrix){
        final int OFFSET = 0;
        for(int i = 0; i < meshes.length; i++){
            if(meshes[i] == null){ continue; }
            Matrix.setIdentityM(m_model_matrix, OFFSET); //reset model matrix
            Matrix.translateM(m_model_matrix, OFFSET, get_position().m_x + (glyphWidth + spacing)*i, get_position().m_y, get_z());
            Matrix.scaleM(m_model_matrix, OFFSET, get_scale().m_x, get_scale().m_y, 1f);
            Matrix.multiplyMM(m_viewport_model_matrix, OFFSET, viewportMatrix, OFFSET,
                              m_model_matrix, OFFSET);
            GLManager.draw(meshes[i],
                           m_viewport_model_matrix,
                           m_colour);
        }
    }

    public void setScale(float factor){
        set_scale(factor, factor);
        spacing = GLYPH_SPACING * get_scale().m_x;
        glyphWidth = GLYPH_WIDTH * get_scale().m_x;
        glyphHeight = GLYPH_HEIGHT * get_scale().m_y;
        set_dimensions((glyphWidth + spacing)* meshes.length, glyphHeight);
    }

    public void setString(final String s){
        meshes = FONT.getString(s);
    }
}
