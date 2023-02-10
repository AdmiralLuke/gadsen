package com.gats.ui.menu;
import com.badlogic.gdx.files.FileHandle;

import java.io.File;
import java.io.FilenameFilter;

public class MapRetriever implements FilenameFilter {
    static FileHandle dirHandler;
    final String defaultMapFiletype = ".json";
    String filetype;

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
     * @return String Array with map Names located in mapDirectory
     */
    public String[] listMaps(){
        return listMaps(defaultMapFiletype);
    }



    /**
     * Lists all Maps files, with the provided File Extension,
     * in {@link MapRetriever#internalMapDirectory}.
     * @param fileExtension Map File Extension.
     * @return String Array with map Names located in mapDirectory
     */
    public String[] listMaps(String fileExtension){
        this.filetype = fileExtension;
         String[] mapNames = new String[0];

        if(dirHandler!=null) {
            mapNames = fileHandleToStringArray(dirHandler.list(this));
            removeExtension(mapNames);
        }
        if(dirHandler==null || !dirHandler.toString().equals(internalMapDirectory)) {
            /**
             * If the directory is not the internal, then hardcoded maps should be added to the directory.
             * Otherwise they would be redundandly listed.
             */
            mapNames = appendHardcodeMaps(mapNames);
        }
        return mapNames;
    }

    public String[] fileHandleToStringArray(FileHandle[] handles){

        String[] fileHandleNames = new String[handles.length];
        for(int i = 0;i<handles.length;i++) {
                fileHandleNames[i] = handles[i].name();
            }
        return fileHandleNames;

    }
    public String[] appendHardcodeMaps(String[] maps){
        String[] newMaps = new String[maps.length+hardCodeMapNames.length];
        int counter = 0;
        for (String map:hardCodeMapNames) {
            newMaps[counter] = map;
            counter++;
        }
        for (String map:maps) {
            newMaps[counter] = map;
            counter++;
        }
        return newMaps;
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
