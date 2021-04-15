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

import org.socialcars.sinziana.pfara.agents.CVehicle;
import org.socialcars.sinziana.pfara.agents.events.EEventType;
import org.socialcars.sinziana.pfara.agents.events.IEvent;
import org.socialcars.sinziana.pfara.environment.CGraph;
import org.socialcars.sinziana.pfara.environment.IEdge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * class for all actions necessary
 * when a vehicle/platoon reaches the end of an edge
 */
public class CEdgeEnd
{
    private final ArrayList<CVehicle> m_pods;
    private final HashMap<CVehicle, List<IEdge>> m_routes;
    private final Integer m_time;
    private final CGraph m_env;

    /**
     * ctor
     * triggers the edge end protocols
     * @param p_pods pods
     * @param p_routes routes
     * @param p_time time
     */
    public CEdgeEnd( final ArrayList<CVehicle> p_pods, final HashMap<CVehicle, List<IEdge>> p_routes, final Integer p_time, final CGraph p_env )
    {
        m_pods = p_pods;
        m_routes = p_routes;
        m_time = p_time;
        m_env = p_env;
        checkSplits();
    }

    /**
     * checks for vehicles who have to split from their platoon
     */
    private void checkSplits()
    {
        m_pods.forEach( p ->
        {
            edgeEnd( p );
            if ( ( p.platooning() ) && ( p.position().equals( 0.00 ) ) && ( p.location() != p.destination() ) )
            {
                final AtomicBoolean l_trig = new AtomicBoolean( false );
                final IEdge l_edge = m_routes.get( p ).iterator().next();
                p.companions().forEach( c ->
                {
                    if ( ( !m_routes.get( c ).isEmpty() ) && ( !l_edge.equals( m_routes.get( c ).iterator().next() ) ) ) l_trig.set( true );
                } );
                if ( l_trig.get() )
                {
                    p.split( p.location(), m_time );
                    p.companions().forEach( c ->
                    {
                        c.updatePrecedence();
                        c.companions().remove( p );
                    } );
                }
            }
        } );

    }


    /**
     * deals with actions when pod reaches the end node of an edge
     * @param p_pod vehicle
     */
    private void edgeEnd( final CVehicle p_pod )
    {
        if ( p_pod.location().contentEquals( p_pod.destination() ) )
        {
            final AtomicBoolean l_dest = new AtomicBoolean( false );
            final Collection<IEvent> l_events = p_pod.events();
            l_events.forEach( e ->
            {
                if ( e.what().equals( EEventType.COMPLETED ) ) l_dest.set( true );
            } );
            if ( !l_dest.get() ) destinationReach( p_pod );
        }
    }

    /**
     * deals with the actions when a pod reaches it's destination
     * @param p_pod vehicle
     */
    private void destinationReach( final CVehicle p_pod )
    {
        if ( p_pod.platooning() )
        {
            p_pod.companions().forEach( c ->
            {
                c.updatePrecedence();
                c.companions().remove( p_pod );
            } );
            p_pod.split( p_pod.location(), m_time );
        }
        p_pod.completed( p_pod.destination(), m_time );
    }

    /**
     * checks if any pods are travelling alone after being a part of a platoon
     * registers a split if yes
     * public since it needs to be called before departure
     * other functions are called at arrival
     */
    public void checkLoners()
    {
        m_pods.forEach( p ->
        {
            final AtomicBoolean l_dest = new AtomicBoolean( false );
            if ( ( p.platooning() ) && ( p.companions().isEmpty() ) )
            {
                p.events().forEach( e ->
                {
                    if ( e.what().equals( EEventType.FORMED ) ) l_dest.set( true );
                    if ( e.what().equals( EEventType.SPLIT ) ) l_dest.set( false );
                } );
            }
            if ( l_dest.get() ) p.split( p.location(), m_time );
        } );
    }
}
