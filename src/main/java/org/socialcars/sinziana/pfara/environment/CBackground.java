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

package org.socialcars.sinziana.pfara.environment;

import org.socialcars.sinziana.pfara.data.input.CBackgroundpojo;

public class CBackground
{
    private final Double m_maxspeed;
    private final Double m_density;
    private final Double m_flow;

    /**
     * ctor simple
     * @param p_density the density of the edge
     * @param p_flow the flow of the edge
     */
    public CBackground( final Double p_density, final Double p_flow )
    {
        m_density = p_density;
        m_flow = p_flow;
        if ( p_flow.equals( 0.0 ) ) m_maxspeed = 100000.0;
        else m_maxspeed = m_flow / m_density;
    }

    /**
     * ctor from pojo
     * @param p_pojo the input pojo
     */
    public CBackground( final CBackgroundpojo p_pojo )
    {
        m_flow = p_pojo.getFlow();
        m_density = p_pojo.getDensity();
        if ( m_flow.equals( 0.0 ) ) m_maxspeed = 100000.0;
        else m_maxspeed = m_flow / m_density;
    }

    /**
     * the maximum speed on the edge
     * flow divided by density
     * @return maximum speed
     */
    public Double maxspeed()
    {
        return m_maxspeed;
    }

    /**
     * traffic density on the edge
     * number of vehicles per distance unit
     * @return the density
     */
    public Double density()
    {
        return m_density;
    }

    /**
     * traffic flow on the edge
     * number of vehicles per time unit
     * @return the flow
     */
    public Double flow()
    {
        return m_flow;
    }

    /**
     * changes to the to String function
     * @return the new string message
     */
    @Override
    public String toString()
    {
        final String l_string = "Background: [flow= " + m_flow + ", density= " + m_density + ", maxSpeed= " + m_maxspeed + "]";
        return l_string;
    }
}
