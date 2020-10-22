package com.jukaio.jumpandrun;

import android.content.Context;
import android.graphics.Bitmap;

import com.jukaio.jumpandrun.components.BitmapComponent;
import com.jukaio.jumpandrun.components.ComponentType;
import com.jukaio.jumpandrun.components.GroundSensorsComponent;
import com.jukaio.jumpandrun.components.InputComponent;
import com.jukaio.jumpandrun.components.JumpComponent;
import com.jukaio.jumpandrun.components.KineticComponent;
import com.jukaio.jumpandrun.components.RecorderComponent;
import com.jukaio.jumpandrun.components.RectangleColliderComponent;
import com.jukaio.jumpandrun.components.WallSensorsComponent;
import com.jukaio.jumpandrun.components.WorldPhysicsComponent;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EntityXML extends Entity
{
    public static class Params
    {
        World m_world = null;
        EntityType m_type = null;
        int m_x = 0;
        int m_y = 0;
        Bitmap m_bitmap = null;
        String m_source = null;
    }

    public EntityXML(boolean p_active, Params p_params, Context p_context)
    {
        super(p_active, p_params.m_type);
        
        set_position(p_params.m_x, p_params.m_y);
        set_rotation(0.0f);
        set_scale(1, 1);
        Document doc = XML.get_document(p_params.m_source, p_context);
        
        Node first = doc.getFirstChild();
        int w = XML.parse_int(first, "width");
        int h = XML.parse_int(first, "height");
        set_dimensions(w, h);
        
        NodeList children = first.getChildNodes();
        for(int i = 0; i < children.getLength(); i++)
        {
            Node node = children.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element)node;
                if(element.getNodeName().equals("Components"))
                {
                    for(int j = 0; j < element.getChildNodes().getLength(); j++)
                    {
                        Node component_node = element.getChildNodes().item(j);
                        if(component_node.getNodeType() == Node.ELEMENT_NODE)
                        {
                            Element component_element = (Element)component_node;
                            ComponentType type_enum = ComponentType.valueOf(component_element.getNodeName().toUpperCase());
                            
                            switch(type_enum)
                            {
                                default:
                                case UNKNOWN:
                                    throw new AssertionError("COMPONENT DOES NOT EXIST:" + component_element.getNodeName().toUpperCase());
                                    
                                case BITMAP:
                                    add_component(new BitmapComponent(this, p_params.m_bitmap));
                                    break;
                                    
                                case INPUT:
                                    add_component(new InputComponent(this, component_element));
                                    break;
                                    
                                case KINEMATIC:
                                    add_component(new KineticComponent(this, component_element));
                                    break;
                                    
                                case WORLD_PHYSICS:
                                    add_component(new WorldPhysicsComponent(this, p_params.m_world, component_element));
                                    break;
                                    
                                case WALL_SENSORS:
                                    add_component(new WallSensorsComponent(this, component_element));
                                    break;
                                    
                                case GROUND_SENSORS:
                                    add_component(new GroundSensorsComponent(this, p_params.m_world, component_element));
                                    break;
                                    
                                case RECORDER:
                                    add_component(new RecorderComponent(this));
                                    break;
                                    
                                case JUMP:
                                    add_component(new JumpComponent(this));
                                    break;
                                    
                                case RECTANGLE_COLLIDER:
                                    add_component(new RectangleColliderComponent(this));
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }
}
