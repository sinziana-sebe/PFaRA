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

package org.socialcars.sinziana.pfara.experiments;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.socialcars.sinziana.pfara.data.input.CDemandspojo;
import org.socialcars.sinziana.pfara.data.input.CInputpojo;
import org.socialcars.sinziana.pfara.environment.CBackground;
import org.socialcars.sinziana.pfara.environment.CDemand;
import org.socialcars.sinziana.pfara.environment.CGraph;
import org.socialcars.sinziana.pfara.environment.IEdge;
import org.socialcars.sinziana.pfara.environment.IGraph;
import org.socialcars.sinziana.pfara.units.CUnits;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

public class CScenarioGeneration
{
    private final CDemandspojo m_inputd;
    private final CInputpojo m_input;

    private ArrayList<CDemand> m_demand;
    private CGraph m_env;

    private final CUnits m_unit;

    CScenarioGeneration( final String p_demand, final String p_graph, final Double p_space, final Integer p_time ) throws IOException
    {
        m_input = new ObjectMapper().readValue( new File( p_graph ), CInputpojo.class );
        m_inputd = new ObjectMapper().readValue( new File( p_demand ), CDemandspojo.class );
        m_unit = new CUnits( p_time, p_space );
        m_env = new CGraph( m_input.getGraph() );
        m_demand = new ArrayList<>();
        m_inputd.getDemand().forEach( j ->
        {
            final CDemand l_new = new CDemand( j );
            m_demand.add( l_new );
        } );

    }

    public void generateDensityFlow( final Integer p_ratio, final String p_endfile ) throws IOException
    {
        //this generates deltaN
        final IGraph l_env = new CGraph( m_input.getGraph() );
        final HashMap<IEdge, Integer> l_countingmap = new HashMap<>();

        m_demand.forEach( i -> IntStream.range( 0, Math.round( i.howMany() ) ).boxed()
                .flatMap( j -> l_env.route( l_env.randomnodebyzone( i.from() ), l_env.randomnodebyzone( i.to() ) ).stream() )
                .forEach( j -> l_countingmap.put( j, l_countingmap.getOrDefault( j, 0 ) + 1 ) ) );

        final HashMap<IEdge, CBackground> l_background = new HashMap<>();
        l_countingmap.keySet().forEach( e ->
        {
            //then we generate Q by dividing into hours
            final Double l_flow = Double.valueOf( l_countingmap.get( e ) / p_ratio );
            //then we generate D by dividing by length
            final Double l_density = l_flow / m_unit.distanceToBlocks( e.length() ).doubleValue();
            l_background.put( e, new CBackground( l_density, l_flow ) );
        } );

        writeInfo( l_background, p_endfile );

    }

    private void writeInfo( final HashMap<IEdge, CBackground> p_values, final String p_endfile ) throws IOException
    {
        final File l_filedir = new File( p_endfile );

        final Writer l_out = new BufferedWriter( new OutputStreamWriter(
                new FileOutputStream( l_filedir ), StandardCharsets.UTF_8 ) );

        final HashMap<String, Object> l_result = new HashMap<>();
        final ObjectMapper l_objmapper = new ObjectMapper();
        p_values.keySet().forEach( s ->
        {
            try
            {
                l_result.put( s.name(), l_objmapper.writeValueAsString( p_values.get( s ) ) );
            }
            catch ( final JsonProcessingException l_err )
            {
                l_err.printStackTrace();
            }
        } );
        final JSONObject l_json =  new JSONObject( l_result );
        l_out.write( l_json.toJSONString() );
        l_out.flush();
        l_out.close();
    }
}
