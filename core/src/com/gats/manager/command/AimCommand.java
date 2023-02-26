package com.gats.manager.command;

import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.ActionLog;
import com.gats.simulation.GameCharacterController;

public class AimCommand extends Command{
    private Vector2 angle;
    private float strength;

    public AimCommand(GameCharacterController controller, Vector2 angle, float strength) {
        super(controller);
        this.angle = angle;
        this.strength = strength;
    }

    @Override
    public ActionLog run() {
        return controller.aim(angle, strength);
    }
}
