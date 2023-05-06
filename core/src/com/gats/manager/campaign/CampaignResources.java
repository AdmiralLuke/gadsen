package com.gats.manager.campaign;

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

}
