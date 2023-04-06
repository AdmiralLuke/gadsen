package com.gats.animation.action.uiActions;

import com.gats.animation.action.Action;
import com.gats.ui.hud.UiMessenger;

public abstract class NotifyUiAction extends Action {
	UiMessenger uiMessenger;
	public NotifyUiAction(float start, UiMessenger uiMessenger) {
		super(start);
		this.uiMessenger = uiMessenger;
	}

}
