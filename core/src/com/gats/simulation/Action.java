package com.gats.simulation;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Repräsentiert ein einzelnes UI-relevantes Ereignis, das während der Simulation aufgetreten ist
 */
public abstract class Action {
    private float delay = 0;
    private ArrayList<Action> arrayList = new ArrayList<>();

    public Action(float delay) {
        this.delay = delay;
    }

    public float getDelay() {
        return this.delay;
    }


    public ArrayList<Action> getChildren() {
        return this.arrayList;
    }

    protected void addChild(Action a) {
        this.arrayList.add(a);
    }

    public Iterator<Action> iterator() {
        return this.arrayList.iterator();
    }

}
