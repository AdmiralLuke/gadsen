package com.gats.ui.menu;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class MapRetriever implements FilenameFilter {
    static FileHandle dirHandler;
    final String defaultMapFiletype = ".json";
    String filetype = defaultMapFiletype;

    boolean debug = true;

    /**
     * For dev use in the Project/Ide.
     */
    String internalDir = "core/resources/";

    String internalMapFolder = "maps/";
    String internalMapDirectory = internalDir + internalMapFolder;
    /**
     * /**
     * Path relative to the jar after building
     */
    String externalMapDirectory = "map/";

    String[] hardCodeMapNames = new String[]{"christmasMap", "kratzbaumMap", "MangoMap"};

    ArrayList<String> internalMaps = new ArrayList<String>(Arrays.asList(hardCodeMapNames));

    String campaignMapFolder = "maps/campaign/";

    String campaignDirectory = internalDir + campaignMapFolder;

    String[] campaignMaps = {"level1_1", "level1_2", "level1_3", "level2_1", "level2_2", "level2_3", "level7", "level8", "level9", "level10", "level11", "level12"};

    /**
     * Creates a {@link MapRetriever}, wich is responsible for providing the selectable Maps
     * found in a directory.
     * Currently, does not look into subdirectories.
     * assigns it the directory that can be found
     * first checks if the internal directory is available
     * then looks for for the external dir
     */

    protected MapRetriever() {


    }

    /**
     * checks whether the given directory exists
     *
     * @param directoryPath Directory to check
     * @return directory if it exists, otherwise returns null
     */
    public FileHandle determineMapDirectory(String directoryPath) {
        FileHandle dir = new FileHandle(directoryPath);
        if (dir.isDirectory()) {
            return dir;
        }
        return null;
    }

    /**
     * Removes the File extension from the Name of all Maps in a String array.
     *
     * @param maps String[] of the map names.
     */
    void removeExtension(ArrayList<String> maps) {
        maps.replaceAll(s -> s.replace(filetype, ""));
    }

    /**
     * Lists all Map files, with the default File Extension,
     * in {@link MapRetriever#internalMapDirectory}.
     *
     * @return String Array with map Names located in mapDirectory
     */
    public GameMap[] getMaps() {

        GameMap[] maps = new GameMap[0];
        ArrayList<GameMap> allMaps = new ArrayList<>();


        //allMaps.addAll(listMaps(externalMapDirectory));
        //if the internal directory can be accessed -> if project is opened in intelli

        //otherwise the dir needs to be set to look at a different path

        allMaps.addAll(listMaps(internalMapFolder));
        allMaps.addAll(listMaps(externalMapDirectory));

        if (allMaps.size() == 0) {
            allMaps.add(new GameMap("Maps could not be loaded", 0));
        }
        return allMaps.toArray(maps);


    }

    public GameMap[] getCampaignMaps() {

        GameMap[] maps = new GameMap[0];
        return listMaps(campaignMapFolder).toArray(maps);

    }


    /**
     * Lists all Maps files, with the provided File Extension,
     * in {@link MapRetriever#internalMapDirectory}.
     *
     * @return {@link ArrayList<GameMap>} with map Names located in mapDirectory
     * @return Empty List if the dir is not available.
     */
    public ArrayList<GameMap> listMaps(String dir) {
        if (isInternalDir(dir)) {
            return loadInternalMaps(dir);
        }
        //create Filehandle for the target directory
        FileHandle directory = new FileHandle(dir);

        if (!directory.isDirectory()) {
            //the directory is not valid
            System.out.println(dir + " is not a valid directory.");
            return new ArrayList<GameMap>();
        }

        ArrayList<String> mapNames;
        ArrayList<GameMap> maps;

        mapNames = getFileHandleNames(directory.list(this));
        removeExtension(mapNames);

        maps = stringArrayListToGameMap(mapNames, dir);

//        if(dirHandler==null || !dirHandler.toString().equals(internalMapDirectory)) {
//            /*
//             * If the directory is not the internal, then hardcoded maps should be added to the directory.
//             * Otherwise they would be redundandly listed.
//             */
//            maps.addAll(0, stringArrayListToGameMap(internalMaps,internalMapDirectory);
//        }
//

        return maps;

    }

    private ArrayList<GameMap> loadInternalMaps(String dir) {
        ArrayList<GameMap> mapList = new ArrayList<>();
        String[] mapNames = hardCodeMapNames;
        if (dir.equals(campaignMapFolder)) {

            mapNames = campaignMaps;
        } else {
            dir = internalMapFolder;

        }
        for (String map : mapNames) {
            mapList.add(readMapFromFile(map, dir,true));
        }
        return mapList;
    }

    public ArrayList<String> getFileHandleNames(FileHandle[] handles) {


        ArrayList<String> fileHandleNames = new ArrayList<>();
        for (FileHandle handle : handles) {
            fileHandleNames.add(handle.name());
        }
        return fileHandleNames;

    }


    /**
     * Reads a map file, with the specified name.
     *
     * @param mapName
     * @return
     */
    private GameMap readMapFromFile(String mapName, String dir, boolean internalDir) {
        boolean mapLoaded = false;

        JsonValue map = null;

        String mapFolder = "";

        String filename = dir + mapName + filetype;

        if (internalDir) {

            filename = dir + mapName + filetype;


            map = readInternalMap(filename);

        } else {

            map = readExternalMap(filename);


        }
        if (map!=null) {
            return createGameMap(map,mapName);
        }
            //if map could not be loaded, return this hint in selection
            return new GameMap("ProblemLoadingMap: " + mapName, 0);
    }


    private JsonValue readInternalMap(String filename){

            JsonReader reader = new JsonReader();
            JsonValue map = null;
            try {
                if (debug) {

                    System.out.println("Get internal Map from: " + getClass().getClassLoader().getResourceAsStream(filename));
                }

                map = reader.parse(getClass().getClassLoader().getResourceAsStream(filename));
            } catch (Exception e) {
                if (debug) {
                    System.out.println("Could not find/load internal mapfile!");
                }
            }

        return map;
        }
    private JsonValue readExternalMap(String filename){
        JsonReader reader = new JsonReader();
        JsonValue map = null;
        try {
            if (debug) {
                System.out.println("Try to get external Mapfile from:" + Paths.get(filename));
            }
            map = reader.parse(new FileHandle(Paths.get(filename).toFile()));
        } catch (Exception e) {
            if (debug) {
                System.out.println("Could not find/load external mapfile!");
            }
        }
       return map;
    }


    private GameMap createGameMap(JsonValue map,String mapName){

            int width = map.get("width").asInt();
            int height = map.get("height").asInt();

            JsonValue tileData = map.get("layers").get(0).get("data");
        HashMap<Integer,Integer> teams=new LinkedHashMap<>();

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int type = tileData.get(i + (height - j - 1) * width).asInt();
                    if (type > 100) {
                        if(teams.containsKey(type)){
                            teams.replace(type,teams.get(type)+1);
                        }
                       else{teams.put(type,1);}
                    }
                }
            }


            ArrayList<Integer> spawnpoints =new  ArrayList<>(teams.values());
            //get lowest number of spawnpoints
            spawnpoints.sort(Comparator.naturalOrder());
            if(spawnpoints.size()==0){
                spawnpoints.add(0);
            }
          return new GameMap(mapName, spawnpoints.get(0),spawnpoints.size());
        }


    /**
     * Converts an {@link ArrayList<String>} of mapNames to {@link ArrayList<GameMap>}
     * @param mapNames to convert to GameMaps
     * @return List of GameMaps
     */
    private ArrayList<GameMap> stringArrayListToGameMap(ArrayList<String> mapNames,String path)  {
       ArrayList<GameMap> maps = new ArrayList<GameMap>();
        for (String mapName:mapNames) {
           maps.add(readMapFromFile(mapName,path,false));

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

    public boolean isInternalDir(String dir){


        return dir.equals(campaignMapFolder) || dir.equals(internalMapFolder);

    }

}
