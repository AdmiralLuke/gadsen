package com.gats.simulation.action;

/**
 * Repräsentiert die Zusammenfassung aller UI-relevanten {@link Action Ereignisse},
 * die während der Simulation eines einzelnen Spielzuges aufgetreten sind
 */
public class ActionLog {
    // alle ActionLogs die übergeben wurden sind abgearbeitet


    private Action rootAction;


    public ActionLog(Action rootAction) {
        this.rootAction = rootAction;
    }


    public Action getRootAction() {
        return rootAction;
    }

}
