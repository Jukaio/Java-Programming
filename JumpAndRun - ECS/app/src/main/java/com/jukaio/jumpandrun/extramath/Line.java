package com.jukaio.jumpandrun.extramath;

import android.graphics.Point;

public class Line
{
    public Point m_start;
    public Point m_end;

    public Line()
    {
        m_start = new Point(0, 0);
        m_end = new Point(0, 0);
    }

    public Line(Line p_other)
    {
        m_start = new Point(p_other.m_start);
        m_end = new Point(p_other.m_end);
    }

    public Line(int start_x, int start_y, int end_x, int end_y)
    {
        m_start = new Point(start_x, start_y);
        m_end = new Point(end_x, end_y);
    }
}
