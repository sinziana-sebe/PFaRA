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
        final CInputpojo l_configuration = new ObjectMapper().readValue( new File( "src/test/resources/negmid.json" ), CInputpojo.class );
        l_configuration.getVehicles().forEach( v -> m_utility = new CUtility( v.getUtility() ) );
    }

    /**
     * tests the alpha coefficient
     */
    @Test
    public void testAlpha()
    {
        Assume.assumeNotNull( m_utility );
        Assert.assertTrue( m_utility.rho().equals( 0.2 ) );
    }

    /**
     * tests the beta coefficient
     */
    @Test
    public void testBeta()
    {
        Assume.assumeNotNull( m_utility );
        Assert.assertTrue( m_utility.sigma().equals( 0.8 ) );
    }
}
