package org.socialcars.sinziana.pfara.environment;

public interface IEdge
{
    /**
     * name of the edge
     * @return the edge's name
     */
    String name();

    /**
     * origin node
     * @return the origin node
     */
    INode from();

    /**
     * end node
     * @return the end node
     */
    INode to();

    /**
     * weigh of the edge
     * @return the edge's weight
     */
    Double weight();

    /**
     * length of the edge
     * @return the edge's length
     */
    Double length();

    /**
     * adds a stoplight to this edge
     * @param p_light the stoplight object
     */
    void addStoplight( final CStoplight p_light );

    /**
     * the stoplight
     * @return the stoplight partaining to the edge
     */
    IStoplight stoplight();

}
