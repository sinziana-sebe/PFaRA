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

package org.socialcars.sinziana.pfara.agents;

import org.socialcars.sinziana.pfara.data.input.CPreferencepojo;

public class CPreference implements IPreference
{
    private final Double m_minspeed;
    private final Double m_maxspeed;
    private final Double m_maxaccel;
    private final Double m_maxdecel;
    private Integer m_timelimit;
    private Double m_lengthlimit;
    private Double m_maxcost;

    /**
     * ctor
     * @param p_pref the plain old java object for preference
     **/
    public CPreference( final CPreferencepojo p_pref )
    {
        m_minspeed = p_pref.getMinSpeed();
        m_maxspeed = p_pref.getMaxSpeed();
        m_maxaccel = p_pref.getMaxAcceleration();
        m_maxdecel = p_pref.getMaxDeceleration();
        m_timelimit = p_pref.getMaxTime();
        m_lengthlimit = p_pref.getMaxLength();
        m_maxcost = p_pref.getMaxCost();
    }

    /**
     * the vehicles minimum speed
     * @return minspeed
     */
    @Override
    public Double minSpeed()
    {
        return m_minspeed;
    }

    /**
     * the vehicles maximum speed
     * @return maxspeed
     */
    @Override
    public Double maxSpeed()
    {
        return m_maxspeed;
    }

    /**
     * the vehicles maximum acceleration
     * @return maxaccel
     */
    @Override
    public Double maxAccel()
    {
        return m_maxaccel;
    }

    /**
     * the vehicles maximum deceleration
     * @return maxdecel
     */
    @Override
    public Double maxDecel()
    {
        return m_maxdecel;
    }

    /**
     * the vehicles upper limit on travel time
     * @return time limit
     */
    @Override
    public Integer timeLimit()
    {
        return m_timelimit;
    }

    /**
     * the vehicles upper limit on travel distance
     * @return length limit
     */
    @Override
    public Double lengthLimit()
    {
        return m_lengthlimit;
    }

    /**
     * the vehicles upper limit on travel cost
     * @return maximum cost
     */
    @Override
    public Double maxCost()
    {
        return m_maxcost;
    }

    /**
     * reduces the upper limit on travel distance based on the length of the edge already travelled
     * @param p_edge the edge travelled
     */
    protected void reduceMaxLength( final Double p_edge )
    {
        m_lengthlimit = m_lengthlimit - p_edge;
    }

    /**
     * reduces the upper limit on travel cost based on the given cost
     * @param p_cost the cost of the edge travelled
     */
    protected void reduceMaxCost( final Double p_cost )
    {
        m_maxcost = m_maxcost - p_cost;
    }

    /**
     * reduces the upper limit on travel time based on the time it took to travel the last edge
     * @param p_time the travel time
     */
    protected void reduceMaxTime( final Integer p_time )
    {
        m_timelimit = m_timelimit - p_time;
    }
}
