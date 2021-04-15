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

import gurobi.GRBException;
import org.socialcars.sinziana.pfara.agents.CVehicle;
import org.socialcars.sinziana.pfara.environment.IEdge;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * interface for the
 * platooning shortest path problem
 */
public interface IPSPP
{
    /**
     * solving the problem
     * @throws GRBException gurobi
     */
    void solve() throws GRBException;

    /**
     * displaying the results
     * @throws GRBException gurobi
     */
    void display() throws GRBException;

    /**
     * gets the sorted routes
     * @return the routes found by the solver
     */
    HashMap<CVehicle, ArrayList<IEdge>> getRoutes();

    /**
     * the flagged vehicles
     * @return vehicles whose cost is higher with the solver
     */
    ArrayList<CVehicle> getFlagged();

    /**
     * the final cost( sum of the y)
     * @return the cost of the complete solution
     */
    HashMap<IEdge, Integer> getNP();
}
