package com.gats.ui.hud;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gats.simulation.GameCharacter;
import com.gats.simulation.WeaponType;
import com.gats.ui.Hud;
import com.gats.ui.assets.AssetContainer;


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
	 * Applies the necessary changes to the Hud for a new turn.
	 * @param currentPlayer
	 */
	public void turnChanged(GameCharacter currentPlayer){
		changeInventory(currentPlayer);
		drawTurnChangePopup(null);
		refillStaminaBar(currentPlayer.getStamina());
		//Todo update/notify every element so it sets its status to that of the current player
	}

	public void playerMoved(GameCharacter currentPlayer){
		playerStaminaChanged(currentPlayer.getStamina());
	}
	/**
	 * Will update the current Inventory Display, with Information from the player but only on the item with the weaponType.
	 * Call whenever the inventory of a player is changed.
	 * (e.g. new Weapon pickup or loss of ammunition)
	 */
	public void changeSelectedWeapon(WeaponType weaponType){
		hud.getInventoryDrawer().setSelectedItem(weaponType);
	}
	public void updateInventoryItem(GameCharacter currentPlayer, WeaponType weaponType){
		hud.getInventoryDrawer().updateItem(currentPlayer,weaponType);
	};


	/**
	 * Update the ui Elements when a player aimed.
	 * @param angle
	 * @param strength
	 */
	public void playerAimed(float angle,float strength){
		hud.setAimIndicatorValues(angle,strength);
	}

	/**
	 * Will update the current Inventory Display, with Information from the player.
	 * Call whenever the inventory of a player is changed.
	 * If only a single value changed call {@link UiMessenger#updateInventoryItem(GameCharacter, WeaponType)} with an additional parameter being the weapon index.
	 * (e.g. new Weapon pickup or loss of ammunition)
	 *
	 */
	private void changeInventory(GameCharacter currentPlayer){
		hud.getInventoryDrawer().changeInventory(currentPlayer);
	}


	/**
	 * Will call {@link Hud#createTurnChangePopup()} to temporarily draw it to the Hud.
	 * @param gameCharacter Sprite/Skin of the current/new Player -> could be implemented
	 */
	private void drawTurnChangePopup(TextureRegion gameCharacter){
		hud.createTurnChangePopup();
	}

	/**
	 * Changes the rendering speed of animation
	 * @param speed
	 */
	public void changeAnimationPlaybackSpeed(float speed){
		hud.setRenderingSpeed(speed);
	}



	/**
	 * Pass the the turnTime to the Hud for displaying it
	 * @param time
	 */
	public void setTurnTimeLeft(int time){
		hud.setTurntimeRemaining(time);
	}


	/**
	 * Start the turn Timer
	 */
	public void startTurnTimer(){hud.startTurnTimer();};


	public void stopTurnTimer(){
		hud.stopTurnTimer();
	}

	public void gameEnded(boolean won, int team){
		hud.gameEnded(won,team);

	}

	/**
	 * Calls necessary functions to update Ui Elements after a player shot.
	 * @param currentPlayer
	 * @param weapon
	 */



	public void playerShot(GameCharacter currentPlayer, WeaponType weapon){
		updateInventoryItem(currentPlayer,weapon);
	}

	/**
	 * Update the stamina of the current player
	 */
	private void playerStaminaChanged(int stamina){
			hud.updateCurrentStamina(stamina);
	}

	private void refillStaminaBar(int maxStamina){
		hud.setMaxStamina(maxStamina);
		hud.updateCurrentStamina(maxStamina);
	}


}
