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

import org.socialcars.sinziana.pfara.data.input.CCoordinatespojo;

/**
 * the coordinate class used for both synthetic and real coordinate systems
 */
public class CCoordinates implements ICoordinates
{
    private final Double m_latitude;
    private final Double m_longitude;

    /**
     * constructor using the jsonschema defined
     * used to create a coordinate from file
     * @param p_pojo the plain old java object
     */
    public CCoordinates( final CCoordinatespojo p_pojo )
    {
        m_latitude = p_pojo.getLat();
        m_longitude = p_pojo.getLon();
    }

    /**
     * returns the latitude, or X-axis coordinate
     * @return latidude/ X-axis
     */
    public Double latitude()
    {
        return m_latitude;
    }

    /**
     * returns the longitude, or Y-axis coordinate
     * @return longitude / Y-axis
     */
    public Double longitude()
    {
        return m_longitude;
    }
}
