package org.socialcars.sinziana.pfara.environment;

import org.socialcars.sinziana.pfara.data.input.CNodepojo;

/**
 * the node class
 */
public class CNode implements INode
{
    private final String m_name;
    private final CCoordinates m_coordinates;

    /**
     * ctor with given input
     * @param p_name name
     * @param p_coordinates coordinates object
     */
    public CNode( final String p_name, final CCoordinates p_coordinates )
    {
        m_name = p_name;
        m_coordinates = p_coordinates;
    }

    /**
     * ctor
     * @param p_pojo pojo object read from file
     */
    public CNode( final CNodepojo p_pojo )
    {
        m_name = p_pojo.getName();
        m_coordinates = new CCoordinates( p_pojo.getCoordinates() );
    }

    /**
     * name
     * @return the name of the node
     */
    @Override
    public String name()
    {
        return m_name;
    }

    /**
     * coordinates
     * @return the coordinates for the node
     */
    @Override
    public CCoordinates coordinates()
    {
        return m_coordinates;
    }
}
