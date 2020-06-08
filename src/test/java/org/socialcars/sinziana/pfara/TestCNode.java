package org.socialcars.sinziana.pfara;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.pfara.data.input.CInputpojo;
import org.socialcars.sinziana.pfara.environment.CNode;

import java.io.File;
import java.io.IOException;

public class TestCNode
{
    private CInputpojo m_input;
    private CNode m_node;

    /**
     * initialises the node object
     * @throws IOException file exception
     */
    @Before
    public void init() throws IOException
    {
        m_input = new ObjectMapper().readValue( new File( "src/test/resources/minimal-graph.json" ), CInputpojo.class );
        m_node = new CNode( m_input.getGraph().getNodes().iterator().next() );
    }

    /**
     * tests the name
     */
    @Test
    public void testName()
    {
        Assume.assumeNotNull( m_node );
        Assert.assertNotNull( m_node.name() );
        Assert.assertTrue( m_node.name().contains( "node" ) );
    }

    /**
     * tests the coordinates
     */
    @Test
    public void testCoordinate()
    {
        Assume.assumeNotNull( m_node );
        Assert.assertNotNull( m_node.coordinates() );
        Assert.assertTrue( m_node.coordinates().latitude().equals( 0.0 ) );
        Assert.assertTrue( m_node.coordinates().longitude().equals( 0.0 ) );
    }
}
