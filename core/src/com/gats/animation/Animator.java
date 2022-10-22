package com.gats.animation;

import com.gats.simulation.ActionLog;
import com.gats.simulation.GameState;

/**
 * Kernklasse für die Visualisierung des Spielgeschehens.
 * Übersetzt {@link com.gats.simulation.GameState GameState} und {@link com.gats.simulation.ActionLog ActionLog}
 * des {@link com.gats.simulation Simulation-Package} in für libGDX renderbare Objekte
 */
public class Animator {

    private EntityGroup root;

    /**
     * Setzt eine Welt basierend auf den Daten in state auf und bereitet diese für nachfolgende Animationen vor
     * @param state Anfangszustand der Welt in der Simulation
     */
    public Animator(GameState state){

    }

    /**
     * Animiert die in log enthaltenen Ereignisse
     * @param log Queue aller {@link com.gats.simulation.Action animations-relevanten Ereignisse}
     */
    public void animate(ActionLog log){

    }


}
