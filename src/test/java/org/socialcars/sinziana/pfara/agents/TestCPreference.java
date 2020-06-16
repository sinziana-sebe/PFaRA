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

package org.socialcars.sinziana.pfara.agents;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.pfara.data.input.CInputpojo;

import java.io.File;
import java.io.IOException;

public class TestCPreference
{
    private CPreference m_preference;

    /**
     * initialising
     * @throws IOException file exception
     */
    @Before
    public void init() throws IOException
    {
        final CInputpojo l_configuration = new ObjectMapper().readValue( new File( "src/test/resources/minimal-graph.json" ), CInputpojo.class );
        l_configuration.getVehicles().forEach( v -> m_preference = new CPreference( v.getPreference() ) );
    }

    /**
     * testing the minimum speed
     */
    @Test
    public void testMinSpeed()
    {
        Assume.assumeNotNull( m_preference );
        Assert.assertTrue( m_preference.minSpeed().equals( 0.1 ) );
    }

    /**
     * testing themaximum speed
     */
    @Test
    public void testMaxSpeed()
    {
        Assume.assumeNotNull( m_preference );
        Assert.assertTrue( m_preference.maxSpeed().equals( 1.0 ) );
    }

    /**
     * testing the maximum acceleration
     */
    @Test
    public void testMaxAccel()
    {
        Assume.assumeNotNull( m_preference );
        Assert.assertTrue( m_preference.maxAccel().equals( 0.9 ) );
    }

    /**
     * testing the maximum deceleration
     */
    @Test
    public void testMaxDecel()
    {
        Assume.assumeNotNull( m_preference );
        Assert.assertTrue( m_preference.maxDecel().equals( 0.7 ) );
    }

    /**
     * testing the maximum time limit
     */
    @Test
    public void testTimeLimit()
    {
        Assume.assumeNotNull( m_preference );
        Assert.assertTrue( m_preference.timeLimit().equals( 100 ) );
    }

    /**
     * testing the maximum length limit
     */
    @Test
    public void testLengthLimit()
    {
        Assume.assumeNotNull( m_preference );
        Assert.assertTrue( m_preference.lengthLimit().equals( 100.0 ) );
    }

    /**
     * testing the maximum cost
     */
    @Test
    public void testMaxCost()
    {
        Assume.assumeNotNull( m_preference );
        Assert.assertTrue( m_preference.maxCost().equals( 10.0 ) );
    }

}
