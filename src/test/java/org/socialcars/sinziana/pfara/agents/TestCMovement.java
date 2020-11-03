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

package org.socialcars.sinziana.pfara.agents;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.pfara.data.input.CInputpojo;
import org.socialcars.sinziana.pfara.data.input.CVehiclepojo;
import org.socialcars.sinziana.pfara.environment.CGraph;
import org.socialcars.sinziana.pfara.environment.IEdge;
import org.socialcars.sinziana.pfara.units.CUnits;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class TestCMovement
{
    private static final Logger LOGGER = Logger.getLogger( TestCMovement.class.getName() );

    private ArrayList<CVehicle> m_pods = new ArrayList<>();
    private CGraph m_env;
    private CUnits m_unit;
    private Integer m_time;
    private HashMap<CVehicle, List<IEdge>> m_routes;


    /**
     * initialisation
     * @throws IOException file
     */
    @Before
    public void init() throws IOException
    {
        final FileHandler l_handler = new FileHandler( "MovementOutput", true );
        LOGGER.addHandler( l_handler );
        l_handler.setFormatter( new SimpleFormatter() );
        m_unit = new CUnits( 1, 1 );
        final CInputpojo l_configuration = new ObjectMapper().readValue( new File( "src/test/resources/minimal-graph.json" ), CInputpojo.class );
        final List<CVehiclepojo> l_pods = l_configuration.getVehicles();
        m_env = new CGraph( l_configuration.getGraph() );
        m_time = 0;
        l_pods.forEach( p -> m_pods.add( new CVehicle( p, 0, LOGGER, m_unit ) ) );
        m_routes = new HashMap<>();
    }

    /**
     * testing pod movement
     * @throws IOException file
     */
    @Test
    public void movement() throws IOException
    {
        m_pods.forEach( p -> m_routes.put( p, m_env.route( p.origin(), p.destination() ) ) );
        final HashMap<CVehicle, String> l_status = new HashMap<>();
        m_pods.forEach( p -> l_status.put( p, "Incomplete" ) );

        while ( l_status.containsValue( "Incomplete" ) )
        {
            m_pods.forEach( p ->
            {
                if ( m_routes.get( p ).isEmpty() ) l_status.put( p, "Complete" );
                else
                {
                    final IEdge l_edge = m_routes.get( p ).iterator().next();
                    if ( p.position().equals( 0.0 ) ) p.departed( l_edge, m_time );
                    if ( p.position().doubleValue() < l_edge.length() ) p.moveMikro();
                    else
                    {
                        p.arrived( l_edge, m_time );
                        m_routes.get( p ).remove( 0 );

                    }
                    if ( m_routes.get( p ).isEmpty() ) p.completed( p.location(), m_time );
                }
            } );
            m_time++;
        }
    }
}
