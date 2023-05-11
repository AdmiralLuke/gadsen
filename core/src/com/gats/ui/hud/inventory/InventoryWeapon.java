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

	private Weapon weapon;
	private TextureRegion icon;

	private int index;
	public InventoryWeapon(Weapon weapon, TextureRegion icon){
		{
			this.weapon = weapon;
			this.icon = icon;
		}
	}

	public Weapon getWeapon(){
		return weapon;
	}
	public WeaponType getWeaponType(){
		return weapon.getType();
	}

	public TextureRegion getIcon() {
		return icon;
	}

}
