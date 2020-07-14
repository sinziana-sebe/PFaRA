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
import org.socialcars.sinziana.pfara.environment.IEdge;

import java.util.List;

/**
 * the complete offer object
 * used to keep track of all the offers exchanged
 */
public class CCompleteOffer implements IOffer
{

    private final String m_id;
    private final CVehicle m_offeror;
    private final List<IEdge> m_route;
    private Double m_buyout;
    private final CVehicle m_acceptor;
    private EOfferState m_state;
    private Double m_savings = 0.0;
    private final List<IEdge> m_altroute;

    /**
     * ctor
     * @param p_offeror the initiating agent
     * @param p_offer the offer
     * @param p_acceptor the accepting agent
     * @param p_newroute the alternative route proposed
     */
    public CCompleteOffer( final CVehicle p_offeror, final CInitialOffer p_offer, final CVehicle p_acceptor, final List<IEdge> p_newroute )
    {
        m_id = p_offeror.name() + "-" + p_acceptor.name();
        m_offeror = p_offeror;
        m_route = p_offer.route();
        m_buyout = p_offer.buyout();
        m_state = EOfferState.PENDING;
        m_acceptor = p_acceptor;
        m_route.forEach( e -> m_savings += e.weight().doubleValue() - ( e.weight().doubleValue() + e.weight().doubleValue() / 3 * 2 ) / 2 );
        m_altroute = p_newroute;
    }

    /**
     * the id of the offer
     * @return the id
     */
    @Override
    public String id()
    {
        return m_id;
    }

    /**
     * the buyout of the offer
     * @return buyout
     */
    @Override
    public Double buyout()
    {
        return m_buyout;
    }

    /**
     * the route of the route
     * @return the route
     */
    @Override
    public List<IEdge> route()
    {
        return m_route;
    }

    /**
     * the savings that platooning generates
     * @return the savings
     */
    public Double savings()
    {
        return m_savings;
    }

    /**
     * changes the buyout of the offer as bids are exchanged
     * @param p_bo the buyout
     */
    protected void changeBuyout( final Double p_bo )
    {
        m_buyout = p_bo;
    }

    /**
     * the accepting agent
     * @return the acceptor
     */
    protected CVehicle acceptor()
    {
        return m_acceptor;
    }

    /**
     *
     * @return
     */
    protected CVehicle offeror()
    {
        return m_offeror;
    }

    /**
     * the alternative route, the one which was proposed
     * @return the alternative route
     */
    public List<IEdge> alternativeRoute()
    {
        return m_altroute;
    }

    /**
     * changes the state of the offer to accept
     */
    protected void accept()
    {
        m_state = EOfferState.ACCEPTED;
    }

    /**
     * changes the state of the offer to reject
     */
    protected void reject()
    {
        m_state = EOfferState.REJECTED;
    }

    /**
     * changes the offer state to closed
     */
    protected void close()
    {
        m_state = EOfferState.CLOSED;
    }

    /**
     * the current state of the offer
     * @return the state
     */
    protected EOfferState state()
    {
        return m_state;
    }
}
