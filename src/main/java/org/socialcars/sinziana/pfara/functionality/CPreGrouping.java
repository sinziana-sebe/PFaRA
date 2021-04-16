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

package org.socialcars.sinziana.pfara.functionality;

import com.google.common.util.concurrent.AtomicDouble;
import gurobi.GRBException;
import org.socialcars.sinziana.pfara.agents.CVehicle;
import org.socialcars.sinziana.pfara.environment.CGraph;
import org.socialcars.sinziana.pfara.environment.ELightState;
import org.socialcars.sinziana.pfara.environment.IEdge;
import org.socialcars.sinziana.pfara.units.CUnits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

/**
 * class for pre-sorting vehicles
 * on location, speed and light-cycle
 */
public class CPreGrouping
{
    private final ArrayList<CVehicle> m_pods;
    private final CGraph m_env;
    private final CUnits m_unit;
    private final Integer m_time;
    private HashMap<CVehicle, List<IEdge>> m_routes;
    private final Boolean m_mikro;
    private final Boolean m_opt;
    private final Double m_omega;

    /**
     * ctor
     * triggers the platoon-matching algorithm
     * @param p_pods pods
     * @param p_env environment
     * @param p_unit unit
     * @param p_routes routes
     * @param p_time time
     */
    public CPreGrouping( final ArrayList<CVehicle> p_pods, final CGraph p_env, final CUnits p_unit, final HashMap<CVehicle, List<IEdge>> p_routes,
                         final Integer p_time, final Boolean p_mikro, final Boolean p_opt, final Double p_omega )
    {
        m_pods = p_pods;
        m_env = p_env;
        m_unit = p_unit;
        m_routes = p_routes;
        m_time = p_time;
        m_mikro = p_mikro;
        m_opt = p_opt;
        m_omega = p_omega;
        checkforPlatoon();
    }

    /**
     * groups the vehicles by their location
     * the node where they are currently
     */
    private void checkforPlatoon()
    {
        final ArrayList<CVehicle> l_platooners = new ArrayList<>();
        IntStream.range( 0, m_pods.size() ).boxed().forEach( i ->
        {
            //if the vehicle was not already grouped, is at a node and not at its destination
            if ( ( !l_platooners.contains( m_pods.get( i ) ) )
                    && ( m_pods.get( i ).position().equals( 0.0 ) )
                    && ( !m_pods.get( i ).location().contentEquals( m_pods.get( i ).destination() ) ) )
            {
                //create a new platooning group
                final HashSet<CVehicle> l_platoon = new HashSet<>();
                l_platoon.add( m_pods.get( i ) );
                //find all the other vehicles at the same position
                //that were not already grouped and not at their destination
                IntStream.range( i + 1, m_pods.size() ).boxed().forEach( j ->
                {
                    if ( ( m_pods.get( j ).position().equals( 0.0 ) )
                            & ( m_pods.get( j ).location().contentEquals( m_pods.get( i ).location() ) )
                            & ( !m_pods.get( j ).destination().contentEquals( m_pods.get( j ).location() ) )
                            & ( !l_platooners.contains( m_pods.get( j ) ) ) )
                        l_platoon.add( m_pods.get( j ) );
                } );
                //if there are more than 2 vehicles in a group,
                //move to step 2
                if ( l_platoon.size() > 1 )
                {
                    l_platooners.addAll( l_platoon );
                    checkforPlatoonStage2( l_platoon );
                }
            }
        } );
    }

    /**
     * checks if the group of vehicles is a new platoon or not
     * @param p_platoon group of pods
     * if same platoon do nothing, otherwise group the vehicles
     * based on their possible minimum speed
     * ensuring that they can drive together in a platoon and no vehicle gets left behind
     */
    private void checkforPlatoonStage2( final HashSet<CVehicle> p_platoon )
    {
        final ArrayList<CVehicle> l_pl = new ArrayList<>( p_platoon );
        if ( !platoonIntact( l_pl ) )
        {
            while ( !l_pl.isEmpty() )
            {
                final ArrayList<CVehicle> l_plat = new ArrayList<>();
                final ArrayList<Double> l_minspeeds = new ArrayList<>();
                l_pl.forEach( p -> l_minspeeds.add( p.preferences().minSpeed() ) );
                final Double l_maxminspeed = Collections.max( l_minspeeds );
                l_pl.forEach( p ->
                {
                    if ( p.preferences().maxSpeed() > l_maxminspeed ) l_plat.add( p );
                } );
                l_plat.forEach( p -> l_pl.remove( p ) );
                //if the vehicles are speed compatible, move on to the actual grouping
                if ( l_plat.size() > 1  ) groupPlatoon( l_plat );
            }
        }
    }

    /**
     * method to check if a platoon needs re-forming or stays intact
     * @param p_platoon platoon
     * @return true if reforming is necessary, false otherwise
     */
    private Boolean platoonIntact( final ArrayList<CVehicle> p_platoon )
    {
        final AtomicBoolean l_trig = new AtomicBoolean( false );
        final AtomicReference<Integer> l_count = new AtomicReference<>( 0 );
        //if there are no vehicles that are currently platooning
        p_platoon.forEach( p ->
        {
            if ( !p.platooning() ) l_count.getAndSet( l_count.get() + 1 );
        } );
        if ( l_count.get().equals( 0 ) )
        {
            final CVehicle l_pod = p_platoon.get( 0 );
            l_count.set( 0 );
            //if the vehicles are not in the same platoon
            p_platoon.forEach( c ->
            {
                if ( ( !l_pod.companions().contains( c ) ) && ( !l_pod.equals( c ) ) )
                    l_count.getAndSet( l_count.get() + 1 );
            } );
            if ( l_count.get().equals( 0 ) ) l_trig.set( true );
        }
        return l_trig.get();
    }

    /**
     * given the movement-type categorises the vehicles further
     * also based on whether the optimiser approach is needed, runs the optimiser to group vehicles in platoon
     * @param p_platoon collection of pods
     * update routes for pods
     */
    private void groupPlatoon( final ArrayList<CVehicle> p_platoon )
    {
        final HashMap<String, ArrayList<CVehicle>> l_vehicles = new HashMap<>();
        if ( m_mikro ) groupByLights( p_platoon, l_vehicles );
        else l_vehicles.put( "", p_platoon );
        if ( m_opt ) runOptimiser( l_vehicles );
        l_vehicles.keySet().forEach( k -> platoonSort2( l_vehicles.get( k ) ) );
    }

    /**
     * given pods at certain node, checks the stoplight status and groups vehicles accordingly
     * then runs the optimiser to group vehicles in platoon
     * @param p_platoon collection of vehicles
     * @param p_vehicles the vehicles sorted by light groups
     */
    private void groupByLights( final ArrayList<CVehicle> p_platoon, final HashMap<String, ArrayList<CVehicle>> p_vehicles )
    {
        final ArrayList<CVehicle> l_red = new ArrayList<>();
        final ArrayList<CVehicle> l_green = new ArrayList<>();
        p_platoon.forEach( p ->
        {
            if ( p.getLastEdge() != null )
            {
                if ( ( p.getLastEdge().stoplight() != null ) && ( p.getLastEdge().stoplight().state().equals( ELightState.RED ) ) ) l_red.add( p );
                else l_green.add( p );
            }
            else l_green.add( p );
        } );

        if ( l_green.size() > 0 ) p_vehicles.put( "green", l_green );
        if ( l_red.size() > 0 ) p_vehicles.put( "red", l_red );
    }

    /**
     * runs the optimiser and performs the subsequent checks
     * namely ensuring the flagged vehicles are dealt with accordingly
     * @param p_vehicles the map with the grouped vehicles
     */
    private void runOptimiser( final HashMap<String, ArrayList<CVehicle>> p_vehicles )
    {
        p_vehicles.keySet().forEach( k ->
        {
            try
            {
                final COptimiser l_opt = new COptimiser( m_env, Integer.valueOf( p_vehicles.get( k ).iterator().next().location() ),
                        p_vehicles.get( k ), m_unit, m_omega );
                final HashMap<CVehicle, ArrayList<IEdge>> l_platroutes = l_opt.getRoutes();
                final HashMap<IEdge, ArrayList<CVehicle>> l_clusters = platoonSort1( l_platroutes );
                if ( checkFlagged( l_opt.getFlagged(), l_platroutes, l_opt.getNP(), l_clusters ) )
                {
                    l_platroutes.keySet().forEach( p ->
                    {
                        if ( !l_opt.getFlagged().contains( p ) ) m_routes.replace( p, l_platroutes.get( p ) );
                    } );
                }
            }
            catch ( final GRBException l_err )
            {
                l_err.printStackTrace();
            }
        } );
    }


    /**
     * clusters the vehicles based on the optimiser found route
     * @param p_platroutes routes given by the optimiser
     * @return clustered vehicles by edges
     */
    private HashMap<IEdge, ArrayList<CVehicle>> platoonSort1( final HashMap<CVehicle, ArrayList<IEdge>> p_platroutes )
    {
        final HashMap<IEdge, ArrayList<CVehicle>> l_res = new HashMap<>();
        p_platroutes.keySet().forEach( p ->
        {
            if ( l_res.keySet().contains( p_platroutes.get( p ).get( 0 ) ) )
                l_res.get( p_platroutes.get( p ).get( 0 ) ).add( p );
            else
            {
                l_res.put(  p_platroutes.get( p ).get( 0 ), new ArrayList<>() );
                l_res.get(  p_platroutes.get( p ).get( 0 ) ).add( p );
            }

        } );
        return l_res;
    }

    /**
     * checks the vehicles that have higher cost with the optimiser solution than their cost allowance
     * @param p_flagged the flagged vehicles
     * @param p_platroutes the complete routes given by the optimiser
     * @param p_np the number of vehicles per edge
     * @param p_clusters the cluster the flagged vehicles belong to
     * @return true if a solution is found and the vehicles can platoon, false if the solution is unviable for the flagged vehicle or their respective partners
     */
    private Boolean checkFlagged( final ArrayList<CVehicle> p_flagged, final HashMap<CVehicle, ArrayList<IEdge>> p_platroutes,
                                  final HashMap<IEdge, Integer> p_np, final HashMap<IEdge, ArrayList<CVehicle>> p_clusters )
    {
        final AtomicBoolean l_failsafe = new AtomicBoolean( true );
        p_flagged.forEach( p ->
        {
            final ArrayList<IEdge> l_platroute = p_platroutes.get( p );
            final AtomicBoolean l_trig = new AtomicBoolean( false );
            //attemps to find still SOME common route between the vehicles
            while ( ( l_platroute.size() > 2 ) && ( !l_trig.get() ) )
            {
                final IEdge l_final = l_platroute.get( l_platroute.size() - 1 );
                l_platroute.remove( l_final );
                final AtomicDouble l_cost = new AtomicDouble( 0.0 );
                l_platroute.forEach( e -> l_cost.getAndAdd( ( e.weight().doubleValue() + ( e.weight().doubleValue() / m_omega * p_np.get( e ) ) ) / p_np.get( e ) ) );
                final List<IEdge> l_patched = m_env.route( l_final.from().name(), p.destination()  );
                l_patched.forEach( e -> l_cost.getAndAdd( e.weight().doubleValue() ) );
                if ( l_cost.get() <= p.preferences().maxCost() ) l_trig.set( true );
            }
            //if an appropriate shorter route is found
            //the vehicles are grouped and the routes changed accordingly
            if ( l_trig.get() )
            {
                final AtomicReference<IEdge> l_pos = new AtomicReference<>();
                p_clusters.keySet().forEach( e ->
                {
                    if ( p_clusters.get( e ).contains( p ) ) l_pos.set( e );
                } );
                final ArrayList<CVehicle> l_cluster = p_clusters.get( l_pos.get() );
                l_cluster.remove( p );
                if ( checkPartners( l_cluster, l_platroute, p_platroutes, p_np ) )
                {
                    final List<IEdge> l_patched = m_env.route( l_platroute.get( l_platroute.size() - 1  ).to().name(), p.destination() );
                    l_platroute.addAll( l_patched );
                    m_routes.replace( p, l_platroute );
                }
                else l_failsafe.set( false );
            }
            else l_failsafe.set( false );
        } );
        return l_failsafe.get();
    }

    /**
     * checks the costs of the partners of a flagged vehicle
     * @param p_partners partners
     * @param p_flaggedroute the flagged common route
     * @param p_platroutes the routes given by the optimiser
     * @param p_np the number of vehicles per edge
     * @return true if solution is viable for partners, false otherwise
     */
    private Boolean checkPartners( final ArrayList<CVehicle> p_partners, final ArrayList<IEdge> p_flaggedroute,
                                   final HashMap<CVehicle, ArrayList<IEdge>> p_platroutes, final HashMap<IEdge, Integer> p_np )
    {
        final AtomicBoolean l_trig = new AtomicBoolean( true );
        p_partners.forEach( p ->
        {
            final AtomicDouble l_cost = new AtomicDouble( 0.0 );
            p_platroutes.get( p ).forEach( e ->
            {
                if ( p_flaggedroute.contains( e ) ) l_cost.getAndAdd( ( e.weight().doubleValue() + ( e.weight().doubleValue() / m_omega * p_np.get( e ) ) ) / p_np.get( e ) );
                else  l_cost.getAndAdd( ( e.weight().doubleValue() + ( e.weight().doubleValue() / m_omega * ( p_np.get( e ) - 1 )  ) ) / ( p_np.get( e ) - 1 ) );
            } );
            if ( l_cost.get() > p.preferences().maxCost() ) l_trig.set( false );
        } );
        return l_trig.get();
    }

    /**
     * sorts the platoon into groups based on route
     * @param p_platoon platoon
     */
    private void platoonSort2( final ArrayList<CVehicle> p_platoon )
    {
        final ArrayList<CVehicle> l_index = new ArrayList<>( p_platoon );
        p_platoon.forEach( p ->
        {
            if ( p.platooning() ) p.companions().removeAll( p.companions() );
        } );
        IntStream.range( 0, p_platoon.size() ).boxed().forEach( i ->
        {
            l_index.remove( p_platoon.get( i ) );
            final ArrayList<CVehicle> l_companions = new ArrayList<>();
            IntStream.range( i + 1, p_platoon.size() ).boxed().forEach( j ->
            {
                if ( ( m_routes.get( p_platoon.get( i ) ).get( 0 ).equals( m_routes.get( p_platoon.get( j ) ).get( 0 ) ) )
                        && ( !p_platoon.get( i ).equals( p_platoon.get( j ) ) )
                        && ( l_index.contains( p_platoon.get( j ) ) ) )
                {
                    l_companions.add( p_platoon.get( j ) );
                    l_index.remove( p_platoon.get( j ) );
                }
            } );
            l_companions.add( p_platoon.get( i ) );
            if ( l_companions.size() > 1 ) platoonForm( l_companions );
        } );
    }

    /**
     * actions to logically form the platoon
     * @param p_platoon platoon
     */
    private void platoonForm( final ArrayList<CVehicle> p_platoon )
    {
        p_platoon.forEach( p ->
        {
            final ArrayList<CVehicle> l_pl = new ArrayList<>( p_platoon );
            l_pl.remove( p );
            p.formed( p.location(), m_time, l_pl );
        } );
    }
}
