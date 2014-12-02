package com.july.popbubbles.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.july.popbubbles.Constants;

/**
 * 用于显示连消后的荣耀成就
 * 
 * @author Administrator
 * 
 */
public class Honour extends Image {
	private TextureRegion back;

	public Honour(int flag, TextureAtlas atlas) {
		switch (flag) {
		case Constants.HONOUR_GOOD:
			back = atlas.findRegion("good");
			break;
		case Constants.HONOUR_COOL:
			back = atlas.findRegion("cool");
			break;
		case Constants.HONOUR_GREAT:
			back = atlas.findRegion("great");
			break;
		case Constants.HONOUR_AMAZING:
			back = atlas.findRegion("amazing");
			break;
		}
		setDrawable(new TextureRegionDrawable(back));
		setBounds(0, 0, 0, 0);
	}

	public void show() {
		setBounds(Constants.honourX, Constants.honourY, Constants.honourWidth,
				Constants.honourHeight);
		setOrigin(getWidth() / 2, getHeight() / 2);
		addAction();
	}

	private void addAction() {
		Action a1 = Actions.scaleTo(0.99f, 0.99f,
				Gdx.graphics.getDeltaTime() * 2);
		Action a2 = Actions.scaleTo(1, 1, Gdx.graphics.getDeltaTime() * 2);
		Action a3 = Actions.repeat(4, Actions.sequence(a1, a2));
		Action a4 = Actions
				.scaleTo(0.5f, 0.5f, Gdx.graphics.getDeltaTime() * 8);
		Action a5 = Actions
				.scaleTo(0.5f, 0.5f, Gdx.graphics.getDeltaTime() * 8);
		Action a6 = Actions.scaleTo(0, 0);
		addAction(Actions.sequence(a3, a4, a5, a6));
	}
}
