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
 * the utility interface
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
     * calculates the utility for a given route
     * @param p_route the route
     * @param p_speed the speed
     * @param p_unit the transformation unit
     * @param p_buyout the buyout
     * @param p_pref the preference
     * @return the utility
     */
    Double calculate( final List<IEdge> p_route, final Double p_speed, final CUnits p_unit, final Double p_buyout, final CPreference p_pref );

    /**
     * calculates the utility for a given route
     * @param p_routelength the route length
     * @param p_speed the speed
     * @param p_unit the transformation unit
     * @param p_buyout the buyout
     * @param p_pref the preference
     * @return the utility
     */
    Double calculate( final Double p_routelength, final Double p_speed, final CUnits p_unit, final Double p_buyout, final CPreference p_pref );

    /**
     * calculates the reservation value
     * @param p_route the route
     * @param p_speed the speed
     * @param p_unit the transformation unit
     * @param p_oldutility the old utility value
     * @return the reservation value
     */
    Double calculateRV( final List<IEdge> p_route, final Double p_speed, final CUnits p_unit, final Double p_oldutility );
}
