package com.july.popbubbles.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.july.popbubbles.Assets;
import com.july.popbubbles.Constants;
import com.july.popbubbles.MusicManager;

/**
 * 道具管理器
 * 
 * @author wzb
 * 
 */
public class PropsManager {
	public static final PropsManager manager = new PropsManager();

	TextureRegion tool;
	private float x, y, width, height;
	private float addx, addy, addWidth, addHeight;
	public int type;
	int row, column;
	float time = 0;
	boolean over; // 表征道具效果是否结束的标志
	boolean acted; // 表示是否开始显示移动道具动画（主要是针对炸弹和锤子）
	Animation anim;
	float stateTime = 0;
	TextureRegion bombs[];

	public PropsManager() {
		bombs = new TextureRegion[5];
		bombs[1] = Assets.instance.effectAtlas.findRegion("bomb", 0);
		bombs[0] = Assets.instance.effectAtlas.findRegion("bomb", 1);
		bombs[2] = bombs[0];
		bombs[3] = bombs[1];
		bombs[4] = bombs[0];
	}

	public void show(int type) {
		if (acted) // 正使用道具的时候不接受道具切换
			return;

		time = 0;
		stateTime = 0;
		acted = false;
		over = false;
		this.type = type;
		switch (type) {
		case Constants.BOMB_BTN:
			anim = new Animation(0.07f, bombs);
			tool = Assets.instance.effectAtlas.findRegion("bomb", 0);
			height = Constants.bubbleWidth;
			width = height * 59 / 67f;
			x = Constants.bombBtnX;
			y = Constants.toolBtnY - height;
			break;
		case Constants.HAMMER_BTN:
			anim = new Animation(0.1f,
					Assets.instance.effectAtlas.findRegions("Anim_Hammer"));
			tool = Assets.instance.effectAtlas.findRegion("Anim_Hammer", 1);
			height = Constants.bubbleHeight;
			width = height * 131 / 101f;
			x = Constants.hammerBtnX;
			y = Constants.toolBtnY - height;
			break;
		case Constants.COLOR_BTN:
			tool = Assets.instance.gameAtlas.findRegion("d", 8);
			width = Constants.bubbleWidth * 0.7f;
			height = width;
			x = Constants.colorBtnX + width * 0.15f;
			y = Constants.toolBtnY - height;
			break;
		case Constants.FRESH_BTN:
			width = Constants.width;
			height = width * 347f / 363f;
			x = 0;
			y = (Constants.width - height) / 2;
			tool = Assets.instance.effectAtlas.findRegion("Anim_Redo");
			rotation = 1;
			count = 20;
			break;
		}
	}

	/**
	 * 
	 * @param row
	 * @param column
	 */
	private boolean choosed = false; // 表示选中了执行道具的位置之后，不能在进行修改，当道具效果结束后，自动设置为false

	public void act(int row, int column) {
		if (choosed)
			return;
		choosed = true;

		if (type == Constants.FRESH_BTN) {
			MusicManager.manager.playSound(MusicManager.FRESH);
			acted = true;
			return;
		}

		this.row = row;
		this.column = column;
		addAction(Constants.bubbleWidth * column, Constants.bubbleHeight * row);
	}

	private void addAction(float x, float y) {
		time = 10;
		switch (type) {
		case Constants.BOMB_BTN:
			addx = (x - this.x) / time;
			addy = (y - this.y) / time;
			addWidth = (Constants.bubbleWidth * 1.2f - width) / time;
			addHeight = addWidth * 67 / 59f;
			break;
		case Constants.HAMMER_BTN:
			addHeight = (Constants.bubbleHeight - height) / time;
			addWidth = addHeight * 101 / 131f;
			addx = (x - this.x) / time;
			addy = (y - this.y) / time;
			break;
		case Constants.COLOR_BTN:
			addWidth = (Constants.bubbleWidth - width) / time;
			addHeight = addWidth;
			addx = (x - this.x) / time;
			addy = (y - this.y) / time;
			break;
		}
		time = 1;
	}

	/**
	 * 绘制界面
	 * 
	 * @param batch
	 */
	int rotation = 1;
	int count = 20;
	Color c;

	public void draw(Batch batch, float parentAlpha) {
		if (type == Constants.FRESH_BTN) {
			c = batch.getColor();
			batch.begin();
			if (rotation % count == 0) {
				if (acted) {
					batch.setColor(c.r, c.g, c.b, c.a * (2 - 0.1f * count));
					if (++count == 20) {
						acted = false;
						choosed = false;
						over = true;
					}
				} else if (count > 10)
					count--;
				BubbleFactory.instance.freshBubbles();
			}
			if (rotation == 360)
				rotation = 0;

			batch.draw(tool, x, y, width / 2, height / 2, width, height, 1, 1,
					(++rotation) % 360);
			batch.end();
			batch.setColor(c);
			return;
		}

		batch.begin();
		if (!acted) {
			if (time != 0) { // 表示绘制移动过程
				if (time++ == 10) {
					acted = true;
					MusicManager.manager.playSound(type);
				}

				x += addx;
				y += addy;
				width += addWidth;
				height += addHeight;
				batch.draw(tool, x, y, width, height);
			} else {
				batch.draw(tool, (float) (x + Math.random() * 2 - 1),
						(float) (y + Math.random() * 2 - 1), (float) (width
								+ Math.random() * 2 - 1), (float) (height
								+ Math.random() * 2 - 1));
			}
		} else {
			// batch.draw(tool, x, y, width, height);
			drawEffect(batch);
		}
		batch.end();
	}

	private void drawEffect(Batch batch) {
		if (type == Constants.COLOR_BTN) {
			acted = false;
			over = true;
			choosed = false;
			return;
		}

		stateTime += Gdx.graphics.getDeltaTime();
		if (anim.isAnimationFinished(stateTime)) {
			acted = false;
			over = true;
			choosed = false;
		} else {
			tool = anim.getKeyFrame(stateTime, false);
			batch.draw(tool, x, y, width, height);
		}

	}

	public boolean isOver() {
		return over;
	}
}
