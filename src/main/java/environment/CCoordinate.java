package environment;

import org.socialcars.sinziana.simulation.data.input.CCoordinatepojo;

/**
 * the coordinate class used for both synthetic and real coordinate systems
 */
public class CCoordinate implements ICoordinate
{
    private final Double m_latitude;
    private final Double m_longitude;

    /**
     * simple constructor with given latitude and longitude
     * @param p_latitude latitude
     * @param p_longitude longitude
     */
    public  CCoordinate( final Double p_latitude, final Double p_longitude )
    {
        m_latitude = p_latitude;
        m_longitude = p_longitude;
    }

    /**
     * constructor using the jsonschema defined
     * used to create a coordinate from file
     * @param p_pojo the plain old java object
     */
    public CCoordinate( final CCoordinatepojo p_pojo )
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
