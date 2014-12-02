package com.july.popbubbles.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.july.popbubbles.Assets;
import com.july.popbubbles.Constants;

public class ScoreTip extends Label implements Poolable {

	/**
	 * 添加一个用于显示添加分数的label
	 */
	public ScoreTip() {
		// TODO Auto-generated constructor stub
		super("", Assets.instance.tipStyle);
		setColor(Color.RED);
		setSize(Constants.bubbleWidth * 2, Constants.bubbleHeight);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		setText("");
		setColor(Color.RED);
		setSize(Constants.bubbleWidth * 2, Constants.bubbleHeight);
	}

	public void addAction() {
		Action a = Actions.moveBy(5, 5, Gdx.graphics.getDeltaTime() * 5);
		Action a1 = Actions.moveTo(Constants.scoreX + Constants.scoreWidth / 2,
				Constants.scoreY + Constants.scoreHeight / 2,
				Gdx.graphics.getDeltaTime() * 20);
		Action a2 = Actions.fadeOut(Gdx.graphics.getDeltaTime() * 20);
		addAction(Actions.sequence(a, Actions.parallel(a1, a2)));
	}
}
