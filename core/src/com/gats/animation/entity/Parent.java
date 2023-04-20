package com.gats.animation.entity;

public interface Parent {

    Entity asEntity();
    void add(Entity child);
    void remove(Entity child);
}
