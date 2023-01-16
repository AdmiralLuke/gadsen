package com.gats;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.gats.ui.GADS;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setOpenGLEmulation(Lwjgl3ApplicationConfiguration.GLEmulation.GL20, 3, 3);
		config.setForegroundFPS(60);
		config.setTitle("G.A.D.S.E.N.");
		new Lwjgl3Application(new GADS(), config);
	}
}
