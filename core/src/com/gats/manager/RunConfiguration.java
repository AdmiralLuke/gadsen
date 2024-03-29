package com.gats.manager;

import com.gats.simulation.GameState.GameMode;
import com.gats.ui.hud.UiMessenger;

import java.util.ArrayList;

public class RunConfiguration {

    //Todo add default values
    public GameMode gameMode = GameMode.Normal;
    public int inventorySize = 6;

    public GameMode[] getGameModes() {
        return GameMode.values();
    }

    public boolean gui = false;

    public AnimationLogProcessor animationLogProcessor = null;

    public UiMessenger uiMessenger = null;

    public InputProcessor inputProcessor = null;

    public String mapName;

    public boolean replay = false;

    public int teamCount;
    public int teamSize;

    public ArrayList<Class<? extends Player>> players;

    public boolean isValid(){

        if(teamSize<=0||teamCount<=0){
            System.err.println("RunConfig: TeamSize/Count is not valid. TSize:" + teamSize + " TCount: " + teamCount);
           return false;
        }
        if(mapName==null){
            return false;
        }
        if(players==null){
            return false;
        }
        if(gameMode == null){
            return false;
        }

        return true;
    }

    public String toString(){
        String nl = "\n";
        String output = "";

       output+= "GameMode: " + gameMode + nl;
      output+=  "Gui: " + gui +nl;

      output+= "AnimationLogProcessor: " + checkNullToString(animationLogProcessor)+nl;
      output+= "InputProcessor: " + checkNullToString(inputProcessor)+nl;
      output+= "mapName: " + mapName+nl;
      output+= "teamCount: " + teamCount+nl;
      output+= "teamSize: " + teamSize+nl;
      output+= "players: " + checkNullToString(players)+nl;

      return output;
    }

    private String checkNullToString(Object n){
       if(n!=null){
           return n.toString();
       }
       else {
           return null;
       }
    }
}
