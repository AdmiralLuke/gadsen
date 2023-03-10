package com.gats.simulation.weapons;

import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.Action;
import com.gats.simulation.CharacterShootAction;
import com.gats.simulation.WeaponType;

public class Weapon {

    private Projectile projectile;
    private int ammo;
    private WeaponType type;
    int team;
    int teamN;

    public Weapon(Projectile projectile, int ammo, WeaponType type, int team, int teamN) {
        this.projectile = projectile;
        this.ammo = ammo;
        this.type = type;
        this.team = team;
        this.teamN = teamN;
    }


    public Action shoot(Action head, Vector2 dir, float strength, Vector2 pos) {
        if (ammo <= 0) return head;
        ammo--;
        pos.x += 5;
        pos.y += 5;
        projectile.setPos(pos);
        CharacterShootAction shootAction = new CharacterShootAction(team, teamN);
        head.getChildren().add(shootAction);
        return projectile.shoot(shootAction, dir, strength, projectile);
    }

    public WeaponType getType() {
        return type;
    }
}
