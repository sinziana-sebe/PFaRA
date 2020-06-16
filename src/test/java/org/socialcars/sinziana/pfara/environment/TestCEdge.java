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
 */

package org.socialcars.sinziana.pfara.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.pfara.data.input.CEdgepojo;
import org.socialcars.sinziana.pfara.data.input.CInputpojo;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class TestCEdge
{
    private CInputpojo m_input;
    private Map<String, INode> m_nodes;
    private CEdge m_edge;

    /**
     * initialises the needed edge and node elements
     * @throws IOException file exception
     */
    @Before
    public void init() throws IOException
    {
        m_input = new ObjectMapper().readValue( new File( "src/test/resources/minimal-graph.json" ), CInputpojo.class );
        m_nodes = m_input.getGraph().getNodes().stream().map( CNode::new ).collect( Collectors.toMap( CNode::name, i -> i ) );
        final CEdgepojo l_edgepojo = m_input.getGraph().getEdges().iterator().next();
        m_edge = new CEdge( l_edgepojo, m_nodes.get( l_edgepojo.getFrom() ), m_nodes.get( l_edgepojo.getTo() ) );
    }

    /**
     * tests the name
     */
    @Test
    public void testName()
    {
        Assume.assumeNotNull( m_edge );
        Assert.assertTrue( m_edge.name().contains( "edge" ) );
    }

    /**
     * tests the origin node
     */
    @Test
    public void testFrom()
    {
        Assume.assumeNotNull( m_edge );
        Assert.assertTrue( m_edge.from().name().contains( "node" ) );
    }

    /**
     * test the end node
     */
    @Test
    public void testTo()
    {
        Assume.assumeNotNull( m_edge );
        Assert.assertTrue( m_edge.to().name().contains( "node" ) );
    }

    /**
     * tests the weight of the edge
     */
    @Test
    public void testWeight()
    {
        Assume.assumeNotNull( m_edge );
        Assert.assertTrue( m_edge.weight().equals( 10.0 ) );
    }

    /**
     * tests the length of the edge
     */
    @Test
    public void testLength()
    {
        Assume.assumeNotNull( m_edge );
        Assert.assertTrue( m_edge.length() == 10.0 );
    }

    /**
     * test the stoplight adding function
     */
    @Test
    public void testStoplight()
    {
        Assume.assumeNotNull( m_edge );
        final CStoplight l_sl = new CStoplight( m_input.getStoplights().get( 0 ) );
        m_edge.addStoplight( l_sl );
        Assert.assertTrue( m_edge.stoplight() != null );
    }

    /**
     * test the toString function
     */
    @Test
    public void testString()
    {
        Assume.assumeNotNull( m_edge );
        Assert.assertTrue( m_edge.toString().contentEquals( "edge0-1(10)" ) );
    }
}
