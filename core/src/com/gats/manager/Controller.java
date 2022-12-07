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
    private Manager manager;
    private GameCharacterController gcController;
    private GameCharacter gameCharacter;

    protected Controller(Manager manager, GameCharacterController gcController) {

        this.manager = manager;
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
        if (active) manager.queueCommand(new MoveCommand(gcController, dx));

    }

    public void shoot(Vector2 angle, float strength, WeaponType type) {
        aim(angle, strength);
        selectWeapon(type);
        shoot();
    }

    public void shoot(float angle, float strength, WeaponType type) {
        aim(angle, strength);
        selectWeapon(type);
        shoot();
    }

    public void aim(float angle, float strength) {
        double radians = angle / 180f * Math.PI;
        aim(new Vector2((float) Math.cos(radians), (float) Math.sin(radians)), strength);
    }

    public void aim(Vector2 angle, float strength) {
        if (active) manager.queueCommand(new AimCommand(gcController, angle, strength));
    }

    public void selectWeapon(WeaponType type) {
        if (active) manager.queueCommand(new WeaponSelectCommand(gcController, type));
    }

    public void shoot() {
        if (active) manager.queueCommand(new ShootCommand(gcController));
    }

    protected void activate() {
        active = true;
    }

    protected void deactivate() {
        active = false;
    }

}
