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

import com.google.common.util.concurrent.AtomicDouble;
import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;
import org.socialcars.sinziana.pfara.agents.CVehicle;
import org.socialcars.sinziana.pfara.environment.CGraph;
import org.socialcars.sinziana.pfara.environment.IEdge;
import org.socialcars.sinziana.pfara.units.CUnits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.IntStream;

/**
 * the platooning shortest path class
 */
public class CPSPP implements IPSPP
{
    private final GRBEnv m_env;
    private final GRBModel m_model;
    private final GRBVar[][] m_ys;
    private final HashMap<String, GRBVar[][]> m_xs;
    private final Integer m_source;
    private final CGraph m_graph;
    private Double m_optimum;
    private ArrayList<CVehicle> m_pods;
    private Double m_speed;
    private CUnits m_unit;
    private final Double m_omega;

    private HashMap<CVehicle, Integer> m_endtimes = new HashMap<>();
    private HashMap<CVehicle, Double> m_lengths = new HashMap<>();

    private final HashMap<CVehicle, ArrayList<IEdge>> m_results;
    private final HashMap<IEdge, Integer> m_np;
    private ArrayList<CVehicle> m_flagged;


    /**
     * ctor
     * @param p_env jung environemnt
     * @param p_source source node
     * @param p_pods pods
     * @throws GRBException gurobi exception
     */
    public CPSPP( final CGraph p_env, final Integer p_source, final ArrayList<CVehicle> p_pods, final CUnits p_unit, final Double p_omega ) throws GRBException
    {
        m_env = new GRBEnv( "pspp.log" );
        m_model = new GRBModel( m_env );
        m_ys = new GRBVar[ p_env.size() + 1][p_env.size() + 1];
        m_xs = new HashMap<>();
        p_pods.forEach( p -> m_xs.put( p.name(), new GRBVar[p_env.size() + 1][p_env.size() + 1] ) );
        m_source = p_source;
        m_pods = p_pods;
        m_graph = p_env;
        m_np = new HashMap<>();
        m_results = new HashMap<>();
        p_pods.forEach( p -> m_results.put( p, new ArrayList<>() ) );
        m_unit = p_unit;
        m_omega = p_omega;

        //defines the objective function
        final GRBLinExpr l_obj = new GRBLinExpr();
        p_env.edges().forEach( e ->
        {
            final Integer l_start = Integer.valueOf( e.from().name() );
            final Integer l_end = Integer.valueOf( e.to().name() );
            try
            {
                final Double l_edge = 0.5 * e.weight().doubleValue();
                m_ys[l_start][l_end] = m_model.addVar( 0.0, 1.0, l_edge,
                        GRB.BINARY,
                        "y" + l_start  + "-" + l_end );
                l_obj.addTerm( l_edge, m_ys[l_start][l_end] );

                p_pods.forEach( p ->
                {
                    final String l_ds = p.name();
                    final GRBVar[][] l_temp = m_xs.get( l_ds );
                    try
                    {
                        l_temp[l_start][l_end] = m_model.addVar( 0.0, 1.0, 0.0,
                                GRB.BINARY,
                                "x" + "_" + l_ds + ":" + l_start + "-" + l_end );
                    }
                    catch ( final GRBException l_err )
                    {
                        l_err.printStackTrace();
                    }
                } );
            }
            catch ( final GRBException l_e1 )
            {
                l_e1.printStackTrace();
            }

        } );
        m_model.setObjective( l_obj );
        m_optimum = null;
    }

    @Override
    public void solve() throws GRBException
    {
        addConstraints();
        addLengthConstraint();
        addSpeedConstrain();
        addTimeConstraint();
        addCostConstraint();
        m_model.optimize();
        m_optimum = m_model.get( GRB.DoubleAttr.ObjVal );
        if ( !m_optimum.equals( null ) )
        {
            saveResults();
            m_flagged = secondCostCheck();
        }
        cleanUp();
    }

    /**
     * adds the flow and binary constraints
     */
    private void addConstraints()
    {
        //flow constraint
        m_pods.forEach( p ->
        {
            final GRBVar[][] l_temp = m_xs.get( p.name() );
            IntStream.range( 0, m_graph.size() + 1 ).boxed().forEach( i ->
            {
                final GRBLinExpr l_expr = new GRBLinExpr();
                IntStream.range( 0, m_graph.size() + 1 ).boxed().forEach( j ->
                {
                    if ( l_temp[i][j] != null ) l_expr.addTerm( 1.0, l_temp[i][j] );
                    if ( l_temp[j][i] != null ) l_expr.addTerm( -1.0, l_temp[j][i] );
                } );
                try
                {
                    if  ( i.equals( m_source ) )
                        m_model.addConstr( l_expr, GRB.EQUAL, 1.0, "Origin" +  p );
                    else if ( i.equals( Integer.valueOf( p.destination() ) ) )
                        m_model.addConstr( l_expr, GRB.EQUAL, -1.0, "Destination" +  p );
                    else
                        m_model.addConstr( l_expr, GRB.EQUAL, 0.0, "Flow" +  p );
                }
                catch ( final GRBException l_err )
                {
                    l_err.printStackTrace();
                }
            } );
        } );

        //x<=y
        m_graph.edges().forEach( e ->
        {
            final Integer l_start = Integer.valueOf( e.from().name() );
            final Integer l_end = Integer.valueOf( e.to().name() );

            m_pods.forEach( p ->
            {
                final GRBLinExpr l_expr = new GRBLinExpr();
                final GRBVar[][] l_temp = m_xs.get( p.name() );
                l_expr.addTerm( 1.0, l_temp[l_start][l_end] );
                l_expr.addTerm( -1.0, m_ys[l_start][l_end] );
                try
                {
                    m_model.addConstr( l_expr, GRB.LESS_EQUAL, 0.0, "x<=y" );
                }
                catch ( final GRBException l_ex )
                {
                    System.out.println( l_ex.getErrorCode() );
                }
            } );
        } );
    }

    /**
     * adds the length constraint
     */
    private void addLengthConstraint()
    {
        m_pods.forEach( p ->
        {
            try
            {
                final GRBVar[][] l_temp = m_xs.get( p.name() );
                final double l_ml = p.preferences().lengthLimit();
                final GRBLinExpr l_dist = new GRBLinExpr();
                m_graph.edges().forEach( e -> l_dist.addTerm( e.length(), l_temp[Integer.valueOf( e.from().name() )][Integer.valueOf( e.to().name() )] ) );
                m_model.addConstr( l_dist, GRB.LESS_EQUAL, l_ml, "maxdist" + p.name() );
            }
            catch ( final GRBException l_err )
            {
                l_err.printStackTrace();
            }
        } );
    }

    /**
     * adds the speed constraint
     */
    private void addSpeedConstrain()
    {
        final ArrayList<Double> l_maxspeeds = new ArrayList<>();
        m_pods.forEach( p -> l_maxspeeds.add( p.preferences().maxSpeed() ) );
        m_speed = Collections.min( l_maxspeeds );
    }

    /**
     * adds the time consintraint
     */
    private void addTimeConstraint()
    {
        final HashMap<IEdge, Double> l_times = new HashMap<>();
        m_graph.edges().forEach( e ->
                l_times.put( e, e.length() / ( m_speed * m_unit.getBlock().doubleValue() ) )
        );
        m_pods.forEach( p ->
        {
            try
            {
                final GRBVar[][] l_temp = m_xs.get( p.name() );
                final Integer l_tl = p.preferences().timeLimit();
                final GRBLinExpr l_time = new GRBLinExpr();
                m_graph.edges().forEach( e -> l_time.addTerm( l_times.get( e ), l_temp[Integer.valueOf( e.from().name() )][Integer.valueOf( e.to().name() )] ) );
                m_model.addConstr( l_time, GRB.LESS_EQUAL, l_tl, "maxtime" + p );
            }
            catch ( final GRBException l_err )
            {
                l_err.printStackTrace();
            }
        } );
    }

    /**
     * adds the cost constraint
     */
    private void addCostConstraint()
    {
        m_pods.forEach( p ->
        {
            try
            {
                final GRBVar[][] l_temp = m_xs.get( p.name() );
                final double l_mc = p.preferences().maxCost();
                final GRBLinExpr l_cost = new GRBLinExpr();
                m_graph.edges().forEach( e -> l_cost.addTerm( e.weight().doubleValue(), l_temp[Integer.valueOf( e.from().name() )][Integer.valueOf( e.to().name() )] ) );
                m_model.addConstr( l_cost, GRB.LESS_EQUAL, l_mc + l_mc / 2, "maxcost" + p.name() );
            }
            catch ( final GRBException l_err )
            {
                l_err.printStackTrace();
            }
        } );
    }

    /**
     * saves the result so model can be destroyed after solving
     */
    private void saveResults()
    {
        m_graph.edges().forEach( e ->
        {
            final Integer l_start = Integer.valueOf( e.from().name() );
            final Integer l_end = Integer.valueOf( e.to().name() );

            m_pods.forEach( p ->
            {
                try
                {
                    final GRBVar[][] l_temp = m_xs.get( p.name() );
                    final ArrayList<IEdge> l_res = m_results.get( p );
                    if ( ( l_temp[l_start][l_end] != null ) && ( l_temp[l_start][l_end].get( GRB.DoubleAttr.X ) == 1 ) )
                    {
                        m_np.put( e, m_np.getOrDefault( e, 0 ) + 1 );
                        l_res.add( e );
                        m_endtimes.put( p, m_endtimes.getOrDefault( p.name(), 0 ) + Math.toIntExact( Math.round( e.length() / m_speed ) ) );
                        m_lengths.put( p, m_lengths.getOrDefault( p.name(), 0.0 ) + e.length() );
                    }
                }
                catch ( final GRBException l_err )
                {
                    l_err.printStackTrace();
                }
            } );
        } );
        sortResult();
    }

    /**
     * sorts the result found
     * so that the route is ordered
     */
    private void sortResult()
    {
        m_results.keySet().forEach( p ->
        {
            final ArrayList<IEdge> l_sort = new ArrayList<>();
            final String[] l_last = {p.location()};
            while ( !l_last[0].contentEquals( p.destination() ) )
            {
                m_results.get( p ).forEach( i ->
                {
                    if ( i.from().name().contentEquals( l_last[0] ) )
                    {
                        l_sort.add( i );
                        l_last[0] = i.to().name();
                    }
                } );
            }
            m_results.put( p, l_sort );
        } );
    }


    /**
     * gets the sorted routes
     * @return the routes found by the solver
     */
    @Override
    public HashMap<CVehicle, ArrayList<IEdge>> getRoutes()
    {
        return m_results;
    }

    /**
     * failsafe function to ensure the solver solution
     * outperforms selfish routing
     * @return vehicles whose cost is higher
     */
    private ArrayList<CVehicle> secondCostCheck()
    {
        final ArrayList<CVehicle> l_flagged = new ArrayList<>();
        m_results.keySet().forEach( p ->
        {
            final AtomicDouble l_cost = new AtomicDouble( 0.0 );
            m_results.get( p ).forEach( e -> l_cost.getAndAdd( ( e.weight().doubleValue() + ( e.weight().doubleValue() / m_omega * m_np.get( e ) ) ) / m_np.get( e ) )  );
            if ( l_cost.get() > p.preferences().maxCost() ) l_flagged.add( p );
        } );
        return l_flagged;
    }


    /**
     * the flagged vehicles
     * @return vehicles whose cost is higher with the solver
     */
    @Override
    public ArrayList<CVehicle> getFlagged()
    {
        return m_flagged;
    }

    /**
     * the final solution( all y's selected)
     * @return the complete solution
     */
    @Override
    public HashMap<IEdge, Integer> getNP()
    {
        return m_np;
    }

    @Override
    public void display()
    {
        System.out.println( "Origin is: " + m_source.toString() );
        System.out.println( "Destinations are: " );
        m_pods.forEach( p -> System.out.print( p.name() + ": " +  p.destination() + " " ) );
        System.out.println();
        System.out.println();

        m_results.keySet().forEach( d -> m_results.get( d ).forEach( x -> System.out.println( d.name() + " : " + x.name() ) ) );
        System.out.println();

        m_np.keySet().forEach( y -> System.out.println( "y:" + y.name() ) );
    }

    /**
     * destroys the model
     * @throws GRBException gurobi
     */
    private void cleanUp() throws GRBException
    {
        m_model.dispose();
        m_env.dispose();
    }
}
