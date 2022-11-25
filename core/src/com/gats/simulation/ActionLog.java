package com.gats.simulation;
import java.util.ArrayDeque;

/**
 * Repräsentiert die Zusammenfassung aller UI-relevanten {@link Action Erignisse},
 * die während der Simulation eines einzelnen Spielzuges aufgetreten sind
 */
public class ActionLog {
    private ArrayDeque<Action> actions;

    ActionLog() {
        actions = new ArrayDeque<Action>(1024);
    }

    void addAction(Action action) {
        this.actions.addLast(action);
    }

    void addActions(ActionLog log) {
        this.actions.addAll(log.actions);
    }

    public Action getNextAction() {
        return actions.poll();
    }

    public void removeNextAction() {
        actions.pop();
    }

}
