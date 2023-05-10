package com.gats.ui.hud.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.Disposable;
import com.gats.manager.RunConfiguration;
import com.gats.simulation.GameCharacter;
import com.gats.simulation.WeaponType;

/**
 * Class responsible for Drawing the Inventory of the current GameCharacter.
 */
public class InventoryDrawer extends Container<Inventory>{

	Inventory[] playerInventory;
	Inventory current;

	public InventoryDrawer(RunConfiguration runConfiguration) {
		playerInventory = new Inventory[runConfiguration.teamCount];
		current = new Inventory(6);
		setActor(current);
	}

	/**
	 * gets the current weapons of a GameCharacter and stores them for drawing
	 * Could be solved a bit more efficiently, yet our inventories are very small so it would not be worth it.
	 * Currently working on the efficient solution.
	 * Alternatively we could create an Inventory for every GameCharacter and store it inside an array similar to the teams Array with 2 indexes.
	 * Then we could just get the information from there and update it, if necessary.
	 *
	 * @param character
	 */
	private Inventory createCharacterInventory(GameCharacter character) {
		return new Inventory(character);
	}


	/**
	 * Updates the displayed inventory, with that of the current character.
	 *
	 * @param character
	 */
	public void changeInventory(GameCharacter character) {

		checkPlayerInventoryExists(character);
		current = playerInventory[character.getTeam()];
		this.setActor(current);
	}

	/**
	 * Updates only the slot with first applicable WeaponType of the currently displaying inventory.
	 * Call whenever an Event happens, changing a characters inventory.
	 * (e.g. shooting and reducing ammo, gaining a new Weapon)
	 *
	 * @param character
	 * @param weaponType
	 */

	public void updateItem(GameCharacter character, WeaponType weaponType) {

		checkPlayerInventoryExists(character);
		current = playerInventory[character.getTeam()];
		current.updateItem(character, weaponType);
	}

	private void checkPlayerInventoryExists(GameCharacter character) {
		Inventory currInv = playerInventory[character.getTeam()];
		if (currInv == null) {
			playerInventory[character.getTeam()] = createCharacterInventory(character);
		}

	}

	public void setSelectedItem(WeaponType weaponType) {
		current.setSelectedItem(weaponType);
	}



}