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

package org.socialcars.sinziana.pfara.agents.proprieties;

import com.google.common.util.concurrent.AtomicDouble;
import org.socialcars.sinziana.pfara.agents.CPreference;
import org.socialcars.sinziana.pfara.data.input.CUtilitypojo;
import org.socialcars.sinziana.pfara.environment.IEdge;
import org.socialcars.sinziana.pfara.units.CUnits;

import java.util.List;

public class CUtility implements IUtility
{
    private final Double m_alpha;
    private final Double m_beta;

    private Double m_altcost;

    /**
     * ctor
     * @param p_pojo the pojo object
     */
    public CUtility( final CUtilitypojo p_pojo )
    {
        m_alpha = p_pojo.getAlpha();
        m_beta = p_pojo.getBeta();
    }

    /**
     * the first coefficient
     * @return alpha
     */
    @Override
    public Double alpha()
    {
        return m_alpha;
    }

    /**
     * the second coefficient
     * @return beta
     */
    @Override
    public Double beta()
    {
        return m_beta;
    }

    /**
     * calculates the utility for a given route
     * @param p_route the route
     * @param p_speed the speed
     * @param p_unit the transformation unit
     * @param p_buyout the buyout
     * @param p_pref the preference
     * @return the utility
     */
    @Override
    public Double calculate( final List<IEdge> p_route, final Double p_speed, final CUnits p_unit, final Double p_buyout, final CPreference p_pref )
    {
        final AtomicDouble l_time = new AtomicDouble( 0.0 );
        final AtomicDouble l_length = new AtomicDouble( 0.0 );
        p_route.forEach( e ->
        {
            //time
            l_time.getAndAdd( p_unit.distanceToBlocks( e.length().doubleValue() ).doubleValue() / p_unit.speedToBlocks( p_speed ).doubleValue( ) );
            //length
            l_length.getAndAdd( p_unit.distanceToBlocks( e.length().doubleValue() ).doubleValue() );
        } );
        //m_altcost = l_cost.get();
        //l_cost.getAndAdd( -p_buyout );
        if ( ( l_length.get() > p_pref.lengthLimit() ) || ( l_time.get() > p_pref.timeLimit() ) )
            return null;
        else return m_alpha * l_length.get() + m_beta * l_time.get();
    }

    @Override
    public Double calculate( final Double p_routelength, final Double p_speed, final CUnits p_unit, final Double p_buyout, final CPreference p_pref )
    {
        //m_altcost = l_cost.get();
        //l_cost.getAndAdd( -p_buyout );
        if ( ( p_routelength > p_pref.lengthLimit() ) || ( p_routelength / p_speed > p_pref.timeLimit() ) )
            return null;
        else return m_alpha * p_routelength + m_beta * p_routelength / p_speed;
    }

    /**
     * calculates the reservation value
     * @param p_route the route
     * @param p_speed the speed
     * @param p_unit the transformation unit
     * @param p_oldutility the old utility value
     * @return the reservation value
     */
    @Override
    public Double calculateRV( final List<IEdge> p_route, final Double p_speed, final CUnits p_unit, final Double p_oldutility )
    {
        final AtomicDouble l_length = new AtomicDouble( 0.0 );
        p_route.forEach( e -> l_length.getAndAdd( p_unit.distanceToBlocks( e.length().doubleValue() ).doubleValue() ) );
        Double l_rv =  p_oldutility - m_alpha * l_length.get();
        l_rv = l_rv / m_beta;
        return Math.abs( l_rv );
    }


}
