package com.gats.ui.hud;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gats.simulation.GameCharacter;
import com.gats.simulation.WeaponType;
import com.gats.ui.Hud;


/**
 * This class is planned as an Interface between the Animator and the HudElements.
 * It provides the Animator an easy way to communicate/call Hud functions.
 */
public class UiMessenger {


	Hud hud;

	public UiMessenger(Hud hud){
		this.hud = hud;
	}

	/**
	 * Will update the current Inventory Display, with Information from the player but only on item with the weaponType.
	 * Call whenever the inventory of a player is changed.
	 * (e.g. new Weapon pickup or loss of ammunition)
	 */
	public void updateInventoryItem(GameCharacter currentPlayer, WeaponType weaponType){
		hud.getInventoryDrawer().updateItem(currentPlayer,weaponType);
	};




	/**
	 * Will update the current Inventory Display, with Information from the player.
	 * Call whenever the inventory of a player is changed.
	 * If only a single value changed call {@link UiMessenger#updateInventoryItem(GameCharacter, WeaponType)} with an additional parameter being the weapon index.
	 * (e.g. new Weapon pickup or loss of ammunition)
	 *
	 */
	public void changeInventory(GameCharacter currentPlayer){
		hud.getInventoryDrawer().changeInventory(currentPlayer);
	}
	public void changeSelectedWeapon(WeaponType weaponType){
		hud.getInventoryDrawer().setSelectedItem(weaponType);
	}

	/**
	 * Will call {@link Hud#createTurnChangePopup()} to temporarily draw it to the Hud.
	 * @param gameCharacter Sprite/Skin of the current/new Player -> could be implemented
	 */
	public void drawTurnChangePopup(TextureRegion gameCharacter){
		hud.createTurnChangePopup();
	}

	public void changeAnimationPlaybackSpeed(float speed){
		hud.setRenderingSpeed(speed);
	}

	public void turnChanged(GameCharacter currentPlayer){
		changeInventory(currentPlayer);
		drawTurnChangePopup(null);
	}

	public void playerShot(GameCharacter currentPlayer, WeaponType weapon){
		updateInventoryItem(currentPlayer,weapon);
	}

}
