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

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

public class TestCInitialOffer
{
    private CInitialOffer m_offer;


    /**
     * initialising
     */
    @Before
    public void init()
    {
        m_offer = new CInitialOffer( "01", 5.0, Collections.emptyList() );
    }

    /**
     * tests the id
     */
    @Test
    public void testID()
    {
        Assume.assumeNotNull( m_offer );
        Assert.assertTrue( m_offer.id().contentEquals( "01" ) );
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
}
