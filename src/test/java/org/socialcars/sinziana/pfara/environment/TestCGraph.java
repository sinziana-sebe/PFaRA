package org.socialcars.sinziana.pfara.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.pfara.data.input.CInputpojo;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;


public class TestCGraph
{
    private CInputpojo m_input;

    private CGraph m_graph;

    private String m_edgename;
    private String m_from;
    private String m_to;


    @Before
    public void init() throws IOException
    {
        m_input = new ObjectMapper().readValue( new File( "src/test/resources/minimal-graph.json" ), CInputpojo.class );
        m_graph = new CGraph( m_input.getGraph() );
        m_edgename = "edge0-1";
        m_from = "node0";
        m_to = "node1";
    }

    /**
     * tests the nodebyname function
     */
    @Test
    public void testNodebyName()
    {
        Assume.assumeNotNull( m_graph );
        Assume.assumeNotNull( m_graph.nodeByName( m_from ) );
        final INode l_node = m_graph.nodeByName( m_from );
        Assert.assertTrue( l_node.name().contentEquals( m_from ) );
    }

    /**
     * test edgebyname function
     */
    @Test
    public void testEdgebyName()
    {
        Assume.assumeNotNull( m_graph );
        Assume.assumeNotNull( m_graph.edgeByName( m_edgename ) );
        final IEdge l_edge = m_graph.edgeByName( m_edgename );
        Assert.assertTrue( l_edge.name().contentEquals( m_edgename ) );
    }

    /**
     * tests the routing function based on nodes
     */
    @Test
    public void routeNode()
    {
        Assume.assumeNotNull( m_graph );
        Assume.assumeNotNull( m_graph.nodeByName( m_from ) );
        final INode l_from = m_graph.nodeByName( m_from );
        Assume.assumeNotNull( m_graph.nodeByName( m_to ) );
        final INode l_to = m_graph.nodeByName( m_to );
        List<IEdge> l_route = m_graph.route( l_from, l_to, Stream.empty() );
        Assume.assumeNotNull( l_route );
        Assert.assertTrue( l_route.size() == 1 );
        Assert.assertTrue( l_route.contains( m_graph.edgeByName( m_edgename ) ) );
        l_route = m_graph.route( l_from, l_to );
        Assume.assumeNotNull( l_route );
        Assert.assertTrue( l_route.size() == 1 );
        Assert.assertTrue( l_route.contains( m_graph.edgeByName( m_edgename ) ) );
    }

    /**
     * tests the routing function based on strings
     */
    @Test
    public void routeString()
    {
        Assume.assumeNotNull( m_graph );
        Assume.assumeNotNull( m_graph.nodeByName( m_from ) );
        Assume.assumeNotNull( m_graph.nodeByName( m_to ) );
        List<IEdge> l_route = m_graph.route( m_from, m_to, Stream.empty() );
        Assume.assumeNotNull( l_route );
        Assert.assertTrue( l_route.size() == 1 );
        Assert.assertTrue( l_route.contains( m_graph.edgeByName( m_edgename ) ) );
        l_route = m_graph.route( m_from, m_to );
        Assume.assumeNotNull( l_route );
        Assert.assertTrue( l_route.size() == 1 );
        Assert.assertTrue( l_route.contains( m_graph.edgeByName( m_edgename ) ) );
    }

    /**
     * tests the stoplight function
     */
    @Test
    public void testStoplights()
    {
        Assume.assumeNotNull( m_graph );
        Assume.assumeNotNull( m_input.getStoplights() );
        m_graph.createStoplights( m_input.getStoplights() );
        Assume.assumeNotNull( m_graph.edgeByName( m_edgename ) );
        final IEdge l_edge = m_graph.edgeByName( m_edgename );
        Assert.assertNotNull( l_edge.stoplight() );
    }
}
