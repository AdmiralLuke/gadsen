package com.gats.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.gats.util.FileUtils;
import org.lwjgl.Sys;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pipeline {

    static final String GROUP_DESCRIPTOR_NAME = ".group";
    static final String EXCLUSIONS_NAME = ".exclude";

    static final FileFilter FILTER_NORMAL_FILES = File::isFile;

    static final FileFilter FILTER_DIRECTORIES = File::isDirectory;

    static final String TEXTURE_PACKER_DIR = "texture";

    //By default, clear the tmp directory; can be disabled for debugging
    static final boolean CLEAR_TMP_ON_EXIT = true;

    //Setting up Paths
    //Origin of unprocessed assets
    static File fileRoot;
    //Working directory
    static File tmp;
    //Destination for processed assets
    static File output;

    public static void main(String[] args) {
        System.out.println(Arrays.toString(args));
        fileRoot = new File(args[0]);
        tmp = new File(args[1]);
        output = new File(args[2]);

        //Clear the tmp directory
        FileUtils.delDirRec(tmp);
        //Attempt to create the tmp directory for resources  in assets/build
        if (!tmp.exists() && !tmp.mkdirs())
            throw new RuntimeException(String.format("Could not create %s directory", tmp));

        if (CLEAR_TMP_ON_EXIT) tmp.deleteOnExit();


        //Clear the out directory
        FileUtils.delDirRec(output);
        //Attempt to create the out directory
        if (!output.exists() && !output.mkdirs())
            throw new RuntimeException(String.format("Could not create %s directory", output));

        //Read Excludes
        List<String> exclusionList = getExcludes(fileRoot);

        //Run TexturePacker after preprocessing unless excluded
        if (!exclusionList.contains(TEXTURE_PACKER_DIR)) {
            File texturePackerSrc = new File(fileRoot, TEXTURE_PACKER_DIR);
            if (texturePackerSrc.isDirectory()) {
                texturePacker(texturePackerSrc);
                exclusionList.add(TEXTURE_PACKER_DIR);
            }
        }

        //Copy remaining non-excluded files without processing
        copy(fileRoot, output, exclusionList);
    }

    /**
     * Loads the contents of the directories .exclude file, if available.
     *
     * @param dir Directory to read exclusions for.
     * @return List of all file and directory names that are supposed to be excluded from processing.
     */
    public static List<String> getExcludes(File dir) {
        //Change the group if the directory contains a descriptor
        File exclusions = new File(dir, EXCLUSIONS_NAME);
        List<String> exclusionList = new ArrayList<>();
        exclusionList.add(GROUP_DESCRIPTOR_NAME);
        exclusionList.add(EXCLUSIONS_NAME);
        if (exclusions.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(exclusions))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    exclusionList.add(line);
                }
            } catch (IOException e) {
                throw new RuntimeException("Couldn't read list of exclusions at: " + exclusions.getAbsolutePath() +
                        "\nStacktrace: " + e.getMessage());
            }
        }
        return exclusionList;
    }


    /**
     * Organizes textures into groups, determined by .group files, before compiling them into a TextureAtlas.
     * Every group corresponds to a separate set of pages. All pages of a set have to be loaded together during runtime.
     * The default group is 'misc'. The group for all files and subdirectories in a directory is determined the string in a .group file.
     * The group is inherited until overwritten by another .group file
     *
     * @param input The directory to process
     */
    private static void texturePacker(File input) {
        File packerRoot = new File(tmp, TEXTURE_PACKER_DIR);
        //Attempt to create the working directory for texturePacker  in assets/build/tmp/res
        if (!packerRoot.exists() && !packerRoot.mkdirs())
            throw new RuntimeException(String.format("Could not create %s directory", packerRoot));
        if (CLEAR_TMP_ON_EXIT) packerRoot.deleteOnExit();

        //Move every Texture to a directory corresponding to its group
        moveContents("misc", input, packerRoot);

        File texturePackerOut = new File(output, "texture_atlas");
        if (!texturePackerOut.exists() && !texturePackerOut.mkdirs())
            throw new RuntimeException(String.format("Could not create %s directory", texturePackerOut));

        TexturePacker.Settings settings = new TexturePacker.Settings();

        //Einstellungen, um die Skalierung auf NearestNeighbour zu setzen
        settings.filterMag = Texture.TextureFilter.Nearest;
        settings.filterMin = Texture.TextureFilter.Nearest;

        TexturePacker.process(settings, packerRoot.getAbsolutePath(), texturePackerOut.getAbsolutePath(), "TextureAtlas");
    }

    /**
     * Recursively moves Files to their correct group in tmp/res/text/texture directory to prepare for texture packing.
     * <p>
     * Groups are defined by a .group file in the directory: they contain the name of the group that should be used for
     * the files of this directory and all subdirectories unless overwritten.
     * If no group is given the group of the parent directory is assigned.
     * thus groups in subdirectories overwrite groups from higher directories.
     * <p>
     * .exclude files list the names of all files and subdirectories that should be excluded from processing
     * <p>
     * After moving all required files to their corresponding group, the subdirectories are traversed recursively.
     *
     * @param group               Parent-group, will be used if no .group file in the directory overwrites it
     * @param dir                 directory to be traversed
     * @param texturePackerOutput working-directory of the TexturePacker
     */
    private static void moveContents(String group, File dir, File texturePackerOutput) {
        if (dir == null) throw new RuntimeException("Subdirectory may not be null");
        if (!dir.exists()) throw new RuntimeException("Subdirectory at " + dir.getAbsolutePath() + " does not exist");


        //Change the group if the directory contains a descriptor
        File groupDescriptor = new File(dir, GROUP_DESCRIPTOR_NAME);
        if (groupDescriptor.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(groupDescriptor))) {
                group = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException("Couldn't read group descriptor at: " + groupDescriptor.getAbsolutePath() +
                        "\nStacktrace: " + e.getMessage());
            }
        }

        List<String> exclusionList = getExcludes(dir);

        //Copy all files to the correct group
        File[] content = dir.listFiles(FILTER_NORMAL_FILES);
        File groupPath = new File(texturePackerOutput, group);
        //Create the group directory if it doesn't exist already
        if (!groupPath.exists() && !groupPath.mkdirs())
            throw new RuntimeException(String.format("Could not create %s directory", groupPath));
        if (CLEAR_TMP_ON_EXIT) groupPath.deleteOnExit();
        if (content == null) return;
        Arrays.sort(content);
        for (File cur :
                content) {

            if (!exclusionList.contains(cur.getName()))
                try {
                    File dest = new File(groupPath, cur.getName());
                    Files.copy(cur.toPath(), dest.toPath());
                    if (CLEAR_TMP_ON_EXIT) dest.deleteOnExit();
                } catch (IOException e) {
                    throw new RuntimeException("Couldn't copy file at: " + cur.getAbsolutePath() +
                            "\nStacktrace: " + e.getMessage());
                }
        }

        //Recursively traverse every subdirectory
        File[] subDirs = dir.listFiles(FILTER_DIRECTORIES);
        if (subDirs == null) return;
        Arrays.sort(subDirs);
        for (File cur :
                subDirs) {
            if (!exclusionList.contains(cur.getName()))
                moveContents(group, cur, texturePackerOutput);
        }
    }

    /**
     * Recursively copies all files and subdirectories from one directory to another.
     * Ignores files and subDirs listed in .exclude files.
     * The excludes parameter is passed to give the caller an opportunity for selectively copying certain directories.
     *
     * @param srcDir   directory to copy files from
     * @param destDir  directory to copy files to
     * @param excludes list of files and directories to be excluded
     */
    private static void copy(File srcDir, File destDir, List<String> excludes) {

        File[] content = srcDir.listFiles(FILTER_NORMAL_FILES);
        if (content == null) return;
        for (File cur :
                content) {

            if (!excludes.contains(cur.getName()))
                try {
                    File dest = new File(destDir, cur.getName());
                    Files.copy(cur.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException("Couldn't copy file at: " + cur.getAbsolutePath() +
                            "\nStacktrace: " + e.getMessage());
                }
        }

        //Recursively traverse every subdirectory
        File[] subDirs = srcDir.listFiles(FILTER_DIRECTORIES);
        if (subDirs == null) return;
        for (File cur :
                subDirs) {
            if (!excludes.contains(cur.getName())) {
                File destCur = new File(destDir, cur.getName());
                if (!destCur.exists() && !destCur.mkdirs())
                    throw new RuntimeException(String.format("Could not create %s directory", destCur));
                List<String> newExcludes = getExcludes(cur);
                copy(cur, new File(destDir, cur.getName()), newExcludes);
            }
        }
    }


}
