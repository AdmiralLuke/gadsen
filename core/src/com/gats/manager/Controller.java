package com.gats.manager;

import com.badlogic.gdx.math.Vector2;
import com.gats.manager.command.AimCommand;
import com.gats.manager.command.MoveCommand;
import com.gats.manager.command.ShootCommand;
import com.gats.manager.command.WeaponSelectCommand;
import com.gats.simulation.GameCharacter;
import com.gats.simulation.GameCharacterController;
import com.gats.simulation.WeaponType;

/**
 * Provides an access-controlled interface to send commands to players
 * <p>
 * Ermöglicht die Kontrolle eines bestimmten Charakters.
 * Ist nur für einen einzelnen Zug gültig und deaktiviert sich nach Ende des aktuellen Zuges.
 */
public class Controller {

    private boolean active = true;
    private Game game;
    private GameCharacterController gcController;
    private GameCharacter gameCharacter;

    protected Controller(Game game, GameCharacterController gcController) {
//        System.out.println("Created new Controller: " + this);
        this.game = game;
        this.gcController = gcController;
        this.gameCharacter = gcController.getGameCharacter();
    }

    /**
     * @return Den Charakter, welcher durch diesen Controller gesteuert wird.
     */
    public GameCharacter getGameCharacter() {
        return gameCharacter;
    }

    /**
     * Veranlasst den Charakter unter Aufwendung von Stamina einen dx Schritt zu tätigen.
     * Positive Werte bewegen den Charakter nach rechts, Negative nach links.
     */
    public void move(int dx) {
        if (active) game.queueCommand(new MoveCommand(gcController, dx));

    }


    /**
     * Ermöglicht es Zielen, Waffe wählen und Schiessen in einem Statement auszuführen
     * @param angle Schussrichtung als 2D-Vektor
     * @param strength Stärke des Schusses zwischen 0 und 1 (inklusive).
     * @param type Die Waffe die gewählt werden soll.
     */
    public void shoot(Vector2 angle, float strength, WeaponType type) {
        aim(angle, strength);
        selectWeapon(type);
        shoot();
    }

    /**
     * Ermöglicht es Zielen, Waffe wählen und Schiessen in einem Statement auszuführen
     * @param angle Schussrichtung als Winkel zur X-Achse in Grad. D.h. 0 ist nach Rechts; 90 nach Oben; 180 nach Links etc.
     * @param strength Stärke des Schusses zwischen 0 und 1 (inklusive).
     * @param type Die Waffe die gewählt werden soll.
     */
    public void shoot(float angle, float strength, WeaponType type) {
        aim(angle, strength);
        selectWeapon(type);
        shoot();
    }

    /**
     * Zielt mit dem angegebenen Winkel und der gewählten Stärke.
     * @param angle Schussrichtung als Winkel zur X-Achse in Grad. D.h. 0 ist nach Rechts; 90 nach Oben; 180 nach Links etc.
     * @param strength Stärke des Schusses zwischen 0 und 1 (inklusive).
     */
    public void aim(float angle, float strength) {
        double radians = angle / 180f * Math.PI;
        aim(new Vector2((float) Math.cos(radians), (float) Math.sin(radians)), strength);
    }

    /**
     * Zielt mit dem angegebenen Winkel und der gewählten Stärke
     * @param angle Schussrichtung als 2D-Vektor
     * @param strength Stärke des Schusses zwischen 0 und 1 (inklusive).
     */
    public void aim(Vector2 angle, float strength) {
        if (active) game.queueCommand(new AimCommand(gcController, angle, strength));
    }

    /**
     * Wählt die angegebene Waffe aus.
     * @param type Die Waffe die gewählt werden soll.
     */
    public void selectWeapon(WeaponType type) {
        if (active) game.queueCommand(new WeaponSelectCommand(gcController, type));
    }

    /**
     * Befehligt den Charakter mit der ausgewählten Waffe, Winkel und Stärke zu schießen.
     * Es kann nur einmal pro Zug geschossen werden.
     */
    public void shoot() {
        if (active) game.queueCommand(new ShootCommand(gcController));
    }

    /**
     * Reaktiviert diesen Controller. Wird von internen Komponenten genutzt, um den Zugfolge der Charaktere zu steuern.
     */
    protected void activate() {
        active = true;
    }

    /**
     * Deaktiviert diesen Controller (Wenn der Zug vorbei ist). Wird von internen Komponenten genutzt, um den Zugfolge der Charaktere zu steuern.
     */
    protected void deactivate() {
        active = false;
    }

}
