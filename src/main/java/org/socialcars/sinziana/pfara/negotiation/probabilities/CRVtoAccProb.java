package org.socialcars.sinziana.pfara.negotiation.probabilities;

import com.google.common.util.concurrent.AtomicDouble;
import org.socialcars.sinziana.pfara.negotiation.simultaneoussearch.CCollege;
import org.socialcars.sinziana.pfara.negotiation.simultaneoussearch.CColleges;

import java.util.ArrayList;

/**
 * class for transforming the opponent payment limit(RV)
 * to and acceptandce probability of ego bids
 */
public class CRVtoAccProb
{
    private final ArrayList<Double> m_rvprob;
    private final String m_opponent;
    private Double m_opponenttype;
    private final Double m_firstbid;
    private final Integer m_deadline;
    private ArrayList<Double> m_accprob;

    /**
     * ctor
     * @param p_type the type of opponent (initiator/acceptor)
     * @param p_opptype the opponent strategy (boulware,linear,conceder)
     * @param p_firstbid the first bid
     * @param p_deadline the deadline
     * @param p_rvprob the payment limit distribution
     */
    public CRVtoAccProb( final String p_type, final Double p_opptype, final Double p_firstbid, final Integer p_deadline, final ArrayList<Double> p_rvprob )
    {
        m_rvprob = p_rvprob;
        m_firstbid = p_firstbid;
        m_deadline = p_deadline;
        m_opponenttype = p_opptype;
        m_opponent = p_type;
    }

    /**
     * the RV probability
     * @return the RV probability
     */
    public ArrayList<Double> getRVProb()
    {
        return m_rvprob;
    }

    /**
     * calculates the ego bid acceptance probability
     * @param p_time the current moment of negotiation
     * @return the acceptance probability distribution
     */
    public ArrayList<Double> calculateAccProb( final Integer p_time )
    {
        final Double l_alpha = m_firstbid + ( 1 - m_firstbid ) * Math.pow( p_time.doubleValue() / m_deadline.doubleValue(), m_opponenttype );
        m_accprob = new ArrayList<>();
        m_rvprob.forEach( c ->
        {
            Double l_xs = 0.0;
            switch ( m_opponent )
            {
                case "Initiator":
                    l_xs = m_firstbid + l_alpha * ( c - m_firstbid );
                    break;
                case "Acceptor":
                    l_xs = c + ( 1 - l_alpha ) * ( m_firstbid - c );
                    break;
                default:
            }
            m_accprob.add( l_xs );
        } );
        return m_accprob;
    }

    /**
     * calculates the acceptance probability of one bid
     * @param p_bid the desired bid
     * @param p_time the current moment in the negotiation
     * @return the acceptance rate of the bid
     */
    public Double calculateForBid( final Double p_bid, final Integer p_time )
    {
        final AtomicDouble l_prob = new AtomicDouble( 0.0 );
        m_accprob = calculateAccProb( p_time );
        final ArrayList<Double> l_new = new ArrayList<>();
        m_accprob.forEach( c ->
        {
            switch ( m_opponent )
            {
                case "Initiator":
                    if ( c >= p_bid )
                    {
                        l_prob.getAndAdd( 1.0 );
                        l_new.add( c );
                    }
                    break;
                case "Acceptor":
                    if ( c <= p_bid )
                    {
                        l_prob.getAndAdd( 1.0 );
                        l_new.add( c );
                    }
                    break;
                default:
            }
        } );
        return l_prob.get() / m_rvprob.size();
    }

    /**
     * calculates the acceptance probability for multiple bids
     * @param p_bids the desired bids
     * @param p_time the current moment in the negotiation
     * @return touples consisting o bids and their acceptance rate
     */
    public CColleges calculateForBids( final ArrayList<Double> p_bids, final Integer p_time )
    {
        final CColleges l_bidprob = new CColleges();
        p_bids.forEach( b ->
        {
            final AtomicDouble l_prob = new AtomicDouble( 0.0 );
            m_accprob = calculateAccProb( p_time );
            final ArrayList<Double> l_new = new ArrayList<>();
            m_accprob.forEach( c ->
            {
                switch ( m_opponent )
                {
                    case "Initiator":
                        if ( c >= b )
                        {
                            l_prob.getAndAdd( 1.0 );
                            l_new.add( c );
                        }
                        break;
                    case "Acceptor":
                        if ( c <= b )
                        {
                            l_prob.getAndAdd( 1.0 );
                            l_new.add( c );
                        }
                        break;
                    default:
                }
            } );
            l_bidprob.add( new CCollege( b, l_prob.get() / m_rvprob.size() ) );
        } );
        return l_bidprob;
    }
}
