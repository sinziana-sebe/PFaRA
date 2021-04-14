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

package org.socialcars.sinziana.pfara.visualisation;

import com.google.common.base.Function;
import org.socialcars.sinziana.pfara.environment.IEdge;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * heat function class
 * maps the values collected for the edges to an index in the colour map
 */
public class CHeatFunction implements Function<IEdge, Paint>
{
    private Map<IEdge, Color> m_coding;

    /**
     * ctor
     * @param p_countingmap the map
     */
    public CHeatFunction( final Map<IEdge, Integer> p_countingmap )
    {
        final Integer l_max = p_countingmap.entrySet().stream().max( Map.Entry.comparingByValue() ).get().getValue();
        //colour palette can be changed here
        m_coding = p_countingmap.entrySet().stream().collect( Collectors.toMap( Map.Entry::getKey, i -> EColourMap.INFERNO.apply( i.getValue(), l_max ) ) );
    }

    /**
     * apply function
     * @param p_edge the edge requested
     * @return the Paint colour applicable
     */
    @Nullable
    @Override
    public Paint apply( @Nullable final IEdge p_edge )
    {
        return m_coding.get( p_edge );
    }
}
