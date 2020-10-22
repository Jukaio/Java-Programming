package com.jukaio.jumpandrun.ecs.worldmodule;

import android.content.Context;

import com.jukaio.jumpandrun.ecs.ECS;
import com.jukaio.jumpandrun.ecs.componentmodule.components.Component;
import com.jukaio.jumpandrun.ecs.componentmodule.components.AssetLoaderXML;
import com.jukaio.jumpandrun.ecs.componentmodule.components.MovementSensors;
import com.jukaio.jumpandrun.ecs.componentmodule.components.RectangleCollider;
import com.jukaio.jumpandrun.ecs.componentmodule.components.physics.Jump;
import com.jukaio.jumpandrun.ecs.componentmodule.components.physics.Kinematic;
import com.jukaio.jumpandrun.ecs.componentmodule.components.tilemap.Tileset;
import com.jukaio.jumpandrun.ecs.componentmodule.components.Transform;
import com.jukaio.jumpandrun.ecs.componentmodule.components.tilemap.Grid;
import com.jukaio.jumpandrun.ecs.componentmodule.components.tilemap.SingleTile;
import com.jukaio.jumpandrun.ecs.componentmodule.components.tilemap.TileMap;
import com.jukaio.jumpandrun.ecs.componentmodule.components.tilemap.TileMapCollider;
import com.jukaio.jumpandrun.ecs.componentmodule.sharedcomponents.SharedComponentType;
import com.jukaio.jumpandrun.ecs.entitymodule.Entity;
import com.jukaio.jumpandrun.ecs.systemmodule.ApplyForces;
import com.jukaio.jumpandrun.ecs.systemmodule.InputHandler;
import com.jukaio.jumpandrun.ecs.systemmodule.Jumping;
import com.jukaio.jumpandrun.ecs.systemmodule.MovermentSensorRendererDebug;
import com.jukaio.jumpandrun.ecs.systemmodule.MovementSensorsTilemapCollision;
import com.jukaio.jumpandrun.ecs.systemmodule.PrepareJump;
import com.jukaio.jumpandrun.ecs.systemmodule.SingleTileRenderer;
import com.jukaio.jumpandrun.ecs.systemmodule.TileMapCore;
import com.jukaio.jumpandrun.ecs.systemmodule.UpdateColliderPosition;
import com.jukaio.jumpandrun.ecs.systemmodule.UpdateSensorPosition;

public class LevelZero extends World
{
    public LevelZero(ECS p_ecs, Context p_context)
    {
        super(p_ecs, p_context);
        create_entities(p_ecs, p_context);
        add_systems(p_ecs);
    }
    

    private void create_entities(ECS p_ecs, Context p_context)
    {
        Tileset tileset = new Tileset();
    
        // Gather a free entity
        Entity tilemap = p_ecs.create_entity();

        // Create a pack of tilemap_components - In this case 5 tilemap_components
        Component[] tilemap_components = new Component[5];
        AssetLoaderXML source = new AssetLoaderXML();
        source.m_source = "tilemap.xml";
        source.m_context = p_context;
        tilemap_components[0] = source;
        tilemap_components[1] = new TileMap();
        tilemap_components[2] = tileset;
        tilemap_components[3] = new Grid();
        tilemap_components[4] = new TileMapCollider();

        // Register the tilemap_components
        p_ecs.register_components(p_ecs.components_to_signature(tilemap_components));
        p_ecs.add_shared_component(tilemap, SharedComponentType.RENDER_CANVAS);

        // Associate tilemap_components with entity
        p_ecs.add_components(tilemap, tilemap_components);

        // Place entity in world
        m_entities.add(tilemap);
        
        Entity entity = p_ecs.create_entity();
        Transform entity_transform = new Transform();
        //entity_transform.set_position(60, 100);
        //entity_transform.set_position(200, 30);
        entity_transform.set_position(120, 30);
        entity_transform.set_dimensions(16, 16); //Tile dimension
        SingleTile entity_tile = new SingleTile();
        entity_tile.m_tile_id = 42;
        MovementSensors sensors = new MovementSensors();
        sensors.m_offset_y = 0; // Tile size * 0.125f
        sensors.m_offset_x = 2; // Tile size * 0.0625f (1 / 16th)
        Kinematic kinematic = new Kinematic();
        kinematic.set_velocity(0.0f, 0.0f);
        kinematic.set_speed(2.0f, 10.0f);
        Jump jump =  new Jump();
        jump.set_jump_force(2.0f);
        
        Component[] entity_components = new Component[7];
        entity_components[0] = entity_transform;
        entity_components[1] = entity_tile;
        entity_components[2] = tileset;
        entity_components[3] = new RectangleCollider();
        entity_components[4] = sensors;
        entity_components[5] = kinematic;
        entity_components[6] = jump;
        p_ecs.register_components(p_ecs.components_to_signature(entity_components));
        
        p_ecs.add_components(entity, entity_components);
        p_ecs.add_shared_component(entity, SharedComponentType.GRAVITY);
        p_ecs.add_shared_component(entity, SharedComponentType.RENDER_CANVAS);
        p_ecs.add_shared_component(entity, SharedComponentType.INPUT_CONTROLLER);
        
        m_entities.add(entity);
    }
    
    void add_systems(ECS p_ecs)
    {
        p_ecs.add_system(new PrepareJump());
        p_ecs.add_system(new InputHandler());
        p_ecs.add_system(new Jumping());
        p_ecs.add_system(new ApplyForces());
        p_ecs.add_system(new UpdateSensorPosition());
        p_ecs.add_system(new MovementSensorsTilemapCollision());
        p_ecs.add_system(new UpdateColliderPosition());
        
        p_ecs.add_system(new TileMapCore());
        p_ecs.add_system(new SingleTileRenderer());
        //p_ecs.add_system(new ColliderRendererDebug());
        p_ecs.add_system(new MovermentSensorRendererDebug());
    }
}
