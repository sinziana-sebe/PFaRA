/*
 *  This file is part of the mesoscopic traffic simulation PFaRA of Clauthal University of
 *  Technology-Mobile and Enterprise Computing aswell as SocialCars Research Training Group.
 *  Copyright (c) 2017-2021 Sinziana-Maria Sebe (sms14@tu-clausthal.de)
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the
 *  GNUGeneral Public License as  published by the Free Software Foundation, either version of
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
import org.socialcars.sinziana.pfara.environment.CBackground;
import org.socialcars.sinziana.pfara.environment.CGraph;
import org.socialcars.sinziana.pfara.environment.IEdge;
import org.socialcars.sinziana.pfara.environment.INode;
import org.socialcars.sinziana.pfara.functionality.CEdgeEnd;
import org.socialcars.sinziana.pfara.functionality.CPreGrouping;
import org.socialcars.sinziana.pfara.functionality.CReadBackground;
import org.socialcars.sinziana.pfara.negotiation.CAlternativeOffers;
import org.socialcars.sinziana.pfara.negotiation.CTakeItOrLeaveIt;
import org.socialcars.sinziana.pfara.negotiation.IProtocol;
import org.socialcars.sinziana.pfara.units.CUnits;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CNegotiationMakro
{
    private static final Logger LOGGER = Logger.getLogger( CNegotiationMakro.class.getName() );

    private final CInputpojo m_input;
    private final CGraph m_env;
    private final CReadBackground m_readbackground;
    private final HashMap<IEdge, CBackground> m_backinfo;

    private final CUnits m_unit;
    private Integer m_time;

    private final ArrayList<CVehicle> m_vehicles;
    private final HashMap<CVehicle, String> m_status = new HashMap<>();
    private HashMap<CVehicle, List<IEdge>> m_routes = new HashMap<>();
    private final Map<IEdge, Integer> m_countingmap = new HashMap<>();
    private final Map<CVehicle, List<IEdge>> m_finalroute = new HashMap<>();
    private final Map<INode, List<CVehicle>> m_clusters = new HashMap<>();

    private CPreGrouping m_grouping;
    private CEdgeEnd m_edgeend;

    private Double m_omega;
    private final Boolean m_ao;
    private final Integer m_deadline;

    public CNegotiationMakro( final String p_infile, final String p_backfile, final String p_outfile,
                              final Integer p_time, final Double p_space, final Double p_omega, final Boolean p_ao, final Integer p_rounds ) throws IOException
    {
        final FileHandler l_handler = new FileHandler( p_outfile );
        LOGGER.addHandler( l_handler );
        l_handler.setFormatter( new SimpleFormatter() );

        m_input = new ObjectMapper().readValue( new File( p_infile ), CInputpojo.class );
        m_env = new CGraph( m_input.getGraph() );
        m_readbackground = new CReadBackground( m_env );
        m_backinfo = m_readbackground.getBackground( p_backfile );
        m_unit = new CUnits( p_time, p_space );
        m_time = 0;
        m_vehicles = new ArrayList<>();
        m_input.getVehicles().forEach( p -> m_vehicles.add( new CVehicle( p, 0, LOGGER, m_unit, false, p_omega ) ) );
        m_vehicles.forEach( p ->
        {
            m_status.put( p, "Incomplete" );
            m_routes.put( p, m_env.route( p.origin(), p.destination() ) );
            m_finalroute.put( p, new ArrayList<>() );
            //if ( p.name().equals("red") ) m_routes.put( p, List.of( m_env.edgeByName(  "edge322-14" ) ) );
            //if ( p.name().equals( "blue" ) ) m_routes.put( p, List.of( m_env.edgeByName( "edge322-318" ) ) );
        } );
        m_ao = p_ao;
        m_deadline = p_rounds;
        m_omega = p_omega;
    }

    public void run()
    {
        m_grouping = new CPreGrouping( m_vehicles, m_env, m_unit, m_routes, m_time, false, true, m_omega );
        checkNegotiation();
        while ( m_status.containsValue( "Incomplete" ) )
        {
            LOGGER.log( Level.INFO, "Time is " + m_time );
            move();
            m_clusters.clear();
            m_time++;
        }
    }

    private void checkNegotiation()
    {
        m_vehicles.forEach( p ->
        {
            if ( ( !p.platooning() )
                    && ( p.position().equals( 0.0 ) )
                    && ( !p.location().equals( p.destination() ) ) )
            {
                final INode l_node = m_env.nodeByName( p.location() );
                if ( m_clusters.containsKey( l_node ) )
                {
                    m_clusters.get( l_node ).add( p );
                }
                else
                {
                    m_clusters.put( l_node, new ArrayList<CVehicle>() );
                    m_clusters.get( l_node ).add( p );
                }
            }
        } );
        m_clusters.keySet().forEach( n ->
        {
            if ( m_clusters.get( n ).size() > 1 )
            {
                final IProtocol l_protocol;
                if ( m_ao ) l_protocol = new CAlternativeOffers( m_clusters.get( n ), n, m_env, m_routes, m_deadline );
                else l_protocol = new CTakeItOrLeaveIt( m_clusters.get( n ), n, m_env, m_routes );
                l_protocol.open( m_time );
                final CVehicle l_vehicle = m_clusters.get( n ).get( 1 );
                l_vehicle.sendOffer( m_routes.get( l_vehicle ) );
            }
        } );
    }

    /**
     * moves vehicles one by one
     * if vehicle reached the destination updates status
     * if any vehicle reached the end of an edge
     * triggers the platoon search
     */
    private void move()
    {
        final AtomicBoolean l_trig = new AtomicBoolean( false );
        m_vehicles.forEach( p ->
        {
            if ( m_routes.get( p ).isEmpty() ) m_status.put( p, "Complete" );
            else if ( p.getDelay() <= 0 )
            {
                final IEdge l_edge = m_routes.get( p ).iterator().next();
                if ( p.position().equals( 0.0 ) ) p.departed( l_edge, m_time );
                if ( p.position().doubleValue() < l_edge.length() ) p.moveMakro( m_backinfo.get( l_edge ).getmaxspeed() );
                else
                {
                    p.arrived( l_edge, m_time );
                    m_countingmap.put( l_edge, m_countingmap.getOrDefault( l_edge, 0 ) + 1 );
                    m_finalroute.get( p ).add( l_edge );
                    m_routes.get( p ).remove( 0 );
                    l_trig.set( true );
                }
            }
            else if ( p.getDelay() > 0 ) p.updateDelay();
        } );
        if ( l_trig.get() )
        {
            m_edgeend = new CEdgeEnd( m_vehicles, m_routes, m_time, m_env );
            m_grouping = new CPreGrouping( m_vehicles, m_env, m_unit, m_routes, m_time, false, true, m_omega );
            m_edgeend.checkLoners();
            checkNegotiation();
        }
    }
}
