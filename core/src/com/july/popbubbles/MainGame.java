package com.july.popbubbles;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.july.popbubbles.sprite.BubbleFactory;

public class MainGame extends Game {
	public BushEvent event;

	public MainGame() {

	}

	public MainGame(BushEvent bushEvent) {
		// TODO Auto-generated constructor stub
		this.event = bushEvent;
	}

	@Override
	public void create() {
		Assets.instance.init();
		MusicManager.manager.loadMusic();
		MusicManager.manager.loadSound();
		MusicManager.manager.playMusic();
		setScreen(new MenuScreen(this));
	}

	@Override
	public void dispose() {
		Gdx.app.log("maigame", "main game exit");
		Assets.instance.save();
		Assets.instance.dispose();
		MusicManager.manager.dispose();
		BubbleFactory.instance.dispose();
	}
}
