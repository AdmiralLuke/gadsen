package com.gats.ui.hud;

import com.badlogic.gdx.graphics.Color;
import com.gats.simulation.GameCharacter;
import com.gats.simulation.GameState;
import com.gats.simulation.WeaponType;
import com.gats.ui.Hud;

import java.awt.*;


/**
 * This class is planned as an Interface between the Animator and the HudElements.
 * It provides the Animator an easy way to communicate/call Hud functions.
 */
public class UiMessenger {


	private Hud hud;

	public UiMessenger(Hud hud){
		this.hud = hud;
	}

	/**
	 * Applies the necessary changes to the Hud for a new turn.
	 * @param currentPlayer
	 */
	public void turnChanged(GameState state,GameCharacter currentPlayer, com.gats.animation.GameCharacter animPlayer){
		changeInventory(currentPlayer);
		drawTurnChangePopup(animPlayer.getTeamColor());
		refillStaminaBar(currentPlayer.getStamina());
		hud.adjustScores(state.getScores());
		//Todo update/notify every element so it sets its status to that of the current player
	}

	public void playerMoved(GameCharacter currentPlayer, int stamina){
		playerStaminaChanged(stamina);
	}
	/**
	 * Will update the current Inventory Display, with Information from the player but only on the item with the weaponType.
	 * Call whenever the inventory of a player is changed.
	 * (e.g. new Weapon pickup or loss of ammunition)
	 */
	public void changeSelectedWeapon(WeaponType weaponType){
		hud.getInventoryDrawer().setSelectedItem(weaponType);
	}


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
	 * If only a single value changed call {@link UiMessenger#updateInventoryItem(GameCharacter, WeaponType, int)} with an additional parameters being the weapon index and new ammo.
	 * (e.g. new Weapon pickup or loss of ammunition)
	 *
	 */
	private void changeInventory(GameCharacter currentPlayer){
		hud.getInventoryDrawer().changeInventory(currentPlayer);
	}

	public void updateInventoryItem(int team, WeaponType weaponType, int amount){
		hud.getInventoryDrawer().updateItem(team, weaponType, amount);
	};

	/**
	 * Will call {@link Hud#createTurnChangePopup(Color)} to temporarily draw it to the Hud.
	 * @param outlinecolor  Teamcolor of the current/new Player -> could be implemented
	 */
	private void drawTurnChangePopup(Color outlinecolor){
		hud.createTurnChangePopup(outlinecolor);
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
	 * Starts the turn timer for specified time.
	 * @param
	 */
	public void startTurnTimer(int turnTime,boolean currPlayerIsHuman){
		if(currPlayerIsHuman) {
			hud.startTurnTimer(turnTime);
		}
		else{
			stopTurnTimer();

		}
	}


	public void stopTurnTimer(){
		hud.stopTurnTimer();
	}

	public void gameEnded(boolean won, int team,boolean isDraw,Color color){
		hud.gameEnded(won,team,isDraw,color);

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
