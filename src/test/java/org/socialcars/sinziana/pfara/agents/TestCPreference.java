package org.socialcars.sinziana.pfara.agents;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
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

}
