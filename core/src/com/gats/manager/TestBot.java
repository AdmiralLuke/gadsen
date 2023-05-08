package com.gats.manager;
// ^ das auskommentieren oder löschen

// v bei folgenden statements müssen die Kommentar / entfernt werden, damit euer Bot funktioniert:
// package.bots;
// import com.gats.manager

import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.GameCharacter;
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
        // System.out.println("Position " + controller.getGameCharacter().getPlayerPos());
        // controller.move(16);
        int team = controller.getGameCharacter().getTeam();
        for (int i = 0; i < state.getTeamCount(); i++) {
            if (i != team) {
                for (int j = 0; j < state.getCharactersPerTeam(); j++) {
                    GameCharacter character = state.getCharacterFromTeams(i, j);
                    if (character.isAlive()) {
                        Vector2 st = controller.getGameCharacter().getPlayerPos();
                        Vector2 e = character.getPlayerPos();
                        Vector2 dir = calcParableDir(st, e, 1f);
                        controller.selectWeapon(WeaponType.WATER_PISTOL);
                        controller.aim(dir, 1f);
                        controller.shoot();
                        // System.out.println("try to shoot from " + st + " to " + e + " in " + dir);
                    }
                }
            }
        }
    }

    private static Vector2 calcParableDir(Vector2 startPos, Vector2 endPos, float strength) {
        float dx = endPos.x - startPos.x;
        float dy = endPos.y - startPos.y;
        strength -= 0.2;

        float th = (float) ((dy / dx) + ((9.81 * 8) * dx) / (2 * (400 * strength) * (400 * strength) * Math.cos(Math.atan(dy / dx))));
        float arcTh = (float)Math.atan(th);

        float thDeg = (float)Math.toDegrees(arcTh);
        return dx < 0 ? new Vector2(-1, 0).rotateDeg(thDeg) : new Vector2(1, 0).rotateDeg(thDeg);
    }
}
