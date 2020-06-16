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

package org.socialcars.sinziana.pfara.units;

public interface IUnits
{

    /**
     * transforms real life distance to interal distance in blocks
     * @param p_distance real life distance
     * @return the number of blocks
     */
    Number distanceToBlocks( final Number p_distance );

    /**
     * transforms the given number of blocks into real world distance
     * @param p_blocks the number of blocks given
     * @return the real world distance
     */
    Number blockToDistance( Number p_blocks );

    /**
     * calculates the distance based on given speed
     * @param p_speed the speed of travel
     * @return the real world distance travelled in one time step
     */
    Number speedToDistance( Number p_speed );

    /**
     * calculates the new speed based on acceleration
     * @param p_acceleration the given acceleration
     * @return the new speed
     */
    Number accelerationToSpeed( Number p_acceleration );

    /**
     * calculates the speed needed to travel given distance in one time step
     * @param p_distance the given distance
     * @return the speed
     */
    Number distanceToSpeed( Number p_distance );

    /**
     * calculates the number of blocks travelled with the given speed in one time step
     * @param p_speed the given speed
     * @return the number of blocks
     */
    Number speedToBlocks( Number p_speed );

    /**
     * calculates the time in seconds for the given number of steps
     * @param p_steps the given nb of steps
     * @return time in seconds
     */
    Number stepsToTimesteps( Number p_steps );

    /**
     * calculates the new position based on the speed and the old position
     * @param p_oldposition the old position
     * @param p_speed the given speed
     * @return the new position
     */
    Number newPosition( Number p_oldposition, Number p_speed );

}
