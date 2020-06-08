package org.socialcars.sinziana.pfara.environment;

public interface IStoplight
{
    /**
     * state
     * @return the current state
     */
    ELightState state();

    /**
     * time left
     * @return how many timesteps untill light changes
     */
    Integer timeLeft();
}
