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

import org.socialcars.sinziana.pfara.environment.IEdge;

import java.util.List;

/**
 * the initial offer object
 * only used at the beginning of a negotiation
 */
public class CInitialOffer implements IOffer
{
    private final String m_id;
    private Double m_buyout;
    private List<IEdge> m_route;

    /**
     * ctor
     * @param p_id the offer id
     * @param p_buyout the buyout ammount
     * @param p_route the alternative route offered
     */
    public CInitialOffer( final String p_id, final Double p_buyout, final List<IEdge> p_route )
    {
        m_id = p_id;
        m_buyout = p_buyout;
        m_route = p_route;
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
}
