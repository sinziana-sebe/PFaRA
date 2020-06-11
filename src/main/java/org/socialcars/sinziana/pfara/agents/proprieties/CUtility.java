package org.socialcars.sinziana.pfara.agents.proprieties;

import org.socialcars.sinziana.pfara.data.input.CUtilitypojo;

public class CUtility implements IUtility
{
    private final Double m_alpha;
    private final Double m_beta;

    private Double m_altcost;

    /**
     * ctor
     * @param p_pojo the pojo object
     */
    public CUtility( final CUtilitypojo p_pojo )
    {
        m_alpha = p_pojo.getAlpha();
        m_beta = p_pojo.getBeta();
    }

    /**
     * the first coefficient
     * @return alpha
     */
    @Override
    public Double alpha()
    {
        return m_alpha;
    }

    /**
     * the second coefficient
     * @return beta
     */
    @Override
    public Double beta()
    {
        return m_beta;
    }


}
