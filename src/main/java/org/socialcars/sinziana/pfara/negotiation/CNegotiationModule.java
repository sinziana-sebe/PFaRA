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
import java.util.logging.Logger;

public class CNegotiationModule implements INegotiationModule
{
    private static Logger s_logger;

    private EAgentType m_role;
    private final IProtocol m_protocol;
    private IOffer m_offer;
    private Double m_lastoffer;
    private Double m_rv;
    private Double m_av;
    private Double m_altroucost;
    private final CUtility m_utility;
    private final CUnits m_unit;
    private final CPreference m_preference;
    private CBiddingModule m_bb;
    private Double m_rvpeak;

    public CNegotiationModule( final IProtocol p_protocol, final CUtility p_utility, final CUnits p_unit, final CPreference p_preference )
    {
        m_protocol = p_protocol;
        m_utility = p_utility;
        m_unit = p_unit;
        m_preference = p_preference;
    }

    @Override
    public String receiveOffer( final IOffer p_offer, final Double p_oldroutelength, final Double p_speed ) throws IOException
    {
        m_role = EAgentType.ACCEPTOR;
        m_lastoffer = p_offer.buyout();
        final List<IEdge> l_newroute = p_offer.route();
        final Double l_oldutility = m_utility.calculate( p_oldroutelength, p_speed, m_unit, 0.0, m_preference );
        final Double l_newutility = m_utility.calculate( l_newroute, p_speed, m_unit, p_offer.buyout(), m_preference );
        switch ( m_protocol.type() )
        {
            case AO:
                if ( ( l_newutility != null ) && ( m_utility.rho() < 0.75 ) && ( m_utility.sigma() < 0.75 ) )
                {
                    m_rv = m_utility.calculateRV( p_offer.route(), p_speed, m_unit, l_oldutility );
                    if ( l_newutility > l_oldutility )
                    {
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

    private String initiatorHaggle( final CSimpleOffer p_offer ) throws IOException
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

    private String acceptorHaggle( final CSimpleOffer p_offer ) throws IOException
    {
        if ( ( m_protocol.getRoundCounter() < m_protocol.getDeadline() )
                && ( m_altroucost - p_offer.buyout() > m_rv ) || ( m_protocol.getRoundCounter() < m_protocol.getDeadline() * 0.75 ) )
        {
            chooseBestBid();
            if (  m_av < m_altroucost - m_rv ) m_av = m_altroucost - m_rv;
            p_offer.changeBuyout( m_av );
            m_lastoffer = p_offer.buyout();
            return "haggle";

        }
        else if (  ( ( m_protocol.getRoundCounter() >= m_protocol.getDeadline() )
                && ( m_altroucost - p_offer.buyout() > m_rv ) )
                || ( ( m_protocol.getRoundCounter() >= m_protocol.getDeadline() )
                && ( m_altroucost - p_offer.buyout() >= m_preference.maxCost() ) ) ) return "reject";
        else return "accept";
    }

    private void chooseBestBid()
    {
        final ArrayList<Double> l_bids = new ArrayList<>();
        Iterator<Double> l_it =  new Random().doubles( 0, 1 ).iterator();
        final Double l_lim = Math.abs( m_av - m_rv ) / m_protocol.getDeadline() * m_protocol.getRoundCounter();
        if ( m_rvpeak == null ) m_rvpeak = m_lastoffer;
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
        //return l_bb;
    }

}
