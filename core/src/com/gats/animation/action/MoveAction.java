package com.gats.animation.action;

import com.gats.animation.entity.Entity;
import com.gats.simulation.Path;

public class MoveAction extends Action{

    private Entity target;

    private Path path;

    public MoveAction(float start, float end, float current, Entity target, Path path) {
        super(start, current, end);
        this.target = target;
        this.path = path;
    }

    @Override
    protected void runAction(float oldTime, float current) {
        target.setRelPos(path.getPath(Math.min(end, current)));
    }
}
