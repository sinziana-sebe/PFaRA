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
 * the simplest of offers object
 * used for the haggling portion of negotiation
 */
public class CSimpleOffer implements IOffer
{
    private final String m_id;
    private Double m_buyout;

    public CSimpleOffer( final String p_id, final Double p_buyout )
    {
        m_id = p_id;
        m_buyout = p_buyout;
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
     * returns the non-existant route
     * @return null
     */
    @Override
    public List<IEdge> route()
    {
        return null;
    }


    /**
     * changes the buyout of the offer
     * @param p_buyout the new buyout
     */
    public void changeBuyout( final Double p_buyout )
    {
        m_buyout = p_buyout;
    }
}
