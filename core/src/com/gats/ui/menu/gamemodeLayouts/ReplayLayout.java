package com.gats.ui.menu.gamemodeLayouts;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gats.ui.menu.Menu;
import com.gats.ui.menu.buttons.FileChooserButton;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

public class ReplayLayout extends GamemodeLayout{
	public ReplayLayout(Skin skin, Menu menuInstance) {
		super(skin, menuInstance);
		positionButtons(menuInstance);
	}

	@Override
	protected void positionButtons(Menu menu) {

		add(menu.getFileChooserButton());


	}
}
