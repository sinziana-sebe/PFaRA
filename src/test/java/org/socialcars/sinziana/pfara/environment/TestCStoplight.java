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

public class TestCStoplight
{
    private CInputpojo m_input;
    private CStoplight m_stoplight;

    /**
     * initialises the stoplight object
     * @throws IOException file exception
     */
    @Before
    public void init() throws IOException
    {
        m_input = new ObjectMapper().readValue( new File( "src/test/resources/minimal-graph.json" ), CInputpojo.class );
        m_stoplight = new CStoplight( m_input.getStoplights().get( 0 ) );
    }

    /**
     * tests the start time
     */
    @Test
    public void testStartTime()
    {
        Assume.assumeNotNull( m_stoplight );
        Assert.assertTrue( m_stoplight.startTime() == 0 );
    }

    /**
     * tests the starting of the stoplight
     */
    @Test
    public void testStart()
    {
        Assume.assumeNotNull( m_stoplight );
        m_stoplight.start();
        Assert.assertTrue( m_stoplight.state() == ELightState.GREEN );
        Assert.assertTrue( m_stoplight.timeLeft() == 5 );
    }

    /**
     * test the update function
     * both the normal update, where the time remaining is decremented
     * as well two changes in the light
     */
    @Test
    public void testUpdate()
    {
        Assume.assumeNotNull( m_stoplight );
        m_stoplight.start();
        Assert.assertTrue( m_stoplight.state() == ELightState.GREEN );
        Assert.assertTrue( m_stoplight.timeLeft() == 5 );
        m_stoplight.update();
        Assert.assertTrue( m_stoplight.timeLeft() == 4 );
        m_stoplight.update();
        m_stoplight.update();
        m_stoplight.update();
        m_stoplight.update();
        Assert.assertTrue( m_stoplight.state() == ELightState.RED );
        Assert.assertTrue( m_stoplight.timeLeft() == 5 );
        m_stoplight.update();
        Assert.assertTrue( m_stoplight.timeLeft() == 4 );
        m_stoplight.update();
        m_stoplight.update();
        m_stoplight.update();
        Assert.assertTrue( m_stoplight.timeLeft() == 1 );
        m_stoplight.update();
        Assert.assertTrue( m_stoplight.state() == ELightState.GREEN );
        Assert.assertTrue( m_stoplight.timeLeft() == 5 );
    }

}
