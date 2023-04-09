package com.gats.animation.action.uiActions;

import com.gats.animation.action.Action;
import com.gats.ui.hud.UiMessenger;

/**
 * Action for updating the ui with specific changes via the {@link UiMessenger}
 */
public abstract class MessageUiAction extends Action {
	UiMessenger uiMessenger;
	public MessageUiAction(float start, UiMessenger uiMessenger) {
		super(start);
		this.uiMessenger = uiMessenger;
	}

}
