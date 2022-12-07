package com.gats.manager;

import com.gats.simulation.GameCharacter;
import com.gats.simulation.GameCharacterController;
import com.gats.simulation.GameState;

public abstract class Player {

    enum PlayerType{
        Human,
        AI
    }

    /**
     * @return Der (Anzeige-) Name des Spielers
     */
    protected abstract String getName();

    /**
     * Wird vor Beginn des Spiels aufgerufen. Besitzt eine sehr hohe maximale Berechnungszeit von TBA ms.
     * Diese Funktion kann daher verwendet werden, um Variablen zu initialisieren und
     * einmalig, sehr rechenaufwändige Operationen durchzuführen.
     * @param state Der {@link GameState Spielzustand} zu Beginn des Spiels
     */
    protected abstract void init(GameState state);

    /**
     * Wird aufgerufen, wenn der Spieler einen Zug für einen seiner Charaktere durchführen soll.
     * Der {@link GameState Spielzustand} state reflektiert dabei den Zug des Spielers ohne Verzögerung.
     * Der Controller ermöglicht die Steuerung des Charakters, welcher am Zug ist.
     * Die übergebene Controller-Instanz deaktiviert sich nach Ende des Zuges permanent.
     * @param state Der {@link GameState Spielzustand} während des Zuges
     * @param characterController Der {@link GameCharacterController Controller}, Charakter gehört
     */
    protected abstract void executeTurn(GameState state, Controller controller);

    /**
     * Wird für interne Zwecke verwendet und besitzt keine Relevanz für die Bot-Entwicklung.
     * @return What kind of implementation the Player is
     */
    protected abstract PlayerType getType();

}
