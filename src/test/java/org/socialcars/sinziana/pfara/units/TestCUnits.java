package org.socialcars.sinziana.pfara.units;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

public class TestCUnits
{
    private CUnits m_units;

    /**
     * initializing
     */
    @Before
    public void init()
    {
        m_units = new CUnits( 1, 10 );
    }

    /**
     * testing the getters
     */
    @Test
    public void get()
    {
        Assume.assumeNotNull( m_units );
        Assert.assertTrue( m_units.getBlock().equals( 10.0 ) );
        Assert.assertTrue( m_units.getTimestep().equals( 1.0 ) );
        Assert.assertTrue( m_units.getBlockTimesteps().equals( 10.0 ) );
    }

    /**
     * testing the new position
     */
    @Test
    public void pos()
    {
        Assume.assumeNotNull( m_units );
        Assert.assertTrue( m_units.newPosition( 2, 1 ).equals( 12.00 ) );
    }

    /**
     * testing speed to blocks
     */
    @Test
    public void speedToBlocks()
    {
        Assume.assumeNotNull( m_units );
        Assert.assertTrue( m_units.speedToBlocks( 2 ).equals( 20.0 ) );
    }

    /**
     * testing accel to speed
     */
    @Test
    public void accelToSpeed()
    {
        Assume.assumeNotNull( m_units );
        Assert.assertTrue( m_units.accelerationToSpeed( 13 ).equals( 13.0 ) );
    }

    /**
     * testing block to dist
     */
    @Test
    public void blockToDist()
    {
        Assume.assumeNotNull( m_units );
        Assert.assertTrue( m_units.blockToDistance( 8 ).equals( 80.0 ) );
    }

    /**
     * testing distance to speed
     */
    @Test
    public void distanceToSpeed()
    {
        Assume.assumeNotNull( m_units );
        Assert.assertTrue( m_units.distanceToSpeed( 14 ).equals( 14.0 ) );
    }

    /**
     * testing steps to timesteps
     */
    @Test
    public void timeToTimesteps()
    {
        Assume.assumeNotNull( m_units );
        Assert.assertTrue( m_units.stepsToTimesteps( 74 ).equals( 74.0 ) );
    }

    /**
     * testing speed to dist
     */
    @Test
    public void testToDist()
    {
        Assume.assumeNotNull( m_units );
        Assert.assertTrue( m_units.speedToDistance( 65 ).equals( 65.0 ) );
    }

    /**
     * testing dist to block
     */
    @Test
    public void distToBlocks()
    {
        Assume.assumeNotNull( m_units );
        Assert.assertTrue( m_units.distanceToBlocks( 54 ).equals( 5.4 ) );
    }
}
