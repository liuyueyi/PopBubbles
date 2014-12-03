package com.july.popbubbles.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.july.popbubbles.Assets;
import com.july.popbubbles.Constants;
import com.july.popbubbles.MyScreen;

public class Store {
	public static Store store = new Store();
	MyScreen screen;

	Stage stage;
	MyLabel one;
	MyLabel tow;
	MyLabel five;
	Image close;
	Label heartNum;
	private LabelStyle style;

	private Store() {
		stage = new Stage();
		style = new LabelStyle(Assets.instance.tip, Color.BLACK);
		style.background = Assets.instance.btnDrwa;

		one = new MyLabel("гд1");
		one.setY(Constants.storeY + Constants.storeHeight * 0.58f);
		stage.addActor(one);

		tow = new MyLabel("гд2");
		tow.setY(Constants.storeY + Constants.storeHeight * 0.38f);
		stage.addActor(tow);

		five = new MyLabel("гд5");
		five.setY(Constants.storeY + Constants.storeHeight * 0.16f);
		stage.addActor(five);

		close = new Image(Assets.instance.storeAtlas.findRegion("Common_Close"));
		close.setPosition(Constants.storeX + Constants.storeWidth * 0.74f,
				Constants.storeY + Constants.storeHeight * 0.86f);
		close.setWidth(Constants.storeWidth * 0.13f);
		close.setHeight(close.getWidth());
		close.addListener(listener);
		stage.addActor(close);

		heartNum = new Label("100", Assets.instance.tipStyle);
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
				Gdx.app.log("store", "one yuan");
			} else if (event.getListenerActor() == tow) {
				Gdx.app.log("store", "one yuan");
			} else if (event.getListenerActor() == five) {
				Gdx.app.log("store", "one yuan");
			} else { // close button clicked
				screen.setInputProcessor();
			}
		}
	};

	private class MyLabel extends Label {
		public MyLabel(String text) {
			super(text, style);
			setSize(0.22f * Constants.storeWidth, 0.1f * Constants.storeHeight);
			setX(0.66f * Constants.storeWidth + Constants.storeX);
			addListener(listener);
			setAlignment(Align.center);
		}
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
