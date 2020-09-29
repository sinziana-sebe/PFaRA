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
    //private CPodsUtilitySPP m_opt;

    /**
     * ctor
     * triggers the platoon-matching algorithm
     * @param p_pods pods
     * @param p_env environment
     * @param p_unit unit
     * @param p_routes routes
     * @param p_time time
     */
    public CPreGrouping( final ArrayList<CVehicle> p_pods, final CGraph p_env, final CUnits p_unit, final HashMap<CVehicle, List<IEdge>> p_routes, final Integer p_time )
    {
        m_pods = p_pods;
        m_env = p_env;
        m_unit = p_unit;
        m_routes = p_routes;
        m_time = p_time;
        checkforPlatoon();
    }

    /*
    private void runOptimiser( final ArrayList<CVehicle> p_pods, final Integer p_origin ) throws GRBException
    {
        m_opt = new CPodsUtilitySPP( m_env, p_origin, p_pods, m_unit );
        m_opt.solve();
    }*/

    /**
     * groups the vehicles by their location
     * the node where they are currently
     */
    private void checkforPlatoon()
    {
        final ArrayList<CVehicle> l_platooners = new ArrayList<>();
        IntStream.range( 0, m_pods.size() ).boxed().forEach( i ->
        {
            if ( ( !l_platooners.contains( m_pods.get( i ) ) )
                    && ( m_pods.get( i ).position().equals( 0.0 ) )
                    && ( !m_pods.get( i ).location().contentEquals( m_pods.get( i ).destination() ) ) )
            {
                final HashSet<CVehicle> l_platoon = new HashSet<>();
                l_platoon.add( m_pods.get( i ) );
                IntStream.range( i + 1, m_pods.size() ).boxed().forEach( j ->
                {
                    if ( ( m_pods.get( j ).position().equals( 0.0 ) )
                            & ( m_pods.get( j ).location().contentEquals( m_pods.get( i ).location() ) )
                            & ( !m_pods.get( j ).destination().contentEquals( m_pods.get( j ).location() ) )
                            & ( !l_platooners.contains( m_pods.get( j ) ) ) )
                        l_platoon.add( m_pods.get( j ) );
                } );
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
     * based on minimum speed
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
        p_platoon.forEach( p ->
        {
            if ( !p.platooning() ) l_count.getAndSet( l_count.get() + 1 );
        } );
        if ( l_count.get().equals( 0 ) )
        {
            final CVehicle l_pod = p_platoon.get( 0 );
            l_count.set( 0 );
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
     * given pods at certain node, checks the stoplight status and groups vehicles accordingly
     * then runs the optimiser to group vehicles in platoon
     * @param p_platoon collection of pods
     * update routes for pods
     */
    private void groupPlatoon( final ArrayList<CVehicle> p_platoon )
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
        final HashMap<String, ArrayList<CVehicle>> l_pods = new HashMap<>();
        if ( l_green.size() > 0 ) l_pods.put( "green", l_green );
        if ( l_red.size() > 0 ) l_pods.put( "red", l_red );
        l_pods.keySet().forEach( k ->
        {
            /*try
            {
                runOptimiser( new ArrayList<>( l_pods.get( k ) ), Integer.valueOf( p_platoon.iterator().next().location() ) );
                final HashMap<CVehicle, ArrayList<IEdge>> l_platroutes = m_opt.getRoutes();
                final HashMap<IEdge, ArrayList<CVehicle>> l_clusters = platoonSort1( l_platroutes );
                if ( checkFlagged( m_opt.getFlagged(), l_platroutes, m_opt.getNP(), l_clusters ) )
                {
                    l_platroutes.keySet().forEach( p ->
                    {
                        if ( !m_opt.getFlagged().contains( p ) ) m_routes.replace( p, l_platroutes.get( p ) );
                    }  );
                    platoonSort2( new ArrayList<>( p_platoon ) );
                }
            }
            catch ( final GRBException l_err )
            {
                l_err.printStackTrace();
            }*/
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
     * @param p_flagged the flagged pods
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
            while ( ( l_platroute.size() > 2 ) && ( !l_trig.get() ) )
            {
                final IEdge l_final = l_platroute.get( l_platroute.size() - 1 );
                l_platroute.remove( l_final );
                final AtomicDouble l_cost = new AtomicDouble( 0.0 );
                l_platroute.forEach( e -> l_cost.getAndAdd( ( e.weight().doubleValue() + ( e.weight().doubleValue() / 3 * p_np.get( e ) ) ) / p_np.get( e ) ) );
                final List<IEdge> l_patched = m_env.route( l_final.from().name(), p.destination()  );
                l_patched.forEach( e -> l_cost.getAndAdd( e.weight().doubleValue() ) );
                if ( l_cost.get() <= p.preferences().maxCost() ) l_trig.set( true );
            }
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
     * checks the costs of the parters of a flagged vehicle
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
                if ( p_flaggedroute.contains( e ) ) l_cost.getAndAdd( ( e.weight().doubleValue() + ( e.weight().doubleValue() / 3 * p_np.get( e ) ) ) / p_np.get( e ) );
                else  l_cost.getAndAdd( ( e.weight().doubleValue() + ( e.weight().doubleValue() / 3 * ( p_np.get( e ) - 1 )  ) ) / ( p_np.get( e ) - 1 ) );
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
