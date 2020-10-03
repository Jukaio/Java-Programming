package com.jukaio.jumpandrun.ecs.componentmodule.components;

public class Player extends Component
{
    @Override
    public ComponentType get_type() {
        return ComponentType.PLAYER;
    }

    public int m_tile_id;
}
