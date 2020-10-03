package com.jukaio.jumpandrun.ecs;

public class ElementSignaturePair<Element>
{
    public ElementSignaturePair()
    {
        m_element = null;
        m_signature = 0;
    }

    public ElementSignaturePair(Element p_element, int p_signature)
    {
        m_element = p_element;
        m_signature = p_signature;
    }

    public Element m_element;
    public int m_signature;
}
