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

import java.io.IOException;
import java.util.List;

public interface INegotiationModule
{
    /**
     * handles offers received
     * @param p_offer the offer
     * @param p_oldroute the ego-agent's old route
     * @param p_speed the speed of travel
     * @return string with the response to the offer (accept,reject,haggle)
     * @throws IOException file
     */
    String receiveOffer( final IOffer p_offer, final List<IEdge> p_oldroute, final Double p_speed ) throws IOException;

    /**
     * handles the sending of an offer
     * @param p_route the route offered
     * @param p_name the name of the ego agent
     * @return the offer
     */
    CInitialOffer sendOffer( final List<IEdge> p_route, final String p_name );

    /**
     * handles the haggling process
     * @param p_offer the offer in discussion
     * @return string with response to the offer (accept, reject, haggle)
     * @throws IOException file
     */
    String haggle( final CSimpleOffer p_offer ) throws IOException;

    /**
     * gives the cost of the alternative (the one offered) route
     * @return cost
     */
    Double alternativeRouteCost();

    /**
     * the role of the ego agent
     * @return role
     */
    EAgentType role();

}
