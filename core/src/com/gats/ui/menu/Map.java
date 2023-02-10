package com.gats.ui.menu;

public class Map {

	private final String mapName;
	private final int numberOfSpawnpoints;


	public Map(String name,int spawnpoints){
		this.mapName=name;
		this.numberOfSpawnpoints=spawnpoints;
	}

	public String getMapName(){
		return this.mapName;
	}

	public int getNumberOfSpawnpoints(){
		return this.numberOfSpawnpoints;
	}
}
