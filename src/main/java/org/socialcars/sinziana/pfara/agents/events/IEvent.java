package org.socialcars.sinziana.pfara.agents.events;

import org.socialcars.sinziana.pfara.agents.IDynamic;

import java.util.Collection;

/**
 * the event interface
 */
public interface IEvent
{
    /**
     * the subject of the event
     * @return a dynamic agent
     */
    IDynamic who();

    /**
     * the type of event
     * @return type
     */
    EEventType what();

    /**
     * location of the event
     * @return the name of the location
     */
    String where();

    /**
     * in case of platooning, specifies the partners
     * @return a collection of other dynamic agents
     */
    Collection<IDynamic> with();

    /**
     * the time that the event occured
     * @return timestep
     */
    Integer when();
}
