package com.gats.simulation;

/**
 * Repräsentiert ein einzelnes UI-relevantes Ereignis, das während der Simulation aufgetreten ist
 */
public abstract class Action {
    private long delay = 0;



    public long getDelay() {
        return delay;
    }
}
