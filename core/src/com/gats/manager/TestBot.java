package com.gats.manager;

import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.GameState;
import com.gats.simulation.WeaponType;

public class TestBot extends Bot {

    /**
     * Hier gebt ihr euren Namen an
     * @return Format: Vorname Nachname
     */
    @Override
    public String getStudentName() {
        return "Mio Goodman";
    }

    /**
     * Hier gebt ihr eure Matrikelnummer an
     * @return eure Matrikelnummer
     */
    @Override
    public int getMatrikel() {
        return 42069;
    }

    /**
     * Hier könnt ihr eueren Bot einen (kreativen) Namen geben
     * @return Name des Bots
     */
    @Override
    public String getName() {
        return "TestBot";
    }

    /**
     * Diese Methode wird beim Laden der Map aufgerufen. Ideal um gegebenfalls Werte zu initialisieren
     * @param state Der {@link GameState Spielzustand} zu Beginn des Spiels
     */
    @Override
    protected void init(GameState state) {
        System.out.println("Loaded the Test Bot");
    }

    /**
     * Diese Methode beschreibt den Zug den eure Gadse macht
     * @param state Der {@link GameState Spielzustand} während des Zuges -> Spielinformationen
     * @param controller Der {@link Controller Controller}, zum Charakter gehört, welcher am Zug ist -> Charaktersteuerung
     */
    @Override
    protected void executeTurn(GameState state, Controller controller) {
        // Aktuelle Position des aktiven Spielers
        Vector2 position = controller.getGameCharacter().getPlayerPos();
        // kann ich da hinlaufen...?
        if (state.getTile((int)(position.x / 16) + 1, (int)(position.y) - 1) != null) {
            // einen Schritt weiter
            controller.move(16);
        }

        // wähle die Zuckerstange aus
        controller.selectWeapon(WeaponType.SUGAR_CANE);
        controller.shoot(20 + (int)(Math.random() * 30), 0.5f, WeaponType.SUGAR_CANE);
    }
}
