package com.gats.animation.action;

public abstract class Action {
    protected float delay;
    protected float current;
    protected float end = Float.MAX_VALUE/2;

    protected boolean hasEnded = false;
    private Action[] children;

    public Action(float start) {
        this.delay = start;
        this.current = 0;
    }

    public void setChildren(Action[] children) {
        this.children = children;
    }

    public Action[] getChildren() {
        return children;
    }

    /**
     *
     * @param deltaTime The time difference to the last frame in ms
     * @return True, if and only if the action has been completed
     */
    public float step(float deltaTime){
        float oldTime = current;
        this.current += deltaTime;
        if (current < delay) return -1;
        runAction(oldTime, current);
        if (current <= end) return -1;
        return current - end;
    }

    protected void endAction(float endTime){
        end = endTime;
        hasEnded = true;
    }

    /**
     * Executes the Actions respective time-dependent task
     * @param oldTime
     * @param current
     */
    protected abstract void runAction(float oldTime, float current);

    public float getDelay() {
        return delay;
    }

    public float getCurrent() {
        return current;
    }

    public float getEnd() {
        return end;
    }
}
