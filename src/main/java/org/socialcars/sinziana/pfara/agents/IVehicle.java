package org.socialcars.sinziana.pfara.agents;

import org.socialcars.sinziana.pfara.environment.INode;

import java.util.ArrayList;

public interface IVehicle extends IDynamic
{

    /**
     * the necessary actions for the formation of a platoon
     * @param p_position the current node
     * @param p_timestep the current timestep
     * @param p_platoon the co-platooners
     */
    void formed( final INode p_position, final Integer p_timestep, final ArrayList<CVehicle> p_platoon );


    /**
     * the necessary actions for the splitting from a platoon
     * @param p_position the current node
     * @param p_timestep the current timestep
     */
    void split( final INode p_position, final Integer p_timestep );

}
