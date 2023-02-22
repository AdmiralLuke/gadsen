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

    void addAction(Action action) {
        this.lastAddedAction.getChildren().add(action);
    }

    void goToLast() {
        lastAddedAction = lastAddedAction.getChildren().get(lastAddedAction.getChildren().size() - 1);
    }

    void returnToRoot() {
        this.lastAddedAction = rootAction;
    }

    void goToNextAction() {
        if (lastAddedAction == rootAction) {
            return;
        }
        this.lastAddedAction = lastAddedAction.getChildren().get(0);
    }

    void addActions(ActionLog log) {
        this.lastAddedAction.getChildren().add(log.rootAction);
    }

    public Action getRootAction() {
        return rootAction;
    }

    @Override
    public String toString() {
        String output = "ActionLog\n";

        output += rootAction.toString() +"\n";
        for (Action action: lastAddedAction.getChildren()) {
           output += " "+action.toString()+"\n";
        }

        return output+="EndLog\n";
    }
}
