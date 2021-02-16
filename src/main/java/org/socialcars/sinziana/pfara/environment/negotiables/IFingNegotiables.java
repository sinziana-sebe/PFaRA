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

package org.socialcars.sinziana.pfara.environment.negotiables;

public interface IFingNegotiables
{
    /**
     * finds simple negotiable triangles
     */
    void findSimple();

    /**
     * validates simple negotiable triangles
     * @param p_el the triangle
     * @return whether it is valid or not
     */
    Boolean validateSimple( final INegotiableElement p_el );

    /**
     * finds complex negotiable shapes (trapezoids)
     */
    void findComplex();

    /**
     * validates the complex negotiable trapezoid
     * @param p_el the trapezoid
     * @return whether it is valid or not
     */
    Boolean validateComplex( final INegotiableElement p_el );
}
