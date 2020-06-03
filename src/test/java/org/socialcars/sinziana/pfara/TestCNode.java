package org.socialcars.sinziana.pfara;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.pfara.environment.CCoordinates;
import org.socialcars.sinziana.pfara.environment.CNode;

public class TestCNode
{
    private CNode m_node;

    @Before
    public void init()
    {
        m_node = new CNode( "node", new CCoordinates( 0.0, 0.0 ) );
    }

    @Test
    public void testName()
    {
        Assume.assumeNotNull( m_node );
        Assert.assertNotNull( m_node.name() );
        Assert.assertTrue( m_node.name().contains( "node" ) );
    }

    @Test
    public void testCoordinate()
    {
        Assume.assumeNotNull( m_node );
        Assert.assertNotNull( m_node.coordinates() );
        Assert.assertTrue( m_node.coordinates().latitude().equals( 0.0 ) );
        Assert.assertTrue( m_node.coordinates().longitude().equals( 0.0 ) );
    }
}
