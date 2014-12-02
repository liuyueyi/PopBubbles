package com.july.popbubbles.sprite;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.july.popbubbles.Assets;
import com.july.popbubbles.Constants;

public class BtnSprite extends Image {

	public BtnSprite(int tag, int index) {
		super(Assets.instance.btn[tag].btn[index]);
		setBounds(Assets.instance.btn[tag].rectangle.x,
				Assets.instance.btn[tag].rectangle.y,
				Assets.instance.btn[tag].rectangle.width,
				Assets.instance.btn[tag].rectangle.height);
		if (tag == Constants.STORE_BTN)
			addAction(0.08f);
		else if(tag == Constants.START_BTN)
			addAction(0.11f);
	}

	private void addAction(float time) {
		setOrigin(getWidth() / 2, getHeight() / 2);
		Action scal1 = Actions.scaleTo(1.01f, 1.01f,
				time);
		Action scal2 = Actions.scaleTo(0.97f, 0.97f,
				time);
		addAction(Actions.forever(Actions.sequence(scal1, scal2)));
	}
}
