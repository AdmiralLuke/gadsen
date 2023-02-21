package com.gats.ui.menu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.gats.simulation.IntVector2;
import com.gats.simulation.Tile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MapRetriever implements FilenameFilter {
    static FileHandle dirHandler;
    final String defaultMapFiletype = ".json";
    String filetype;

    boolean debug = true;

    /**
     * For dev use in the Project/Ide.
      */
    String internalMapDirectory =  "core/resources/maps";
    /**
    /**
     * Path relative to the jar after building
     */
    String externalMapDirectory = "maps";

    String[] hardCodeMapNames = new String[]{"christmasMap","map1","MapSafeGround","testingMap"};


    /**Creates a {@link MapRetriever}, wich is responsible for providing the selectable Maps
     * found in a directory.
     * Currently, does not look into subdirectories.
     * assigns it the directory that can be found
     * first checks if the internal directory is available
     * then looks for for the external dir
     */

    protected MapRetriever() {
        FileHandle directory = determineMapDirectory(internalMapDirectory);
        if (directory!=null){
            dirHandler = directory;
        }
        else {
            directory = determineMapDirectory(externalMapDirectory);
            dirHandler = directory;
        }
    }

    /**
     * checks whether the given directory exists
     * @param directoryPath Directory to check
     * @return directory if it exists, otherwise returns null
     */
    public FileHandle determineMapDirectory(String directoryPath){
        FileHandle dir = new FileHandle(directoryPath);
        if(dir.isDirectory()){
            return dir;
        }
        return null;
    }

    /**
     * Removes the File extension from the Name of all Maps in a String array.
     * @param maps String[] of the map names.
     */
    void removeExtension(String[] maps) {
        for (int i = 0; i<maps.length;i++) {
           maps[i] = maps[i].replace(filetype , "");

        }
    }
    /**
     * Lists all Map files, with the default File Extension,
     * in {@link MapRetriever#internalMapDirectory}.
     *
     * @return String Array with map Names located in mapDirectory
     */
    public GameMap[] listMaps(){

    return listMaps(defaultMapFiletype);

    }



    /**
     * Lists all Maps files, with the provided File Extension,
     * in {@link MapRetriever#internalMapDirectory}.
     * @param fileExtension Map File Extension.
     * @return String Array with map Names located in mapDirectory
     */
    public GameMap[] listMaps(String fileExtension){
        this.filetype = fileExtension;
         String[] mapNames = new String[0];
        ArrayList<GameMap> maps = new ArrayList<>();

        if(dirHandler!=null) {
            mapNames = fileHandleToStringArray(dirHandler.list(this));
            removeExtension(mapNames);
            maps = strToGameMap(mapNames);
        }
        if(dirHandler==null || !dirHandler.toString().equals(internalMapDirectory)) {
            /*
             * If the directory is not the internal, then hardcoded maps should be added to the directory.
             * Otherwise they would be redundandly listed.
             */
            maps.addAll(0,strToGameMap(hardCodeMapNames));
        }


        GameMap[] gameMapArr = new GameMap[maps.size()];
        for(int i = 0;i<maps.size();i++){
            gameMapArr[i]=maps.get(i);
        }
        return gameMapArr;

    }

    public String[] fileHandleToStringArray(FileHandle[] handles){

        String[] fileHandleNames = new String[handles.length];
        for(int i = 0;i<handles.length;i++) {
                fileHandleNames[i] = handles[i].name();
            }
        return fileHandleNames;

    }
    public GameMap[] appendHardcodeMaps(GameMap[] maps){
        GameMap[] newMaps = new GameMap[maps.length+hardCodeMapNames.length];
        int counter = 0;


        for (String map:hardCodeMapNames) {
            newMaps[counter] = readMapFromFile(map);
            counter++;
        }
        for (GameMap map:maps) {
            newMaps[counter] = map;
            counter++;
        }
        return newMaps;
    }

    private GameMap readMapFromFile(String mapName) {
        JsonReader reader = new JsonReader();
        JsonValue map;
        try {
            if(debug) {
                System.out.println("Get Map from: " + getClass().getClassLoader().getResourceAsStream("maps/" + mapName + ".json"));
            }
            map = reader.parse(getClass().getClassLoader().getResourceAsStream("maps/" + mapName + ".json"));
        }
        catch(Exception e){
            if(debug) {
                System.out.println("Could not find/load file!");
            }
            map = null;
        }
        if(map==null) {
              try {
                  if(debug) {
                      System.out.println("Try to get File from:" + Paths.get("./maps/" + mapName + ".json"));
                  }
                  map = reader.parse(new FileHandle(Paths.get("./maps/" + mapName + ".json").toFile()));
              } catch (Exception e) {
                  if(debug) {
                      System.out.println("Could not find/load file!");
                  }
              }
          }
        if(map != null) {
            int width = map.get("width").asInt();
            int height = map.get("height").asInt();

            JsonValue tileData = map.get("layers").get(0).get("data");

            int spawnpoints = 0;

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int type = tileData.get(i + (height - j - 1) * width).asInt();
                    if (type == 3) {
                        spawnpoints++;
                    }
                }
            }

            return new GameMap(mapName, spawnpoints);
        }
        return new GameMap("ProblemLoadingMap",0);
    }

    private ArrayList<GameMap> strToGameMap(String[] mapNames)  {
       ArrayList<GameMap> maps = new ArrayList<GameMap>();
       int count=0;
        for (String map:mapNames) {
           maps.add(readMapFromFile(map));

        }

        return maps;

    }


    /** Checks whether a given file contains the given extension .
     * @param name Filename that is tested for the extension provided in {@link MapRetriever#filetype}.
     * @return Returns true, if File contains the extension.
     */
    @Override
    public boolean accept(File dir, String name) {
        return name.endsWith(filetype.toLowerCase());
    }
}
