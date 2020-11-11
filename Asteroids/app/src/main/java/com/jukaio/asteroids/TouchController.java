package com.jukaio.asteroids;

import android.view.MotionEvent;
import android.view.View;

public class TouchController extends InputManager implements View.OnTouchListener{
    public TouchController(View view)
    {
        view.findViewById(R.id.keypad_left).setOnTouchListener(this);
        view.findViewById(R.id.keypad_right).setOnTouchListener(this);
        view.findViewById(R.id.keypad_a).setOnTouchListener(this);
        view.findViewById(R.id.keypad_b).setOnTouchListener(this);
    }
    
    @Override
    public boolean onTouch(final View v, final MotionEvent event)
    {
    
        final int action = event.getActionMasked();
        final int id = v.getId();
        
        if(action == MotionEvent.ACTION_DOWN)
        {
            if (id == R.id.keypad_left)
            {
                m_horizontal_factor -= 1;
            }
            else if(id == R.id.keypad_right)
            {
                m_horizontal_factor += 1;
            }
            if (id == R.id.keypad_a)
            {
                m_pressing_a[CURRENT] = true;
            }
            if (id == R.id.keypad_b)
            {
                m_pressing_b[CURRENT] = true;
            }
        }
        else if(action == MotionEvent.ACTION_UP)
        {
            // User released a key
            if (id == R.id.keypad_left)
            {
                m_horizontal_factor += 1;
            } else if (id == R.id.keypad_right)
            {
                m_horizontal_factor -= 1;
            }
            if (id == R.id.keypad_a)
            {
                m_pressing_a[CURRENT] = false;
            }
            if (id == R.id.keypad_b)
            {
                m_pressing_b[CURRENT] = false;
            }
        }
        return false;
    }
}