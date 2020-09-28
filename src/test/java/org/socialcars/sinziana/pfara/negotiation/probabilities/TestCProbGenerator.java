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

package org.socialcars.sinziana.pfara.negotiation.probabilities;

import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.pfara.negotiation.simultaneoussearch.CColleges;

public class TestCProbGenerator
{
    private CProbGenerator m_gen;

    @Before
    public void init()
    {
        m_gen = new CProbGenerator();
    }

    @Test
    public void normal()
    {
        final CColleges l_prob = m_gen.getCollegeDistribution( "normal", 10 );
        l_prob.sort();
        l_prob.getColleges().forEach( c -> System.out.println( c.getU() + "  "  + c.getAlpha() ) );
    }

    @Test
    public void beta()
    {
        final CColleges l_prob = m_gen.getCollegeDistribution( "beta", 100 );
        l_prob.sort();
        l_prob.getColleges().forEach( c -> System.out.println( c.getU() + " " + c.getAlpha() ) );
    }

    @Test
    public void cauchy()
    {
        final CColleges l_prob = m_gen.getCollegeDistribution( "cauchy", 100 );
        l_prob.sort();
        l_prob.getColleges().forEach( c -> System.out.println( c.getU() + " " + c.getAlpha() ) );
    }

    @Test
    public void gamma()
    {
        final CColleges l_prob = m_gen.getCollegeDistribution( "gamma", 100 );
        l_prob.sort();
        l_prob.getColleges().forEach( c -> System.out.println( c.getU() + " " + c.getAlpha() ) );
    }

    @Test
    public void logNorm()
    {
        final CColleges l_prob = m_gen.getCollegeDistribution( "log", 100 );
        l_prob.sort();
        l_prob.getColleges().forEach( c -> System.out.println( c.getU() + " " + c.getAlpha() ) );
    }

    @Test
    public void fixed()
    {
        final CColleges l_prob = m_gen.fixedColleges();
        l_prob.getColleges().forEach( c -> System.out.println( c.getU() * c.getAlpha() ) );
    }
}
