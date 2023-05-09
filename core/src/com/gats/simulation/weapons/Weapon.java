package com.gats.simulation.weapons;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.*;
import com.gats.simulation.action.Action;
import com.gats.simulation.action.CharacterShootAction;
import com.gats.simulation.action.ProjectileAction;

import java.io.Serializable;

public class Weapon implements Serializable {

    private transient Projectile projectile;
    private int ammo;
    private WeaponType type;
    float duration;

    public Weapon(Projectile projectile, int ammo, WeaponType type, float duration) {
        this.projectile = projectile;
        this.ammo = ammo;
        this.type = type;
        this.duration = duration;
    }

    private Weapon(Weapon original){
        this.ammo = original.ammo;
        this.type = original.type;
        this.duration = original.duration;
    }


    public Action shoot(Action head, Vector2 dir, float strength, Vector2 pos, GameCharacter character) {
        if (ammo <= 0) return head;
        ammo--;
        pos.add(character.getSize().scl(0.5f));
        Path path = null;
        dir.nor();
        if (type == WeaponType.WATERBOMB) strength = 0.2f;

        Vector2 v = new Vector2((dir.x * strength) * 400, (dir.y * strength) * 400);
        if (type == WeaponType.WOOL || type == WeaponType.WATER_PISTOL || type == WeaponType.GRENADE || type == WeaponType.WATERBOMB) path = new ParablePath(pos.cpy(), duration, v);
        else if(type == WeaponType.MIOJLNIR ||  type == WeaponType.CLOSE_COMBAT) path = new LinearPath(pos.cpy(), dir.cpy(), duration, 40);
        projectile.setPath(path);
        CharacterShootAction shootAction = new CharacterShootAction(character.getTeamPos(), character.getTeamPos());
        head.getChildren().add(shootAction);
        return projectile.shoot(shootAction, dir, strength, projectile, character);
    }

    public WeaponType getType() {
        return type;
    }

    public Weapon copy(){
        return new Weapon(this);
    }

    protected void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public int getAmmo() {
        return ammo;
    }

    public float getDuration() {
        return duration;
    }
}
