package environment;

public class CCoordinate implements ICoordinate
{
    final Double m_latitude;
    final Double m_longitude;

    public  CCoordinate( final Double p_latitude, final Double p_longitude )
    {
        m_latitude = p_latitude;
        m_longitude = p_longitude;
    }

    public Double latitude()
    {
        return m_latitude;
    }

    public Double longitude()
    {
        return m_longitude;
    }
}
