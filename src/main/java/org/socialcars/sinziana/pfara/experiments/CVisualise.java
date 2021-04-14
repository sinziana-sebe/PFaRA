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

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import org.socialcars.sinziana.pfara.data.input.CInputpojo;
import org.socialcars.sinziana.pfara.environment.CGraph;
import org.socialcars.sinziana.pfara.environment.IEdge;
import org.socialcars.sinziana.pfara.environment.INode;
import org.socialcars.sinziana.pfara.functionality.CEdgeEnd;
import org.socialcars.sinziana.pfara.functionality.CPreGrouping;
import org.socialcars.sinziana.pfara.units.CUnits;
import org.socialcars.sinziana.pfara.visualisation.CHeatFunction;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CVisualise
{
    private static final Logger LOGGER = Logger.getLogger( CVisualise.class.getName() );

    private final CInputpojo m_input;
    private final CGraph m_env;

    private final Map<IEdge, Integer> m_countingmap = new HashMap<>();

    private final CUnits m_unit;
    private Integer m_time;

    private CPreGrouping m_grouping;
    private CEdgeEnd m_edgeend;

    private CReporting m_report;

    public CVisualise( final String p_infile, final String p_backfile, final String p_outfile,
                       final Integer p_time, final Double p_space ) throws IOException
    {
        final FileHandler l_handler = new FileHandler( p_outfile );
        LOGGER.addHandler( l_handler );
        l_handler.setFormatter( new SimpleFormatter() );

        m_input = new ObjectMapper().readValue( new File( p_infile ), CInputpojo.class );
        m_env = new CGraph( m_input.getGraph() );
        //m_readbackground = new CReadBackground( m_env );
        //m_backinfo = m_readbackground.getBackground( p_backfile );
        m_unit = new CUnits( p_time, p_space );
        m_time = 0;
        /*m_vehicles = new ArrayList<>();
        //m_input.getVehicles().forEach( p -> m_vehicles.add( new CVehicle( p, 0, LOGGER, m_unit, false, 1.0 ) ) );
        //m_vehicles.forEach( p ->
        {
            m_status.put( p, "Incomplete" );
            m_routes.put( p, m_env.route( p.origin(), p.destination() ) );
            m_finalroute.put( p, new ArrayList<>() );
        } );*/
    }

    public void visualiseNetwork()
    {
        final JFrame l_frame = new JFrame();
        l_frame.setSize( new Dimension( 2000, 2000 ) );
        l_frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );

        final VisualizationViewer<INode, IEdge> l_view = m_env.panel( l_frame.getSize() );
        l_frame.getContentPane().add( l_view );
        l_frame.setVisible( true );

        final DefaultModalGraphMouse<INode, IEdge> l_gm = new DefaultModalGraphMouse<INode, IEdge>();
        l_gm.setMode( ModalGraphMouse.Mode.TRANSFORMING );
        l_view.setGraphMouse( l_gm );
    }


    public void visualiseDensity() throws IOException
    {
        final HashMap<IEdge, Integer> l_density = new HashMap<>();
        m_env.edges().forEach(  e -> l_density.put( e, (int)Math.round( e.weight() ) ) );


        final JFrame l_frame = new JFrame();
        l_frame.setSize( new Dimension( 2000, 2000 ) );
        l_frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );

        final VisualizationViewer<INode, IEdge> l_view = m_env.panel( l_frame.getSize() );
        l_frame.getContentPane().add( l_view );
        l_frame.setVisible( true );

        final DefaultModalGraphMouse<INode, IEdge> l_gm = new DefaultModalGraphMouse<INode, IEdge>();
        l_gm.setMode( ModalGraphMouse.Mode.TRANSFORMING );
        l_view.setGraphMouse( l_gm );

        l_view.getRenderContext().setEdgeFillPaintTransformer( new CHeatFunction( l_density ) );
        l_view.getRenderContext().setVertexFillPaintTransformer( i -> new Color( 0, 0, 0 ) );

    }

    public void visualiseRoute( final ArrayList<IEdge> p_route )
    {
        final JFrame l_frame = new JFrame();
        l_frame.setSize( new Dimension( 2000, 2000 ) );
        l_frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );

        final VisualizationViewer<INode, IEdge> l_view = m_env.panel( l_frame.getSize() );
        l_frame.getContentPane().add( l_view );
        l_frame.setVisible( true );

        final DefaultModalGraphMouse<INode, IEdge> l_gm = new DefaultModalGraphMouse<INode, IEdge>();
        l_gm.setMode( ModalGraphMouse.Mode.TRANSFORMING );
        l_view.setGraphMouse( l_gm );

        final Map<IEdge, Integer> l_countingmap = new HashMap<>();
        p_route.forEach( i -> l_countingmap.put( i, 1 ) );

        l_view.getRenderContext().setEdgeFillPaintTransformer( new CHeatFunction( l_countingmap ) );
        l_view.getRenderContext().setVertexFillPaintTransformer( i -> new Color( 0, 0, 0 ) );
    }

    public void paintEdges( final ArrayList<String> p_route )
    {
        final JFrame l_frame = new JFrame();
        l_frame.setSize( new Dimension( 2000, 2000 ) );
        l_frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );

        final VisualizationViewer<INode, IEdge> l_view = m_env.panel( l_frame.getSize() );
        l_frame.getContentPane().add( l_view );
        l_frame.setVisible( true );

        final DefaultModalGraphMouse<INode, IEdge> l_gm = new DefaultModalGraphMouse<INode, IEdge>();
        l_gm.setMode( ModalGraphMouse.Mode.TRANSFORMING );
        l_view.setGraphMouse( l_gm );

        final Map<IEdge, Integer> l_countingmap = new HashMap<>();
        p_route.forEach( i -> l_countingmap.put( m_env.edgeByName( i ), 1 ) );

        l_view.getRenderContext().setEdgeFillPaintTransformer( new CHeatFunction( l_countingmap ) );
        l_view.getRenderContext().setVertexFillPaintTransformer( i -> new Color( 0, 0, 0 ) );
    }
}
