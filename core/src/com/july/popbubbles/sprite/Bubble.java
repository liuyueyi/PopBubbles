package com.july.popbubbles.sprite;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.july.popbubbles.Assets;
import com.july.popbubbles.Constants;

public class Bubble extends GameObject implements Poolable {
	boolean tag = false; // true 表示被消除选中
	private int value; // 表示代表那种豆子

	boolean moveLeft = false;
	boolean moveDown = false;
	float addX = 0;
	float addY = 0;
	int moveDuration = 0;
	int waitDuration = 0;

	public Bubble() {
		rectangle = new Rectangle(0, Constants.height, Constants.bubbleWidth,
				Constants.bubbleHeight);
	}

	public void init(int row, int column, int value) {
		this.value = value;
		rectangle.x = column * rectangle.width;
		rectangle.y = row * rectangle.height;
		moveLeft = false;
		moveDown = false;
		addX = 0;
		addY = 0;
		moveDuration = 0;
		waitDuration = 0;
		tag = false;
		addInitAction(15);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		rectangle.x = 0;
		rectangle.y = Constants.height;
		tag = false;
		moveLeft = false;
		moveDown = false;
		addX = 0;
		addY = 0;
		moveDuration = 0;
		waitDuration = 0;
	}

	public boolean compareTo(Bubble bubble) {
		if (bubble == null)
			return false;
		return this.value == bubble.getValue() || this.value == 7
				|| bubble.getValue() == 7;
	}

	/**
	 * 判断是否可以开始消灭本豆豆
	 * 
	 * @return true 表示可以消除本豆豆
	 */
	public boolean autoRemoved() {
		return tag && waitDuration <= 0;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	/**
	 * 添加游戏开始或进入下一关，豆豆进入界面的动画
	 * 
	 * @param duration
	 */
	public void addInitAction(int duration) {
		moveDuration = duration;
		addY = Constants.height / moveDuration;
	}

	/**
	 * 添加向下移动的动画
	 * 
	 * @param duration
	 *            移动时间
	 * @param row
	 *            移动多少行
	 */
	public void addMoveDownAction(int duration, int waitduration, int row) {
		moveDown = true;
		moveDuration = duration;
		waitDuration = waitduration;
		addX = 0;
		addY = row * rectangle.height / duration;
		rectangle.y -= row * rectangle.height;
	}

	/**
	 * 添加向左移动的动画
	 * 
	 * @param duration
	 *            移动时间
	 * @param column
	 *            移动多少列
	 */
	public void addMoveLeftAction(int duration, int waitduration, int column) {
		moveLeft = true;
		moveDuration = duration;
		waitDuration = waitduration;
		addY = 0;
		addX = column * rectangle.width / duration;
		rectangle.x -= column * rectangle.width;
	}

	public void draw(SpriteBatch batch) {
		if (waitDuration > 0) {
			waitDuration--;
		}
		if (waitDuration <= 0 && moveDuration > 0) {
			if (moveDuration-- == 0) {
				moveLeft = false;
				moveDown = false;
			}
		}
		batch.draw(Assets.instance.bubbles[value], rectangle.x + addX
				* moveDuration, rectangle.y + addY * moveDuration,
				rectangle.width, rectangle.height);
	}
}
