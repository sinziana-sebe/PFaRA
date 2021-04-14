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

package org.socialcars.sinziana.pfara.environment;

/**
 * the edge sections class
 */
public class CSections implements ISections
{
    private final Double m_begining;
    private final Double m_middle;
    private final Double m_end;

    /**
     * ctor
     * @param p_begining begining
     * @param p_midddle middle
     * @param p_end end
     */
    public CSections( final Double p_begining, final Double p_midddle, final Double p_end )
    {
        m_begining = p_begining;
        m_middle = p_midddle;
        m_end = p_end;
    }

    /**
     * the beginning portion of the edge
     * @return the length
     */
    @Override
    public Double beginning()
    {
        return m_begining;
    }

    /**
     * the middle portion of the edge
     * @return the length
     */
    @Override
    public Double middle()
    {
        return m_middle;
    }

    /**
     * the end portion of the edge
     * @return the length
     */
    @Override
    public Double end()
    {
        return m_end;
    }
}
