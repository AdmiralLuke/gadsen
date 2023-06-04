package com.gats.ui.hud.inventory;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gats.simulation.GameCharacter;
import com.gats.simulation.weapons.Weapon;
import com.gats.simulation.WeaponType;
import com.gats.ui.assets.AssetContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Container class to store information for the various weapon Types
 */
public class InventoryWeapon {

	private final WeaponType weaponType;
	private int count;
	private final TextureRegion icon;

	public InventoryWeapon(WeaponType type, TextureRegion icon){
		{
			this.weaponType = type;
			this.count = 0;
			this.icon = icon;
		}
	}

	public void setCount(int count) {
		this.count = count;
	}

	public WeaponType getWeaponType(){
		return weaponType;
	}

	public int getCount() {
		return count;
	}

	public TextureRegion getIcon() {
		return icon;
	}

}
