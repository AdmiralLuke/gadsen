package com.gats.simulation;

import com.gats.simulation.action.Action;
import com.gats.simulation.weapons.WeaponWrapper;

public class Wrapper {

    private GameCharacter[][] team;
    private GameState state;
    private WeaponWrapper wpWrapper;
    Wrapper(GameState state) {
        this.team = state.getTeams();
        this.state = state;
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


    public Action destroyTile(Tile t, GameCharacter character, Action head) {
        head = onDestroyTile(t, head);
        if (t.getTileType() == Tile.TileType.WEAPON_BOX) {
            //roll two weapons according to the cycle
            int w1 = GameState.getWeaponFromCycleIndex(state.cycleWeapon(character.getTeam()));
            int w2 = GameState.getWeaponFromCycleIndex(state.cycleWeapon(character.getTeam()));

            addAmmoToWeapon(w1, 1, character);
            addAmmoToWeapon(w2, 1, character);
        } else if (t.getTileType() == Tile.TileType.HEALTH_BOX) {
            // health-box

            // random amount of health
            int health = character.getHealth() + 35;

            // dont overheal
            health = Math.min(health, 100);

            head = this.setHealth(head, character.getTeam(), character.getTeamPos(), health, true);
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
