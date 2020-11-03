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

import gurobi.GRBException;
import org.socialcars.sinziana.pfara.agents.CVehicle;
import org.socialcars.sinziana.pfara.environment.CGraph;
import org.socialcars.sinziana.pfara.environment.IEdge;
import org.socialcars.sinziana.pfara.optimisation.CPSPP;
import org.socialcars.sinziana.pfara.units.CUnits;

import java.util.ArrayList;
import java.util.HashMap;

public class COptimiser
{
    private final CPSPP m_opt;

    public COptimiser( final CGraph p_env, final Integer p_origin, final ArrayList<CVehicle> p_vehicles, final CUnits p_unit, final Double p_omega ) throws GRBException
    {
        m_opt = new CPSPP( p_env, p_origin, p_vehicles, p_unit, p_omega );
        m_opt.solve();
    }

    public HashMap<CVehicle, ArrayList<IEdge>> getRoutes()
    {
        return m_opt.getRoutes();
    }

    public ArrayList<CVehicle> getFlagged()
    {
        return m_opt.getFlagged();
    }

    public HashMap<IEdge, Integer> getNP()
    {
        return m_opt.getNP();
    }
}
