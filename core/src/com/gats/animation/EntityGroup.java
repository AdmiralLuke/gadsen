package com.gats.animation;

import java.util.ArrayList;
import java.util.List;

/**
 * Gruppiert mehrere Entities.
 * Dabei sollen die absoluten Positionen aller Kinder angepasst werden, wenn sich die Position der Gruppe ändert.
 */
public class EntityGroup extends Entity {

    private List<Entity> children = new ArrayList<>();

}
