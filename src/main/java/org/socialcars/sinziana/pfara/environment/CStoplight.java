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

import org.socialcars.sinziana.pfara.data.input.CStoplightpojo;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * the stoplight class
 */
public class CStoplight implements IStoplight
{
    private static Logger s_logger;

    private String m_edgename;

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
    @Override
    public void setLogger( final Logger p_log )
    {
        s_logger = p_log;
    }

    /**
     * when the light cycle is started
     * @return start time of the cycle
     */
    @Override
    public Integer startTime()
    {
        return m_start;
    }

    /**
     * starts the light cycle
     */
    @Override
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

    @Override
    public String edgeName()
    {
        return m_edgename;
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
    @Override
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
                    s_logger.log( Level.INFO, m_edgename + " switched to green" );
                    m_current = m_green;
                    break;


                case GREEN: m_state = ELightState.RED;
                    s_logger.log( Level.INFO, m_edgename + " switched to red" );
                    m_current = m_red;
                    break;

                default: break;
            }
        }
    }
}
