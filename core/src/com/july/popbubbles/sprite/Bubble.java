package com.july.popbubbles.sprite;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.july.popbubbles.Assets;
import com.july.popbubbles.Constants;

public class Bubble extends GameObject implements Poolable {
	boolean tag = false; // true ��ʾ������ѡ��
	private int value; // ��ʾ�������ֶ���

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
	 * �ж��Ƿ���Կ�ʼ���𱾶���
	 * 
	 * @return true ��ʾ��������������
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
	 * �����Ϸ��ʼ�������һ�أ������������Ķ���
	 * 
	 * @param duration
	 */
	public void addInitAction(int duration) {
		moveDuration = duration;
		addY = Constants.height / moveDuration;
	}

	/**
	 * ��������ƶ��Ķ���
	 * 
	 * @param duration
	 *            �ƶ�ʱ��
	 * @param row
	 *            �ƶ�������
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
	 * ��������ƶ��Ķ���
	 * 
	 * @param duration
	 *            �ƶ�ʱ��
	 * @param column
	 *            �ƶ�������
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
