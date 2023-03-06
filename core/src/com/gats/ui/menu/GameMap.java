package com.gats.ui.menu;

public class GameMap {

	private final String mapName;
	private final int numberOfSpawnpoints;


	public GameMap(String name, int spawnpoints){
		this.mapName=name;
		this.numberOfSpawnpoints=spawnpoints;

		readFromFile();
	}


	public String getName(){
		return this.mapName;
	}

	public String toString(){
		return getName();
	}

	public int getNumberOfSpawnpoints(){
		return this.numberOfSpawnpoints;
	}

	public void readFromFile(){

	}
}
