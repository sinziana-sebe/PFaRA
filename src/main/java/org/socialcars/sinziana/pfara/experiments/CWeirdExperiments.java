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
import com.google.common.util.concurrent.AtomicDouble;
import org.socialcars.sinziana.pfara.agents.CVehicle;
import org.socialcars.sinziana.pfara.data.input.CInputpojo;
import org.socialcars.sinziana.pfara.environment.CBackground;
import org.socialcars.sinziana.pfara.environment.CGraph;
import org.socialcars.sinziana.pfara.environment.IEdge;
import org.socialcars.sinziana.pfara.environment.negotiables.CNegotiableElement;
import org.socialcars.sinziana.pfara.functionality.CEdgeEnd;
import org.socialcars.sinziana.pfara.functionality.CPreGrouping;
import org.socialcars.sinziana.pfara.functionality.CReadBackground;
import org.socialcars.sinziana.pfara.units.CUnits;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CWeirdExperiments
{
    private static final Logger LOGGER = Logger.getLogger( CWeirdExperiments.class.getName() );

    private final CInputpojo m_input;
    private final CGraph m_env;
    private final CReadBackground m_readbackground;
    private final HashMap<IEdge, CBackground> m_backinfo;

    private final CUnits m_unit;
    private Integer m_time;

    private final ArrayList<CVehicle> m_vehicles;
    private final HashMap<CVehicle, String> m_status = new HashMap<>();
    private final HashMap<CVehicle, List<IEdge>> m_routes = new HashMap<>();
    private final Map<IEdge, Integer> m_countingmap = new HashMap<>();
    private final Map<CVehicle, List<IEdge>> m_finalroute = new HashMap<>();

    private CPreGrouping m_grouping;
    private CEdgeEnd m_edgeend;


    public CWeirdExperiments( final String p_infile, final String p_backfile, final String p_outfile,
                            final Integer p_time, final Double p_space ) throws IOException
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
        m_input.getVehicles().forEach( p -> m_vehicles.add( new CVehicle( p, 0, LOGGER, m_unit, false, 1.0 ) ) );
        m_vehicles.forEach( p ->
        {
            m_status.put( p, "Incomplete" );
            m_routes.put( p, m_env.route( p.origin(), p.destination() ) );
            m_finalroute.put( p, new ArrayList<>() );
        } );
    }

    public void minmaxweight()
    {
        final AtomicDouble l_minweight = new AtomicDouble( 0 );
        final AtomicDouble l_maxweight = new AtomicDouble( 0 );
        m_env.edges().forEach( e ->
        {
            if ( l_maxweight.get() < e.weight() ) l_maxweight.set( e.weight() );
            else if ( l_minweight.get() > e.weight() ) l_minweight.set( e.weight() );
        } );
        System.out.println( "Min weight: " + l_minweight.get() );
        System.out.println( "Max weight: " + l_maxweight.get() );
    }

    public Double edgeLength( final String p_edge )
    {
        return m_env.edgeByName( p_edge ).length();
    }

    public void allEdgeLengths()
    {
        m_env.edges().forEach( e -> System.out.println( e.name() + ": " + e.length() ) );
    }

    public void nodesandedges()
    {
        System.out.println( "Nodes:" + m_env.nodes().size() );
        System.out.println( "Edges:" + m_env.edges().size() );
    }

    public void viewNodes()
    {

    }

    public void utilities( final String p_original, final String p_common, final String p_alone, final Double p_omega )
    {
        final CNegotiableElement l_el = new CNegotiableElement(
                Collections.singletonList( m_env.edgeByName( p_original ) ),
                Collections.singletonList( m_env.edgeByName( p_common ) ),
                Collections.singletonList( m_env.edgeByName( p_alone ) )
        );
        final CVehicle l_veh = m_vehicles.get( 0 );
        final List<IEdge> l_list = new ArrayList<>();

        final Double l_costreduction = l_el.common().get( 0 ).weight().doubleValue()
                - ( l_el.common().get( 0 ).weight().doubleValue() / 2 + l_el.common().get( 0 ).weight().doubleValue() / p_omega );

        l_list.add( l_el.common().get( 0 ) );
        final Double l_common = l_veh.utility().calculateMakro( l_list, 1.0, m_unit, l_costreduction, l_veh.preferences() );
        l_list.clear();
        System.out.println( l_el.common().get( 0 ).name() + " " + l_common );

        l_list.add( l_el.alone().get( 0 ) );
        final Double l_alone = l_veh.utility().calculateMakro( l_list, 1.0, m_unit, 0.0, l_veh.preferences() );
        l_list.clear();
        System.out.println( l_el.alone().get( 0 ).name() + " " + l_alone );

        l_list.add( l_el.original().get( 0 ) );
        final Double l_original =  l_veh.utility().calculateMakro( l_list, 1.0, m_unit, 0.0, l_veh.preferences() );
        l_list.clear();
        System.out.println( l_el.original().get( 0 ).name() + " " + l_original );

        l_list.add( l_el.common().get( 0 ) );
        final Double l_common2 =  l_veh.utility().calculateMakro( l_list, 1.0, m_unit, l_costreduction * 2 - 0.001, l_veh.preferences() );

        System.out.println( l_el.common().get( 0 ).name() + "' " + l_common2 );

    }
}
