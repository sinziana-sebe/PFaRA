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

import com.google.common.util.concurrent.AtomicDouble;
import org.socialcars.sinziana.pfara.agents.CPreference;
import org.socialcars.sinziana.pfara.agents.proprieties.CUtility;
import org.socialcars.sinziana.pfara.environment.IEdge;
import org.socialcars.sinziana.pfara.units.CUnits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * the negotiation module class
 */
public class CNegotiationModule implements INegotiationModule
{
    private EAgentType m_role;
    private final IProtocol m_protocol;
    private Double m_altroutecost;
    private IOffer m_offer;
    private Double m_lastoffer;
    private Double m_rv;
    private Double m_av;
    private final CUtility m_utility;
    private final CUnits m_unit;
    private final CPreference m_preference;
    private CBiddingModule m_bb;
    private Double m_rvpeak;
    private final Boolean m_mikro;

    /**
     * ctor
     * @param p_protocol the negotiation protocol
     * @param p_utility the utility of the agent
     * @param p_unit the transformation unit
     * @param p_preference the agent's preference
     * @param p_mikro type of movement
     */
    public CNegotiationModule( final IProtocol p_protocol, final CUtility p_utility, final CUnits p_unit, final CPreference p_preference, final Boolean p_mikro )
    {
        m_protocol = p_protocol;
        m_utility = p_utility;
        m_unit = p_unit;
        m_preference = p_preference;
        m_mikro = p_mikro;
    }

    /**
     * handles offers received
     * @param p_offer the offer
     * @param p_oldroute the ego-agent's old route
     * @param p_speed the speed of travel
     * @return string with the response to the offer (accept,reject,haggle)
     */
    @Override
    public String receiveOffer( final IOffer p_offer, final List<IEdge> p_oldroute, final Double p_speed )
    {
        m_role = EAgentType.ACCEPTOR;
        m_lastoffer = p_offer.buyout();
        final List<IEdge> l_newroute = p_offer.route();
        final Double l_oldutility;
        final Double l_newutility;
        //switches movement type
        if ( !m_mikro )
        {
            l_oldutility = m_utility.calculateMakro( p_oldroute, p_speed, m_unit, 0.0, m_preference );
            l_newutility = m_utility.calculateMakro( l_newroute, p_speed, m_unit, p_offer.buyout(), m_preference );
            final AtomicDouble l_altroutecost = new AtomicDouble();
            l_newroute.forEach( e -> l_altroutecost.getAndAdd( e.weight() ) );
            m_altroutecost = l_altroutecost.get();
        }
        else
        {
            l_oldutility = m_utility.calculateMikro( p_oldroute, p_speed, m_unit, 0.0, m_preference );
            l_newutility = m_utility.calculateMikro( l_newroute, p_speed, m_unit, p_offer.buyout(), m_preference );
            final AtomicDouble l_altroutecost = new AtomicDouble();
            l_newroute.forEach( e -> l_altroutecost.getAndAdd(
                    m_unit.distanceToBlocks( e.length().doubleValue() ).doubleValue() / m_unit.speedToBlocks( p_speed ).doubleValue( ) ) );
            m_altroutecost = l_altroutecost.get();
        }

        //switches protocol type
        switch ( m_protocol.type() )
        {
            case AO:
                if ( ( l_newutility != null ) && ( m_utility.rho() <= 0.75 ) && ( m_utility.sigma() <= 0.80 ) )
                {
                    m_rv = m_utility.calculateRV( p_offer.route(), p_speed, m_unit, l_oldutility );
                    if ( l_newutility > l_oldutility )
                    {
                        m_av = m_altroutecost;
                        m_bb = new CBiddingModule( "Initiator", p_offer.buyout(), m_protocol.getDeadline() );
                        return "haggle";
                    }
                    else return "accept";
                }
                else return "reject";
            case TILI:
                if ( ( l_newutility != null ) && ( l_newutility < l_oldutility ) ) return "accept";
                else return "reject";
            default:
                return "";
        }
    }

    /**
     * handles the sending of an offer
     * @param p_route the route offered
     * @param p_name the name of the ego agent
     * @return the offer
     */
    @Override
    public CInitialOffer sendOffer( final List<IEdge> p_route, final String p_name )
    {
        m_role = EAgentType.INITIATOR;
        switch ( m_protocol.type() )
        {
            case TILI:
                final AtomicDouble l_count = new AtomicDouble( 0.0 );
                p_route.forEach( e -> l_count.getAndAdd(
                        ( p_route.get( 0 ).weight().doubleValue()
                                - ( p_route.get( 0 ).weight().doubleValue() + p_route.get( 0 ).weight().doubleValue() / 3 * 2 ) / 2 ) / 4 ) );
                Double l_price = l_count.get();
                List<IEdge> l_desired = new ArrayList<>();
                l_desired.add( p_route.get( 0 ) );
                CInitialOffer l_offer = new CInitialOffer( p_name + m_protocol.getNodeID().name() + p_route.get( 0 ).to() + l_price.toString(), l_price, l_desired );
                return l_offer;
            case AO:
                m_rv = p_route.get( 0 ).weight().doubleValue() - ( p_route.get( 0 ).weight().doubleValue() + p_route.get( 0 ).weight().doubleValue() / 3 * 2 ) / 2 - 0.001;
                l_price = 0.001;
                l_desired = new ArrayList<>();
                l_desired.add( p_route.get( 0 ) );
                l_offer = new CInitialOffer( p_name + m_protocol.getNodeID().name() + p_route.get( 0 ).to() + l_price.toString(), l_price, l_desired );
                m_av = l_price;
                return l_offer;
            default:
                return null;
        }
    }

    /**
     * handles the haggling process
     * @param p_offer the offer in discussion
     * @return string with response to the offer (accept, reject, haggle)
     * @throws IOException file
     */
    @Override
    public String haggle( final CSimpleOffer p_offer ) throws IOException
    {
        if ( m_offer == null ) m_offer = p_offer;
        switch ( m_role )
        {
            case INITIATOR:
                return initiatorHaggle( p_offer );
            case ACCEPTOR:
                return acceptorHaggle( p_offer );
            default:
                return "";
        }
    }

    /**
     * gives the cost of the alternative (the one offered) route
     * @return cost
     */
    @Override
    public Double alternativeRouteCost()
    {
        return m_altroutecost;
    }

    /**
     * the role of the ego agent
     * @return role
     */
    @Override
    public EAgentType role()
    {
        return m_role;
    }

    /**
     * handles the haggling process as an initiator
     * @param p_offer the offer received
     * @return the new buyout
     */
    private String initiatorHaggle( final CSimpleOffer p_offer )
    {
        if ( m_lastoffer == null )
        {
            m_bb = new CBiddingModule( "Acceptor", p_offer.buyout(), m_protocol.getDeadline() );
            m_lastoffer = p_offer.buyout();
        }
        if ( ( m_protocol.getRoundCounter() < m_protocol.getDeadline() ) && ( p_offer.buyout() > m_rv ) || ( m_protocol.getRoundCounter() < m_protocol.getDeadline() * 0.75 ) )
        {
            chooseBestBid();
            if ( m_av > m_rv ) m_av = m_rv;
            p_offer.changeBuyout( m_av );
            m_lastoffer = p_offer.buyout();
            return "haggle";
        }
        else if ( ( m_protocol.getRoundCounter() >= m_protocol.getDeadline() ) && ( p_offer.buyout() > m_rv ) ) return "reject";
        else return "accept";
    }

    /**
     * handles the haggling process as an acceptor
     * @param p_offer the offer received
     * @return the new buyout
     */
    private String acceptorHaggle( final CSimpleOffer p_offer )
    {
        if ( ( m_protocol.getRoundCounter() < m_protocol.getDeadline() )
                && ( m_altroutecost - p_offer.buyout() > m_rv ) || ( m_protocol.getRoundCounter() < m_protocol.getDeadline() * 0.75 ) )
        {
            chooseBestBid();
            if (  m_av < m_altroutecost - m_rv ) m_av = m_altroutecost - m_rv;
            p_offer.changeBuyout( m_av );
            m_lastoffer = p_offer.buyout();
            return "haggle";
        }
        else if (  ( ( m_protocol.getRoundCounter() >= m_protocol.getDeadline() )
                && ( m_altroutecost - p_offer.buyout() > m_rv ) )
                || ( ( m_protocol.getRoundCounter() >= m_protocol.getDeadline() )
                && ( m_altroutecost - p_offer.buyout() >= m_preference.maxCost() ) ) ) return "reject";
        else return "accept";
    }

    /**
     * calls the bidding module to select the best bid
     */
    private void chooseBestBid()
    {
        final ArrayList<Double> l_bids = new ArrayList<>();
        Iterator<Double> l_it =  new Random().doubles( 0, 1 ).iterator();
        //the opponent's theorised payment limit based on the deadline and current round
        final Double l_lim = Math.abs( m_av - m_rv ) / m_protocol.getDeadline() * m_protocol.getRoundCounter();
        if ( m_rvpeak == null ) m_rvpeak = m_lastoffer;
        //creates a probability distribution based on opponent's payment limit
        switch ( m_role )
        {
            case INITIATOR:
                l_it = new Random().doubles( m_av, m_av + l_lim ).iterator();
                m_rvpeak -= m_lastoffer / 2;
                break;
            case ACCEPTOR:
                l_it = new Random().doubles( m_av - l_lim, m_av ).iterator();
                m_rvpeak += m_lastoffer / 2;
                break;
            default:
                break;
        }
        //creates bid acceptance probability
        while ( l_bids.size() < 20 ) l_bids.add( l_it.next() );
        Double l_bb = m_bb.getBestBid( l_bids, m_protocol.getRoundCounter(), m_rvpeak );
        if ( l_bb == 0.0 )
        {
            switch ( m_role )
            {
                case INITIATOR:
                    l_bb = l_bids.stream().mapToDouble( d -> d ).max().orElse( m_av );
                    break;
                case ACCEPTOR:
                    l_bb = l_bids.stream().mapToDouble( d -> d ).min().orElse( m_av );
                    break;
                default:
                    break;
            }
        }
        m_av = l_bb;
    }

}
