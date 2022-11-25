package com.gats.animation.action;

public abstract class Action {
    protected float start;
    protected float current;
    protected float end;

    public Action(float start, float current, float end) {
        this.start = start;
        this.current = current;
        this.end = end;
    }

    /**
     *
     * @param deltaTime The time difference to the last frame in ms
     * @return True, if and only if the action has been completed
     */
    public boolean step(float deltaTime){
        float oldTime = current;
        this.current += deltaTime;
        if (current < start) return false;
        runAction(oldTime, current);
        return current > end;
    }

    /**
     * Executes the Actions respective time-dependent task
     * @param oldTime
     * @param current
     */
    protected abstract void runAction(float oldTime, float current);

    public float getStart() {
        return start;
    }

    public float getCurrent() {
        return current;
    }

    public float getEnd() {
        return end;
    }
}
