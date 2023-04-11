package com.gats.ui.hud;

import com.badlogic.gdx.InputProcessor;
import com.gats.manager.HumanPlayer;

import java.util.Collection;
import java.util.List;

public interface GadsenInputProcessor extends InputProcessor {


	public void setHumanPlayers(List<HumanPlayer> players);
	public void activateTurn(HumanPlayer currentPlayer);

	public void endTurn();

}
