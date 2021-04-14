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

package org.socialcars.sinziana.pfara.environment.negotiables;

import com.google.common.util.concurrent.AtomicDouble;
import org.socialcars.sinziana.pfara.agents.CVehicle;
import org.socialcars.sinziana.pfara.environment.CGraph;
import org.socialcars.sinziana.pfara.environment.IEdge;
import org.socialcars.sinziana.pfara.environment.INode;
import org.socialcars.sinziana.pfara.units.CUnits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * the find negotiables class
 * looks for negotiable shapes in the given network
 * searches for alternatives routes between different nodes
 * validates them based on negotiablity
 * ( (  common(no compensation) + alone > original ) && ( common(with compensation) + alone < original ) )
 */
public class CFindNegotiables implements IFingNegotiables
{
    private static Logger s_logger;

    private final CGraph m_env;
    private final CVehicle m_veh;
    private final CUnits m_unit;
    private final Double m_omega;

    /**
     * ctor
     * @param p_env the network
     * @param p_veh the agent whose utility function will be used to determine negotiability
     * @param p_unit the simulations transformation unit
     * @param p_omega the platooning subsidisation coefficient
     * @param p_log the logger
     */
    public CFindNegotiables( final CGraph p_env, final CVehicle p_veh, final CUnits p_unit, final Double p_omega, final Logger p_log )
    {
        s_logger = p_log;
        m_env = p_env;
        m_veh = p_veh;
        m_unit = p_unit;
        m_omega = p_omega;
    }

    /**
     * finds simple negotiable triangles
     */
    @Override
    public void findSimple()
    {
        final HashMap<Integer, CNegotiableElement> l_triangles = new HashMap<>();
        final AtomicInteger l_counter = new AtomicInteger( 0 );
        m_env.nodes().forEach( n ->
        {
            final List<IEdge> l_edges = m_env.edges().stream().filter( e -> e.from().equals( n ) ).collect( Collectors.toList() );
            //tests all the edges in the network
            l_edges.forEach( original ->
            {
                final INode l_dest = original.to();
                //selects all other edges that have the same endpoint as the "original" edge
                final List<IEdge> l_tos =  m_env.edges().stream().filter( et -> et.to().equals( l_dest ) ).collect( Collectors.toList() );
                l_tos.forEach( alone ->
                {
                    //find the third edge which connects the "original" to the "alone" edges
                    if ( !m_env.edges().stream()
                            .filter( ea -> ea.from().equals( n ) && ea.to().equals( alone.from() ) )
                            .findFirst().isEmpty() )
                    {
                        final IEdge l_common = m_env.edges().stream()
                                .filter( ea -> ea.from().equals( n ) && ea.to().equals( alone.from() )  )
                                .findFirst().get();
                        //creates the triangle
                        final CNegotiableElement l_trig = new CNegotiableElement( Collections.singletonList( original ),
                                Collections.singletonList( l_common ), Collections.singletonList( alone ) );
                        l_triangles.put( l_counter.get(), l_trig  );
                        l_counter.getAndIncrement();
                    }
                } );
            } );
        } );
        s_logger.log( Level.INFO, "Total triangles: " + l_triangles.keySet().size() );

        //new list of only valid "negotiable" triangles
        l_counter.set( 0 );
        final HashMap<Integer, CNegotiableElement> l_negotiable = new HashMap<>();
        l_triangles.keySet().forEach( t ->
        {
            if ( validateSimple( l_triangles.get( t ) ) )
            {
                l_negotiable.put( l_counter.get(), l_triangles.get( t ) );
                l_counter.getAndIncrement();
            }
        } );
        s_logger.log( Level.INFO, "Valid triangles: " + l_counter.get() );

        l_negotiable.keySet().forEach( k -> s_logger.log( Level.INFO, "original: " + l_negotiable.get( k ).original()
                + " proposed:" + l_negotiable.get( k ).common()
                + " alone:" + l_negotiable.get( k ).alone() ) );
    }

    /**
     * validates simple negotiable triangles
     * @param p_el the triangle
     * @return whether it is valid or not
     */
    @Override
    public Boolean validateSimple( final INegotiableElement p_el )
    {
        final List<IEdge> l_list = new ArrayList<>();
        //calculates the cost reduction possible based on omega
        final Double l_costreduction = p_el.common().get( 0 ).weight().doubleValue()
                - ( p_el.common().get( 0 ).weight().doubleValue() / 2 + p_el.common().get( 0 ).weight().doubleValue() / m_omega );

        //calculates the utility of the common route with the cost reduction
        l_list.add( p_el.common().get( 0 ) );
        final Double l_common = m_veh.utility().calculateMakro( l_list, 1.0, m_unit, l_costreduction, m_veh.preferences() );
        l_list.clear();

        //calculates the utility of the alone route
        l_list.add( p_el.alone().get( 0 ) );
        final Double l_alone = m_veh.utility().calculateMakro( l_list, 1.0, m_unit, 0.0, m_veh.preferences() );
        l_list.clear();

        //calculates the utility of the original route
        l_list.add( p_el.original().get( 0 ) );
        final Double l_original =  m_veh.utility().calculateMakro( l_list, 1.0, m_unit, 0.0, m_veh.preferences() );
        l_list.clear();

        //calculates the utility of the common route withOUT the cost reduction
        l_list.add( p_el.common().get( 0 ) );
        final Double l_common2 =  m_veh.utility().calculateMakro( l_list, 1.0, m_unit, l_costreduction * 2 - 0.001, m_veh.preferences() );

        //the condition for the negotiability
        if   ( (  l_common + l_alone > l_original ) && ( l_common2 + l_alone < l_original ) )
            return true;
        return false;
    }

    /**
     * finds complex negotiable shapes (polygons)
     */
    @Override
    public void findComplex()
    {
        final HashMap<Integer, CNegotiableElement> l_polygons = new HashMap<>();
        final AtomicInteger l_counter = new AtomicInteger( 0 );
        final AtomicInteger l_trapezoidcount = new AtomicInteger( 0 );
        m_env.nodes().forEach( n ->
        {
            //tests all the edges in the network
            final List<IEdge> l_edges = m_env.edges().stream().filter( e -> e.from().equals( n ) ).collect( Collectors.toList() );
            l_edges.forEach( original ->
            {
                //finds alternative routes
                final List<IEdge> l_alternative = m_env.findMultiplePaths( original );
                if ( ( l_alternative != null ) && ( l_alternative.size() != 1 ) )
                {
                    l_trapezoidcount.getAndIncrement();
                    IntStream.range( 0, l_alternative.size() - 1 ).boxed().forEach( j ->
                    {
                        //constructs polygon
                        final List<IEdge> l_original = Collections.singletonList( original );
                        final List<IEdge> l_common = new ArrayList<>();
                        final List<IEdge> l_alone = new ArrayList<>();
                        IntStream.range( 0, l_alternative.size() - 1  ).boxed().forEach( i ->
                        {
                            if ( i <= j ) l_common.add( l_alternative.get( i ) );
                            else l_alone.add( l_alternative.get( i ) );
                        } );
                        l_alone.add( l_alternative.get( l_alternative.size() - 1 ) );
                        final CNegotiableElement l_trap = new CNegotiableElement( l_original, l_common, l_alone );
                        //validates polygon
                        if ( validateComplex( l_trap ) )
                        {
                            l_polygons.put( l_counter.get(), l_trap );
                            l_counter.getAndIncrement();
                        }
                    } );
                }
            } );
        } );
        s_logger.log( Level.INFO, "Total Trapezes: " + l_trapezoidcount.get() );
        s_logger.log( Level.INFO, "Valid Trapezes: " + l_polygons.size() );
        l_polygons.keySet().forEach( k ->
        {
            s_logger.log( Level.INFO, "Original: " + l_polygons.get( k ).original() );
            s_logger.log( Level.INFO, "Common: " + l_polygons.get( k ).common() );
            s_logger.log( Level.INFO, "Alone: " + l_polygons.get( k ).alone() );
        } );

        //calculates the complexity of the negotiable size
        final HashMap<Integer, Integer> l_complexitymap = new HashMap<>();
        l_polygons.keySet().forEach( k ->
        {
            l_complexitymap.put( l_polygons.get( k ).common().size(), l_complexitymap.getOrDefault( l_polygons.get( k ).common().size(), 1 ) + 1 );
        } );
        l_complexitymap.keySet().forEach( k -> s_logger.log( Level.INFO, "Size: " + k + " has " + l_complexitymap.get( k ) + " occurences." ) );
    }

    /**
     * validates the complex negotiable polygon
     * @param p_el the polygon
     * @return whether it is valid or not
     */
    @Override
    public Boolean validateComplex( final INegotiableElement p_el )
    {
        final AtomicDouble l_routeweight = new AtomicDouble( 0.0 );
        //calculates the cost reduction possible based on omega
        p_el.common().forEach( e -> l_routeweight.getAndAdd( e.weight().doubleValue() ) );
        final Double l_costreduction = l_routeweight.get() - ( l_routeweight.get() / 2 + l_routeweight.get() / m_omega );

        //calculates the utility of the common route with the cost reduction
        final Double l_common = m_veh.utility().calculateMakro( p_el.common(), 1.0, m_unit, l_costreduction, m_veh.preferences() );

        //calculates the utility of the alone route
        final Double l_alone = m_veh.utility().calculateMakro( p_el.alone(), 1.0, m_unit, 0.0, m_veh.preferences() );

        //calculates the utility of the original route
        final Double l_original =  m_veh.utility().calculateMakro( p_el.original(), 1.0, m_unit, 0.0, m_veh.preferences() );

        //calculates the utility of the common route withOUT the cost reduction
        final Double l_common2 = m_veh.utility().calculateMakro( p_el.common(), 1.0, m_unit, l_costreduction * 2 - 0.001, m_veh.preferences() );


        //the condition for the negotiability
        if ( ( l_common != null ) && ( l_alone != null ) && ( l_original != null ) && ( l_common2 != null )
                && ( l_common + l_alone > l_original ) && ( l_common2 + l_alone < l_original ) )
            return true;
        return false;
    }
}
