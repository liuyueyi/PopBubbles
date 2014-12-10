package com.july.popbubbles.sprite;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.july.popbubbles.MusicManager;

public class BubbleEffectManager {
	public static BubbleEffectManager manager = new BubbleEffectManager();

	Pool<BubbleEffect> bubbleEffectPool;
	Array<BubbleEffect> bubbleEffectArray;

	private BubbleEffectManager() {
		bubbleEffectPool = Pools.get(BubbleEffect.class);
		bubbleEffectArray = new Array<BubbleEffect>();
	}

	/**
	 * 自动回收所有出边界的bubbleEffect
	 */
	public void autoFree() {
		for (BubbleEffect b : bubbleEffectArray) {
			if (b.ifFree()) {
				bubbleEffectPool.free(b);
				bubbleEffectArray.removeValue(b, true);
			}
		}
	}

	public void show(int value, float x, float y) {
		MusicManager.manager.playSound(MusicManager.POP);
		for (int i = 0; i < 6; i++) {
			BubbleEffect bubble = bubbleEffectPool.obtain();
			bubble.init(value, x, y);
			bubbleEffectArray.add(bubble);
		}
	}

	public void draw(Batch batch) {
		for (BubbleEffect bubble : bubbleEffectArray) {
			bubble.draw(batch);
		}
	}
}
