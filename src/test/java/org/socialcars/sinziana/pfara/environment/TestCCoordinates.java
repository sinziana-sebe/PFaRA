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

public class TestCCoordinates
{
    private CInputpojo m_input;
    private CCoordinates m_coordinates;

    /**
     * initialises the coordinates
     * @throws IOException file exception
     */
    @Before
    public void init() throws IOException
    {
        m_input = new ObjectMapper().readValue( new File( "src/test/resources/negmid.json" ), CInputpojo.class );
        m_coordinates = new CCoordinates( m_input.getGraph().getNodes().iterator().next().getCoordinates() );
    }

    /**
     * test equals
     */
    @Test
    public void testEquals()
    {
        Assume.assumeNotNull( m_coordinates );
        Assert.assertTrue( m_coordinates.equals( m_coordinates ) );
    }

    /**
     * tests the latitude
     */
    @Test
    public void testLatitude()
    {
        Assume.assumeNotNull( m_coordinates );
        Assert.assertTrue( m_coordinates.latitude().equals( 0.0 ) );
    }

    /**
     * tests the longitude
     */
    @Test
    public void testLongitude()
    {
        Assume.assumeNotNull( m_coordinates );
        Assert.assertTrue( m_coordinates.latitude().equals( 0.0 ) );
    }


}
