package org.socialcars.sinziana.pfara.agents.proprieties;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.pfara.data.input.CInputpojo;


import java.io.File;
import java.io.IOException;


public class TestCUtility
{
    private CUtility m_utility;


    /**
     * initialising test
     * @throws IOException file
     */
    @Before
    public void init() throws IOException
    {
        final CInputpojo l_configuration = new ObjectMapper().readValue( new File( "src/test/resources/minimal-graph.json" ), CInputpojo.class );
        l_configuration.getVehicles().forEach( v -> m_utility = new CUtility( v.getUtility() ) );
    }

    /**
     * tests the alpha coefficient
     */
    @Test
    public void testAlpha()
    {
        Assume.assumeNotNull( m_utility );
        Assert.assertTrue( m_utility.alpha().equals( 0.2 ) );
    }

    /**
     * tests the beta coefficient
     */
    @Test
    public void testBeta()
    {
        Assume.assumeNotNull( m_utility );
        Assert.assertTrue( m_utility.beta().equals( 0.8 ) );
    }
}
