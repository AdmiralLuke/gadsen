package com.gats.ui.hud.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.gats.simulation.GameCharacter;
import com.gats.simulation.weapons.Weapon;
import com.gats.simulation.WeaponType;
import com.gats.ui.assets.AssetContainer;

import java.util.*;

public class Inventory extends VerticalGroup {


	private int selectedItemIndex;
	private InventoryCell[] items;

	public Inventory(int size){
		items = new InventoryCell[WeaponType.values().length -1];
		for(int i =0;i<size-1;i++){
			InventoryCell cell = new InventoryCell(AssetContainer.IngameAssets.inventoryCell, WeaponType.values()[i]);
			items[i]=cell;
			addActor(cell);
		}
	}

	/**
	 * Creates an {@link Inventory} from a {@link GameCharacter} current weapons.
	 * @param character
	 */
	public Inventory(GameCharacter character){
		this(WeaponType.values().length);
		for(int i=0;i<character.getWeaponAmount();i++){
			updateItem(WeaponType.values()[i], character.getWeapon(i).getAmmo());
		}
	}

	public void updateItem(WeaponType weaponType, int amount){
		items[weaponType.ordinal()].setAmmo(amount);
	}


	/**
	 * Sets the selected weapon in the inventory, to draw the outline
	 * @param newSelected
	 */
	public void setSelectedItem(WeaponType newSelected){

		InventoryCell selectedweapon;

		int	newSelectedIndex = newSelected == null? -1:newSelected.ordinal();

		//if current weapon was selected, set to false
		if(selectedItemIndex >=0) {
			items[selectedItemIndex].setSelected(false);
		}
		if(newSelectedIndex>=0)
			items[newSelectedIndex].setSelected(true);

		selectedItemIndex = newSelectedIndex;
	}


	public void setCellSize(float width,float height){
		for (InventoryCell cell:items) {
		//	cell.setSize(width,height);
		}
		//sizeChanged();
	}

}
