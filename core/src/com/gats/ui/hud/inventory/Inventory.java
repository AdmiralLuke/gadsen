package com.gats.ui.hud.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.gats.simulation.GameCharacter;
import com.gats.simulation.Weapon;
import com.gats.simulation.WeaponType;
import com.gats.ui.assets.AssetContainer;

import java.lang.reflect.Array;
import java.util.*;

public class Inventory extends VerticalGroup {

	public Map<WeaponType, Integer> itemsIndex;


	private int selectedItemIndex;
	private ArrayList<InventoryCell> items;
	public Inventory(int size){
		itemsIndex = new HashMap<WeaponType, Integer>(size);
		items = new ArrayList<InventoryCell>();
		for(int i =0;i<size;i++){
			InventoryCell cell = new InventoryCell(AssetContainer.IngameAssets.inventoryCell);
			items.add(cell);
			addActor(cell);
		}

	}

	/**
	 * Creates an {@link Inventory} from a {@link GameCharacter} current weapons.
	 * @param character
	 */
	public Inventory(GameCharacter character){
		this(character.getWeaponAmount());
		for(int i=0;i<character.getWeaponAmount();i++){
			updateItem(character,i);
		}
	}

	public void updateItem(GameCharacter character,int index){

		Weapon current = character.getWeapon(index);
		items.get(index).setWeapon(current);
		itemsIndex.put(current.getType(),index);

	}

	public void updateItem(GameCharacter character,WeaponType weaponType){
		updateItem(character,itemsIndex.get(weaponType));
	}

	public void setSelectedItem(WeaponType newSelected){
			int	newSelectedIndex = itemsIndex.get(newSelected);
			if(selectedItemIndex >=0) {
				items.get(selectedItemIndex).setSelected(false);
			}
			if((newSelectedIndex>=0)&&(newSelectedIndex < items.size())){

				items.get(newSelectedIndex).setSelected(true);
			}

			selectedItemIndex = newSelectedIndex;

	}
	/**
	 * Returns a list of InventoryWeapons of the Characters inventory.
	 * @param character
	 * @return
	 */
	public List<InventoryWeapon> getCharacterWeapons(GameCharacter character){
		List<InventoryWeapon> weapons = new ArrayList<InventoryWeapon>(character.getWeaponAmount());

		for(int i=0;i<character.getWeaponAmount();i++) {
			Weapon current = character.getWeapon(i);
			weapons.add(new InventoryWeapon(current, AssetContainer.IngameAssets.weaponIcons.get(current.getType())));
		}

		return weapons;
	}
}
