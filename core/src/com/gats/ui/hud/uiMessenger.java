package com.gats.ui.hud;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gats.simulation.GameCharacter;
import com.gats.ui.hud.InventoryDrawer;
import com.gats.ui.hud.TurnSplashScreen;

import java.util.Collection;


/**
 * This class is planned as an Interface between the Animator and the HudElements.
 * It provides the Animator an easy way to communicate/call Hud functions.
 */
public class uiMessenger {

	InventoryDrawer inventory;
	TurnSplashScreen turnSplashScreen;


	public uiMessenger (InventoryDrawer inventory, TurnSplashScreen turnSplashScreen){
		this.inventory = inventory;
		this.turnSplashScreen = turnSplashScreen;
	}

	/**
	 * Passes the inventory a new Collection of WeaponIcons.
	 * The Order should be the same as the {@link com.gats.simulation.GameCharacter} Weapons array.
	 * @param weaponIcons collection of the current players Inventory.
	 */
	public void updateInventory(Collection<TextureRegion> weaponIcons, GameCharacter currentPlayer){};

	/**
	 * Will call the {@link TurnSplashScreen} to draw itself with the current Sprite of the Character that owns the new Turn.
	 * @param gameCharacter Sprite of the current/new Player
	 */
	public void drawTurnSplashScreen(TextureRegion gameCharacter){}

}
