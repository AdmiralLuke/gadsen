package com.gats.animation.action;

import com.gats.animation.entity.Entity;

public class DestroyAction extends Action{


    private Entity target;
    private final DestroyListener listener;
    private final Destroyer destroyer;


    public interface DestroyListener{
        void onDestroy(Entity destroyedEntity);

    }

    public interface Destroyer{
        void destroy(Entity toDestroy);
    }

    public DestroyAction(float start, Entity target, DestroyListener listener, Destroyer destroyer) {
        super(start);
        this.target = target;
        this.listener = listener;
        this.destroyer = destroyer;
    }

    @Override
    protected void runAction(float oldTime, float current) {
        if (destroyer != null) destroyer.destroy(target);
        if (listener != null) listener.onDestroy(target);
        endAction(oldTime);
    }

    public void setTarget(Entity target) {
        this.target = target;
    }
}
