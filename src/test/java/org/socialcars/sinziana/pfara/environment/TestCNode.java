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
import org.socialcars.sinziana.pfara.data.input.CInputpojo;

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
