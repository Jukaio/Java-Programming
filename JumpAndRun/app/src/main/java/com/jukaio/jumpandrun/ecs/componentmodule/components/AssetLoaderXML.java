package com.jukaio.jumpandrun.ecs.componentmodule.components;

import android.content.Context;

public class SourceXML extends Component
{
    public String m_source;
    public Context m_context;
    @Override
    public ComponentType get_type() {
        return ComponentType.SOURCE_XML;
    }
}
