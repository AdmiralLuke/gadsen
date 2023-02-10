package com.gats.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.gats.manager.Manager;
import com.gats.manager.RunConfiguration;
import com.gats.ui.menu.buttons.*;

public class Menu {

	/* Was ist für das Menu-Refactor wichtig?

     Buttons
     =======

     - "Aktionsknöpfe"
		- Führen etwas aus bei Berührung

	- Selectboxes
		- Maps
		- Bots

	- Slider
		- Teamanzahl und Größe

	- Titel


	- Hintergrund


	Dynamische Buttons
	==================

	- das Anpassen des Tables funktioniert nicht gut, da man von haus aus nichts removen kann
		- möglicherweise lässt sich das ändern durch eine neue Methode oder das verwenden einer anderen Klasse
			- ist das notwendig? bisher habe ich einen Weg gefunden, dass es funktioniert. dies ist jedoch nicht sehr effiizient

	- bisherige lösung reicht vermutlich aus, table anzupassen ist vermutlich zu aufwändig für ui kram


	- BotSelection als Klasse bzw Multi Selection, sollte sich anpassen lassen
		- Maximale länge angeben die der Table haben sollte?
		- Spaltenanzahl/größe

	- dann mithilfe der Slider einfach bestimmen wie viel Boxen gerendert werden
		- horizontal widget group anschauen?
		- bisher muss bei anpassung immer neuer Table gebaut werden
			- hinzufügen oder löschen eines elements wäre praktischer

		- evtl. manuelles positionieren oder eigenes Layout/Table schreiben.
			- bspw. Menü aufteilen in Zeilen, Elemente mit einem Set Speichern? oder als Matrix?

	*/

/*
	Plan
	manche Buttons in extra klassen auslagern
	getter und setter sowie button generation in eindeutigen bereiche verlagern

	Menu evtl noch in Gamemodespezifische "Screens"/Layouts aufgeteilen
	- ist aber an sich doch eher nicht notwendig


 */

	private SelectBox<String> gameModeSelector;
	private SelectBox<String> mapSelector;
	private SelectBox<Manager.NamedPlayerClass> botSelector;

	private RelationSlider teamSizeSlider;
	private TeamAmountSlider teamAmountSlider;
	//------------


	Table menuTable;
	RunConfiguration configuration;

	public Menu(Skin skin) {
		menuTable = new Table(skin);
		configuration = new RunConfiguration();

	}

	/**
	 * Supposed to get called when start button is pressed.
	 */
	public void startGame() {
		//if(configuration.isValid()){
		//need to check for player amount 0,0
			//Todo:Implement
		//}

	}

	Table buildLayoutTable(Skin skin) {

		//section for creating buttons

		createButtons(skin);
		StartButton startButton = new StartButton("Spiel starten",skin,this);
		ExitButton exitButton = new ExitButton("Beenden",skin);
		//------------------------


		Table menuTable = new Table(skin);
		menuTable.setFillParent(true);


		//platziert den Table an der oberen Kante des Bildschirms
		menuTable.top();

		//titelbild/überschrift

		//Todo get title image sprite
		TextureRegion titleImage = new TextureRegion();
		//Spieltitel wird in der ersten Zeile hinzugefügt und hat eine breite von 4 Spalten
		menuTable.add(new Image(titleImage)).colspan(4).pad(15).height(80).minWidth(titleImage.getRegionWidth());

		//menuTable.row(); erzeugt eine neue Zeile in der Tabelle
		menuTable.row();

		menuTable.add(startButton).colspan(4);
		menuTable.row();

		menuTable.add(gameModeSelector).pad(10).colspan(4);
		menuTable.row();

		menuTable.row();

		//ganz unten im  ist der Exit button
		menuTable.add(exitButton).colspan(4).pad(10);
		return menuTable;
	}


	/*-----------------------------------------------------
	* Button creation Zone
	/*
	 */

	public void createButtons(Skin skin){
		this.gameModeSelector = createGameModeSelector(skin);
		this.mapSelector = createMapSelector(skin);
		this.teamAmountSlider = createTeamAmountSlider(skin);
	}



	private <T> SelectBox<T> createGameModeSelector(Skin skin){
		return new SelectBox<T>(skin);
	}

	private SelectBox<String> createMapSelector(Skin skin) {
		SelectBox<String> mapSelector = new SelectBox<>(skin);
		mapSelector.setItems(new MapRetriever().listMaps());

		mapSelector.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				//teamAmountSlider.adjustTeamSizeToSpawnpoints();
			}
		});


		return mapSelector;
	}
	private TeamAmountSlider createTeamAmountSlider(Skin skin) {
		return null;
		//Todo
	}


}