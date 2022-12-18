package com.gats.manager;

/**
 * Superklasse für alle Bot-Implementationen.
 * Erbt von dieser Klasse, wenn ihr einen Bot implementieren wollt.
 */
public abstract class Bot extends Player{

    /**
     * @return Name des Bot-Authors (Euer vollständiger Name)
     */
    public abstract String getStudentName();

    /**
     * @return Eure Matrikel-Nummer
     */
    public abstract int getMatrikel();

    @Override
    protected final PlayerType getType() {
        return PlayerType.AI;
    }
}
