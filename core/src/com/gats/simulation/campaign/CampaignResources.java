package com.gats.simulation.campaign;

import com.gats.manager.Player;

import java.util.ArrayList;

public class CampaignResources {

    public static ArrayList<Class<? extends Player>> getEnemies(String map){
        ArrayList<Class<? extends Player>> enemies = new ArrayList<>();
        switch (map){
            case "level1":
                enemies.add(Level1Bot.class);
                break;
            case "level2":
                break;
            case "level3":
                break;
            case "level4":
                break;
            case "level5":
                break;
        }
        return enemies;
    }

    public static ArrayList<int[]> getWeaponCounts(String map){
        ArrayList<int[]> inventories = new ArrayList<>();
        switch (map){
            case "level1":
                inventories.add(new int[]{0, 0, 0, 0, 0, 0});
                inventories.add(new int[]{0, 1, 0, 0, 0, 0});
                break;
            case "level2":
                inventories.add(new int[]{0, 0, 0, 0, 0, 0});
                inventories.add(new int[]{0, 0, 0, 0, 0, 0});
                break;
            case "level3":
                inventories.add(new int[]{0, 0, 0, 0, 0, 0});
                inventories.add(new int[]{0, 0, 0, 0, 0, 0});
                break;
            case "level4":
                inventories.add(new int[]{0, 0, 0, 0, 0, 0});
                inventories.add(new int[]{0, 0, 0, 0, 0, 0});
                break;
            case "level5":
                break;
        }
        return inventories;
    }

    public static ArrayList<int[]> getHealth(String map){
        ArrayList<int[]> helth = new ArrayList<>();
        switch (map){
            case "level1":
                helth.add(new int[]{5});
                helth.add(new int[]{100});
                break;
            case "level2":
                helth.add(new int[]{0});
                helth.add(new int[]{0});
                break;
            case "level3":
                helth.add(new int[]{0});
                helth.add(new int[]{0});
                break;
            case "level4":
                helth.add(new int[]{0});
                helth.add(new int[]{0});
                break;
            case "level5":
                break;
        }
        return helth;
    }
}
