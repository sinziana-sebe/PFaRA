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

import org.socialcars.sinziana.pfara.data.input.CDemandpojo;

public class CDemand implements IDemand
{
    private final String m_from;
    private final String m_to;
    private final Float m_howmany;

    /**
     * ctor
     * @param p_pojo pojo object
     */
    public CDemand( final CDemandpojo p_pojo )
    {
        m_from = p_pojo.getFrom();
        m_to = p_pojo.getTo();
        m_howmany = (float) p_pojo.getNb();
    }

    @Override
    public String from()
    {
        return m_from;
    }

    @Override
    public String to()
    {
        return m_to;
    }

    @Override
    public Float howMany()
    {
        return m_howmany;
    }
}
