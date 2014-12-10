package com.july.popbubbles.sprite;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.july.popbubbles.Assets;
import com.july.popbubbles.Constants;

public class BubbleFactory {
	public static BubbleFactory instance = new BubbleFactory();
	Pool<Bubble> bubblePools = Pools.get(Bubble.class);
	Bubble[][] bubbles;

	Pool<ScoreTip> scoreTipPools = Pools.get(ScoreTip.class);
	public Array<ScoreTip> scoreArray = new Array<ScoreTip>();

	// 保存每一次消除的豆子个数
	public int removeCount = 0;

	// 当消除豆子之后，需要重设此变量为true，进行预判断，以验证是否结束本关
	boolean preJudge = false;

	private BubbleFactory() {
		bubbles = new Bubble[10][10];
	}

	public void init() {
		preJudge = false;
		removeCount = 0;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				Bubble bubble = bubblePools.obtain();
				bubble.init(i, j, (int) (Math.random() * 5));
				bubbles[i][j] = bubble;
			}
		}
	}

	private StringBuffer bubbleValueBuffer;

	public void saveState() {
		bubbleValueBuffer = new StringBuffer();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (bubbles[i][j] == null)
					bubbleValueBuffer.append(9);
				else
					bubbleValueBuffer.append(bubbles[i][j].getValue());
			}
		}
		Assets.instance.recordPreference.putString("array",
				bubbleValueBuffer.toString()).flush();
		// Gdx.app.log("wzb", "save record" + bubbleValueBuffer.toString());
	}

	public void loadState() {
		// System.out.println("load stat " +
		// Assets.instance.recordPreference.getBoolean("ifLoad", false));
		if (!Assets.instance.recordPreference.getBoolean("ifLoad", false)) {
			init();
			return;
		}

		String array = Assets.instance.recordPreference.getString("array", "");
		// System.out.println(array);
		if (array.equals("")) {
			init();
			return;
		}
		preJudge = false;
		removeCount = 0;
		int index = 0;
		int tempValue = 9;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				tempValue = Integer.parseInt("" + array.charAt(index++));
				// System.out.print(tempValue + " .... ");
				if (tempValue == 9) {
					bubbles[i][j] = null;
					continue;
				}
				Bubble bubble = bubblePools.obtain();
				bubble.init(i, j, tempValue);
				bubbles[i][j] = bubble;
			}
		}
	}

	/**
	 * 每次消除豆子后，需要判断本关是否结束
	 * 
	 * @return true 表示游戏结束
	 */
	public boolean ifOver() {
		if (!preJudge) {
			return false;
		}

		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++) {
				if (bubbles[i][j] != null
						&& (bubbles[i][j].compareTo(bubbles[i + 1][j]) || bubbles[i][j]
								.compareTo(bubbles[i][j + 1])))
					return false;
			}

		for (int j = 0; j < 9; j++) { // 判断最后一行
			if (bubbles[9][j] == null)
				break;

			if (bubbles[9][j].compareTo(bubbles[9][j + 1]))
				return false;
		}

		for (int i = 0; i < 9; i++) { // 判断之后一列
			if (bubbles[i][9] == null)
				break;

			if (bubbles[i][9].compareTo(bubbles[i + 1][9]))
				return false;
		}
		return true;
	}

	/**
	 * 判断bubbles[row][column] 是否为空
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public boolean containBubble(int row, int column) {
		return bubbles[row][column] != null;
	}

	// ********************************************************
	// 使用道具后的豆豆消灭逻辑
	/**
	 * 标记豆豆立即消灭
	 * 
	 * @param row
	 * @param column
	 */
	private boolean tagBubble(int row, int column) {
		if (row < 0 || row > 9 || column < 0 || column > 9
				|| bubbles[row][column] == null)
			return false;
		bubbles[row][column].tag = true;
		bubbles[row][column].waitDuration = 0;
		return true;
	}

	private void tagThreeRowBubbles(int row, int column) {
		int count = 2;
		if (tagBubble(row - 1, column))
			count = 3;

		if (!tagBubble(row, column))
			return;

		if (!tagBubble(row + 1, column))
			return;

		for (int i = row + 2; i < 10; i++) {
			if (bubbles[i][column] == null)
				break;
			bubbles[i][column].addMoveDownAction(10, 0, count);
			Bubble tmp = bubbles[i][column];
			bubbles[i][column] = bubbles[i - count][column];
			bubbles[i - count][column] = tmp;
		}
	}

	private void bombNineBubbles(int row, int column) {
		tagThreeRowBubbles(row, column);
		tagThreeRowBubbles(row, column - 1);
		tagThreeRowBubbles(row, column + 1);
	}

	private void hammerBubble(int row, int column) {
		tagBubble(row, column);
		for (int i = row + 1; i < 10; i++) {
			if (bubbles[i][column] == null)
				break;
			bubbles[i][column].addMoveDownAction(10, 0, 1);
			Bubble tmp = bubbles[i][column];
			bubbles[i][column] = bubbles[i - 1][column];
			bubbles[i - 1][column] = tmp;
		}
	}

	private boolean moveLeftAfterProps = false; // 一个用于使用道具后，判断剩下的豆豆是否自动左移动的标记，true表示左移

	public void freshBubbles() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (bubbles[i][j] != null)
					bubbles[i][j].setValue((int) (Math.random() * 5));
			}
		}
		preJudge = true;
	}

	public void removeBubblesByProps() {
		switch (PropsManager.manager.type) {
		case Constants.COLOR_BTN:
			bubbles[PropsManager.manager.row][PropsManager.manager.column]
					.setValue(7);
			return;
		case Constants.HAMMER_BTN:
			hammerBubble(PropsManager.manager.row, PropsManager.manager.column);
			moveLeftAfterProps = true;
			break;
		case Constants.BOMB_BTN:
			bombNineBubbles(PropsManager.manager.row,
					PropsManager.manager.column);
			moveLeftAfterProps = true;
			break;
		}
	}

	// 道具消灭豆豆逻辑结束
	// ********************************************************************************

	/**
	 * bubbles[row][column]被点击,若存在可以消除的豆豆时，标记出来，并稍后自动清除可消灭的豆豆
	 * 
	 * @param row
	 * @param column
	 * @return true表示可以消除豆豆
	 */
	public boolean clicked(int row, int column) {
		// Gdx.app.log("factory", "row " + row + " column " + column);
		if (!containBubble(row, column))
			return false;

		// clear score object
		for (ScoreTip tip : scoreArray) {
			tip.clear();
			scoreTipPools.free(tip);
		}
		scoreArray.clear();

		if (judge(row, column)) {
			autoRemoveBubbles();
			prefTipScore = 5;
			return true;
		}
		return false;
	}

	public boolean judge(final int r, final int c) {
		if (r < 9 && bubbles[r + 1][c] != null) { // judge up
			if (bubbles[r + 1][c].tag)
				bubbles[r][c].tag = true;
			else if (bubbles[r][c].compareTo(bubbles[r + 1][c])) {
				bubbles[r][c].tag = true;
				judge(r + 1, c);
			}
		}

		if (c < 9 && bubbles[r][c + 1] != null) { // judge right
			if (bubbles[r][c + 1].tag)
				bubbles[r][c].tag = true;
			else if (bubbles[r][c].compareTo(bubbles[r][c + 1])) {
				bubbles[r][c].tag = true;
				judge(r, c + 1);
			}
		}

		if (r > 0) {
			// judge down
			if (bubbles[r - 1][c].tag)
				bubbles[r][c].tag = true;
			else if (bubbles[r][c].compareTo(bubbles[r - 1][c])) {
				bubbles[r][c].tag = true;
				judge(r - 1, c);
			}
		}

		if (c > 0 && bubbles[r][c - 1] != null) {
			if (bubbles[r][c - 1].tag)
				bubbles[r][c].tag = true;
			else if (bubbles[r][c].compareTo(bubbles[r][c - 1])) {
				bubbles[r][c].tag = true;
				judge(r, c - 1);
			}
		}

		return bubbles[r][c].tag;
	}

	/**
	 * 豆豆被点击后，若存在可以消除的豆豆，则执行本方法，为所有可消灭和可向下移动的豆豆添加动画
	 */
		public void autoRemoveBubbles() {
		for (int r = 0; r < 10; r++)
			for (int c = 0; c < 10; c++) {
				if (bubbles[r][c] == null)
					continue;
				if (bubbles[r][c].tag)
					removeCount++;
			}

		int wait = 0;
		int count;
		for (int c = 0; c < 10; c++) {
			count = 0;
			for (int r = 0; r < 10; r++) {
				if (bubbles[r][c] == null)
					continue;
				if (bubbles[r][c].tag) { // 记录消除的总个数和当前列的个数
					bubbles[r][c].waitDuration = wait;
					wait += 4;
					count++;
				} else if (count > 0) { // 添加移动动画，设置等待时间，交换在数组中的位置
					bubbles[r][c].addMoveDownAction(10, (removeCount) * 4,
							count);
					Bubble tmp = bubbles[r - count][c];
					bubbles[r - count][c] = bubbles[r][c];
					bubbles[r][c] = tmp;
				}
			}
		}
	}

	/**
	 * 消灭豆豆后，需要判断是否可以左移，如果可以为相应的豆豆添加移动动画
	 */
	public void autoMoveLeft() {
		int moveColumn = 0;
		for (int c = 0; c < 10; c++) {
			if (bubbles[0][c] == null) {
				moveColumn++;
				continue;
			}
			if (moveColumn > 0) {
				for (int r = 0; r < 10; r++) {
					if (bubbles[r][c] == null)
						continue;

					bubbles[r][c].addMoveLeftAction(10, 10, moveColumn);
					bubbles[r][c - moveColumn] = bubbles[r][c];
					bubbles[r][c] = null;
				}
			}
		}
		preJudge = true;
	}

	/**
	 * 游戏结束后，自动消灭所有剩余的豆豆
	 * 
	 * @return 返回剩余的豆豆个数的两倍
	 */
	public int removeMantain() {
		removeCount = 0;
		int count = 0;
		for (int c = 0; c < 10; c++)
			for (int r = 0; r < 10; r++) {
				if (bubbles[r][c] == null)
					continue;

				bubbles[r][c].tag = true;
				count += 2;
				bubbles[r][c].waitDuration = count;
			}
		return count;
	}

	int prefTipScore = 5;

	public void draw(SpriteBatch batch) {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (bubbles[i][j] == null)
					continue;
				else if (bubbles[i][j].autoRemoved()) {
					removeBubble(i, j); // 移除可以消除的豆豆
				} else
					bubbles[i][j].draw(batch);
			}
		}

		if (moveLeftAfterProps) {
			autoMoveLeft();
			moveLeftAfterProps = false;
		}
		BubbleEffectManager.manager.draw(batch);
	}

	private void removeBubble(int i, int j) {
		if (removeCount > 0) {
			removeCount--;
			// 添加分数
			ScoreTip tip = scoreTipPools.obtain();
			tip.setPosition(bubbles[i][j].rectangle.x + Constants.bubbleWidth
					/ 2, bubbles[i][j].rectangle.y + Constants.bubbleHeight / 2);
			tip.setText(prefTipScore + "");
			prefTipScore += 10;
			tip.addAction();
			scoreArray.add(tip);

			// 显示豆豆消除的效果
			BubbleEffectManager.manager.show(bubbles[i][j].getValue(),
					bubbles[i][j].rectangle.x, bubbles[i][j].rectangle.y);
			bubblePools.free(bubbles[i][j]);
			bubbles[i][j] = null;

			if (removeCount == 0) {
				// move left
				autoMoveLeft();
			}
		} else {
			// 显示豆豆消除的效果
			BubbleEffectManager.manager.show(bubbles[i][j].getValue(),
					bubbles[i][j].rectangle.x, bubbles[i][j].rectangle.y);
			bubblePools.free(bubbles[i][j]);
			bubbles[i][j] = null;
		}
	}

	public void dispose() {
		bubblePools.clear();
	}
}
