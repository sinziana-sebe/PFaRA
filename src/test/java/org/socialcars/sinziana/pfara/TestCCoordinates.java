package org.socialcars.sinziana.pfara;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.pfara.data.input.CInputpojo;
import org.socialcars.sinziana.pfara.environment.CCoordinates;

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
        m_input = new ObjectMapper().readValue( new File( "src/test/resources/minimal-graph.json" ), CInputpojo.class );
        m_coordinates = new CCoordinates( m_input.getGraph().getNodes().iterator().next().getCoordinates() );
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
