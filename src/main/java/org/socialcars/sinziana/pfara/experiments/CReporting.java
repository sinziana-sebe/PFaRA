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

import com.opencsv.CSVWriter;
import org.socialcars.sinziana.pfara.agents.CVehicle;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * class for final reporting
 */
public class CReporting
{
    private final CSVWriter m_csvwriter;
    private final Boolean m_mikro;

    /**
     * ctor
     * @param p_outfile the output file
     * @param p_mikro type of movement
     * @throws IOException file
     */
    public CReporting( final String p_outfile, final Boolean p_mikro ) throws IOException
    {
        m_csvwriter = new CSVWriter( new FileWriter( "CSV" + p_outfile ) );
        m_mikro = p_mikro;
    }

    /**
     * writes the vehicles' information into a csv file
     * @param p_vehicles the vehicles
     * @throws IOException file
     */
    public void writeCSV( final ArrayList<CVehicle> p_vehicles ) throws IOException
    {
        final ArrayList<String[]> l_stringarray = new ArrayList<>();
        final String[] l_header = new String[]{"Vehicle", "Cost", "Length", "Time", "Utility"};
        l_stringarray.add( l_header );
        p_vehicles.forEach( v ->
        {
            final String[] l_singular = new String[] {v.name(), v.routeCost().toString(), v.routeLength().toString(),
                    v.routeDuration().toString(), v.endUtility( m_mikro ).toString() };
            l_stringarray.add( l_singular );
        } );
        m_csvwriter.writeAll( l_stringarray );
        m_csvwriter.close();
    }
}
