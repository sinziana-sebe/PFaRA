package org.socialcars.sinziana.pfara.environment;

import org.socialcars.sinziana.pfara.data.input.CCoordinatespojo;

/**
 * the coordinate class used for both synthetic and real coordinate systems
 */
public class CCoordinates implements ICoordinates
{
    private final Double m_latitude;
    private final Double m_longitude;

    /**
     * constructor using the jsonschema defined
     * used to create a coordinate from file
     * @param p_pojo the plain old java object
     */
    public CCoordinates( final CCoordinatespojo p_pojo )
    {
        m_latitude = p_pojo.getLat();
        m_longitude = p_pojo.getLon();
    }

    /**
     * returns the latitude, or X-axis coordinate
     * @return latidude/ X-axis
     */
    public Double latitude()
    {
        return m_latitude;
    }

    /**
     * returns the longitude, or Y-axis coordinate
     * @return longitude / Y-axis
     */
    public Double longitude()
    {
        return m_longitude;
    }
}
