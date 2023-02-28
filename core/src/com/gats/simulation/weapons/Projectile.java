package com.gats.simulation.weapons;

import com.gats.simulation.Action;
import com.gats.simulation.ActionLog;

enum ProjType {
    PARABLE,
    LINEAR,
    LASER,
    EXPLOSIVE
}

public interface Projectile {
    ActionLog hitWall(Action head);
   //  ActionLog move();
    ActionLog hitCharacter(Action head);
}
