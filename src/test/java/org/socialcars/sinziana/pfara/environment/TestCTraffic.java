/*
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
 */

package org.socialcars.sinziana.pfara.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.pfara.data.input.CInputpojo;
import org.socialcars.sinziana.pfara.data.input.CTrafficpojo;

import java.io.File;
import java.io.IOException;

public class TestCTraffic
{
    private CGraph m_graph;
    private CBackground m_traffic;
    private CBackground m_traffic1;
    private CTrafficpojo m_trafficpojo;
    private CInputpojo m_input;


    @Before
    public void init() throws IOException
    {
        m_input = new ObjectMapper().readValue( new File( "src/test/resources/minimal-graph.json" ), CInputpojo.class );
        m_graph = new CGraph( m_input.getGraph() );
        m_trafficpojo = m_input.getTraffic().iterator().next();
        m_traffic = new CBackground( m_trafficpojo.getDensity(), m_trafficpojo.getFlow() );
        m_traffic1 = new CBackground( 0.0, 0.0 );
    }

    @Test
    public void testFlow()
    {
        Assume.assumeNotNull( m_traffic );
        Assert.assertTrue( m_traffic.flow().equals( 10.0 ) );
        Assume.assumeNotNull( m_traffic1 );
        Assert.assertTrue( m_traffic1.flow().equals( 0.0 ) );
    }

    @Test
    public void testDensity()
    {
        Assume.assumeNotNull( m_traffic );
        Assert.assertTrue( m_traffic.density().equals( 10.0 ) );
        Assume.assumeNotNull( m_traffic1 );
        Assert.assertTrue( m_traffic1.density().equals( 0.0 ) );
    }

    @Test
    public void testSpeed()
    {
        Assume.assumeNotNull( m_traffic );
        Assert.assertTrue( m_traffic.maxspeed().equals( 1.0 ) );
        Assume.assumeNotNull( m_traffic1 );
        Assert.assertTrue( m_traffic1.maxspeed().equals( 100000.0 ) );
    }

    @Test
    public void testString()
    {
        Assume.assumeNotNull( m_traffic );
        Assert.assertTrue( m_traffic.toString().contentEquals( "Background:[flow= 10, density= 10, maxSpeed= 1" ) );
    }

    @Test
    public void testEdge()
    {
        final IEdge l_edge = m_graph.edgeByName( m_trafficpojo.getEdge() );
        Assert.assertTrue( l_edge != null );
        Assert.assertTrue( l_edge.maxSpeed() == null );
        l_edge.addBackgroundTraffic( m_traffic );
        Assert.assertTrue( l_edge.maxSpeed().equals( 1.0 ) );
    }
}
