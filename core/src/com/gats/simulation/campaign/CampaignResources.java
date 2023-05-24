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
            case "level3_1":
            case "level4_1":
                enemies.add(LeftRightBot.class);
                break;
            case "level3_2":
                enemies.add(Level3_2Bot.class);
                break;
            case "level3_3":
                enemies.add(HammerBot.class);
                break;
            case "level4_2":
            case "level4_3":
                enemies.add(Level4_2Bot.class);
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

            case "level4_1":
            case "level4_2":
            case "level4_3":
                inventories.add(new int[]{0, 99, 0, 0, 0, 0});
                inventories.add(new int[]{0, 99, 0, 0, 0, 0});
                break;
            case "level3_1":
                inventories.add(new int[]{0, 99, 0, 0, 0, 1});
                inventories.add(new int[]{0, 99, 0, 0, 0, 0});
                break;
            case "level3_2":
                inventories.add(new int[]{1, 99, 0, 0, 0, 0});
                inventories.add(new int[]{0, 99, 0, 0, 0, 0});
                break;
            case "level3_3":
                inventories.add(new int[]{5, 99, 0, 0, 0, 0});
                inventories.add(new int[]{0, 99, 20, 0, 0, 0});
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
            case "level2_2":
            case "level2_3":
                helth.add(new int[]{80});
                helth.add(new int[]{100});
                break;
            case "level3_1":
            case "level4_3":
                helth.add(new int[]{20});
                helth.add(new int[]{100});
                break;
            case "level4_1":
                helth.add(new int[]{10});
                helth.add(new int[]{30});
                break;
            case "level2_1":
            case "level3_2":
            case "level4_2":
            case "level3_3":
            default:
                helth.add(new int[]{100});
                helth.add(new int[]{100});
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
            case "level3_1":
            case "level3_2":
            case "level3_3":
            case "level4_1":
            case "level4_2":
            case "level4_3":
                return 1;
        }
        return 1;
    }
}
