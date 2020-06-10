package org.socialcars.sinziana.pfara.units;

public class CUnits implements IUnits
{
    private final double m_timestep;
    private final double m_block;
    private final double m_blocktimesteps;

    /**
     * ctor
     *
     * @param p_time time given by runtime
     * @param p_space space given by runtime
     *
     */
    public CUnits( final Number p_time, final Number p_space )
    {
        m_timestep = p_time.doubleValue();
        m_block = p_space.doubleValue();
        m_blocktimesteps = m_block / m_timestep;

    }

    /**
     * transforms real-life distance (meters/m) in simulation distance (blocks)
     * @param p_dist RL distance
     * @return simulation distance
     */
    @Override
    public final Number distanceToBlocks( final Number p_dist )
    {
        return p_dist.doubleValue() / m_block;
    }

    /**
     * transforms simulation distance (blocks) into real life distance (meters)
     * @param p_blocks number of blocks
     * @return corresponding meters
     */
    @Override
    public final Number blockToDistance( final Number p_blocks )
    {
        return p_blocks.doubleValue() * m_block;
    }

    /**
     * the distance covered in one timestep at the given speed
     * @param p_speed speed
     * @return distance covered
     */
    @Override
    public final Number speedToDistance( final Number p_speed )
    {
        return p_speed.doubleValue() * m_timestep;
    }

    /**
     * returns the simulations distance unit of measure
     * @return block
     */
    public final Number getBlock()
    {
        return m_block;
    }

    /**
     * transforms acceleration to speed for one time step
     * @param p_accel the acceleration/deceleration given
     * @return the resulting speed
     */
    @Override
    public final Number accelerationToSpeed( final Number p_accel )
    {
        return p_accel.doubleValue() * m_timestep;
    }

    /**
     * transforms given distance into speed
     * @param p_distance distance
     * @return speed
     */
    @Override
    public final Number distanceToSpeed( final Number p_distance )
    {
        return p_distance.doubleValue() / m_timestep;
    }

    /**
     * blocks/timestep covered at given speed
     * @param p_speed speed
     * @return number of blocks/timestep
     */
    @Override
    public final Number speedToBlocks( final Number p_speed )
    {
        return p_speed.doubleValue() * m_blocktimesteps;
    }

    /**
     * returns the speed constant of the simulation environment
     * @return blockstimeteps
     */
    public final Number getBlockTimesteps()
    {
        return m_blocktimesteps;
    }

    /**
     * transforms steps into seconds
     * @param p_steps steps
     * @return time is seconds
     */
    @Override
    public final Number stepsToTimesteps( final Number p_steps )
    {
        return p_steps.doubleValue() * m_timestep;
    }

    /**
     * returns simulations time unit
     * @return timestep
     */
    public final Number getTimestep()
    {
        return m_timestep;
    }

    /**
     * gives the new position based on old one and speed
     * @param p_oldposition old position
     * @param p_speed speed
     * @return new position
     */
    @Override
    public final Number newPosition( final Number p_oldposition, final Number p_speed )
    {
        return p_oldposition.doubleValue() + this.speedToBlocks( p_speed ).doubleValue();
    }
}
