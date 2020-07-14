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

import org.socialcars.sinziana.pfara.agents.CVehicle;
import org.socialcars.sinziana.pfara.environment.INode;

import java.io.IOException;

public interface IProtocol
{
    /**
     * the type of protocol
     * @return the protocol's type
     */
    EProtocolType type();

    /**
     * function for receiving an offer and sending offers
     * @param p_veh the initiating agent sending the offer
     * @param p_offer the offer
     */
    void sendOffer( final CVehicle p_veh, final CInitialOffer p_offer );

    /**
     * function for receiving an accepted offer
     * @param p_veh the vehicle accepting the offer
     * @param p_offer the offe
     * @throws IOException file exception
     */
    void receiveAccept( final CVehicle p_veh, final IOffer p_offer ) throws IOException;

    /**
     * function for receiving a rejected offer
     * @param p_offer the offer
     * @throws IOException file exception
     */
    void receiveReject( final IOffer p_offer ) throws IOException;

    /**
     * function for receiving a breakaway offer
     * @param p_offer the offer
     * @throws IOException file exception
     */
    void receiveBreakaway( final IOffer p_offer ) throws IOException;

    /**
     * funtion for the haggling process
     * @param p_veh the vehicle sending the haggling offer
     * @param p_offer the offer
     * @throws IOException file exception
     */
    void haggle( final CVehicle p_veh, final CSimpleOffer p_offer ) throws IOException;

    /**
     * gets the round counter
     * @return round counter
     */
    Integer getRoundCounter();

    /**
     * gets the deadline
     * the maximum number of rounds
     * @return the deadline
     */
    Integer getDeadline();

    /**
     * the id of the protocol
     * given by the node name where the negotiation takes place
     * @return the node
     */
    INode getNodeID();
}
