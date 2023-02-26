package com.gats.simulation;
import java.util.ArrayDeque;

/**
 * Repräsentiert die Zusammenfassung aller UI-relevanten {@link Action Ereignisse},
 * die während der Simulation eines einzelnen Spielzuges aufgetreten sind
 */
public class ActionLog {
    // alle ActionLogs die übergeben wurden sind abgearbeitet


    private Action rootAction;
    Action lastAddedAction;


    ActionLog(Action rootAction) {
        this.rootAction = rootAction;
        this.lastAddedAction = rootAction;
    }


    public Action getRootAction() {
        return rootAction;
    }

}
