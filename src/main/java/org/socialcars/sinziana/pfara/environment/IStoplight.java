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

import java.util.logging.Logger;

/**
 * the stoplight interface
 */
public interface IStoplight
{
    /**
     * sets the logger
     * @param p_log the logger abject
     */
    void setLogger( final Logger p_log );

    /**
     * when the light cycle is started
     * @return start time of the cycle
     */
    Integer startTime();

    /**
     * starts the light cycle
     */
    void start();

    /**
     * state
     * @return the current state
     */
    ELightState state();

    /**
     * time left
     * @return how many timesteps until light changes
     */
    Integer timeLeft();

    /**
     * the edge where the light sits
     * @return the edge name
     */
    String edgeName();

    /**
     * updates the light
     */
    void update();
}
