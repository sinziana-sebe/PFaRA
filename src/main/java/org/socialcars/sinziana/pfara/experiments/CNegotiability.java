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

package org.socialcars.sinziana.pfara.experiments;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.socialcars.sinziana.pfara.agents.CVehicle;
import org.socialcars.sinziana.pfara.data.input.CInputpojo;
import org.socialcars.sinziana.pfara.environment.CGraph;
import org.socialcars.sinziana.pfara.environment.negotiables.CFindNegotiables;
import org.socialcars.sinziana.pfara.units.CUnits;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CNegotiability
{
    private static final Logger LOGGER = Logger.getLogger( CNegotiability.class.getName() );

    private final CInputpojo m_input;
    private final CGraph m_env;

    private final CUnits m_unit;
    private Integer m_time;

    private final ArrayList<CVehicle> m_vehicles;

    private final Double m_omega;

    public CNegotiability( final String p_infile, final String p_outfile, final Integer p_time, final Double p_space, final Double p_omega ) throws IOException
    {
        final FileHandler l_handler = new FileHandler( p_outfile );
        LOGGER.addHandler( l_handler );
        l_handler.setFormatter( new SimpleFormatter() );

        m_input = new ObjectMapper().readValue( new File( p_infile ), CInputpojo.class );
        m_env = new CGraph( m_input.getGraph() );

        m_unit = new CUnits( p_time, p_space );
        m_time = 0;
        m_vehicles = new ArrayList<>();
        m_input.getVehicles().forEach( p -> m_vehicles.add( new CVehicle( p, 0, LOGGER, m_unit, false, 1.0 ) ) );
        m_omega = p_omega;
    }

    public void run()
    {
        final CFindNegotiables l_findneg = new CFindNegotiables( m_env, m_vehicles.get( 0 ), m_unit, m_omega, LOGGER );
        l_findneg.findSimple();
        l_findneg.findComplex();
    }
}
