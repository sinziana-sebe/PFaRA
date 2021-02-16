/**
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

package org.socialcars.sinziana.pfara.environment;

public interface IEdge
{
    /**
     * name of the edge
     * @return the edge's name
     */
    String name();

    /**
     * origin node
     * @return the origin node
     */
    INode from();

    /**
     * end node
     * @return the end node
     */
    INode to();

    /**
     * weigh of the edge
     * @return the edge's weight
     */
    Double weight();

    /**
     * length of the edge
     * @return the edge's length
     */
    Double length();

    /**
     * adds a stoplight to this edge
     * @param p_light the stoplight object
     */
    void addStoplight( final CStoplight p_light );

    /**
     * the stoplight
     * @return the stoplight partaining to the edge
     */
    CStoplight stoplight();

    /**
     * adds sections to the edge
     * @param p_section the section lengths
     */
    void addSections( final CSections p_section );

    /**
     * gives the edge sections
     * @return the sections
     */
    CSections sections();

    /**
     * adds background traffic information
     * @param p_traffic the traffic object
     */
    void addBackgroundTraffic( final CBackground p_traffic );

    /**
     * returns the maximum speed permitted by traffic
     * @return the speed
     */
    Double maxSpeed();

}
