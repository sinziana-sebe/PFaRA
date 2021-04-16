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
import org.socialcars.sinziana.pfara.environment.CBackground;
import org.socialcars.sinziana.pfara.environment.CGraph;
import org.socialcars.sinziana.pfara.environment.CSections;
import org.socialcars.sinziana.pfara.environment.CStoplight;
import org.socialcars.sinziana.pfara.environment.IEdge;
import org.socialcars.sinziana.pfara.functionality.CReadBackground;
import org.socialcars.sinziana.pfara.units.CUnits;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * class for simulation with microscopic movement and no platooning
 */
public class CNoPlatooningMikro
{
    private static final Logger LOGGER = Logger.getLogger( CBenchmarkMikro.class.getName() );

    private final CInputpojo m_input;
    private final CReadBackground m_readbackground;
    private final HashMap<IEdge, CBackground> m_backinfo;
    private final CGraph m_env;
    private HashMap<CStoplight, String> m_stoplights = new HashMap<>();

    private final CUnits m_unit;
    private Integer m_time;

    private final ArrayList<CVehicle> m_vehicles;
    private final HashMap<CVehicle, String> m_status = new HashMap<>();
    private final HashMap<CVehicle, List<IEdge>> m_routes = new HashMap<>();
    private final Map<IEdge, Integer> m_countingmap = new HashMap<>();
    private final Map<CVehicle, List<IEdge>> m_finalroute = new HashMap<>();

    private CReporting m_report;

    /**
     * ctor
     * @param p_infile the input file
     * @param p_backfile the background information file
     * @param p_outfile the output file
     * @param p_time the time transformation coefficient
     * @param p_space the space transformation coefficient
     * @throws IOException file
     */
    public CNoPlatooningMikro( final String p_infile, final String p_backfile, final String p_outfile,
                            final Integer p_time, final Double p_space ) throws IOException
    {
        final FileHandler l_handler = new FileHandler( p_outfile );
        LOGGER.addHandler( l_handler );
        l_handler.setFormatter( new SimpleFormatter() );

        m_input = new ObjectMapper().readValue( new File( p_infile ), CInputpojo.class );
        m_env = new CGraph( m_input.getGraph() );
        m_readbackground = new CReadBackground( m_env );
        m_backinfo = m_readbackground.getBackground( p_backfile );
        m_env.createStoplights( m_input.getStoplights() );
        m_env.edges().forEach( n ->
        {
            n.stoplight().setLogger( LOGGER );
            m_stoplights.put( n.stoplight(), "Incomplete" );
        } );
        m_unit = new CUnits( p_time, p_space );
        m_time = 0;
        m_vehicles = new ArrayList<>();
        m_input.getVehicles().forEach( p -> m_vehicles.add( new CVehicle( p, 0, LOGGER, m_unit, true, 1.0 ) ) );
        m_vehicles.forEach( p ->
        {
            m_status.put( p, "Incomplete" );
            m_routes.put( p, m_env.route( p.origin(), p.destination() ) );
            m_finalroute.put( p, new ArrayList<>() );
        } );
        syncLights();
        addSections();

        m_report = new CReporting( p_outfile, true );
    }

    /**
     * creates sections in the edges
     * to allow for a better transition of movement
     * accelerating in the beginning
     * braking in the end
     */
    private void addSections()
    {
        m_env.edges().forEach( e ->
        {
            final Double l_beg = 0.1 * e.length();
            final Double l_end = 0.3 * e.length();
            final CSections l_section = new CSections( l_beg, e.length() - l_beg - l_end, l_end );
            e.addSections( l_section );
        } );
    }

    /**
     * syncs the traffic light
     * opposing sides must be on opposing cycles
     */
    private void syncLights()
    {
        while ( m_stoplights.containsValue( "Incomplete" ) )
        {
            m_stoplights.keySet().forEach( l ->
            {
                if ( l.startTime() == m_time )
                {
                    l.start();
                    m_stoplights.put( l, "Complete" );
                    LOGGER.log( Level.INFO, "Stoplight " + l.edgeName() + " synced" );
                }
                if ( m_stoplights.get( l ).equals( "Complete" ) ) l.update();
            } );
            LOGGER.log( Level.INFO, "Time is " + m_time );
            m_time++;
        }
    }

    /**
     * switches the movement type used
     * @param p_pod the vehicle
     * @param p_edge the edge it is travelling on
     * on the middle portion the vehicle travels at the speed allowed by the traffic
     * slower if it is congested and normal if it is not
     */
    private void switchmovement( final CVehicle p_pod, final IEdge p_edge )
    {
        if ( p_pod.position().doubleValue() <= p_edge.sections().beginning() ) p_pod.moveMikro();
        else if ( p_pod.position().doubleValue() <= p_edge.sections().beginning() + p_edge.sections().middle() ) p_pod.moveMakro( m_backinfo.get( p_edge ).maxspeed() );
        else p_pod.brake();
    }

    /**
     * start the simulation
     * @throws IOException file
     */
    public void run() throws IOException
    {
        while ( m_status.containsValue( "Incomplete" ) )
        {
            LOGGER.log( Level.INFO, "Time is " + m_time );
            move();
            m_env.edges().forEach( n -> n.stoplight().update() );
            m_time++;
        }
        m_report.writeCSV( m_vehicles );
    }

    /**
     * moves vehicles one by one
     * if vehicle reached the destination updates status
     * if any vehicle reached the end of an edge
     * triggers the platoon search
     */
    private void move()
    {
        m_vehicles.forEach( p ->
        {
            if ( m_routes.get( p ).isEmpty() ) m_status.put( p, "Complete" );
            else if ( p.getDelay() <= 0 )
            {
                final IEdge l_edge = m_routes.get( p ).iterator().next();
                if ( p.position().equals( 0.0 ) )
                {
                    p.departed( l_edge, m_time );
                    p.moveMikro();
                }
                if ( p.position().doubleValue() < l_edge.length() ) switchmovement( p, l_edge );
                else
                {
                    m_env.delayVehicle( p );
                    p.arrived( l_edge, m_time );
                    m_countingmap.put( l_edge, m_countingmap.getOrDefault( l_edge, 0 ) + 1 );
                    m_finalroute.get( p ).add( l_edge );
                    m_routes.get( p ).remove( 0 );
                }
            }
            else if ( p.getDelay() > 0 ) p.updateDelay();
        } );
    }
}
