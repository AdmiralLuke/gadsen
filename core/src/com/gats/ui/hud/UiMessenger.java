package com.gats.ui.hud;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gats.simulation.GameCharacter;
import com.gats.simulation.Weapon;
import com.gats.simulation.WeaponType;
import com.gats.ui.InGameScreen;
import com.gats.ui.hud.inventory.InventoryDrawer;


/**
 * This class is planned as an Interface between the Animator and the HudElements.
 * It provides the Animator an easy way to communicate/call Hud functions.
 */
public class UiMessenger {

	InventoryDrawer inventory;
	TurnSplashScreen turnSplashScreen;

	InGameScreen inGameScreen;

	public UiMessenger(InGameScreen inGameScreen, InventoryDrawer inventory, TurnSplashScreen turnSplashScreen){
		this.inGameScreen = inGameScreen;
		this.inventory = inventory;
		this.turnSplashScreen = turnSplashScreen;
	}

	/**
	 * Will update the current Inventory Display, with Information from the player but only on item with the weaponType.
	 * Call whenever the inventory of a player is changed.
	 * (e.g. new Weapon pickup or loss of ammunition)
	 */
	public void updateInventoryItem(GameCharacter currentPlayer, WeaponType weaponType){
		inventory.updateItem(currentPlayer,weaponType);
	};




	/**
	 * Will update the current Inventory Display, with Information from the player.
	 * Call whenever the inventory of a player is changed.
	 * If only a single value changed call {@link UiMessenger#updateInventoryItem(GameCharacter, WeaponType)} with an additional parameter being the weapon index.
	 * (e.g. new Weapon pickup or loss of ammunition)
	 *
	 */
	public void changeInventory(GameCharacter currentPlayer){
		inventory.changeInventory(currentPlayer);
	}
	public void changeSelectedWeapon(WeaponType weaponType){
		inventory.setSelectedItem(weaponType);
	}

	/**
	 * Will call the {@link TurnSplashScreen} to draw itself with the current Sprite of the Character that owns the new Turn.
	 * @param gameCharacter Sprite of the current/new Player
	 */
	public void drawTurnSplashScreen(TextureRegion gameCharacter){}

	public void changeAnimationPlaybackSpeed(float speed){
		inGameScreen.setRenderingSpeed(speed);
	}

	public void turnChanged(GameCharacter currentPlayer){
		changeInventory(currentPlayer);
		drawTurnSplashScreen(null);
	}

	public void playerShot(GameCharacter currentPlayer, WeaponType weapon){
		updateInventoryItem(currentPlayer,weapon);
	}

}
