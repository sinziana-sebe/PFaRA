/*
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

package org.socialcars.sinziana.pfara.optimisation;

import com.fasterxml.jackson.databind.ObjectMapper;
import gurobi.GRBException;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.pfara.data.input.CInputpojo;
import org.socialcars.sinziana.pfara.environment.CGraph;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

public class TestCSPP
{
    private static final CInputpojo INPUT;

    private CGraph m_env;
    private CSPP m_opt;

    static
    {
        try
        {
            INPUT = new ObjectMapper().readValue( new File( "src/test/resources/tiergarten_negotiation.json" ), CInputpojo.class );
        }
        catch ( final IOException l_exception )
        {
            throw new UncheckedIOException( l_exception );
        }
    }

    /**
     * initializing
     * @throws GRBException gurobi
     */
    @Before
    public void init() throws GRBException
    {
        m_env = new CGraph( INPUT.getGraph() );
        m_opt = new CSPP( m_env, 353, 27 );
    }

    /**
     * testing the solve option
     * @throws GRBException gurobi
     */
    @Test
    public void solve() throws GRBException
    {
        m_opt.solve();
        m_opt.display();
    }

    /**
     * main function
     * @param p_args cli
     * @throws GRBException gurobi
     */
    public static void main( final String[] p_args ) throws GRBException
    {
        final TestCSPP l_test = new TestCSPP();
        l_test.init();
        l_test.solve();
    }
}
