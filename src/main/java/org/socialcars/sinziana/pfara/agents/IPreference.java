package org.socialcars.sinziana.pfara.agents;

public interface IPreference
{
    /**
     * the vehicles minimum speed
     * @return minspeed
     */
    Double minSpeed();

    /**
     * the vehicles maximum speed
     * @return maxspeed
     */
    Double maxSpeed();

    /**
     * the vehicles maximum acceleration
     * @return maxaccel
     */
    Double maxAccel();

    /**
     * the vehicles maximum deceleration
     * @return maxdecel
     */
    Double maxDecel();

    /**
     * the vehicles upper limit on travel time
     * @return time limit
     */
    Integer timeLimit();

    /**
     * the vehicles upper limit on travel distance
     * @return length limit
     */
    Double lengthLimit();

    /**
     * the vehicles upper limit on travel cost
     * @return maximum cost
     */
    Double maxCost();
}
