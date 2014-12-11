package com.july.popbubbles.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.july.popbubbles.Assets;
import com.july.popbubbles.Constants;
import com.july.popbubbles.MusicManager;
import com.july.popbubbles.MyScreen;

public class Store {
	// public static Store store = new Store();
	MyScreen screen;

	Stage stage;
	MyTextButton one;
	MyTextButton two;
	MyTextButton five;
	Image close;
	Label heartNum;
	TextButtonStyle buttonStyle;

	public Store() {
		stage = new Stage();
		buttonStyle = new TextButtonStyle(Assets.instance.btnDrwa,
				Assets.instance.btnDrwaOff, Assets.instance.btnDrwa,
				Assets.instance.tip);
		buttonStyle.fontColor = Color.BLUE;
		buttonStyle.downFontColor = Color.DARK_GRAY;

		one = new MyTextButton("гд1");
		one.setY(Constants.storeY + Constants.storeHeight * 0.58f);
		stage.addActor(one);

		two = new MyTextButton("гд2");
		two.setY(Constants.storeY + Constants.storeHeight * 0.38f);
		stage.addActor(two);

		five = new MyTextButton("гд5");
		five.setY(Constants.storeY + Constants.storeHeight * 0.16f);
		stage.addActor(five);

		close = new Image(Assets.instance.storeAtlas.findRegion("Common_Close"));
		close.setPosition(Constants.storeX + Constants.storeWidth * 0.74f,
				Constants.storeY + Constants.storeHeight * 0.86f);
		close.setWidth(Constants.storeWidth * 0.13f);
		close.setHeight(close.getWidth());
		close.addListener(listener);
		stage.addActor(close);

		heartNum = new Label("" + Assets.instance.heart, Assets.instance.tipStyle);
		heartNum.setColor(Color.BLUE);
		heartNum.setAlignment(Align.center);
		heartNum.setWidth(Constants.width);
		heartNum.setHeight(one.getHeight());
		heartNum.setPosition(0, one.getY() + one.getHeight() * 1.35f);
		stage.addActor(heartNum);
	}

	private EventListener listener = new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			if (event.getListenerActor() == one) {
				screen.game.event.notify(screen.game, Constants.PAY1);
			} else if (event.getListenerActor() == two) {
				screen.game.event.notify(screen.game, Constants.PAY2);
			} else if (event.getListenerActor() == five) {
				screen.game.event.notify(screen.game, Constants.PAY3);
			} else { // close button clicked
				MusicManager.manager.playSound(MusicManager.BUTTON);
				screen.setInputProcessor();
				return;
			}
			MusicManager.manager.playSound(MusicManager.ADDCOIN);
		}
	};

	private class MyTextButton extends TextButton {
		public MyTextButton(String text) {
			super(text, buttonStyle);
			setSize(0.22f * Constants.storeWidth, 0.1f * Constants.storeHeight);
			setX(0.66f * Constants.storeWidth + Constants.storeX);
			addListener(listener);
		}
	}

	public void updateHeartNum() {
		heartNum.setText("" + Assets.instance.heart);
	}

	public void show(MyScreen screen) {
		this.screen = screen;
		Gdx.input.setInputProcessor(stage);
	}

	public void draw(Batch batch) {
		batch.begin();
		Assets.instance.bg[Constants.STORE_BG].draw(batch);
		batch.end();

		stage.draw();
	}
}
