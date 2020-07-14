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

package org.socialcars.sinziana.pfara.negotiation.events;

import org.socialcars.sinziana.pfara.agents.IAgent;
import org.socialcars.sinziana.pfara.negotiation.IOffer;

public class CNegotiationEvent implements INegotiationEvent
{
    private final IAgent m_who;
    private final ENegotiationEventType m_type;
    private final IOffer m_which;

    public CNegotiationEvent( final IAgent p_who, final ENegotiationEventType p_type, final IOffer p_which )
    {
        m_who = p_who;
        m_type = p_type;
        m_which = p_which;
    }

    /**
     * which agent had this event
     * @return the agent
     */
    @Override
    public IAgent who()
    {
        return m_who;
    }

    /**
     * the type of event
     * @return event type
     */
    @Override
    public ENegotiationEventType what()
    {
        return m_type;
    }

    /**
     * the offer concerning the event
     * @return the offer
     */
    @Override
    public IOffer which()
    {
        return m_which;
    }

    /**
     * overides the to string method
     * @return the string of a negotiation event
     */
    @Override
    public String toString()
    {
        String l_res = m_who.name() + "  " + m_type;
        if ( m_which != null ) l_res += " with buyout " + m_which.buyout();
        return l_res;
    }
}
