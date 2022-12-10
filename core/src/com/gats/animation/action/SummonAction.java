package com.gats.animation.action;

import com.gats.animation.entity.Entity;

import java.util.function.Function;

public class SummonAction extends Action{

    private final SummonListener listener;
    private final Summoner summoner;

    public interface SummonListener{
        void onSummon(Entity summonedEntity);

    }

    public interface Summoner{
        Entity summon();
    }

    public SummonAction(float start, SummonListener listener, Summoner summoner) {
        super(start);
        this.listener = listener;
        this.summoner = summoner;
    }

    @Override
    protected void runAction(float oldTime, float current) {
        Entity entity = null;
        if (summoner != null) entity = summoner.summon();
        if (listener != null) listener.onSummon(entity);
        endAction(current);
    }
}
