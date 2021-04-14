/*
 *  This file is part of the mesoscopic traffic simulation PFaRA of Clauthal University of
 *  Technology-Mobile and Enterprise Computing aswell as SocialCars Research Training Group.
 *  Copyright (c) 2017-2021 Sinziana-Maria Sebe (sms14@tu-clausthal.de)
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the
 *  GNUGeneral Public License as  published by the Free Software Foundation, either version 3 of
 *  the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *  PURPOSE.  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this program. If
 *  not, see http://www.gnu.org/licenses/
 *
 */

package org.socialcars.sinziana.pfara.environment.negotiables;

import org.socialcars.sinziana.pfara.environment.IEdge;

import java.util.List;

/**
 * the negotiable element class
 * finds formations in the graph that are "negotiable"
 * the common route without compensation would not have been taken
 * the common route with compensation provided would potentially be taken
 * ( (  common(no compensation) + alone > original ) && ( common(with compensation) + alone < original ) )
 */
public class CNegotiableElement implements INegotiableElement
{
    private final List<IEdge> m_original;
    private final List<IEdge> m_common;
    private final List<IEdge> m_alone;

    /**
     * ctor
     * @param p_original the original route
     * @param p_common the new common route
     * @param p_alone the new route that will be traveled alone
     */
    public CNegotiableElement( final List<IEdge> p_original, final List<IEdge> p_common, final List<IEdge> p_alone )
    {
        m_original = p_original;
        m_common = p_common;
        m_alone = p_alone;
    }

    /**
     * the original route
     * @return route
     */
    @Override
    public List<IEdge> original()
    {
        return m_original;
    }

    /**
     * the new common route
     * @return route
     */
    @Override
    public List<IEdge> common()
    {
        return m_common;
    }

    /**
     * the new alone route
     * @return route
     */
    @Override
    public List<IEdge> alone()
    {
        return m_alone;
    }
}
