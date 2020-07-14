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

import com.codepoetics.protonpack.StreamUtils;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Graphs;
import org.socialcars.sinziana.pfara.data.input.CGraphpojo;
import org.socialcars.sinziana.pfara.data.input.CStoplightpojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CGraph implements IGraph
{

    private final Graph<INode, IEdge> m_graph;
    private final DijkstraShortestPath<INode, IEdge> m_pathalgorithm;
    private final Map<String, INode> m_nodes;
    private final HashMap<String, IEdge> m_edges = new HashMap<>();

    /**
     * ctor
     * @param p_pojo the graph plain old java object
     */
    public CGraph( final CGraphpojo p_pojo )
    {
        final DirectedGraph<INode, IEdge> l_graph = new DirectedSparseMultigraph<>();

        m_nodes = p_pojo.getNodes().stream()
                .map( CNode::new )
                .peek( l_graph::addVertex )
                .collect( Collectors.toMap( CNode::name, i -> i ) );

        p_pojo.getEdges().forEach( e ->
        {
            final IEdge l_edge = new CEdge( e, m_nodes.get( e.getFrom() ), m_nodes.get( e.getTo() ) );
            l_graph.addEdge( l_edge, m_nodes.get( e.getFrom() ), m_nodes.get( e.getTo() ) );
            m_edges.put( e.getName(), l_edge );
        } );

        m_graph = Graphs.unmodifiableGraph( l_graph );
        m_pathalgorithm = new DijkstraShortestPath<>( m_graph, IEdge::weight );
    }


    /**
     * gets a node by its name
     * @param p_name the name of the node requested
     * @return the INode
     */
    @Override
    public INode nodeByName( final String p_name )
    {
        return m_nodes.get( p_name );
    }

    /**
     * gets an edge by its name
     * @param p_name the name of the edge requested
     * @return the IEdge
     */
    @Override
    public IEdge edgeByName( final String p_name )
    {
        return m_edges.get( p_name );
    }

    /**
     * creates a route represented by a list of edges
     * @param p_from the origin node
     * @param p_to the final destination node
     * @return list of edges
     */
    @Override
    public List<IEdge> route( final INode p_from, final INode p_to )
    {
        return m_pathalgorithm.getPath( p_from, p_to );
    }

    /**
     * creates a route represented by a list of edges
     * @param p_from the origin node
     * @param p_to the final destination node
     * @param p_via stream of intermediary nodes
     * @return list of edges
     */
    @Override
    public List<IEdge> route( final INode p_from, final INode p_to, final Stream<INode> p_via )
    {
        return StreamUtils.windowed(
                Stream.concat(
                        Stream.concat(
                                Stream.of( p_from ),
                                p_via
                        ),
                        Stream.of( p_to )
                ),
                2
        ).flatMap(
                i -> m_pathalgorithm.getPath( i.get( 0 ), i.get( 1 ) ).stream()
        ).collect( Collectors.toList() );
    }

    /**
     * creates a route represented by a list of edges
     * @param p_from the name of the origin node
     * @param p_to the name of the final destination node
     * @return list of edges
     */
    @Override
    public List<IEdge> route( final String p_from, final String p_to )
    {
        return m_pathalgorithm.getPath( m_nodes.get( p_from ), m_nodes.get( p_to ) );
    }

    /**
     * creates a route represented by a list of edges
     * @param p_from the name of the origin node
     * @param p_to the name of the final destination node
     * @param p_via stream of the names of intermediary nodes
     * @return list of edges
     */
    @Override
    public List<IEdge> route( final String p_from, final String p_to, final Stream<String> p_via )
    {
        return this.route( m_nodes.get( p_from ), m_nodes.get( p_to ), p_via.map( m_nodes::get ) );
    }

    /**
     * creates a route represented by a list of edges
     * @param p_finish the name of the end node
     * @param p_via a list of edges that must be a part of the final route
     * @return the final route
     */
    public List<IEdge> route( final String p_finish, final List<IEdge> p_via )
    {
        final List<IEdge> l_route = p_via;
        final List<IEdge> l_altroute = m_pathalgorithm.getPath( p_via.get( p_via.size() ).to(), nodeByName( p_finish ) );
        l_altroute.forEach( e -> l_route.add( e ) );
        return l_route;
    }

    /**
     * creates stoplights to be placed in the environment
     * @param p_pojo stoplights plain old java object
     */
    public void createStoplights( final List<CStoplightpojo> p_pojo )
    {
        p_pojo.forEach( s -> m_edges.get( s.getLocation() ).addStoplight( new CStoplight( s ) ) );
    }
}
