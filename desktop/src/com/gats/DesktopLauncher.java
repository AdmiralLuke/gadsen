package com.gats;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.gats.manager.Manager;
import com.gats.manager.RunConfiguration;
import com.gats.simulation.Tile;
import com.gats.ui.GADS;
import org.apache.commons.cli.*;

import java.util.Arrays;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
    public static void main(String[] args) throws ParseException {

        CommandLine params = parseArgs(args);

        if (!params.hasOption("n")) {

            Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
            config.setForegroundFPS(60);
            config.setTitle("G.A.D.S.E.N.");
            config.setWindowIcon(Files.FileType.Classpath, "icon/icon.png");
            //ToDo forward runconfig to main menu
            new Lwjgl3Application(new GADS(), config);
        } else {

            RunConfiguration runConfig = new RunConfiguration();
            runConfig.gui = false;
            if (params.hasOption("g")) runConfig.gameMode = Integer.parseInt(params.getOptionValue("g"));
            runConfig.mapName = params.getOptionValue("m");
            if (params.hasOption("s")) runConfig.teamSize = Integer.parseInt(params.getOptionValue("s", "3"));
            runConfig.players = Manager.getPlayers(params.getOptionValue("p").trim().split("\\s+"));
            Manager manager = new Manager(runConfig);
            manager.start();
            //ToDo: wait for and print results to console
        }
    }

    private static CommandLine parseArgs(String[] args) throws ParseException {
        Options options = new Options();

        options.addOption(Option.builder("n")
                .longOpt("nogui")
                .desc("Runs the simulation without animation").build());

        options.addOption(Option
                .builder("g")
                .longOpt("gamemode")
                .hasArg()
                .type(Number.class)
                .desc("GameMode to be played").build());

        options.addOption(Option
                .builder("m")
                .longOpt("map")
                .hasArg()
                .required()
                .desc("String name of the map without extension").build());

        options.addOption(Option
                .builder("s")
                .longOpt("teamsize")
                .hasArg()
                .desc("Number of players per team, Default: 3").build());

        options.addOption(Option
                .builder("p")
                .longOpt("players")
                .hasArg()
                .required()
                .desc("Names of the bots class files without extension in Format \"Bot1 Bot2 Bot3\" \n Attention: Case-sensitive!").build());


        CommandLineParser parser = new DefaultParser();
        return parser.parse(options, args);
    }
}
