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

package org.socialcars.sinziana.pfara.optimisation;

import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;
import org.socialcars.sinziana.pfara.environment.CGraph;
import org.socialcars.sinziana.pfara.environment.IEdge;
import org.socialcars.sinziana.pfara.environment.INode;

import java.util.ArrayList;

/**
 * class for the simple
 * shortest path problem
 */
public class CSPP implements ISPP
{
    private final GRBEnv m_env;
    private final GRBModel m_model;
    private final GRBVar[][] m_xs;
    private Integer m_length;
    private double m_cost;
    private ArrayList<IEdge> m_results;
    private CGraph m_graph;
    private Integer m_origin;
    private Integer m_destination;

    /**
     * ctor
     * @param p_network jung graph
     * @throws GRBException gurobi
     */
    public CSPP( final CGraph p_network, final Integer p_origin, final Integer p_destination ) throws GRBException
    {
        m_env = new GRBEnv( "spp.log" );
        m_model = new GRBModel( m_env );
        m_xs = new GRBVar[p_network.size() + 1][p_network.size() + 1];
        m_length = 0;
        m_cost = 0;
        m_graph = p_network;
        m_origin = p_origin;
        m_destination = p_destination;
        m_results = new ArrayList<>();

        p_network.edges().forEach( iEdge ->
        {
            final INode l_start = iEdge.from();
            final INode l_end = iEdge.to();
            //creates the objective function
            try
            {
                if ( iEdge.weight().doubleValue() == 0.0 )
                    //some weight must be given, throws the program for a loop
                    m_xs[Integer.valueOf( l_start.name() )][Integer.valueOf( l_end.name() )] = m_model.addVar( 0.0, 1.0, iEdge.weight().doubleValue() + 0.000001,
                            GRB.BINARY,
                            "y" + l_start  + "-" + l_end );
                else m_xs[Integer.valueOf( l_start.name() )][Integer.valueOf( l_end.name() )] = m_model.addVar( 0.0, 1.0, iEdge.weight().doubleValue(),
                        GRB.BINARY,
                        "y" + l_start  + "-" + l_end );
            }
            catch ( final GRBException l_err )
            {
                l_err.printStackTrace();
            }
        } );
    }

    /**
     * solve function
     * @throws GRBException gurobi
     */
    public void solve() throws GRBException
    {
        addConstraints( m_graph, m_origin, m_destination );
        m_model.optimize();
        saveResults();
        System.out.println( "Obj: " + m_model.get( GRB.DoubleAttr.ObjVal ) );
        m_cost = m_model.get( GRB.DoubleAttr.ObjVal );
        System.out.println();
        m_model.dispose();
        m_env.dispose();
    }

    private void addConstraints( final CGraph p_network, final Integer p_origin, final Integer p_destination ) throws GRBException
    {
        //flow constraints
        for ( int i = 0; i < p_network.size(); i++ )
        {
            final GRBLinExpr l_expr = new GRBLinExpr();
            for ( int j = 0; j < p_network.size(); j++ )
            {
                if ( m_xs[i][j] != null ) l_expr.addTerm( 1.0, m_xs[i][j] );
                if ( m_xs[j][i] != null ) l_expr.addTerm( -1.0, m_xs[j][i] );
            }
            if ( i == p_origin ) m_model.addConstr( l_expr, GRB.EQUAL, 1.0, "OriginConstraint" );
            else if ( i == p_destination ) m_model.addConstr( l_expr, GRB.EQUAL, -1.0, "DestinationConstraint" );
            else m_model.addConstr( l_expr, GRB.EQUAL, 0.0, "FlowConstraint" );
        }
    }

    //the results should be available after the execution
    //
    private void saveResults()
    {
        m_graph.edges().forEach( e ->
        {
            try
            {
                if ( ( m_xs[Integer.valueOf( e.from().name() )][Integer.valueOf( e.to().name() )] != null )
                        && ( m_xs[Integer.valueOf( e.from().name() )][Integer.valueOf( e.to().name() )].get( GRB.DoubleAttr.X ) == 1 ) )
                {
                    m_results.add( e );
                    m_length++;
                }
            }
            catch ( final GRBException l_err )
            {
                l_err.printStackTrace();
            }
        } );


    }

    /**
     * displays the result
     */
    @Override
    public void display()
    {
        m_results.forEach( e -> System.out.println( e.name() ) );
    }

    /**
     * the length of the route
     * @return length
     */
    @Override
    public Integer length()
    {
        return m_length;
    }

    /**
     * cost of the route
     * @return cost
     */
    @Override
    public Double cost()
    {
        return m_cost;
    }
}
