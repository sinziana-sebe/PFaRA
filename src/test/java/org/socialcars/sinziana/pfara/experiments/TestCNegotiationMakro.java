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

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class TestCNegotiationMakro
{
    private CNegotiationMakro m_negmakro;

    @Before
    public void init() throws IOException
    {
        m_negmakro = new CNegotiationMakro( "src/test/resources/25.json", "src/test/resources/25_info.json", "25NegMakro", 1, 10.0, 3.0, true, 10 );
    }

    @Test
    public void run()
    {
        m_negmakro.run();
    }
}
