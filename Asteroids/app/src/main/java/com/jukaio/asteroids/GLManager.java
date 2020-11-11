package com.jukaio.asteroids;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

public class GLManager {
    public final static String TAG = "GLManager";
    private static final int OFFSET = 0;

    private static int m_gl_program_handle;
    private static int m_color_uniform_handle;
    private static int m_position_attribute_handle;
    private static int m_MVPMatrix_handle;


    public static void checkGLError(final String func){
        int error;
        while((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR){
            Log.e(func, "glError " + error);
        }
    }

    private static int compileShader(final int type, final String shaderCode){
        assert(type == GLES20.GL_VERTEX_SHADER || type == GLES20.GL_FRAGMENT_SHADER);
        final int handle = GLES20.glCreateShader(type); // Create a shader object and store its handle
        GLES20.glShaderSource(handle, shaderCode); // Pass in the code
        GLES20.glCompileShader(handle); // then compile the shader
        Log.d(TAG, "Shader Compile Log: \n" + GLES20.glGetShaderInfoLog(handle));
        checkGLError("compileShader");
        return handle;
    }

    private static int linkShaders(final int vertexShader, final int fragmentShader){
        final int handle = GLES20.glCreateProgram();
        GLES20.glAttachShader(handle, vertexShader);
        GLES20.glAttachShader(handle, fragmentShader);
        GLES20.glLinkProgram(handle);
        Log.d(TAG, "Shader Link Log: \n" + GLES20.glGetProgramInfoLog(handle));
        checkGLError("linkShaders");
        return handle;
    }

    public static String get_string_from_file(Context p_context, String p_path)
    {
        try
        {
            InputStream is = p_context.getAssets().open(p_path);
            InputStreamReader reader = new InputStreamReader(is);
            
            char[] buffer = new char[is.available()];
            int count = reader.read(buffer);
            Log.d("Loaded shader file ", p_path);

            return String.copyValueOf(buffer, 0, count);
        } catch (FileNotFoundException p_e)
        {
            p_e.printStackTrace();
        } catch (IOException p_e)
        {
            p_e.printStackTrace();
        }
        return null;
    }

    public static void buildProgram(Context p_context, String p_vertex_path, String p_fragment_path)
    {
        final int vertex = compileShader(GLES20.GL_VERTEX_SHADER, get_string_from_file(p_context, p_vertex_path));
        final int fragment = compileShader(GLES20.GL_FRAGMENT_SHADER, get_string_from_file(p_context, p_fragment_path));
        m_gl_program_handle = linkShaders(vertex, fragment);
        
        GLES20.glDeleteShader(vertex);
        GLES20.glDeleteShader(fragment);

        m_position_attribute_handle = GLES20.glGetAttribLocation(m_gl_program_handle, "position");
        m_color_uniform_handle = GLES20.glGetUniformLocation(m_gl_program_handle, "color");
        m_MVPMatrix_handle = GLES20.glGetUniformLocation(m_gl_program_handle, "modelViewProjection");
        //activate the program
        GLES20.glLineWidth(5f);
        GLES20.glUseProgram(m_gl_program_handle);
        checkGLError("buildProgram");
    }

    private static void setModelViewProjection(final float[] modelViewMatrix) {
        final int COUNT = 1;
        final boolean TRANSPOSED = false;
        GLES20.glUniformMatrix4fv(m_MVPMatrix_handle, COUNT, TRANSPOSED, modelViewMatrix, OFFSET);
        checkGLError("setModelViewProjection");
    }

    public static void draw(final Mesh p_model, final float[] p_model_view_matrix, final float[] p_color){
        setShaderColor(p_color);
        uploadMesh(p_model.m_vertex_buffer);
        setModelViewProjection(p_model_view_matrix);
        drawMesh(p_model.m_draw_mode, p_model.m_vertex_count);
    }

    private static void uploadMesh(final FloatBuffer vertexBuffer) {
        final boolean NORMALIZED = false;
        // enable a handle to the vertices
        GLES20.glEnableVertexAttribArray(GLManager.m_position_attribute_handle);
        // prepare the vertex coordinate data
        GLES20.glVertexAttribPointer(GLManager.m_position_attribute_handle, Mesh.COORDS_PER_VERTEX,
                                     GLES20.GL_FLOAT, NORMALIZED, Mesh.VERTEX_STRIDE,
                                     vertexBuffer);
        checkGLError("uploadMesh");
    }

    private static void setShaderColor(final float[] color) {
        final int COUNT = 1;
        // set color for drawing the pixels of our geometry
        GLES20.glUniform4fv(GLManager.m_color_uniform_handle, COUNT, color, OFFSET);
        checkGLError("setShaderColor");
    }

    private static void drawMesh(final int drawMode, final int vertexCount) {
        assert(drawMode == GLES20.GL_TRIANGLES
               || drawMode == GLES20.GL_LINES
               || drawMode == GLES20.GL_POINTS);
        // draw the previously uploaded vertices
        GLES20.glDrawArrays(drawMode, OFFSET, vertexCount);
        // disable vertex array
        GLES20.glDisableVertexAttribArray(GLManager.m_position_attribute_handle);
        checkGLError("drawMesh");
    }

}
