package com.july.popbubbles.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.july.popbubbles.Assets;
import com.july.popbubbles.Constants;
import com.july.popbubbles.GameScreen;
import com.july.popbubbles.MusicManager;

public class Failure {
	GameScreen gameScreen;
	Stage stage;
	TextureRegion region;
	Image bg;
	Label score;
	Label restart;
	Label retry;
	float time;
	private boolean show = false;

	public Failure(GameScreen gameScreen, int value) {
		this.gameScreen = gameScreen;
		stage = new Stage();

		bg = new Image(Assets.instance.effectAtlas.findRegion("glory"));
		bg.setBounds(0, (Constants.height - Constants.width) / 2,
				Constants.width, Constants.width);
		bg.setOrigin(bg.getWidth() / 2, bg.getHeight() / 2);
		stage.addActor(bg);

		score = new FailLabel("" + value);
		score.setColor(Color.BLUE);
		score.setFontScale(1.5f);
		score.setPosition(0, bg.getY() + bg.getHeight() / 2 - score.getHeight()
				/ 3);
		stage.addActor(score);

		time = 8;
		retry = new FailLabel(time + "s后继续");
		retry.setColor(Color.BLACK);
		retry.setPosition(0, score.getY() - retry.getHeight() * 1.2f);
		stage.addActor(retry);
		retry.addListener(listener);

		restart = new FailLabel("重新开始");
		restart.setColor(Color.DARK_GRAY);
		restart.setPosition(0, retry.getY() - restart.getHeight());
		stage.addActor(restart);
		restart.addListener(listener);

		region = Assets.instance.effectAtlas.findRegion("gloryBg");
	}

	EventListener listener = new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			MusicManager.manager.playSound(MusicManager.BUTTON);
			if (event.getListenerActor() == restart) { //
				gameScreen.game.setScreen(new GameScreen(gameScreen.game));
				gameScreen.dispose();
			} else if (event.getListenerActor() == retry) {
				if (Assets.instance.heart >= 5) {
					gameScreen.setInputProcessor();
					gameScreen.restart();
				} else {
					gameScreen.game.setScreen(new GameScreen(gameScreen.game));
					gameScreen.dispose();
				}
			}
		}
	};

	class FailLabel extends Label {
		public FailLabel(String text) {
			super(text, Assets.instance.resultStyle);
			setAlignment(Align.center);
			setSize(Constants.width, Constants.bubbleHeight * 2);
		}
	}

	public void show() {
		show = true;
		time = 8;
		Gdx.input.setInputProcessor(stage);
		bg.addAction(Actions.forever(Actions.rotateBy(0.5f)));
		MusicManager.manager.playSound(MusicManager.GAMEOVER);
	}

	public void draw(Batch batch) {
		batch.begin();
		batch.draw(region, 0, 0, Constants.width, Constants.height);
		batch.end();
		if (show) {
			time -= Gdx.graphics.getDeltaTime();
			retry.setText((int) time + "s后继续");
			if (time < 1) {
				show = false;
				if (Assets.instance.heart >= 5) {
					gameScreen.setInputProcessor();
					gameScreen.restart();
				} else {
					gameScreen.game.setScreen(new GameScreen(gameScreen.game));
					gameScreen.dispose();
				}
			}
		}
		stage.draw();
		stage.act();
	}

	public void dispose() {
		stage.dispose();
	}
}
