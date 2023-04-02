package com.gats.ui.hud;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gats.simulation.GameCharacter;
import com.gats.ui.hud.inventory.InventoryDrawer;


/**
 * This class is planned as an Interface between the Animator and the HudElements.
 * It provides the Animator an easy way to communicate/call Hud functions.
 */
public class UiMessenger {

	InventoryDrawer inventory;
	TurnSplashScreen turnSplashScreen;


	public UiMessenger(InventoryDrawer inventory, TurnSplashScreen turnSplashScreen){
		this.inventory = inventory;
		this.turnSplashScreen = turnSplashScreen;
	}

	/**
	 * Will update the current Inventory Display, with Information from the player but only on item i.
	 * Call whenever the inventory of a player is changed.
	 * (e.g. new Weapon pickup or loss of ammunition)
	 */
	public void updateInventory(GameCharacter currentPlayer,int index){
		inventory.updateInventory(currentPlayer,index);
	};

	/**
	 * Will update the current Inventory Display, with Information from the player.
	 * Call whenever the inventory of a player is changed.
	 * If only a single value changed call this function with an additional parameter being the weapon index.
	 * (e.g. new Weapon pickup or loss of ammunition)
	 *
	 */
	public void updateInventory(GameCharacter currentPlayer){
		inventory.updateInventory(currentPlayer);
	}

	/**
	 * Will call the {@link TurnSplashScreen} to draw itself with the current Sprite of the Character that owns the new Turn.
	 * @param gameCharacter Sprite of the current/new Player
	 */
	public void drawTurnSplashScreen(TextureRegion gameCharacter){}

}
