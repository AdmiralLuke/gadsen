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

    private static final Options cliOptions = new Options();

    static {

        cliOptions.addOption(Option
                .builder("?")
                .longOpt("help")
                .desc("Prints this list").build());

        cliOptions.addOption(Option
                .builder("m")
                .longOpt("map")
                .hasArg()
                .desc("(Required for -n) String name of the map without extension").build());

        cliOptions.addOption(Option
                .builder("p")
                .longOpt("players")
                .hasArg()
                .desc("(Required for -n) Names of the bots class files without extension in format \"Bot1 Bot2 Bot3\" \n Attention: Case-sensitive!").build());

        cliOptions.addOption(Option
                .builder("g")
                .longOpt("gamemode")
                .hasArg()
                .type(Number.class)
                .desc("GameMode to be played (Default: 0)\n" +
                        "  0 - Normal\n" +
                        "  1 - Campaign\n" +
                        "  2 - Exam Admission\n" +
                        "  3 - Tournament: Phase 1\n" +
                        "  4 - Tournament: Phase 2").build());

        cliOptions.addOption(Option.builder("n")
                .longOpt("nogui")
                .desc("Runs the simulation without animation").build());

        cliOptions.addOption(Option
                .builder("s")
                .longOpt("teamsize")
                .hasArg()
                .desc("Number of players per team, Default: 3").build());


    }

    public static void main(String[] args) {

        CommandLineParser parser = new DefaultParser();
        CommandLine params;
        try {
            if (parser.parse(new Options().addOption("?", "help", false, "Prints this list"), args).hasOption("?")) {
                printHelp();
                return;
            }
            params = parser.parse(cliOptions, args);
        } catch (ParseException e) {
            printHelp();
            return;
        }
        RunConfiguration runConfig = new RunConfiguration();
        runConfig.gui = !params.hasOption("n");
        runConfig.mapName = params.getOptionValue("m", null);
        if (params.hasOption("p")) runConfig.players = Manager.getPlayers(params.getOptionValue("p").trim().split("\\s+"), !runConfig.gui);
        runConfig.gameMode = Integer.parseInt(params.getOptionValue("g", "0"));
        runConfig.teamSize = Integer.parseInt(params.getOptionValue("s", "3"));
        if (runConfig.gui) {
            Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
            config.setForegroundFPS(60);
            config.setTitle("G.A.D.S.E.N.");
            config.setWindowIcon(Files.FileType.Classpath, "icon/icon.png");
            new Lwjgl3Application(new GADS(runConfig), config);
        } else {
            boolean invalidConfig = false;
            if (runConfig.mapName == null){
                System.err.println("Param -m is required for no GUI mode");
                invalidConfig = true;
            }
            if (runConfig.players == null){
                System.err.println("Param -m is required for no GUI mode");
                invalidConfig = true;
            } else if (runConfig.players.size() < 2) {
                System.err.println("At least two players are required");
                invalidConfig = true;
            }
            if (invalidConfig){
                printHelp();
                return;
            }
            Manager manager = new Manager(runConfig);
            manager.start();
            //ToDo: wait for and print results to console
        }
    }

    private static void printHelp() {
        String header = "\n\n";
        String footer = "\nPlease report issues at wettbewerb@acagamics.de";

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar gadsen-1.1.jar", header, cliOptions, footer, true);
    }
}
