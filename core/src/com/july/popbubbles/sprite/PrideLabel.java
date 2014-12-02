package com.july.popbubbles.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.july.popbubbles.Assets;
import com.july.popbubbles.Constants;

public class PrideLabel extends Label {
	public PrideLabel() {
		super("", Assets.instance.resultStyle);
		setColor(Color.BLACK);
		setSize(Constants.width, Constants.bubbleHeight * 4);
		setPosition(Constants.width, (Constants.height - getHeight()) / 2);
		setAlignment(Align.center);
	}

	private void addAction(int time) {
		setX(Constants.width);
		float dt = Gdx.graphics.getDeltaTime() * time / 3;
		Action a1 = Actions.moveTo(0, getY(), dt);
		Action a2 = Actions.moveTo(0, getY(), Gdx.graphics.getDeltaTime()
				* time - dt - dt);
		Action a3 = Actions.moveTo(-Constants.width, getY(), dt);

		addAction(Actions.sequence(a1, a2, a3));
	}

	/**
	 * 根据剩余的豆豆个数来设置奖励标签的内容
	 * 
	 * @param num
	 *            剩余的豆豆个数
	 * @return 奖励的分数
	 */
	public int setText(int num) {
		int score = calculateScore(num);
		setText("奖励" + score + "分\n" + "剩余豆豆" + num + "个");
		int time = 2 * num;
		if (time < 80)
			time = 100;
		else
			time += 20;
		addAction(time);
		return score;
	}

	private int calculateScore(int num) {
		if (num >= 10)
			return 0;

		return (2000 - num * num * 20);

	}
}
