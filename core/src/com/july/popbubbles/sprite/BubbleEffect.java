package com.july.popbubbles.sprite;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.july.popbubbles.Assets;
import com.july.popbubbles.Constants;

public class BubbleEffect extends GameObject implements Poolable {
	float time; // ʱ��
	Vector2 speed; // x�� y������ٶ�
	float accelerate; // y ������ٶ�
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
	 * �ж϶����Ƿ���Ա�����
	 * 
	 * @return true ���Ա�����
	 */
	public boolean ifFree() {
		return rectangle.x < -rectangle.width || rectangle.x > Constants.width
				|| rectangle.y < -rectangle.height;

	}

	/**
	 * ��ʼ�����𶹶�����ֵ�С����Ч��
	 * 
	 * @param speedX
	 *            x����ĳ��ٶȣ���Ϊ����
	 * @param speedY
	 *            y����ĳ��ٶȣ���Ϊ����
	 * @param accelerate
	 *            y������ٶȣ�һ��Ϊ��
	 * @param type
	 *            ��ʾ��С��������
	 * @param x
	 *            ���𶹶���x����
	 * @param y
	 *            ���𶹶���y����
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
