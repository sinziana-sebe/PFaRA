package org.socialcars.sinziana.pfara.agents;

import org.socialcars.sinziana.pfara.agents.events.CEvent;
import org.socialcars.sinziana.pfara.agents.events.EEventType;
import org.socialcars.sinziana.pfara.agents.events.IEvent;
import org.socialcars.sinziana.pfara.agents.proprieties.CUtility;
import org.socialcars.sinziana.pfara.data.input.CVehiclepojo;
import org.socialcars.sinziana.pfara.environment.IEdge;
import org.socialcars.sinziana.pfara.environment.INode;
import org.socialcars.sinziana.pfara.units.CUnits;

import java.util.ArrayList;
import java.util.Collection;
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

    private Double m_cost;
    private Double m_routelength;
    private Integer m_timelastarrival;


    /**
     * ctor
     * @param p_pojo plain old java object
     * @param p_timestep the cuurent time
     * @param p_outputfile the output filename
     * @param p_unit the unit object
     */
    public CVehicle( final CVehiclepojo p_pojo, final Integer p_timestep, final String p_outputfile, final CUnits p_unit )
    {
        m_name = p_pojo.getName();
        m_origin = p_pojo.getStart();
        m_destination = p_pojo.getFinish();
        m_midpoints = new ArrayList<>();
        p_pojo.getMiddle().forEach( p -> m_midpoints.add( p ) );

        m_speed = 0.0;
        m_acceleration = 0.0;
        m_position = 0.0;
        m_location = m_origin;

        m_companions = new ArrayList<>();
        m_preference = new CPreference( p_pojo.getPreference() );
        m_utility = new CUtility( p_pojo.getUtility() );
        m_unit = p_unit;

        final CEvent l_created = new CEvent( this, EEventType.CREATED, m_origin, p_timestep, null );
        m_events = new ArrayList<>();
        m_events.add( l_created );
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
     * the current location of the vehicle
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
     * the position of a vehicle on the current edge
     * @return the distance already travelled on the current edge
     */
    @Override
    public Number position()
    {
        return m_position;
    }

    /**
     * moves the vehicle in a microscopic fashion
     * based on the vehicles acceleration, current speed
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
            m_acceleration = m_acceleration - m_preference.maxDecel();
            m_speed =  m_speed + m_unit.accelerationToSpeed( m_acceleration ).doubleValue();
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
            m_speed =  m_speed - Math.abs( m_unit.accelerationToSpeed( m_acceleration ).doubleValue() );
        }
        m_position = m_position + Math.abs( m_unit.speedToBlocks( m_speed ).doubleValue() );
    }

    /**
     * moves the vehicle in a macroscopic fashion, based on the median speed of the current edge
     * @param p_speed the median speed
     */
    @Override
    public void moveMakro( final Double p_speed )
    {
        if ( p_speed > m_preference.maxSpeed() ) m_speed = m_preference.maxSpeed();
        else m_speed = p_speed;
        m_position = m_position + m_unit.speedToBlocks( m_speed ).doubleValue();
    }

    /**
     * the necessary actions for departing a node
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
    }

    /**
     * the necessary actions uppon arrival
     * @param p_position the current edge
     * @param p_timestep the current timestep
     */
    @Override
    public void arrived( final IEdge p_position, final Integer p_timestep )
    {
        final CEvent l_arrived = new CEvent( this, EEventType.ARRIVED, p_position.to().name(), p_timestep, null );
        m_events.add( l_arrived );
        m_location = p_position.to().name();
        s_logger.log( Level.INFO, l_arrived.toString() );
        m_position = 0.0;
        m_preference.reduceMaxLength( p_position.length() );
        m_routelength += p_position.length();
        if ( m_companions.size() > 0 )
        {
            m_cost = m_cost + ( p_position.weight().doubleValue() + ( p_position.weight().doubleValue() / 3 * ( m_companions.size() + 1 ) ) ) / ( m_companions.size() + 1 );
            m_preference.reduceMaxCost( ( p_position.weight().doubleValue() + ( p_position.weight().doubleValue() / 3 * ( m_companions.size() + 1 ) ) )
                    / ( m_companions.size() + 1 ) );
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
     * the necessary actions uppon travel completion
     * @param p_position the current node
     * @param p_timestep the current time-step
     */
    @Override
    public void completed( final INode p_position, final Integer p_timestep )
    {
        final CEvent l_completed = new CEvent( this, EEventType.COMPLETED, p_position.name(), p_timestep, null );
        m_events.add( l_completed );
        s_logger.log( Level.INFO, l_completed.toString() );
        s_logger.log( Level.INFO, m_name + " cost: " + m_cost + ", " + m_routelength + " blocks" );
        s_logger.log( Level.INFO, m_name + " has an allowance of " + m_preference.lengthLimit() + " blocks, "
                + m_preference.timeLimit() + " timesteps and  " + m_preference.maxCost() + " cost" );
    }


    /**
     * the necessary actions for the formation of a platoon
     * @param p_position the current node
     * @param p_timestep the current timestep
     * @param p_platoon the co-platooners
     */
    @Override
    public void formed( final INode p_position, final Integer p_timestep, final ArrayList<CVehicle> p_platoon )
    {
        final Collection<IDynamic> l_plat = new ArrayList<>( p_platoon );
        m_companions = p_platoon;
        //m_precedence = m_companions.size() + 1;
        final CEvent l_formed = new CEvent( this, EEventType.FORMED, p_position.name(), p_timestep, l_plat );
        m_events.add( l_formed );
        m_platooning = true;
        s_logger.log( Level.INFO, l_formed.toString() );
    }

    /**
     * the necessary actions for the splitting from a platoon
     * @param p_position the current node
     * @param p_timestep the current timestep
     */
    @Override
    public void split( final INode p_position, final Integer p_timestep )
    {
        final CEvent l_split = new CEvent( this, EEventType.SPLIT, p_position.name(), p_timestep, null );
        m_events.add( l_split );
        m_platooning = false;
        m_companions.removeAll( m_companions );
        s_logger.log( Level.INFO, l_split.toString() );
    }
}
