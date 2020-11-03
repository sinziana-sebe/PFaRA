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
import org.socialcars.sinziana.pfara.negotiation.CSimpleOffer;
import org.socialcars.sinziana.pfara.negotiation.IOffer;
import org.socialcars.sinziana.pfara.negotiation.IProtocol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface IVehicle extends IDynamic
{

    /**
     * the necessary actions for the formation of a platoon
     * @param p_position the current node name
     * @param p_timestep the current timestep
     * @param p_platoon the co-platooners
     */
    void formed( final String p_position, final Integer p_timestep, final ArrayList<CVehicle> p_platoon );


    /**
     * the necessary actions for the splitting from a platoon
     * @param p_position the current node
     * @param p_timestep the current timestep
     */
    void split( final String p_position, final Integer p_timestep );

    /**
     * wether the vehicle is currently platooning or not
     * @return true if it is and false if not
     */
    Boolean platooning();

    /**
     * companions
     * @return the co-platooning vehicles
     */
    ArrayList<CVehicle> companions();

    /**
     * sets the delay based on the number of co-platooners
     */
    void setDelay( final Integer p_maxdelay );

    /**
     * decreases the precedence
     */
    void updatePrecedence();

    /**
     * delay
     * @return the delay
     */
    Integer getDelay();

    /**
     * decreases the delay
     */
    void updateDelay();

    /**
     * joins a negotiating party
     * @param p_protocol the protocol for communication
     */
    void joinParty( final IProtocol p_protocol );

    /**
     * releases the vehicle from the negotiation party
     * @param p_protocol the protocol for communication
     */
    void release( final IProtocol p_protocol );

    /**
     * actions needed when receiving an offer
     * @param p_offer the offer
     * @throws IOException file write
     */
    void receiveOffer( final IOffer p_offer ) throws IOException;

    /**
     * actions needed when sending an offer
     * @param p_route the route in the offer
     */
    void sendOffer( final List<IEdge> p_route );

    /**
     * actions needed for haggling
     * @param p_offer the offer
     * @throws IOException file write
     */
    void haggle( final CSimpleOffer p_offer ) throws IOException;


}
