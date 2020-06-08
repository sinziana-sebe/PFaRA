package org.socialcars.sinziana.pfara;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.socialcars.sinziana.pfara.data.input.CInputpojo;
import org.socialcars.sinziana.pfara.environment.CGraph;

import java.io.File;
import java.io.IOException;


public class TestCGraph
{
    private CInputpojo m_input;

    private CGraph m_graph;


    @Before
    public void init() throws IOException
    {
        m_input = new ObjectMapper().readValue( new File( "src/test/resources/minimal-graph.json" ), CInputpojo.class );
        m_graph = new CGraph( m_input.getGraph() );
    }

}
