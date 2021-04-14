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

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * graph interface
 * @param <V> used for visualisation
 */
public interface IGraph<V extends JPanel>
{

    /**
     * the size of the network
     * @return the number of nodes
     */
    Integer size();

    /**
     * all the nodes
     * @return nodes
     */
    Collection<INode> nodes();

    /**
     * all the edges
     * @return edges
     */
    Collection<IEdge> edges();

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

    /**
     * gets a random node by zone
     * @param p_zone the zone
     * @return the node
     */
    INode randomnodebyzone( final String p_zone );

    /**
     * returns a panel with graph visualization
     * @param p_dimension dimension
     * @return panel
     */
    V panel( final Dimension p_dimension );

}
