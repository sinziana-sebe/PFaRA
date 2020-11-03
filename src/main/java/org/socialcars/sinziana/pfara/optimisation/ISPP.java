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

/**
 * interface for the simple
 * shortest path problem
 */
public interface ISPP
{
    /**
     * solve function
     * @throws GRBException gurobi
     */
    void solve() throws GRBException;

    /**
     * displays the result
     * @throws GRBException gurobi
     */
    void display() throws GRBException;

    /**
     * the length of the route
     * @return length
     */
    Integer length();

    /**
     * cost of the route
     * @return cost
     */
    Double cost();
}
