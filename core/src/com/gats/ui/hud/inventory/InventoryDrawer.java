package com.gats.ui.hud.inventory;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.Align;
import com.gats.simulation.GameCharacter;
import com.gats.simulation.Weapon;
import com.gats.simulation.WeaponType;
import com.gats.ui.assets.AssetContainer;

import java.util.HashMap;

/**
 * Class responsible for Drawing the Inventory of the current GameCharacter.
 */
public class InventoryDrawer extends VerticalGroup {

	//Array to keep track of every cell. This way we are able to iterate over them.
	InventoryCell[] cells;
	int inventorySize;

	HashMap<WeaponType, TextureRegion> icons;

	public InventoryDrawer() {
		//Todo get Inventory size from sim
		inventorySize = WeaponType.values().length;
		cells = new InventoryCell[inventorySize];
		icons = AssetContainer.IngameAssets.weaponIcons;
		setupCells();
		left();
	}

	private void setupCells() {
		for (int i = 0; i < inventorySize; i++) {
			InventoryCell cell = new InventoryCell(AssetContainer.IngameAssets.inventoryCell);
			addActor(cell);
			cells[i] = cell;
		}
	}

	public void setScale(float scale) {
		for (InventoryCell cell : cells
		) {
			cell.scaleSizeBy(scale);
		}
	}

	public void setInventorySize(int size) {
		if (size != inventorySize) {

			InventoryCell[] newCells;
			newCells = new InventoryCell[size];
			for (int i = 0; i < inventorySize; i++) {
				newCells[i] = cells[i];
			}
			if (inventorySize < size) {
				for (int i = inventorySize; i < size; i++) {
					newCells[i] = new InventoryCell(AssetContainer.IngameAssets.inventoryCell);
				}
			}
			this.inventorySize = size;
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}

	/**
	 * gets the current weapons of a GameCharacter and stores them for drawing
	 * Could be solved a bit more efficiently, yet our inventories are very small so it would not be worth it.
	 * Alternatively we could create an Inventory for every GameCharacter and store it inside an array similar to the teams Array with 2 indexes.
	 * Then we could just get the informaation from there and update it, if necessary.
	 *
	 * @param character
	 */
	private void changeCharacterInventory(GameCharacter character) {
		int size = character.getWeaponAmount();
		for (int i = 0; i < size; i++) {
			TextureRegion weaponIcon = icons.get(character.getWeapon(i).getType());
			cells[i].setItem(weaponIcon);
		}
	}

	/**
	 * Updates the displayed inventory, with that of the current character.
	 * @param character
	 */
	public void updateInventory(GameCharacter character) {
		changeCharacterInventory(character);
	}

	/**
	 * Updates only the slot i of the currently displaying inventory.
	 * Call whenever an Event happens, changing a characters inventory.
	 * (e.g. shooting and reducing ammo, gaining a new Weapon)
	 * @param character
	 * @param i
	 */
	public void updateInventory(GameCharacter character, int i) {
		cells[i].setItem(icons.get(character.getWeapon(i).getType()));

		//Todo Implement ammo changes once we have pickup weapon Action
	}
}
