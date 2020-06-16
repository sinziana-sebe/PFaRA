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

package org.socialcars.sinziana.pfara.agents.proprieties;

import org.socialcars.sinziana.pfara.data.input.CUtilitypojo;

public class CUtility implements IUtility
{
    private final Double m_alpha;
    private final Double m_beta;

    private Double m_altcost;

    /**
     * ctor
     * @param p_pojo the pojo object
     */
    public CUtility( final CUtilitypojo p_pojo )
    {
        m_alpha = p_pojo.getAlpha();
        m_beta = p_pojo.getBeta();
    }

    /**
     * the first coefficient
     * @return alpha
     */
    @Override
    public Double alpha()
    {
        return m_alpha;
    }

    /**
     * the second coefficient
     * @return beta
     */
    @Override
    public Double beta()
    {
        return m_beta;
    }


}
