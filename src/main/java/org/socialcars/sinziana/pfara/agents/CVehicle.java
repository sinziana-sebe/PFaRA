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

package org.socialcars.sinziana.pfara.agents;

import org.socialcars.sinziana.pfara.agents.events.CEvent;
import org.socialcars.sinziana.pfara.agents.events.EEventType;
import org.socialcars.sinziana.pfara.agents.events.IEvent;
import org.socialcars.sinziana.pfara.agents.proprieties.CUtility;
import org.socialcars.sinziana.pfara.data.input.CVehiclepojo;
import org.socialcars.sinziana.pfara.environment.IEdge;
import org.socialcars.sinziana.pfara.negotiation.CInitialOffer;
import org.socialcars.sinziana.pfara.negotiation.CNegotiationModule;
import org.socialcars.sinziana.pfara.negotiation.CSimpleOffer;
import org.socialcars.sinziana.pfara.negotiation.INegotiationModule;
import org.socialcars.sinziana.pfara.negotiation.IOffer;
import org.socialcars.sinziana.pfara.negotiation.IProtocol;
import org.socialcars.sinziana.pfara.negotiation.events.CNegotiationEvent;
import org.socialcars.sinziana.pfara.negotiation.events.ENegotiationEventType;
import org.socialcars.sinziana.pfara.negotiation.events.INegotiationEvent;
import org.socialcars.sinziana.pfara.units.CUnits;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * the vehicle class
 */
public class CVehicle implements IVehicle
{
    private static Logger s_logger;

    private final String m_name;

    private final String m_origin;
    private final String m_destination;
    private final ArrayList<String> m_midpoints;

    private Double m_speed;
    private Double m_acceleration;
    private Double m_position;
    private String m_location;
    private IEdge m_lastedge;

    private CUtility m_utility;
    private CPreference m_preference;
    private CUnits m_unit;

    private final ArrayList<IEvent> m_events;

    private Boolean m_platooning = false;
    private ArrayList<CVehicle> m_companions;

    private Integer m_precedence;
    private Integer m_delay;

    private Double m_cost;
    private Double m_routelength;
    private Integer m_timelastarrival;
    private Integer m_departuretime;


    private Boolean m_negotiating;
    private INegotiationModule m_negmodule;
    private final ArrayList<INegotiationEvent> m_negevents;
    private IProtocol m_protocol;

    private final Boolean m_mikro;
    private final Double m_omega;



    /**
     * ctor
     * @param p_pojo plain old java object
     * @param p_timestep the current time
     * @param p_log the logger
     * @param p_unit the unit object
     * @param p_mikro movement type, true if microscopic, false for macroscopic
     * @param p_omega the subsidisation coefficient
     */
    public CVehicle( final CVehiclepojo p_pojo, final Integer p_timestep, final Logger p_log, final CUnits p_unit, final Boolean p_mikro, final Double p_omega )
    {
        s_logger = p_log;

        m_name = p_pojo.getName();
        m_origin = p_pojo.getStart();
        m_destination = p_pojo.getFinish();
        m_midpoints = new ArrayList<>();
        p_pojo.getMiddle().forEach( p -> m_midpoints.add( p ) );

        m_speed = 0.0;
        m_acceleration = 0.0;
        m_position = 0.0;
        m_location = m_origin;
        m_routelength = 0.0;
        m_cost = 0.0;
        m_precedence = 0;
        m_delay = 0;

        m_companions = new ArrayList<>();
        m_preference = new CPreference( p_pojo.getPreference() );
        m_utility = new CUtility( p_pojo.getUtility() );
        m_unit = p_unit;

        final CEvent l_created = new CEvent( this, EEventType.CREATED, m_origin, p_timestep, null );
        s_logger.log( Level.INFO, l_created.toString() );
        m_events = new ArrayList<>();
        m_events.add( l_created );

        m_negevents = new ArrayList<>();

        m_mikro = p_mikro;
        m_omega = p_omega;
    }

    /**
     * the name of the vehicle
     * @return the string with the name
     */
    @Override
    public String name()
    {
        return m_name;
    }

    /**
     * the current location of the agent
     * @return the name of the node or edge
     */
    @Override
    public String location()
    {
        return m_location;
    }

    /**
     * the name of the origin node
     * @return the origin node name
     */
    @Override
    public String origin()
    {
        return m_origin;
    }

    /**
     * the name of the destination node
     * @return the destination node name
     */
    @Override
    public String destination()
    {
        return m_destination;
    }

    /**
     * the position of an agent on the current edge
     * @return the distance already travelled on the current edge
     */
    @Override
    public Number position()
    {
        return m_position;
    }

    /**
     * preferences
     * @return the vehicle's preferences
     */
    @Override
    public CPreference preferences()
    {
        return m_preference;
    }

    /**
     * utility
     * @return the vehicle's utility function
     */
    @Override
    public CUtility utility()
    {
        return m_utility;
    }

    /**
     * moves the vehicle in a microscopic fashion
     * based on the vehicle's acceleration and current speed
     */
    @Override
    public void moveMikro()
    {
        if (  m_speed < m_preference.maxSpeed() )
        {
            m_acceleration = m_preference.maxAccel();
            m_speed = m_speed + m_unit.accelerationToSpeed( m_acceleration ).doubleValue();
            m_position = m_position + m_unit.speedToBlocks( m_speed ).doubleValue();
        }
        else if ( m_speed > m_preference.maxSpeed() )
        {
            m_acceleration = Math.abs( m_acceleration - m_preference.maxDecel() );
            m_speed =  m_speed - m_unit.accelerationToSpeed( m_preference.maxDecel() ).doubleValue();
            m_position = m_position + m_unit.speedToBlocks( m_speed ).doubleValue();
        }
        else
        {
            m_position = m_position + m_unit.speedToBlocks( m_speed ).doubleValue();
        }
    }

    /**
     * performs the braking action
     * based on the vehicles deceleration and current speed
     */
    @Override
    public void brake()
    {
        if ( m_speed > 0 )
        {
            m_acceleration = m_acceleration - m_preference.maxDecel();
            m_speed =  m_speed - Math.abs( m_unit.accelerationToSpeed( m_preference.maxDecel() ).doubleValue() );
        }
        m_position = m_position + Math.abs( m_unit.speedToBlocks( m_speed ).doubleValue() );
    }

    /**
     * moves the vehicle in a macroscopic fashion, based on the possible speed of the current edge
     * @param p_speed the possible speed
     */
    @Override
    public void moveMakro( final Double p_speed )
    {
        if ( p_speed > m_preference.maxSpeed() ) m_speed = m_preference.maxSpeed();
        else m_speed = p_speed;
        m_position = m_position + m_unit.speedToBlocks( m_speed ).doubleValue();
    }

    /**
     * performs the necessary actions when departing a node
     * @param p_position the current edge
     * @param p_timestep the current timestep
     */
    @Override
    public void departed( final IEdge p_position, final Integer p_timestep )
    {
        final CEvent l_departed = new CEvent( this, EEventType.DEPARTED, p_position.from().name(), p_timestep, null );
        m_events.add( l_departed );
        s_logger.log( Level.INFO, l_departed.toString() );

        m_location = p_position.name();
        m_lastedge = p_position;

        if ( m_departuretime == null ) m_departuretime = p_timestep;
        if ( m_timelastarrival == null ) m_timelastarrival = p_timestep;
    }

    /**
     * performs the necessary actions upon arrival at a node
     * @param p_position the current edge
     * @param p_timestep the current timestep
     */
    @Override
    public void arrived( final IEdge p_position, final Integer p_timestep )
    {
        final CEvent l_arrived = new CEvent( this, EEventType.ARRIVED, p_position.to().name(), p_timestep, null );
        m_events.add( l_arrived );
        s_logger.log( Level.INFO, l_arrived.toString() );

        m_location = p_position.to().name();
        m_position = 0.0;
        m_preference.reduceMaxLength( p_position.length() );
        m_routelength += p_position.length();

        if ( m_companions.size() > 0 )
        {
            // d_e/nvp + d_e/omega
            final Double l_cost = p_position.weight().doubleValue() / ( m_companions.size() + 1 ) + p_position.weight().doubleValue() / m_omega;
            m_cost = m_cost + l_cost;
            m_preference.reduceMaxCost( l_cost );
        }
        else
        {
            m_cost = m_cost + ( p_position.weight().doubleValue() );
            m_preference.reduceMaxCost( p_position.weight().doubleValue() );
        }

        m_preference.reduceMaxTime( p_timestep - m_timelastarrival );
        m_timelastarrival = p_timestep;
    }

    /**
     * performs the necessary actions upon travel completion
     * @param p_position the current node
     * @param p_timestep the current time-step
     */
    @Override
    public void completed( final String p_position, final Integer p_timestep )
    {
        final CEvent l_completed = new CEvent( this, EEventType.COMPLETED, p_position, p_timestep, null );
        m_events.add( l_completed );
        s_logger.log( Level.INFO, l_completed.toString() );
        s_logger.log( Level.INFO, m_name + " cost: " + m_cost + ", " + m_routelength + " blocks" );
        s_logger.log( Level.INFO, m_name + " has an allowance of " + m_preference.lengthLimit() + " blocks, "
                + m_preference.timeLimit() + " timesteps and  " + m_preference.maxCost() + " cost" );
    }


    /**
     * the necessary actions for the formation of a platoon
     * @param p_position the current node name
     * @param p_timestep the current timestep
     * @param p_platoon the co-platooners
     */
    @Override
    public void formed( final String p_position, final Integer p_timestep, final ArrayList<CVehicle> p_platoon )
    {
        final Collection<IDynamic> l_plat = new ArrayList<>( p_platoon );
        m_companions = p_platoon;
        m_precedence = m_companions.size() + 1;
        final CEvent l_formed = new CEvent( this, EEventType.FORMED, p_position, p_timestep, l_plat );
        m_events.add( l_formed );
        m_platooning = true;
        s_logger.log( Level.INFO, l_formed.toString() );
    }

    /**
     * the necessary actions for splitting from a platoon
     * @param p_position the current node
     * @param p_timestep the current timestep
     */
    @Override
    public void split( final String p_position, final Integer p_timestep )
    {
        final CEvent l_split = new CEvent( this, EEventType.SPLIT, p_position, p_timestep, null );
        m_events.add( l_split );
        m_platooning = false;
        m_precedence = 0;
        m_companions.removeAll( m_companions );
        s_logger.log( Level.INFO, l_split.toString() );
    }

    /**
     * whether the vehicle is currently platooning or not
     * @return true if it is and false if not
     */
    @Override
    public Boolean platooning()
    {
        return m_platooning;
    }

    /**
     * companions
     * @return the co-platooning vehicles
     */
    @Override
    public ArrayList<CVehicle> companions()
    {
        return m_companions;
    }

    /**
     * sets the stoplight delay based on the number of co-platooners
     * @param p_maxdelay the maximum possible delay (how many seconds of red there are left)
     */
    @Override
    public void setDelay( final Integer p_maxdelay )
    {
        m_delay = p_maxdelay * m_unit.getTimestep().intValue() - m_precedence;
        m_speed = 0.0;
        m_acceleration = 0.0;
    }

    /**
     * decreases the precedence at stoplight
     * occurs when co-platooners split from the formation
     */
    @Override
    public void updatePrecedence()
    {
        m_precedence--;
    }

    /**
     * current delay at stoplight
     * @return the delay
     */
    @Override
    public Integer getDelay()
    {
        return m_delay;
    }

    /**
     * decreases the delay at stoplights
     */
    @Override
    public void updateDelay()
    {
        m_delay--;
    }

    /**
     * joins a negotiating party
     * @param p_protocol the protocol for communication
     */
    @Override
    public void joinParty( final IProtocol p_protocol )
    {
        m_negotiating = true;
        m_negmodule = new CNegotiationModule( p_protocol, m_utility, m_unit, m_preference, m_mikro );
        m_protocol = p_protocol;
        if ( m_speed == 0.0 ) m_speed = m_preference.maxSpeed();

        final CNegotiationEvent l_join = new CNegotiationEvent( this, ENegotiationEventType.JOINED, null );
        m_negevents.add( l_join );
        s_logger.log( Level.INFO, l_join.toString() + " protocol " + p_protocol.getNodeID().name() );
    }

    /**
     * releases the vehicle from the negotiation party
     * @param p_protocol the protocol for communication
     */
    @Override
    public void release( final IProtocol p_protocol )
    {
        m_negotiating = false;
        m_negmodule = null;
        final CNegotiationEvent l_leave = new CNegotiationEvent( this, ENegotiationEventType.LEFT, null );
        s_logger.log( Level.INFO, l_leave.toString() );
    }

    /**
     * the necessary actions when receiving an offer
     * @param p_offer the offer
     * @param p_oldroute the old routes
     * @throws IOException file write
     */
    @Override
    public void receiveOffer( final IOffer p_offer, final List<IEdge> p_oldroute ) throws IOException
    {
        final CNegotiationEvent l_getoffer = new CNegotiationEvent( this, ENegotiationEventType.RECEIVED, p_offer );
        m_negevents.add( l_getoffer );
        s_logger.log( Level.INFO, l_getoffer.toString() );
        final String l_response = m_negmodule.receiveOffer( p_offer, p_oldroute, m_speed );
        switch ( l_response )
        {
            case "haggle":
                writeHaggle( new CSimpleOffer( p_offer.id(), m_negmodule.alternativeRouteCost() ) );
                m_protocol.haggle( this, new CSimpleOffer( p_offer.id(), m_negmodule.alternativeRouteCost() ) );
                break;
            case "accept":
                writeAccept( p_offer );
                m_protocol.receiveAccept( this, p_offer );
                break;
            case "reject":
                writeReject( p_offer );
                m_protocol.receiveReject(  p_offer );
                break;
            default:
                m_protocol.receiveBreakaway( p_offer );
                break;
        }
    }

    /**
     * the necessary actions when sending an offer
     * @param p_route the route in the offer
     */
    @Override
    public void sendOffer( final List<IEdge> p_route )
    {
        final CInitialOffer l_offer = m_negmodule.sendOffer( p_route, m_name );
        final CNegotiationEvent l_newoffer = new CNegotiationEvent( this, ENegotiationEventType.SENT, l_offer );
        m_negevents.add( l_newoffer );
        s_logger.log( Level.INFO, l_newoffer.toString() );
        m_protocol.sendOffer( this, l_offer );
    }

    /**
     * the necessary actions for haggling
     * @param p_offer the offer
     * @throws IOException file write
     */
    @Override
    public void haggle( final CSimpleOffer p_offer ) throws IOException
    {
        final CNegotiationEvent l_getoffer = new CNegotiationEvent( this, ENegotiationEventType.RECEIVED, p_offer );
        m_negevents.add( l_getoffer );
        s_logger.log( Level.INFO, l_getoffer.toString() );
        final String l_result = m_negmodule.haggle( p_offer );
        switch ( l_result )
        {
            case "haggle":
                writeHaggle( p_offer );
                m_protocol.haggle( this, p_offer );
                break;
            case "accept":
                writeAccept( p_offer );
                m_protocol.receiveAccept( this, p_offer );
                break;
            case "reject":
                writeReject( p_offer );
                m_protocol.receiveReject(  p_offer );
                break;
            default:
                m_protocol.receiveBreakaway( p_offer );
                break;
        }
    }

    /**
     * updates the cost to factor in the payment/reception of a buyout
     * after the successful completion of a negotiation
     * @param p_cost the cost of the buyout
     */
    @Override
    public void acceptUpdateCost( final Double p_cost )
    {
        switch ( m_negmodule.role() )
        {
            case INITIATOR:
                m_cost += p_cost;
                m_preference.reduceMaxCost( p_cost );
                break;
            case ACCEPTOR:
                m_cost -= p_cost;
                m_preference.reduceMaxCost( -p_cost );
                break;
            default:
        }
    }


    /**
     * writes an accept event
     * @param p_offer the offer accepted
     * @throws IOException write file
     */
    private void writeAccept( final IOffer p_offer ) throws IOException
    {
        final CNegotiationEvent l_accept = new CNegotiationEvent( this, ENegotiationEventType.ACCEPTED, p_offer );
        m_negevents.add( l_accept );
        s_logger.log( Level.INFO, l_accept.toString() );
    }

    /**
     * writes a reject event
     * @param p_offer the offer rejected
     * @throws IOException write file
     */
    private void writeReject( final IOffer p_offer ) throws IOException
    {
        final CNegotiationEvent l_reject = new CNegotiationEvent( this, ENegotiationEventType.REJECTED, p_offer );
        m_negevents.add( l_reject );
        s_logger.log( Level.INFO, l_reject.toString() );
    }

    /**
     * writes a haggle event
     * @param p_offer the new haggle offer
     * @throws IOException write file
     */
    private void writeHaggle( final IOffer p_offer ) throws IOException
    {
        final CNegotiationEvent l_getoffer = new CNegotiationEvent( this, ENegotiationEventType.SENT, p_offer );
        m_negevents.add( l_getoffer );
        s_logger.log( Level.INFO, l_getoffer.toString() );
    }

    /**
     * last edge
     * @return the last edge travelled by the vehicle
     */
    public IEdge getLastEdge()
    {
        return m_lastedge;
    }

    /**
     * the vehicles's events
     * @return the list of events
     */
    public ArrayList<IEvent> events()
    {
        return m_events;
    }

    /**
     * the cost of the route so far
     * @return the cost
     */
    public Double routeCost()
    {
        return m_cost;
    }

    /**
     * the length of the route so far
     * @return the length
     */
    public Double routeLength()
    {
        return m_routelength;
    }

    /**
     * the duration of the route so far
     * @return the duration
     */
    public Integer routeDuration()
    {
        return m_timelastarrival - m_departuretime;
    }

    /**
     * calculates the utility at the end of the route
     * @param p_mikro movement type, true for micro, false for macro
     * @return the utility value
     */
    public Double endUtility( final Boolean p_mikro )
    {
        if ( p_mikro ) return m_utility.calculateMikroFinal( routeDuration(), m_routelength );
        else return m_utility.calculateMakroFinal( m_routelength, m_cost );
    }

}
