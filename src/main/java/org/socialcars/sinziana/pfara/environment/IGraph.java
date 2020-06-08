package org.socialcars.sinziana.pfara.environment;

import java.util.List;
import java.util.stream.Stream;

public interface IGraph
{
    /**
     * gets a node by its name
     * @param p_name the name of the node requested
     * @return the INode
     */
    INode nodeByName( final String p_name );

    /**
     * gets an edge by its name
     * @param p_name the name of the edge requested
     * @return the IEdge
     */
    IEdge edgeByName( final String p_name );

    /**
     * creates a route represented by a list of edges
     * @param p_from the origin node
     * @param p_to the final destination node
     * @return list of edges
     */
    List<IEdge> route( final INode p_from, final INode p_to );

    /**
     * creates a route represented by a list of edges
     * @param p_from the origin node
     * @param p_to the final destination node
     * @param p_via stream of intermediary nodes
     * @return list of edges
     */
    List<IEdge> route( final INode p_from, final INode p_to, Stream<INode> p_via );

    /**
     * creates a route represented by a list of edges
     * @param p_from the name of the origin node
     * @param p_to the name of the final destination node
     * @return list of edges
     */
    List<IEdge> route( final String p_from, final String p_to );

    /**
     * creates a route represented by a list of edges
     * @param p_from the name of the origin node
     * @param p_to the name of the final destination node
     * @param p_via stream of the names of intermediary nodes
     * @return list of edges
     */
    List<IEdge> route( final String p_from, final String p_to, Stream<String> p_via );
}
