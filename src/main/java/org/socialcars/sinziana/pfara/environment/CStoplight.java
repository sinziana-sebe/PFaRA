package org.socialcars.sinziana.pfara.environment;

import org.socialcars.sinziana.pfara.data.input.CStoplightpojo;

import java.util.logging.Logger;

public class CStoplight implements IStoplight
{
    private static Logger s_logger;

    private final String m_edgename;
    private final Integer m_red;
    private final Integer m_green;
    private final Integer m_start;

    private ELightState m_state;
    private Integer m_current;

    /**
     * ctor
     * @param p_pojo plain old java object
     */
    public CStoplight( final CStoplightpojo p_pojo )
    {
        m_edgename = p_pojo.getLocation();
        m_red = p_pojo.getRed();
        m_green = p_pojo.getGreen();
        m_start = p_pojo.getStart();
    }

    /**
     * sets the logger
     * @param p_log the logger abject
     */
    public void setLogger( final Logger p_log )
    {
        s_logger = p_log;
    }

    /**
     * when the light cycle is started
     * @return start time of the cycle
     */
    public Integer startTime()
    {
        return m_start;
    }

    /**
     * starts the light cycle
     */
    public void start()
    {
        m_state = ELightState.GREEN;
        m_current = m_green;
    }

    /**
     * state
     * @return the current state
     */
    @Override
    public Integer timeLeft()
    {
        return m_current;
    }

    /**
     * time left
     * @return how many timesteps untill light changes
     */
    @Override
    public ELightState state()
    {
        return m_state;
    }

    /**
     * updates the light
     */
    public void update()
    {
        if ( m_current > 1 )
            m_current--;
        else if ( m_current == 1 )
        {
            switch ( m_state )
            {
                case RED:
                    m_state = ELightState.GREEN;
                    //s_logger.log( Level.INFO, m_edgename + " switched to green" );
                    m_current = m_green;
                    break;


                case GREEN: m_state = ELightState.RED;
                    //s_logger.log( Level.INFO, m_edgename + " switched to red" );
                    m_current = m_red;
                    break;

                default: break;
            }
        }
    }
}
