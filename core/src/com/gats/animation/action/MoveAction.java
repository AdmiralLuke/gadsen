package com.gats.animation.action;

import com.badlogic.gdx.math.Vector2;
import com.gats.animation.entity.Entity;
import com.gats.simulation.Path;

public class MoveAction extends Action{

    private Entity target;

    private Path path;

    private float end;

    public MoveAction(float delay, Entity target, float duration, Path path) {
        super(delay);
        this.target = target;
        this.path = path;
        this.end = delay + duration;
    }

    @Override
    protected void runAction(float oldTime, float current) {
        if(target!= null) target.setRelPos(path.getPos(Math.min(end, current)));
        if(current > end) endAction(end);
    }

    public void setTarget(Entity target) {
        this.target = target;
    }
}
