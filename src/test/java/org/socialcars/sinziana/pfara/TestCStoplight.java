package org.socialcars.sinziana.pfara;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.pfara.data.input.CInputpojo;
import org.socialcars.sinziana.pfara.environment.CStoplight;
import org.socialcars.sinziana.pfara.environment.ELightState;

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
        m_stoplight.update();
        Assert.assertTrue( m_stoplight.state() == ELightState.GREEN );
        Assert.assertTrue( m_stoplight.timeLeft() == 5 );
    }

}
