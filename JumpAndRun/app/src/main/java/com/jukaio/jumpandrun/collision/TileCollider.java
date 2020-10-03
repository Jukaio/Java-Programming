package com.jukaio.jumpandrun.collision;

import android.graphics.Point;

import com.jukaio.jumpandrun.extramath.Line;
import com.jukaio.jumpandrun.extramath.Vector2;

import java.util.ArrayList;

public class TileCollider extends Collider
{
    public final static int START_POINT_IDX    = 3;     // 0000 0011
    public final static int TOP_LEFT_START     = 0;
    public final static int TOP_RIGHT_START    = 1;
    public final static int BOTTOM_LEFT_START  = 2;
    public final static int BOTTOM_RIGHT_START = 3;

    public final static int TOP_LEFT           = 1 << 2;// 0000 0100
    public final static int TOP_RIGHT          = 1 << 3;// 0000 1000
    public final static int BOTTOM_RIGHT       = 1 << 4;// 0001 0000
    public final static int BOTTOM_LEFT        = 1 << 5;// 0010 0000


    ArrayList<Point> m_bounds;

    TileCollider(int p_flag, Vector2 position, Vector2 dimensions)
    {
        super(ColliderType.TILE_COLLIDER);

        m_bounds = new ArrayList<>();

        int x = position.m_x.intValue();
        int y = position.m_y.intValue();
        int w = dimensions.m_x.intValue();
        int h = dimensions.m_y.intValue();

        // TODO: Layer needs "has collider" property
        int flag = p_flag;
        int index = 1;
        while (index < 15)
        {
            switch (flag & index)
            {
                case(TOP_LEFT):     m_bounds.add(new Point(x, y)); break;
                case(TOP_RIGHT):    m_bounds.add(new Point(x + w, y)); break;
                case(BOTTOM_LEFT):  m_bounds.add(new Point(x, y + h)); break;
                case(BOTTOM_RIGHT): m_bounds.add(new Point(x + w, y + h)); break;
            }
            index = index << 1;
        }
    }

}
