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
        System.out.printf("queueing animator action at %s ms\n", System.currentTimeMillis());
        System.out.printf("Move Action created with destination:" + path.getPos(endTime).toString());
    }

    @Override
    protected void runAction(float oldTime, float current) {
        System.out.printf("executing animator action at %s ms\n", System.currentTimeMillis());
//        System.out.println("executing animator move action");
//        System.out.printf("current time: %f endtime: %f\n", current, endTime);
//        System.out.printf("moving from: %s moving to: %s\n", target.getPos().toString(), path.getPos(Math.min(endTime, current)).toString());
        if(target!= null) target.setRelPos(path.getPos(Math.min(endTime, current)));
        if(current > endTime) endAction(endTime);
    }

    public void setTarget(Entity target) {
        this.target = target;
    }
}
