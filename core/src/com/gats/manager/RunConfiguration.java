package com.gats.manager;

import com.gats.ui.HudStage;

import java.util.ArrayList;

public class RunConfiguration {

    public int gameMode = 0;

    public boolean gui = false;

    public AnimationLogProcessor animationLogProcessor = null;

    //Generalize to Interface
    public HudStage hud = null;

    public String mapName;

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
        if(gameMode<0){
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
      output+= "HudStage: " + checkNullToString(hud)+nl;
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
