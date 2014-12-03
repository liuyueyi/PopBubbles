package com.july.popbubbles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.july.popbubbles.dialog.Store;
import com.july.popbubbles.sprite.BtnSprite;

public class MenuScreen extends MyScreen {
	MainGame game;

	SpriteBatch batch;
	Stage stage;
	InputMultiplexer mul;

	Image startBtn;
	Image courseBtn;
	Image musicBtn;
	Image soundBtn;
	Image storeBtn;

	boolean showStore;

	public MenuScreen(MainGame game) {
		this.game = game;

		stage = new Stage();
		batch = new SpriteBatch();
		mul = new InputMultiplexer();
		mul.addProcessor(this);
		mul.addProcessor(stage);
		showStore = false;
		Gdx.input.setInputProcessor(mul);
		Gdx.input.setCatchBackKey(true);
	}

	EventListener btnClickListener = new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			if (event.getListenerActor() == startBtn) {
				dispose();
				game.setScreen(new GameScreen(game));
			} else if (event.getListenerActor() == courseBtn) {
			} else if (event.getListenerActor() == musicBtn) {
				if (Assets.instance.musicOn) {
					musicBtn.setDrawable(Assets.instance.btn[Constants.MUSIC_BTN].btn[1]);
					Assets.instance.musicOn = false;
				} else {
					musicBtn.setDrawable(Assets.instance.btn[Constants.MUSIC_BTN].btn[0]);
					Assets.instance.musicOn = true;
				}
			} else if (event.getListenerActor() == soundBtn) {
				if (Assets.instance.soundOn) {
					soundBtn.setDrawable(Assets.instance.btn[Constants.SOUND_BTN].btn[1]);
					Assets.instance.soundOn = false;
				} else {
					soundBtn.setDrawable(Assets.instance.btn[Constants.SOUND_BTN].btn[0]);
					Assets.instance.soundOn = true;
				}
			} else if (event.getListenerActor() == storeBtn) {
				showStore = true;
				Store.store.show(MenuScreen.this);
			}
		}
	};

	@Override
	public void setInputProcessor() {
		showStore = false;
		Gdx.input.setInputProcessor(mul);
	}

	@Override
	public void show() {
		startBtn = new BtnSprite(Constants.START_BTN, 0);
		startBtn.addListener(btnClickListener);
		stage.addActor(startBtn);

		courseBtn = new BtnSprite(Constants.COURSE_BTN, 0);
		courseBtn.addListener(btnClickListener);
		stage.addActor(courseBtn);

		if (Assets.instance.musicOn)
			musicBtn = new BtnSprite(Constants.MUSIC_BTN, 0);
		else
			musicBtn = new BtnSprite(Constants.MUSIC_BTN, 1);
		musicBtn.addListener(btnClickListener);
		stage.addActor(musicBtn);

		if (Assets.instance.soundOn)
			soundBtn = new BtnSprite(Constants.SOUND_BTN, 0);
		else
			soundBtn = new BtnSprite(Constants.SOUND_BTN, 1);
		soundBtn.addListener(btnClickListener);
		stage.addActor(soundBtn);

		storeBtn = new BtnSprite(Constants.STORE_BTN, 0);
		storeBtn.addListener(btnClickListener);
		stage.addActor(storeBtn);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		Assets.instance.bg[Constants.MENU_BG].draw(batch);
		batch.end();

		stage.draw();
		stage.act();

		if (showStore)
			Store.store.draw(batch);
	}

	@Override
	public void dispose() {
		Gdx.app.log("Menu", "menu dispose");
		stage.dispose();
		batch.dispose();
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		if (keycode == Keys.BACK || keycode == Keys.A) {
			// exit game
			Gdx.app.log("wzb", "exit");
			Gdx.app.exit();
			return true;
		}
		return false;
	}
}
