package org.socialcars.sinziana.pfara;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.pfara.data.input.CCoordinatespojo;
import org.socialcars.sinziana.pfara.data.input.CEdgepojo;
import org.socialcars.sinziana.pfara.data.input.CGraphpojo;
import org.socialcars.sinziana.pfara.data.input.CInputpojo;
import org.socialcars.sinziana.pfara.data.input.CNodepojo;
import org.socialcars.sinziana.pfara.data.input.CStoplightpojo;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class TestCInput
{
    /**
     * configuration
     */
    private CInputpojo m_input;

    /**
     * initialize configuration
     *
     * @throws IOException on example reading error
     */
    @Before
    public final void init() throws IOException
    {
        m_input = new ObjectMapper().readValue( new File( "src/test/resources/minimal-graph.json" ), CInputpojo.class );
    }

    /**
     * test the whole input
     */
    @Test
    public void input()
    {
        Assert.assertTrue( m_input.equals( m_input ) );
        Assert.assertTrue( !m_input.toString().isEmpty() );
        Assert.assertTrue( m_input.getAdditionalProperties().size() == 0 );
        Assert.assertTrue( m_input.hashCode() != 0 );
        Assert.assertNotNull( m_input.getGraph() );
        Assert.assertNotNull( m_input.getStoplights() );
    }

    /**
     * test graph
     */
    @Test
    public void testgraph()
    {
        Assume.assumeNotNull( m_input );
        Assert.assertNotNull( m_input.getGraph() );
        final CGraphpojo l_graph = m_input.getGraph();
        Assert.assertTrue( l_graph.equals( l_graph ) );
        Assert.assertTrue( !l_graph.toString().isEmpty() );
        Assert.assertTrue( l_graph.getAdditionalProperties().size() == 0 );
        l_graph.setAdditionalProperty( "extra1", 1 );
        Assert.assertTrue( l_graph.getAdditionalProperties().size() == 1 );
    }

    /**
     * testing the nodes in the graph
     */
    @Test
    public void nodes()
    {
        Assume.assumeNotNull( m_input );
        Assert.assertNotNull( m_input.getGraph() );
        final CGraphpojo l_graph = m_input.getGraph();
        Assert.assertNotNull( l_graph.getNodes() );
        final Set<CNodepojo> l_nodes = l_graph.getNodes();
        Assert.assertTrue( l_nodes.size() == 2 );
        l_nodes.forEach( j ->
        {
            Assert.assertTrue( j.getName().contains( "node" ) );
            final CCoordinatespojo l_coo = j.getCoordinates();
            Assert.assertTrue( l_coo.equals( l_coo ) );
            Assert.assertTrue( l_coo.getAdditionalProperties().size() == 0 );
            l_coo.setAdditionalProperty( "extra2", 1 );
            Assert.assertTrue( l_coo.getAdditionalProperties().size() == 1 );
            Assert.assertTrue( j.getAdditionalProperties().size() == 0 );
            Assert.assertTrue( j.equals( j ) );
            Assert.assertTrue( !j.toString().isEmpty() );
            j.setAdditionalProperty( "plus", 1 );
            Assert.assertTrue( j.getAdditionalProperties().size() == 1 );

        } );
    }

    /**
     * testing the edges
     */
    @Test
    public void edges()
    {
        Assume.assumeNotNull( m_input );
        Assert.assertNotNull( m_input.getGraph() );
        final CGraphpojo l_graph = m_input.getGraph();
        Assert.assertNotNull( l_graph.getEdges() );
        Assert.assertTrue( l_graph.getEdges().size() == 1 );
        final Set<CEdgepojo> l_edges = l_graph.getEdges();
        l_edges.forEach( e ->
        {
            Assert.assertTrue( e.equals( e ) );
            Assert.assertTrue( e.getAdditionalProperties().size() == 0 );
            e.setAdditionalProperty( "extra3", 1 );
            Assert.assertTrue( e.getAdditionalProperties().size() == 1 );
            Assert.assertTrue( e.getName().contains( "edge" ) );
            Assert.assertTrue( e.getFrom().contains( "node" ) );
            Assert.assertTrue( e.getTo().contains( "node" ) );
            Assert.assertTrue( e.getWeight() == 10 );
        } );
    }

    /**
     * testing the stoplights
     */
    @Test
    public void stoplights()
    {
        Assume.assumeNotNull( m_input );
        Assume.assumeNotNull( m_input.getStoplights() );
        final List<CStoplightpojo> l_stoplights = m_input.getStoplights();
        l_stoplights.forEach( s ->
        {
            Assert.assertNotNull( s );
            Assert.assertTrue( s.getGreen() > 0 );
            Assert.assertTrue( s.getRed() > 0 );
            Assert.assertTrue( s.getLocation() != null );
            Assert.assertNotNull( s.getStart() );
        } );
    }
}