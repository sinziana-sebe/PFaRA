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

import org.socialcars.sinziana.pfara.data.input.CNodepojo;

/**
 * the node class
 */
public class CNode implements INode
{
    private final String m_name;
    private final CCoordinates m_coordinates;

    /**
     * ctor
     * @param p_pojo pojo object read from file
     */
    public CNode( final CNodepojo p_pojo )
    {
        m_name = p_pojo.getName();
        m_coordinates = new CCoordinates( p_pojo.getCoordinates() );
    }

    /**
     * name
     * @return the name of the node
     */
    @Override
    public String name()
    {
        return m_name;
    }

    /**
     * coordinates
     * @return the coordinates for the node
     */
    @Override
    public CCoordinates coordinates()
    {
        return m_coordinates;
    }
}
