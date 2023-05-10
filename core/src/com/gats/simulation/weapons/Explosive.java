package com.gats.simulation.weapons;

import com.badlogic.gdx.math.Vector2;
import com.gats.simulation.*;
import com.gats.simulation.action.Action;
import com.gats.simulation.action.ProjectileAction;

import java.util.HashMap;
import java.util.Set;

public class Explosive implements Projectile {

    Projectile proj;
    int radius;

    public Explosive(Projectile proj, int radius) {
        this.proj = proj;
        this.radius = radius;
    }

    @Override
    public Action hitWall(Action head, Tile t, Projectile dec, BaseProjectile bsProj) {

        head = proj.hitNothing(head, dec, bsProj);

        return explode(head, t, null, bsProj.path.getPos(bsProj.t), bsProj.sim, bsProj);
    }

    @Override
    public Action hitCharacter(Action head, GameCharacter character, Projectile dec, BaseProjectile bsProj) {
        return null;
    }

    @Override
    public Action hitNothing(Action head, Projectile dec, BaseProjectile bsProj) {
        head = proj.hitNothing(head, dec, bsProj);
        return explode(head, null, null, bsProj.path.getPos(bsProj.t), bsProj.sim, bsProj);
    }

    @Override
    public Action shoot(Action head, Vector2 dir, float strength, Projectile dec, GameCharacter character) {
        return proj.shoot(head, dir, strength, dec, character);
    }

    @Override
    public void setPath(Path path) {
        proj.setPath(path);
    }

    @Override
    public Action move(Action head, float strength, Projectile dec) {
        return proj.move(head, strength, dec);
    }

    public Action explode(Action head, Tile t, GameCharacter character, Vector2 midPos, Simulation sim, BaseProjectile bsProj) {
        HashMap<Tile, Integer> tilesInRadius = new HashMap<>();
        HashMap<GameCharacter, Integer> charactersInRadius = new HashMap<>();
        traverseCircle(tilesInRadius, charactersInRadius, this.radius, midPos, sim, head, bsProj);
        if (t != null) tilesInRadius.put(t, 0);
        if (character != null) charactersInRadius.put(character, 0);

        if (bsProj.type != ProjectileAction.ProjectileType.WATERBOMB) {
            Set<Tile> tileToDestroy = tilesInRadius.keySet();
            if (!tileToDestroy.isEmpty()) {
                for (Tile td : tileToDestroy) {
                    if (td != null) td.onDestroy(head);
                }
            }
        }


        Set<GameCharacter> charactersToMove = charactersInRadius.keySet();
        // System.out.println(tileToDestroy.size());
        // System.out.println(charactersToMove.size());
        if (!charactersToMove.isEmpty()) {
            for (GameCharacter ch : charactersToMove) {
                if (ch == null) break;
                Vector2 bsPos = bsProj.path.getPos(bsProj.t).cpy();
                Vector2 playerPos = ch.getPlayerPos().cpy().add(GameCharacter.getSize().scl(0.5f));

                Vector2 dir = bsPos.sub(playerPos).nor().scl(-1);
                Vector2 v = new Vector2((dir.x * (0.4f * bsProj.knockback)) * 400, (dir.y * (0.4f * bsProj.knockback)) * 400);
                Path path = new ParablePath(ch.getPlayerPos(), 15, v);
                sim.getWrapper().setHealth(head, ch.getTeam(), ch.getTeamPos(), ch.getHealth() - bsProj.damage, false);
                head = BaseProjectile.traverse(head, ch, path, sim, bsProj);
            }
        }
        return head;
    }

    void traverseCircle(HashMap<Tile, Integer> tiles, HashMap<GameCharacter, Integer> characters, int rad, Vector2 midPos, Simulation sim, Action head, BaseProjectile bsProj) {
        if (rad <= 0) return;

        int steps = rad * 7 * 200;
        float stepSize = (float)(2 * Math.PI / steps);
        for (int i = 0; i < steps; i++) {
            float theta = i * stepSize;
            float x = (midPos.x / 16) + (rad * (float) Math.cos(theta));
            float y = (midPos.y / 16) + (rad * (float) Math.sin(theta));

            Tile t = sim.getState().getTile((int) x, (int) y);

            // Tile on our circle?
            if (rad == radius) {
                if (t != null && !tiles.containsKey(t)) tiles.put(t, 0);
            } else {
                if (t != null && bsProj.type !=  ProjectileAction.ProjectileType.WATERBOMB) t.destroyTileDirect(head);
            }


            for (int j = 0; j < sim.getState().getTeamCount(); j++) {
                for (int k = 0; k < sim.getState().getCharactersPerTeam(); k++) {
                    GameCharacter character = sim.getState().getCharacterFromTeams(j, k);
                    if (character != null && (int) x == (int) character.getPlayerPos().x / 16 && (int) y == character.getPlayerPos().y / 16 && character.isAlive()) {
                        if (!characters.containsKey(character)) characters.put(character, 0);
                    }
                }
            }
        }
        traverseCircle(tiles, characters, rad - 1, midPos, sim, head, bsProj);
    }
}
