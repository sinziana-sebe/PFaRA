package org.socialcars.sinziana.pfara.agents;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.pfara.data.input.CInputpojo;
import org.socialcars.sinziana.pfara.environment.CGraph;
import org.socialcars.sinziana.pfara.units.CUnits;

import java.io.File;
import java.io.IOException;

public class TestCVehicle
{
    private CVehicle m_veh;
    private CGraph m_graph;
    private Integer m_time;
    private CUnits m_unit;

    /**
     * initialising
     * @throws IOException file exception
     */
    @Before
    public void init() throws IOException
    {
        final CInputpojo l_configuration = new ObjectMapper().readValue( new File( "src/test/resources/minimal-graph.json" ), CInputpojo.class );
        m_time = 0;
        m_unit = new CUnits( 1, 10 );
        m_graph = new CGraph( l_configuration.getGraph() );
        m_veh = new CVehicle( l_configuration.getVehicles().get( 0 ), 0, "outpufile", m_unit );
    }

    /**
     * tests the name
     */
    @Test
    public void testName()
    {
        Assume.assumeNotNull( m_veh );
        Assert.assertTrue( m_veh.name().contentEquals( "veh0" ) );
    }

    /**
     * tests the location
     */
    @Test
    public void testLocation()
    {
        Assume.assumeNotNull( m_veh );
        Assert.assertTrue( m_veh.location().contentEquals( "node0" ) );
    }

    /**
     * tests the origin
     */
    @Test
    public void testOrigin()
    {
        Assume.assumeNotNull( m_veh );
        Assert.assertTrue( m_veh.origin().contentEquals( "node0" ) );
    }

    /**
     * tests the destination
     */
    @Test
    public void testDestination()
    {
        Assume.assumeNotNull( m_veh );
        Assert.assertTrue( m_veh.destination().contentEquals( "node1" ) );
    }

    /**
     * tests the position
     */
    @Test
    public void testPosition()
    {
        Assume.assumeNotNull( m_veh );
        Assert.assertTrue( m_veh.position().equals( 0.0 ) );
    }

    /**
     * tests the microscopic movement
     */
    @Test
    public void testMikro()
    {
        Assume.assumeNotNull( m_veh );
        m_veh.moveMikro();
        Assert.assertTrue( m_veh.position().equals( 9.0 ) );
        m_veh.moveMikro();
        Assert.assertTrue( m_veh.position().equals( 27.0 ) );
        m_veh.moveMikro();
        Assert.assertTrue( m_veh.position().equals( 38.0 ) );
        m_veh.moveMikro();
        Assert.assertTrue( m_veh.position().equals( 42.0 ) );
    }

    /**
     * tests the braking
     */
    @Test
    public void testBrake()
    {
        Assume.assumeNotNull( m_veh );
        m_veh.moveMikro();
        Assert.assertTrue( m_veh.position().equals( 9.0 ) );
        m_veh.brake();
        Assert.assertTrue( m_veh.position().equals( 11.0 ) );
    }

    /**
     * tests the macroscopic movement
     */
    @Test
    public void testMacro()
    {
        Assume.assumeNotNull( m_veh );
        m_veh.moveMakro( 0.9 );
        Assert.assertTrue( m_veh.position().equals( 9.0 ) );
        m_veh.moveMakro( 1.1 );
        Assert.assertTrue( m_veh.position().equals( 19.0 ) );
    }

    /**
     * test the departed function
     */
    @Test
    public void testDeparted()
    {
        Assume.assumeNotNull( m_veh );
        m_veh.departed( m_graph.edgeByName( "edge0-1" ), 0 );
        Assert.assertTrue( m_veh.location().contentEquals( "edge0-1" ) );
        Assert.assertTrue( m_veh.events().size() == 2 );
    }

    /**
     * test the arrived function
     */
    @Test
    public void testArrived()
    {
        Assume.assumeNotNull( m_veh );
        m_veh.arrived( m_graph.edgeByName( "edge0-1" ), 10 );
        Assert.assertTrue( m_veh.location().contentEquals( "node1" ) );
        Assert.assertTrue( m_veh.position().equals( 0.0 ) );
        Assert.assertTrue( m_veh.events().size() == 2 );
    }

    /**
     * test the completed function
     */
    @Test
    public void testCompleted()
    {
        Assume.assumeNotNull( m_veh );
        m_veh.completed( m_graph.nodeByName( "node1" ), 10 );
        Assert.assertTrue( m_veh.events().size() == 2 );
    }
}
