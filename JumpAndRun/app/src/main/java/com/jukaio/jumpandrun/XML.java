package com.jukaio.jumpandrun;

import android.content.Context;
import android.graphics.Color;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XML
{
    public static Document get_document(String p_source, Context p_context)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document document = null;
        try
        {
            builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(p_context.getResources().getAssets().open(p_source));
            document = builder.parse(is);
        } catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        } catch (SAXException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return document;
    }
    
    public static int parse_int(Node p_node, String p_item_name)
    {
        return Integer.parseInt(p_node.getAttributes().getNamedItem(p_item_name).getNodeValue());
    }
    
    public static float parse_float(Element p_element, String p_item_name)
    {
        return Float.parseFloat(p_element.getAttribute(p_item_name));
    }
}
