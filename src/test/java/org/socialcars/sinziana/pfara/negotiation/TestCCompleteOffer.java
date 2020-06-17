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

package org.socialcars.sinziana.pfara.negotiation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.pfara.agents.CVehicle;
import org.socialcars.sinziana.pfara.data.input.CInputpojo;
import org.socialcars.sinziana.pfara.data.input.CVehiclepojo;
import org.socialcars.sinziana.pfara.units.CUnits;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TestCCompleteOffer
{
    private CCompleteOffer m_offer;

    /**
     * initialising
     */
    @Before
    public void init() throws IOException
    {
        final CUnits l_unit = new CUnits( 1, 10 );

        final Set<CVehiclepojo> l_pod = new HashSet<>();
        final ArrayList<CVehicle> l_pods = new ArrayList<>();
        new ObjectMapper().readValue( new File( "src/test/resources/minimal-graph.json" ), CInputpojo.class ).getVehicles().forEach( p -> l_pod.add( p ) );
        l_pod.forEach( p -> l_pods.add( new CVehicle( p, 0, "output", l_unit ) ) );
        final CInitialOffer l_offer = new CInitialOffer( "01", 5.0, Collections.emptyList() );

        m_offer = new CCompleteOffer( l_pods.get( 1 ), l_offer, l_pods.get( 0 ), null  );
    }

    /**
     * tests the id
     */
    @Test
    public void testID()
    {
        Assume.assumeNotNull( m_offer );
        Assert.assertTrue( m_offer.id().contentEquals( "red-blue" ) );
    }

    /**
     * tests the buyout
     */
    @Test
    public void testBuyout()
    {
        Assume.assumeNotNull( m_offer );
        Assert.assertTrue( m_offer.buyout().equals( 5.0 ) );
    }

    /**
     * tests the route
     */
    @Test
    public void testRoute()
    {
        Assume.assumeNotNull( m_offer );
        Assert.assertTrue( m_offer.route().isEmpty() );
    }

    /**
     * tests the saving incurred from platooning
     */
    @Test
    public void testSavings()
    {
        Assume.assumeNotNull( m_offer );
        Assert.assertTrue( m_offer.savings().equals( 0.0 ) );
    }

    /**
     * tests the changing of the buyout
     */
    @Test
    public void testChangeBuyout()
    {
        Assume.assumeNotNull( m_offer );
        m_offer.changeBuyout( 6.0 );
        Assert.assertTrue( m_offer.buyout().equals( 6.0 ) );
    }

    @Test
    public void testAcceptor()
    {
        Assume.assumeNotNull( m_offer );
        Assert.assertTrue( m_offer.acceptor().name().contentEquals( "blue" ) );
    }

    @Test
    public void testAlternativeRoute()
    {
        Assume.assumeNotNull( m_offer );
        Assert.assertTrue( m_offer.alternativeRoute() == null );
    }

    @Test
    public void testStates()
    {
        Assume.assumeNotNull( m_offer );
        Assert.assertTrue( m_offer.state().equals( EOfferState.PENDING ) );
        m_offer.accept();
        Assert.assertTrue( m_offer.state().equals( EOfferState.ACCEPTED ) );
        m_offer.reject();
        Assert.assertTrue( m_offer.state().equals( EOfferState.REJECTED ) );
        m_offer.close();
        Assert.assertTrue( m_offer.state().equals( EOfferState.CLOSED ) );
    }
}
