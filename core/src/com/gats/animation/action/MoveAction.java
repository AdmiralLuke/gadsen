package com.gats.animation.action;

import com.badlogic.gdx.math.Vector2;
import com.gats.animation.entity.Entity;
import com.gats.simulation.Path;
import org.lwjgl.Sys;

public class MoveAction extends Action{

    private Entity target;

    private Path path;

    private float endTime;

    public MoveAction(float delay, Entity target, float duration, Path path) {
        super(delay);
        this.target = target;
        this.path = path;
        this.endTime = delay + duration;
    }

    @Override
    protected void runAction(float oldTime, float current) {
        if(target!= null) target.setRelPos(path.getPos(Math.min(endTime, current)));
        if(current > endTime) endAction(endTime);
    }

    public void setTarget(Entity target) {
        this.target = target;
    }
}
