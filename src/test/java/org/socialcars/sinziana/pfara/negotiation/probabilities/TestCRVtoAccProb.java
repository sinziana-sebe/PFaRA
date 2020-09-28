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

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class TestCRVtoAccProb
{
    private CRVtoAccProb m_rvacci;
    private CRVtoAccProb m_rvacca;
    private ArrayList<Double> m_prob;

    @Before
    public void init() throws IOException
    {
        final CProbGenerator l_probgen = new CProbGenerator();
        m_prob = l_probgen.fixed();
        m_rvacci = new CRVtoAccProb( "Initiator", 1.0, 0.1, 10, m_prob );
        m_rvacca = new CRVtoAccProb( "Acceptor", 1.0, 0.9, 10, m_prob );
    }

    @Test
    public void testAccprob()
    {
        final ArrayList<Double> l_resi = m_rvacci.calculateAccProb( 1 );
        IntStream.range( 0, l_resi.size() ).boxed().forEach( i ->
        {
            System.out.println( "RVi: " + m_prob.get( i ) );
            System.out.println( "ACCPi: " + l_resi.get( i ) );
        } );

        System.out.println( "EMPTY LINE" );

        final ArrayList<Double> l_resa = m_rvacca.calculateAccProb( 1 );
        IntStream.range( 0, l_resi.size() ).boxed().forEach( i ->
        {
            System.out.println( "RVa: " + m_rvacca.getRVProb().get( i ) );
            System.out.println( "ACCPa: " + l_resa.get( i ) );
        } );
    }

    @Test
    public void testBid()
    {
        System.out.println( m_rvacci.calculateForBid( 0.5, 9 ) );
        System.out.println( "EMPTY LINE" );
        System.out.println( m_rvacca.calculateForBid( 0.5, 9 ) );
    }

    @Test
    public void testBids()
    {
        final ArrayList<Double> l_bids = new ArrayList<>();
        l_bids.add( 0.1 );
        l_bids.add( 0.2 );
        l_bids.add( 0.3 );
        l_bids.add( 0.4 );
        l_bids.add( 0.5 );
        l_bids.add( 0.6 );
        l_bids.add( 0.7 );
        l_bids.add( 0.8 );
        l_bids.add( 0.9 );
        System.out.println( m_rvacci.calculateForBids( l_bids, 9 ) );
        System.out.println( m_rvacca.calculateForBids( l_bids, 9 ) );
    }
}

