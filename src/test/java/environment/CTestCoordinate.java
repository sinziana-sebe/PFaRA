package environment;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

public class CTestCoordinate
{
    private CCoordinate m_coordinate;

    @Before
    public void init()
    {
        m_coordinate = new CCoordinate( 0.0, 0.0 );
    }

    @Test
    public void testLatitude()
    {
        Assume.assumeNotNull( m_coordinate );
        Assert.assertTrue( m_coordinate.latitude().equals( 0.0 ) );
    }

    @Test
    public void testLongitude()
    {
        Assume.assumeNotNull( m_coordinate );
        Assert.assertTrue( m_coordinate.latitude().equals( 0.0 ) );
    }

}
