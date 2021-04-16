package org.socialcars.sinziana.pfara.negotiation.probabilities;

import org.apache.commons.math3.distribution.BetaDistribution;
import org.apache.commons.math3.distribution.CauchyDistribution;
import org.apache.commons.math3.distribution.GammaDistribution;
import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.socialcars.sinziana.pfara.negotiation.simultaneoussearch.CCollege;
import org.socialcars.sinziana.pfara.negotiation.simultaneoussearch.CColleges;

import java.util.ArrayList;
import java.util.stream.IntStream;

/**
 * class for generating a probability distribuition
 */
public class CProbGenerator
{
    private CColleges m_colprob;
    private ArrayList<Double> m_prob;

    /**
     * ctor
     */
    public CProbGenerator()
    {
        m_prob = new ArrayList<>();
        m_colprob = new CColleges();
    }


    /**
     * creates touples of a probability distribution dependant on type
     * @param p_type the type desired
     * @param p_samplesize the number of entries
     * @return the probability distribution
     */
    public CColleges getCollegeDistribution( final String p_type, final Integer p_samplesize )
    {
        switch ( p_type )
        {
            case "normal":
                colNormal( p_samplesize );
                break;
            case "log":
                collogNormal( p_samplesize );
                break;
            case "beta":
                colbeta( p_samplesize );
                break;
            case "cauchy":
                colcauchy( p_samplesize );
                break;
            case "gamma":
                colgamma( p_samplesize );
                break;
            default:
        }

        return m_colprob;
    }

    /**
     * creates a simple set of a probability distribution based on type
     * @param p_type the distribution type
     * @param p_samplesize the size of the distribution
     * @param p_peak the peak of the distribution
     * @return the distribution
     */
    public ArrayList<Double> getDistribution( final String p_type, final Integer p_samplesize, final Double p_peak )
    {
        switch ( p_type )
        {
            case "normal":
                normal( p_samplesize, p_peak );
                break;
            case "log":
                logNormal( p_samplesize, p_peak );
                break;
            case "beta":
                beta( p_samplesize, p_peak );
                break;
            case "cauchy":
                cauchy( p_samplesize, p_peak );
                break;
            case "gamma":
                gamma( p_samplesize, p_peak );
                break;
            default:
        }
        return m_prob;
    }

    /**
     * collage normal distribution
     * @param p_samplesize the size
     */
    private void colNormal( final Integer p_samplesize )
    {
        final NormalDistribution l_norm = new NormalDistribution( 0.5, 0.18 );
        final double[] l_sample = l_norm.sample( p_samplesize );

        final ArrayList<Double> l_prob = new ArrayList<>();
        IntStream.range( 0, p_samplesize ).boxed().forEach( i -> l_prob.add( i, 1 - Math.abs( l_sample[i] - 0.5 ) / 0.5 ) );
        IntStream.range( 0, p_samplesize ).boxed().forEach( i -> m_colprob.add( new CCollege( l_sample[i], l_prob.get( i ) ) ) );
    }

    /**
     * normal distribution
     * @param p_samplesize size
     * @param p_peak peak
     */
    private void normal( final Integer p_samplesize, final Double p_peak )
    {
        final NormalDistribution l_norm = new NormalDistribution( p_peak, p_peak / 3 );
        final double[] l_sample = l_norm.sample( p_samplesize );
        IntStream.range( 0, p_samplesize ).boxed().forEach( i -> m_prob.add(  l_sample[i] ) );
    }

    /**
     * college beta distribution
     * @param p_samplesize size
     */
    private void colbeta( final Integer p_samplesize )
    {
        final BetaDistribution l_te = new BetaDistribution( 2, 2 );
        final double[] l_sample = l_te.sample( p_samplesize );
        final ArrayList<Double> l_prob = new ArrayList<>();
        IntStream.range( 0, p_samplesize ).boxed().forEach( i -> l_prob.add( i, 1 - Math.abs( l_sample[i] - 0.5 ) / 0.5 ) );
        IntStream.range( 0, p_samplesize ).boxed().forEach( i -> m_colprob.add( new CCollege( l_sample[i], l_prob.get( i ) ) ) );
    }

    /**
     * beta distribution
     * @param p_samplesize size
     * @param p_peak peak
     */
    private void beta( final Integer p_samplesize, final Double p_peak )
    {
        final BetaDistribution l_norm = new BetaDistribution( 2, 2 );
        final double[] l_sample = l_norm.sample( p_samplesize );
        IntStream.range( 0, p_samplesize ).boxed().forEach( i -> m_prob.add(  l_sample[i] ) );
    }

    /**
     * college cauchy distribution
     * @param p_samplesize size
     */
    private void colcauchy( final Integer p_samplesize )
    {
        final CauchyDistribution l_cauchy = new CauchyDistribution( 0.5, 0.1 );
        final double[] l_sample = l_cauchy.sample( p_samplesize );
        final ArrayList<Double> l_prob = new ArrayList<>();
        IntStream.range( 0, p_samplesize ).boxed().forEach( i -> l_prob.add( i, 1 - Math.abs( l_sample[i] - 0.5 ) / 0.5 ) );
        IntStream.range( 0, p_samplesize ).boxed().forEach( i -> m_colprob.add( new CCollege( l_sample[i], l_prob.get( i ) ) ) );
    }

    /**
     * cauchy distribution
     * @param p_samplesize size
     * @param p_peak peak
     */
    private void cauchy( final Integer p_samplesize, final Double p_peak )
    {
        final CauchyDistribution l_norm = new CauchyDistribution( 0.5, 0.1 );
        final double[] l_sample = l_norm.sample( p_samplesize );
        IntStream.range( 0, p_samplesize ).boxed().forEach( i -> m_prob.add(  l_sample[i] ) );
    }

    /**
     * college gamma distribution
     * @param p_samplesize size
     */
    private void colgamma( final Integer p_samplesize )
    {
        final GammaDistribution l_gamma = new GammaDistribution( 0.5, 1 );
        final double[] l_sample = l_gamma.sample( p_samplesize );
        final ArrayList<Double> l_prob = new ArrayList<>();
        IntStream.range( 0, p_samplesize ).boxed().forEach( i -> l_prob.add( i, 1 - Math.abs( l_sample[i] - 0.5 ) / 0.5 ) );
        IntStream.range( 0, p_samplesize ).boxed().forEach( i -> m_colprob.add( new CCollege( l_sample[i], l_prob.get( i ) ) ) );
    }

    /**
     * gamma distribution
     * @param p_samplesize size
     * @param p_peak peak
     */
    private void gamma( final Integer p_samplesize, final Double p_peak )
    {
        final GammaDistribution l_norm = new GammaDistribution( 0.5, 1 );
        final double[] l_sample = l_norm.sample( p_samplesize );
        IntStream.range( 0, p_samplesize ).boxed().forEach( i -> m_prob.add(  l_sample[i] ) );
    }

    /**
     * college lognormal distribution
     * @param p_samplesize size
     */
    private void collogNormal( final Integer p_samplesize )
    {
        final LogNormalDistribution l_lognorm = new LogNormalDistribution( -0.5, 0.125 );
        final double[] l_sample = l_lognorm.sample( p_samplesize );
        final ArrayList<Double> l_prob = new ArrayList<>();
        IntStream.range( 0, p_samplesize ).boxed().forEach( i -> l_prob.add( i, 1 - Math.abs( l_sample[i] - 0.5 ) / 0.5 )  );
        IntStream.range( 0, p_samplesize ).boxed().forEach( i -> m_colprob.add( new CCollege( l_sample[i], l_prob.get( i ) ) ) );
    }

    /**
     * lognormal distribution
     * @param p_samplesize size
     * @param p_peak peak
     */
    private void logNormal( final Integer p_samplesize, final Double p_peak )
    {
        final LogNormalDistribution l_norm = new LogNormalDistribution( -0.5, 0.125 );
        final double[] l_sample = l_norm.sample( p_samplesize );
        IntStream.range( 0, p_samplesize ).boxed().forEach( i -> m_prob.add(  l_sample[i] ) );
    }

    /**
     * fixed colleges for testing purposes
     * @return the probability distribution
     */
    public CColleges fixedColleges()
    {
        m_colprob.add( new CCollege( 0.6430336328991796,  0.7139327342016408 ) );
        m_colprob.add( new CCollege( 0.5506689434307167,  0.8986621131385666 ) );
        m_colprob.add( new CCollege( 0.4770024715900329,  0.9540049431800658 ) );
        m_colprob.add( new CCollege( 0.42107626757093114,  0.8421525351418623 ) );
        m_colprob.add( new CCollege( 0.39256257723817983,  0.7851251544763597 ) );
        m_colprob.add( new CCollege( 0.3920240289045138,  0.7840480578090276 ) );
        m_colprob.add( new CCollege( 0.37490620114134326,  0.7498124022826865 ) );
        m_colprob.add( new CCollege( 0.3509479069562612,  0.7018958139125224 ) );
        m_colprob.add( new CCollege( 0.3217605222550841,  0.6435210445101682 ) );
        m_colprob.add( new CCollege( 0.2041920703259128,  0.4083841406518256 ) );
        return m_colprob;
    }

    /**
     * fixed distribution for testing purposes
     * @return the distribution
     */
    public ArrayList<Double> fixed()
    {
        final ArrayList<Double> l_du = new ArrayList<>();
        l_du.add( 0.6430336328991796 );
        l_du.add( 0.5506689434307167 );
        l_du.add( 0.4770024715900329 );
        l_du.add( 0.39256257723817983 );
        l_du.add( 0.3920240289045138 );
        l_du.add( 0.37490620114134326 );
        l_du .add( 0.3509479069562612 );
        l_du.add( 0.3217605222550841 );
        l_du.add( 0.2041920703259128 );
        return l_du;
    }
}
