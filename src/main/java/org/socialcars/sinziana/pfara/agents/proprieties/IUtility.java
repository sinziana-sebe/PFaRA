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

import org.socialcars.sinziana.pfara.agents.CPreference;
import org.socialcars.sinziana.pfara.environment.IEdge;
import org.socialcars.sinziana.pfara.units.CUnits;

import java.util.List;

/**
 * the agent utility interface
 */
public interface IUtility
{
    /**
     * the first coefficient
     * @return rho
     */
    Double rho();

    /**
     * the second coefficient
     * @return sigma
     */
    Double sigma();

    /**
     * calculates the utility for a given route based on cost
     * @param p_route the route
     * @param p_speed the speed
     * @param p_unit the transformation unit
     * @param p_buyout the buyout
     * @param p_pref the preference
     * @return the utility
     */
    Double calculateMakro( final List<IEdge> p_route, final Double p_speed, final CUnits p_unit, final Double p_buyout, final CPreference p_pref );

    /**
     * calculates the utility for a given route based on time
     * @param p_route the route
     * @param p_speed the speed
     * @param p_unit the transformation unit
     * @param p_buyout the buyout
     * @param p_pref the preference
     * @return the utility
     */
    Double calculateMikro( final List<IEdge> p_route, final Double p_speed, final CUnits p_unit, final Double p_buyout, final CPreference p_pref );

    /**
     * calculates the agent's reservation value
     * @param p_route the route
     * @param p_speed the speed
     * @param p_unit the transformation unit
     * @param p_oldutility the old utility value
     * @return the reservation value
     */
    Double calculateRV( final List<IEdge> p_route, final Double p_speed, final CUnits p_unit, final Double p_oldutility );

    /**
     * calculates the utility based on cost at the end of the route
     * @param p_distance the distance of the travel
     * @param p_cost the cost of the travel
     * @return the utility
     */
    Double calculateMakroFinal( final Double p_distance, final Double p_cost );

    /**
     * calculates the utility based on time at the end of the route
     * @param p_duration the duration of the travel
     * @param p_distance the distance of the travel
     * @return the utility
     */
    Double calculateMikroFinal( final Integer p_duration, final Double p_distance );

}
