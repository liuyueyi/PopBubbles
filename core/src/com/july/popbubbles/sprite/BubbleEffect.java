package com.july.popbubbles.sprite;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.july.popbubbles.Assets;
import com.july.popbubbles.Constants;

public class BubbleEffect extends GameObject implements Poolable {
	float time; // 时间
	Vector2 speed; // x， y方向的速度
	float accelerate; // y 方向加速度
	TextureRegion back;
	float preY;

	public BubbleEffect() {
		speed = new Vector2();
		rectangle = new Rectangle();
		reset();
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		rectangle.x = 0;
		rectangle.y = 0;
		rectangle.width = 0;
		rectangle.height = 0;
		speed.x = 0;
		speed.y = 0;
		time = 0;
		accelerate = 0;
	}

	/**
	 * 判断对象是否可以被回收
	 * 
	 * @return true 可以被回收
	 */
	public boolean ifFree() {
		return rectangle.x < -rectangle.width || rectangle.x > Constants.width
				|| rectangle.y < -rectangle.height;

	}

	/**
	 * 初始化消灭豆豆后出现的小豆子效果
	 * 
	 * @param speedX
	 *            x方向的初速度，可为正负
	 * @param speedY
	 *            y方向的初速度，可为正负
	 * @param accelerate
	 *            y方向加速度，一般为正
	 * @param type
	 *            显示的小豆子类型
	 * @param x
	 *            消灭豆豆的x坐标
	 * @param y
	 *            消灭豆豆的y坐标
	 */

	public void init(float speedX, float speedY, int type, float x, float y) {
		time = 0;
		speed.x = speedX;
		speed.y = speedY;
		this.accelerate = -1f;
		back = Assets.instance.bubbles[type];
		rectangle.x = x;
		rectangle.y = y;
		float rate = (float) (0.2f + Math.random() * 0.5f);
		rectangle.width = Constants.bubbleWidth * rate;
		rectangle.height = Constants.bubbleHeight * rate;
		preY = y;
	}

	private int generateSymble() {
		if (Math.random() > 0.5f)
			return 1;
		else
			return -1;
	}

	public void init(int type, float x, float y) {
		
		float speedx = (float) (generateSymble() * (Math.random() * 5 + 8));
		float speedy = (float) ((Math.random() * 35 - 10));
		init(speedx, speedy, type, x, y);
//		System.out.println("speed x: " + speedx);
	}

	public void draw(Batch batch) {
		if (ifFree())
			return;
		time += 0.4f;
		preY += speed.y + accelerate * time + accelerate / 2;
		batch.draw(back, rectangle.x + speed.x * time, rectangle.y + time
				* (speed.y + time * accelerate), rectangle.width,
				rectangle.height);

	}
}
