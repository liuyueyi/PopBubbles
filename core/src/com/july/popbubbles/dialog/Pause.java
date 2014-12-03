package com.july.popbubbles.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.july.popbubbles.Assets;
import com.july.popbubbles.Constants;
import com.july.popbubbles.GameScreen;
import com.july.popbubbles.MenuScreen;
import com.july.popbubbles.sprite.BtnSprite;

public class Pause {
	GameScreen gameScreen;

	protected Stage stage;

	Image continueBtn;
	Image backBtn;
	Image newBtn;

	public Pause(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
		stage = new Stage();

		continueBtn = new BtnSprite(Constants.CONTINUE_BTN, 0);
		continueBtn.addListener(listener);
		stage.addActor(continueBtn);

		backBtn = new BtnSprite(Constants.BACK_BTN, 0);
		backBtn.addListener(listener);
		stage.addActor(backBtn);

		newBtn = new BtnSprite(Constants.NEW_BTN, 0);
		newBtn.addListener(listener);
		stage.addActor(newBtn);
	}

	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	EventListener listener = new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			if (event.getListenerActor() == continueBtn) {
				gameScreen.setInputProcessor();
			} else if (event.getListenerActor() == backBtn) { // 返回菜单，要求记录当前状态
//				gameScreen.saveState();
				gameScreen.game.setScreen(new MenuScreen(gameScreen.game));
				gameScreen.pause();
				gameScreen.dispose();
			} else { // 重新开始
				gameScreen.game.setScreen(new GameScreen(gameScreen.game));
				gameScreen.dispose();
			}
		}
	};

	public void draw(Batch batch) {
		batch.begin();
		Assets.instance.bg[Constants.RESULT_BG].draw(batch);
		batch.end();

		stage.draw();
		stage.act();
	}

	public void dispose() {
		stage.dispose();
	}
}
