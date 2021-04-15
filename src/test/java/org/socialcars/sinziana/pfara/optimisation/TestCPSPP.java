/*
 *  This file is part of the mesoscopic traffic simulation PFaRA of Clauthal University of
 *  Technology-Mobile and Enterprise Computing aswell as SocialCars Research Training Group.
 *  Copyright (c) 2017-2021 Sinziana-Maria Sebe (sms14@tu-clausthal.de)
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the
 *  GNUGeneral Public License as  published by the Free Software Foundation, either version 3 of
 *  the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *  PURPOSE.  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this program. If
 *  not, see http://www.gnu.org/licenses/
 *
 */

package org.socialcars.sinziana.pfara.optimisation;

import com.fasterxml.jackson.databind.ObjectMapper;
import gurobi.GRBException;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.pfara.agents.CVehicle;
import org.socialcars.sinziana.pfara.data.input.CInputpojo;
import org.socialcars.sinziana.pfara.environment.CGraph;
import org.socialcars.sinziana.pfara.units.CUnits;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * test class for platooning shortest path problem
 */
public class TestCPSPP
{
    private static final Logger LOGGER = Logger.getLogger( TestCPSPP.class.getName() );

    private CInputpojo m_input;

    private CGraph m_env;
    private CPSPP m_opt;
    private CUnits m_unit;
    private ArrayList<CVehicle> m_vehicles;

    /**
     * initializing
     * @throws GRBException gurobi
     * @throws IOException file
     */
    @Before
    public void init() throws GRBException, IOException
    {
        m_input = new ObjectMapper().readValue( new File( "src/test/resources/tiergarten" ), CInputpojo.class );
        m_unit = new CUnits( 1, 0.01 );
        m_env = new CGraph( m_input.getGraph() );
        m_vehicles = new ArrayList<>();
        m_input.getVehicles().forEach( p -> m_vehicles.add( new CVehicle( p, 0, LOGGER, m_unit, false, 1.0 ) ) );
        m_opt = new CPSPP( m_env, 0, m_vehicles, m_unit, 3.0 );
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
