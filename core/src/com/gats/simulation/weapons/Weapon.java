package com.gats.simulation.weapons;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.*;
import com.gats.simulation.action.Action;
import com.gats.simulation.action.CharacterShootAction;

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


    public Action shoot(Action head, Vector2 dir, float strength, Vector2 pos, GameCharacter character) {
        if (ammo <= 0) return head;
        ammo--;
        pos.x += 10;
        pos.y += 10;
        Path path = null;
        dir.nor();
        Vector2 v = new Vector2((dir.x * strength) * 400, (dir.y * strength) * 400);
        if (type == WeaponType.COOKIE) path = new ParablePath(pos.cpy(), 15, v);
        else if(type == WeaponType.SUGAR_CANE) path = new LinearPath(pos.cpy(), dir.cpy(), 15, 40);
        projectile.setPath(path);
        CharacterShootAction shootAction = new CharacterShootAction(team, teamN);
        head.getChildren().add(shootAction);
        return projectile.shoot(shootAction, dir, strength, projectile, character);
    }

    public WeaponType getType() {
        return type;
    }
}
