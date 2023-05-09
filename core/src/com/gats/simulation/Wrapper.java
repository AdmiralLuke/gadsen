package com.gats.simulation;

import com.gats.simulation.action.Action;
import com.gats.simulation.weapons.WeaponWrapper;

public class Wrapper {

    private GameCharacter[][] team;
    private WeaponWrapper wpWrapper;
    Wrapper(GameCharacter[][] team) {
        this.team = team;
        this.wpWrapper = WeaponWrapper.instance();
    }

    public Action setHealth(Action head, int team, int gchar, int newHealth, boolean environmental) {
        return this.team[team][gchar].setHealth(newHealth, head, environmental);
    }

    public void setPosition(int team, int gchar, int x, int y) {
        this.team[team][gchar].setPosX(x);
        this.team[team][gchar].setPosY(y);
    }

    public Action fall(Action head, int team, int gchar) {
        return this.team[team][gchar].fall(head);
    }

    public void tileSetAnchored(Tile t) {
        t.setAnchor(true);
    }

    public Action destroyTile(Tile t, GameCharacter character, Action head) {
        head = onDestroyTile(t, head);
        if (t.getTileType() == Tile.TileType.WEAPON_BOX) {
            // weapon-box

            // select random weapon
            int weapon = (int)(Math.random() * character.getWeaponAmount());
            // select random amount of ammo
            int ammo = (int)(Math.random() * 3) + 1; //min 1, max 4
            addAmmoToWeapon(weapon, ammo, character);
        } else if (t.getTileType() == Tile.TileType.HEALTH_BOX) {
            // health-box

            // random amount of health
            int health = (int)(Math.random() * 25) + 10;

            // dont overheal
            health = Math.min(health, 100 - character.getHealth());

            head = this.setHealth(head, character.getTeam(), character.getTeamPos(), character.getHealth() + health);
        }
        return head;
    }

    public Action onDestroyTile(Tile t, Action head) {
        return t.onDestroy(head);
    }

    public void addAmmoToWeapon(int weapon, int addAmmo, GameCharacter character) {
        wpWrapper.addAmmo(character.getWeapon(weapon), addAmmo);
    }
}
