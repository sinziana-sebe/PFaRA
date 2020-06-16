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

package org.socialcars.sinziana.pfara.agents.events;

import org.socialcars.sinziana.pfara.agents.IDynamic;

import java.util.Collection;

/**
 * the event interface
 */
public interface IEvent
{
    /**
     * the subject of the event
     * @return a dynamic agent
     */
    IDynamic who();

    /**
     * the type of event
     * @return type
     */
    EEventType what();

    /**
     * location of the event
     * @return the name of the location
     */
    String where();

    /**
     * in case of platooning, specifies the partners
     * @return a collection of other dynamic agents
     */
    Collection<IDynamic> with();

    /**
     * the time that the event occured
     * @return timestep
     */
    Integer when();
}
