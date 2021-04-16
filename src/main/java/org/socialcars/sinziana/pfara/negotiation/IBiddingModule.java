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

import java.util.ArrayList;

/**
 * the bidding module interface
 */
public interface IBiddingModule
{
    /**
     * gives the best bid in the current round
     * @param p_bids the previous bids
     * @param p_time the current round
     * @param p_rv opponent's estimated reservation value
     * @return best offer
     */
    Double getBestBid( final ArrayList<Double> p_bids, final Integer p_time, final Double p_rv );

    /**
     * updates the opponents perceived strategy
     * @param p_offer the current offer made by the opponent
     */
    void updateStrategy( final Double p_offer );
}
