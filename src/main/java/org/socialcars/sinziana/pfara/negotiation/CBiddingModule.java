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

import org.socialcars.sinziana.pfara.negotiation.probabilities.CProbGenerator;
import org.socialcars.sinziana.pfara.negotiation.probabilities.CRVtoAccProb;
import org.socialcars.sinziana.pfara.negotiation.simultaneoussearch.CCollege;
import org.socialcars.sinziana.pfara.negotiation.simultaneoussearch.CSimultaneousSearch;

import java.util.ArrayList;

public class CBiddingModule implements IBiddingModule
{
    private final String m_type;
    private Double m_strategy;
    private final Double m_firstbid;
    private final Integer m_deadline;

    private final ArrayList<Double> m_offers;
    private Integer m_round;

    private final CProbGenerator m_probgen;
    private CRVtoAccProb m_rvtoacc;
    private final CSimultaneousSearch m_ss;

    public CBiddingModule( final String p_type, final Double p_firstbid, final Integer p_deadline )
    {
        m_type = p_type;
        m_strategy = 1.0;
        m_firstbid = p_firstbid;
        m_deadline = p_deadline;

        m_offers = new ArrayList<>();
        m_round = 0;

        m_probgen = new CProbGenerator();
        m_ss = new CSimultaneousSearch();
    }

    /**
     * gives the best bid in the current round
     * @param p_bids the previous bids
     * @param p_time the current round
     * @param p_rv opponent's estimated reservation value
     * @return best offer
     */
    @Override
    public Double getBestBid( final ArrayList<Double> p_bids, final Integer p_time, final Double p_rv )
    {
        //generates the opponets payment limit(RV) distribution
        final ArrayList<Double> l_rvs = m_probgen.getDistribution( "normal", 100, p_rv );
        //transforms the RV distribution to acceptance probability of ego bids
        m_rvtoacc = new CRVtoAccProb( m_type, m_strategy, m_firstbid, m_deadline, l_rvs );
        //uses the adapted simultaneous search to determine the best bid to make at this point in the negotiation
        m_ss.fromAccProb( m_rvtoacc.calculateForBids( p_bids, p_time ) );
        //chooses the best bid
        final CCollege l_res = m_ss.getBestBid();
        return l_res.getU();
    }

    /**
     * updates the opponents perceived strategy
     * @param p_offer the current offer made by the opponent
     */
    @Override
    public void updateStrategy( final Double p_offer )
    {
        //the percentage change from the previous bids
        final Double l_percentage;
        switch ( m_type )
        {
            case "Acceptor":
                l_percentage = Math.abs( p_offer  - m_offers.get( m_offers.size() - 1 ) ) / m_offers.get( 0 ) * 100;
                if ( l_percentage < 5 ) m_strategy = m_strategy * 25 / 100;
                else if ( l_percentage > 10 ) m_strategy += 10;
                m_offers.add( p_offer );
                m_round++;
                break;
            case "Initiator":
                l_percentage = Math.abs( p_offer - m_offers.get( m_offers.size() - 1 ) ) / m_offers.get( 0 ) * 100;
                if ( l_percentage < 10 ) m_strategy = m_strategy * 25 / 100;
                else if ( l_percentage > 100 ) m_strategy += 10;
                m_offers.add( p_offer );
                m_round++;
                break;
            default:
        }
    }
}
