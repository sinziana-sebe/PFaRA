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

import java.text.MessageFormat;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Simulation event class
 */
public class CEvent implements IEvent
{
    private final IDynamic m_who;
    private final EEventType m_type;
    private final String m_where;
    private final Collection<IDynamic> m_with;
    private final Integer m_when;


    /**
     * ctor
     * @param p_type type
     * @param p_where where
     * @param p_when when
     * @param p_who with whom
     */
    public CEvent( final IDynamic p_who, final EEventType p_type, final String p_where, final Integer p_when, final Collection<IDynamic> p_with )
    {
        m_who = p_who;
        m_type = p_type;
        m_where = p_where;
        m_when = p_when;
        m_with = p_with;
    }


    /**
     * the subject of the event
     * @return a dynamic agent
     */
    @Override
    public IDynamic who()
    {
        return m_who;
    }

    /**
     * the type of event
     * @return type
     */
    @Override
    public EEventType what()
    {
        return m_type;
    }

    /**
     * location of the event
     * @return the name of the location
     */
    @Override
    public String where()
    {
        return m_where;
    }

    /**
     * in case of platooning, specifies the partners
     * @return a collection of other dynamic agents
     */
    @Override
    public Collection<IDynamic> with()
    {
        return m_with;
    }

    /**
     * the time that the event occurred
     * @return timestep
     */
    @Override
    public Integer when()
    {
        return m_when;
    }

    /**
     * overrides the toString function to display the event information in a readable form
     * @return the string message
     */
    @Override
    public String toString()
    {
        String l_message = MessageFormat.format( "{0} {1} at {2} at {3} timestep", m_who.name(), m_type, m_where, m_when );
        if ( ( m_with != null ) && ( !m_with.isEmpty() )  )
        {
            final AtomicReference<String> l_with = new AtomicReference<>();
            l_message += " with ";
            m_with.forEach( e -> l_with.getAndSet( l_with.get() + " " + e.name() ) );
            l_message += l_with.get();
        }
        return l_message;
    }

}
