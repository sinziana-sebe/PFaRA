/**
 * This file is part of the mesoscopic traffic simulation PFaRA of Clauthal University of
 * Technology-Mobile and Enterprise Computing aswell as SocialCars Research Training Group.
 *  Copyright (c) 2017-2021 Sinziana-Maria Sebe (sms14@tu-clausthal.de)
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 *  General Public License as  published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *  PURPOSE.  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this program. If not, see http://www.gnu.org/licenses/
 *
 */

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
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class TestCVehicle
{
    private static final Logger LOGGER = Logger.getLogger( TestCVehicle.class.getName() );

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
        final CInputpojo l_configuration = new ObjectMapper().readValue( new File( "src/test/resources/tiergarten.json" ), CInputpojo.class );
        m_time = 0;
        m_unit = new CUnits( 1, 0.1 );
        m_graph = new CGraph( l_configuration.getGraph() );
        final FileHandler l_handler = new FileHandler( "MovementOutput", true );
        LOGGER.addHandler( l_handler );
        l_handler.setFormatter( new SimpleFormatter() );
        m_veh = new CVehicle( l_configuration.getVehicles().get( 1 ), 0, LOGGER, m_unit, false, 1.0 );
    }

    /**
     * tests the name
     */
    @Test
    public void testName()
    {
        Assume.assumeNotNull( m_veh );
        Assert.assertTrue( m_veh.name().contentEquals( "red" ) );
    }

    /**
     * tests the location
     */
    @Test
    public void testLocation()
    {
        Assume.assumeNotNull( m_veh );
        Assert.assertTrue( m_veh.location().contentEquals( "312" ) );
    }

    /**
     * tests the origin
     */
    @Test
    public void testOrigin()
    {
        Assume.assumeNotNull( m_veh );
        Assert.assertTrue( m_veh.origin().contentEquals( "312" ) );
    }

    /**
     * tests the destination
     */
    @Test
    public void testDestination()
    {
        Assume.assumeNotNull( m_veh );
        Assert.assertTrue( m_veh.destination().contentEquals( "311" ) );
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
        Assert.assertTrue( m_veh.location().contentEquals( "1" ) );
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
        m_veh.completed( "1", 10 );
        Assert.assertTrue( m_veh.events().size() == 2 );
    }

    @Test
    public void testFormed()
    {
        Assume.assumeNotNull( m_veh );
        Assert.assertFalse( m_veh.platooning() );
        m_veh.formed( "0", 0, new ArrayList<>() );
        Assert.assertTrue( m_veh.platooning() );
        Assert.assertTrue( m_veh.events().size() == 2 );
    }

    @Test
    public void testSplit()
    {
        Assume.assumeNotNull( m_veh );
        Assert.assertFalse( m_veh.platooning() );
        m_veh.formed( "node0", 0, new ArrayList<>() );
        Assert.assertTrue( m_veh.platooning() );
        Assert.assertTrue( m_veh.events().size() == 2 );
        m_veh.split( "node0", 0 );
        Assert.assertFalse( m_veh.platooning() );
        Assert.assertTrue( m_veh.events().size() == 3 );
    }
}
