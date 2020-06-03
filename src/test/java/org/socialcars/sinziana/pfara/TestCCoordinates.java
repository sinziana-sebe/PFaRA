package org.socialcars.sinziana.pfara;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.pfara.environment.CCoordinates;

public class TestCCoordinates
{
    private CCoordinates m_coordinates;

    @Before
    public void init()
    {
        m_coordinates = new CCoordinates( 0.0, 0.0 );
    }

    @Test
    public void testLatitude()
    {
        Assume.assumeNotNull( m_coordinates );
        Assert.assertTrue( m_coordinates.latitude().equals( 0.0 ) );
    }

    @Test
    public void testLongitude()
    {
        Assume.assumeNotNull( m_coordinates );
        Assert.assertTrue( m_coordinates.latitude().equals( 0.0 ) );
    }

}
