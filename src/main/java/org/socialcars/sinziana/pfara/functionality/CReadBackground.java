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

package org.socialcars.sinziana.pfara.functionality;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.socialcars.sinziana.pfara.environment.CBackground;
import org.socialcars.sinziana.pfara.environment.CGraph;
import org.socialcars.sinziana.pfara.environment.IEdge;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CReadBackground
{
    private final CGraph m_env;

    public CReadBackground( final CGraph p_env )
    {
        m_env = p_env;
    }

    /**
     * returns hashmap structure containing the background info of a graph
     * @return Hashmap of edges and background
     */
    public HashMap<IEdge, CBackground> getBackground( final String p_file )
    {
        final HashMap<IEdge, CBackground> l_storing = new HashMap<>();
        final JSONParser l_parser = new JSONParser();
        try
        {
            final Object l_obj = l_parser.parse( new FileReader( p_file ) );
            final JSONObject l_jsonobj = (JSONObject) l_obj;

            for ( final Object l_edge : l_jsonobj.keySet() )
            {
                final String l_values = (String) l_jsonobj.get( l_edge );
                final Pattern l_pattern = Pattern.compile( "\\:([^,}]*)" );
                final Matcher l_matcher = l_pattern.matcher( l_values );
                final ArrayList<String> l_word = new ArrayList<>();
                while ( l_matcher.find() )
                {
                    l_word.add( l_matcher.group().replace( ":", "" ) );
                }
                final CBackground l_cbg = new CBackground( Double.parseDouble( l_word.get( 1 ) ), Double.parseDouble( l_word.get( 2 ) ) );
                final IEdge l_iedge = m_env.edgeByName( l_edge.toString() );
                l_storing.put( l_iedge, l_cbg );
            }
        }
        catch ( final Exception l_err )
        {
            l_err.printStackTrace();
        }
        l_storing.remove( null );

        m_env.edges().forEach( e ->
        {
            if ( l_storing.get( e ) == null )
            {
                final CBackground l_cbg = new CBackground( 0.0, 1.0 );
                l_storing.put( e, l_cbg );
            }
        } );

        return l_storing;
    }
}
