package com.july.popbubbles;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.july.popbubbles.sprite.BubbleFactory;

public class MainGame extends Game {

	@Override
	public void create() {
		Assets.instance.init();
		setScreen(new MenuScreen(this));
	}

	@Override
	public void dispose() {
		Gdx.app.log("maigame", "main game exit");
		Assets.instance.save();
		Assets.instance.dispose();
		BubbleFactory.instance.dispose();
	}
}
