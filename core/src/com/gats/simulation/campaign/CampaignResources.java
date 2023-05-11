package com.gats.simulation.campaign;

import com.gats.manager.Player;

import java.util.ArrayList;

public class CampaignResources {

    public static ArrayList<Class<? extends Player>> getEnemies(String map){
        ArrayList<Class<? extends Player>> enemies = new ArrayList<>();
        switch (map){
            case "level1_1":
            case "level1_2":
            case "level1_3":
                enemies.add(Level1Bot.class);
                break;
            case "level2_1":
            case "level2_2":
            case "level2_3":
                enemies.add(Level2Bot.class);
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
            case "level1_1":
                inventories.add(new int[]{0, 0, 0, 0, 0, 0});
                inventories.add(new int[]{0, 1, 0, 0, 0, 0});
                break;
            case "level1_2":
            case "level1_3":

            case "level2_1":
            case "level2_2":
            case "level2_3":
                inventories.add(new int[]{0, 99, 0, 0, 0, 0});
                inventories.add(new int[]{0, 99, 0, 0, 0, 0});
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
            case "level1_1":
                helth.add(new int[]{5});
                helth.add(new int[]{100});
                break;
            case "level1_2":
                helth.add(new int[]{100});
                helth.add(new int[]{15});
                break;
            case "level1_3":
                helth.add(new int[]{100});
                helth.add(new int[]{80});
                break;
            case "level2_1":
                helth.add(new int[]{100});
                helth.add(new int[]{100});
                break;
            case "level2_2":
            case "level2_3":
                helth.add(new int[]{80});
                helth.add(new int[]{100});
                break;
            case "level5":
                break;
        }
        return helth;
    }

    public static int getCharacterCount(String map) {
        switch (map){
            case "level1_1":
            case "level1_2":
            case "level1_3":
            case "level2_1":
            case "level2_2":
            case "level2_3":
                return 1;
        }
        return 1;
    }
}