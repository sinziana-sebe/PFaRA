/**
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

package org.socialcars.sinziana.pfara.environment;

import org.socialcars.sinziana.pfara.data.input.CEdgepojo;

import java.text.MessageFormat;

public class CEdge implements IEdge
{
    private final String m_name;
    private final INode m_from;
    private final INode m_to;
    private final Double m_weight;
    private CStoplight m_stoplight;
    private CSections m_sections;
    private CTraffic m_traffic;

    /**
     * ctor
     * @param p_pojo the edge plain old jaca object
     * @param p_from the origin node
     * @param p_to the end node
     */
    public CEdge( final CEdgepojo p_pojo, final INode p_from, final INode p_to )
    {
        m_name = p_pojo.getName();
        m_from = p_from;
        m_to = p_to;
        m_weight = p_pojo.getWeight();
    }

    /**
     * name of the edge
     * @return the edge's name
     */
    @Override
    public String name()
    {
        return m_name;
    }

    /**
     * origin node
     * @return the origin node
     */
    @Override
    public INode from()
    {
        return m_from;
    }

    /**
     * end node
     * @return the end node
     */
    @Override
    public INode to()
    {
        return m_to;
    }

    /**
     * weigh of the edge
     * @return the edge's weight
     */
    @Override
    public Double weight()
    {
        return m_weight;
    }

    /**
     * length of the edge
     * @return the edge's length
     */
    @Override
    public Double length()
    {
        return Math.sqrt( Math.pow( m_from.coordinates().latitude() - m_to.coordinates().latitude(), 2 )
                + Math.pow( m_from.coordinates().longitude() - m_to.coordinates().longitude(), 2 ) );
    }

    /**
     * adds a stoplight to this edge
     * @param p_stoplight the stoplight object
     */
    @Override
    public void addStoplight( final CStoplight p_stoplight )
    {
        m_stoplight = p_stoplight;
    }

    /**
     * the stoplight
     * @return the stoplight partaining to the edge
     */
    @Override
    public CStoplight stoplight()
    {
        return m_stoplight;
    }

    /**
     * adds sections to the edge
     * @param p_sections the section lengths
     */
    @Override
    public void addSections( final CSections p_sections )
    {
        m_sections = p_sections;
    }

    /**
     * gives the edge sections
     * @return the sections
     */
    @Override
    public CSections sections()
    {
        return m_sections;
    }

    /**
     * adds background traffic information
     * @param p_traffic the traffic object
     */
    @Override
    public void addBackgroundTraffic( final CTraffic p_traffic )
    {
        m_traffic = p_traffic;
    }

    /**
     * returns the maximum speed permitted by traffic
     * @return the speed
     */
    @Override
    public Double maxSpeed()
    {
        if ( m_traffic != null ) return m_traffic.maxSpeed();
        return null;
    }

    /**
     * transforms the main information into a string message
     * message composed of the name and the weight
     * @return the string message
     */
    @Override
    public String toString()
    {
        return MessageFormat.format( "{0}({1})", m_name, m_weight );
    }

}
