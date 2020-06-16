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

import org.socialcars.sinziana.pfara.environment.IEdge;
import org.socialcars.sinziana.pfara.environment.INode;

/**
 * interface for dynamic agents like vehicles, bikes, pedestrians etc
 */
public interface IDynamic extends IAgent
{
    /**
     * the current location of the agent
     * @return the name of the node or edge
     */
    String location();

    /**
     * the name of the origin node
     * @return the origin node name
     */
    String origin();

    /**
     * the name of the destination node
     * @return the destination node name
     */
    String destination();

    /**
     * the position of an agent on the current edge
     * @return the distance already travelled on the current edge
     */
    Number position();

    /**
     * moves the agent in a microscopic fashion
     * based on the agents acceleration, current speed
     */
    void moveMikro();

    /**
     * performs the braking action
     * based on the aggents deceleration and current speed
     */
    void brake();

    /**
     * moves the agent in a macroscopic fashion, based on the median speed of the current edge
     * @param p_speed the median speed
     */
    void moveMakro( final Double p_speed );

    /**
     * the necessary actions for departing a node
     * @param p_position the current edge
     * @param p_timestep the current timestep
     */
    void departed( final IEdge p_position, final Integer p_timestep );

    /**
     * the necessary actions uppon arrival
     * @param p_position the current edge
     * @param p_timestep the current timestep
     */
    void arrived( final IEdge p_position, final Integer p_timestep );

    /**
     * the necessary actions uppon travel completion
     * @param p_position the current node
     * @param p_timestep the current time-step
     */
    void completed( final INode p_position, final Integer p_timestep );

}
