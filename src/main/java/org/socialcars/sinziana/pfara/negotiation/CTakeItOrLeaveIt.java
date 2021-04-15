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

package org.socialcars.sinziana.pfara.negotiation;

import org.socialcars.sinziana.pfara.agents.CVehicle;
import org.socialcars.sinziana.pfara.environment.CGraph;
import org.socialcars.sinziana.pfara.environment.IEdge;
import org.socialcars.sinziana.pfara.environment.INode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * the take it or leave it protocol class
 */
public class CTakeItOrLeaveIt implements IProtocol
{
    private final CGraph m_env;
    private final INode m_id;
    private final List<CVehicle> m_crowd;
    private CCompleteOffer m_offer;
    private HashMap<CVehicle, List<IEdge>> m_routes;
    private Integer m_time;

    public CTakeItOrLeaveIt( final List<CVehicle> p_potentials, final INode p_id, final CGraph p_env, final HashMap<CVehicle, List<IEdge>> p_routes )
    {
        m_env = p_env;
        m_id = p_id;
        m_crowd = p_potentials;
        m_routes = p_routes;
    }


    /**
     * opens the protocol
     * @param p_time the time of opening
     */
    @Override
    public void open( final Integer p_time )
    {
        m_crowd.forEach( p -> p.joinParty( this ) );
        m_time = p_time;
    }

    /**
     * the type of protocol
     * @return the protocol's type
     */
    @Override
    public EProtocolType type()
    {
        return EProtocolType.TILI;
    }

    /**
     * function for receiving an offer and sending offers
     * @param p_offeror the initiating agent sending the offer
     * @param p_offer the offer
     */
    @Override
    public void sendOffer( final CVehicle p_offeror, final CInitialOffer p_offer )
    {
        m_crowd.forEach( p ->
        {
            if ( p != p_offeror )
            {
                final List<IEdge> l_newroute = m_env.route( p.destination(), p_offer.route() );
                final CCompleteOffer l_newoffer = new CCompleteOffer( p_offeror, p_offer, p, l_newroute );
                m_offer = l_newoffer;
                try
                {
                    p.receiveOffer( new CInitialOffer( l_newoffer.id(), l_newoffer.savings() + p_offer.buyout(), l_newroute ), m_routes.get( p ) );
                }
                catch ( final IOException l_err )
                {
                    l_err.printStackTrace();
                }
            }
        } );
    }

    /**
     * function for receiving an accepted offer
     * @param p_acceptor the vehicle accepting the offer
     * @param p_offer the offe
     * @throws IOException file exception
     */
    @Override
    public void receiveAccept( final CVehicle p_acceptor, final IOffer p_offer ) throws IOException
    {
        final CCompleteOffer l_co = m_offer;
        l_co.accept();
        if ( l_co.acceptor().equals( p_acceptor ) )
        {
            l_co.changeBuyout( p_offer.buyout() - l_co.savings() );
        }
        else
        {
            l_co.changeBuyout( p_offer.buyout() );
        }
        m_routes.get( l_co.acceptor() ).clear();
        m_routes.put( l_co.acceptor(), l_co.alternativeRoute() );
        l_co.offeror().acceptUpdateCost( l_co.buyout() );
        l_co.offeror().release( this );
        l_co.acceptor().acceptUpdateCost( l_co.buyout() );
        l_co.acceptor().release( this );
        final ArrayList<CVehicle> l_of = new ArrayList<>();
        l_of.add( l_co.offeror() );
        l_co.acceptor().formed( p_acceptor.location(), m_time, l_of );
        final ArrayList<CVehicle> l_ac = new ArrayList<>();
        l_ac.add( l_co.acceptor() );
        l_co.offeror().formed( p_acceptor.location(), m_time, l_ac );
    }

    /**
     * function for receiving a rejected offer
     * @param p_offer the offer
     * @throws IOException file exception
     */
    @Override
    public void receiveReject( final IOffer p_offer ) throws IOException
    {
        final CCompleteOffer l_co = m_offer;
        l_co.reject();
        l_co.offeror().release( this );
        l_co.acceptor().release( this );
    }

    /**
     * function for receiving a breakaway offer
     * @param p_offer the offer
     * @throws IOException file exception
     */
    @Override
    public void receiveBreakaway( final IOffer p_offer ) throws IOException
    {
        final CCompleteOffer l_co = m_offer;
        l_co.reject();
        l_co.offeror().release( this );
        l_co.acceptor().release( this );
    }

    /**
     * funtion for the haggling process
     * @param p_veh the vehicle sending the haggling offer
     * @param p_offer the offer
     * @throws IOException file exception
     */
    @Override
    public void haggle( final CVehicle p_veh, final CSimpleOffer p_offer )
    {
    }

    /**
     * gets the round counter
     * @return round counter
     */
    @Override
    public Integer getRoundCounter()
    {
        return null;
    }

    /**
     * gets the deadline
     * the maximum number of rounds
     * @return the deadline
     */
    @Override
    public Integer getDeadline()
    {
        return 1;
    }

    /**
     * the id of the protocol
     * given by the node name where the negotiation takes place
     * @return the node
     */
    @Override
    public INode getNodeID()
    {
        return m_id;
    }
}
