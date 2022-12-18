package com.gats.ui;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.File;
import java.io.FilenameFilter;

public class MapRetriever implements FilenameFilter {
    static FileHandle dirHandler;
    final String defaultMapFiletype = ".json";
    String filetype;
    String mapDirectory =  "core/resources/maps";


    /**Creates a {@link MapRetriever}, wich is responsible for providing the selectable Maps
     * found in {@link MapRetriever#mapDirectory}.
     * Currently, does not look into subdirectories.
     */
    MapRetriever(){
       new MapRetriever(mapDirectory);
    }

    /**Creates a {@link MapRetriever}, wich is responsible for providing the selectable Maps
     * found in a directory.
     * Currently, does not look into subdirectories.
     * @param newDirectory Changes the {@link MapRetriever#mapDirectory}, where it will look for maps.
     */
     MapRetriever(String newDirectory) {
        dirHandler = Gdx.files.internal(newDirectory);
        if(!dirHandler.isDirectory()){
            System.err.println("Provided Path for MapHandler is not a valid directory!");
            throw new RuntimeException();
        }

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
     * in {@link MapRetriever#mapDirectory}.
     * @return String Array with map Names located in mapDirectory
     */
    public String[] listMaps(){
        this.filetype = defaultMapFiletype;
       String[] mapNames = fileHandleToStringArray(dirHandler.list(this));
        removeExtension(mapNames);
        //does not work yet
        return mapNames;
    }



    /**
     * Lists all Maps files, with the provided File Extension,
     * in {@link MapRetriever#mapDirectory}.
     * @param fileExtension Map File Extension.
     * @return String Array with map Names located in mapDirectory
     */
    public String[] listMaps(String fileExtension){
        this.filetype = fileExtension;
        String[] maps= fileHandleToStringArray(dirHandler.list(this));
        removeExtension(maps);
        return maps;
    }

    public String[] fileHandleToStringArray(FileHandle[] handles){

        String[] fileHandleNames = new String[handles.length];
        for(int i = 0;i<handles.length;i++) {
                fileHandleNames[i] = handles[i].name();
            }
        return fileHandleNames;

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
