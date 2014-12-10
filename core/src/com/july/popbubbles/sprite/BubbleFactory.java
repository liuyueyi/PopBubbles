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

	// ����ÿһ�������Ķ��Ӹ���
	public int removeCount = 0;

	// ����������֮����Ҫ����˱���Ϊtrue������Ԥ�жϣ�����֤�Ƿ��������
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
	 * ÿ���������Ӻ���Ҫ�жϱ����Ƿ����
	 * 
	 * @return true ��ʾ��Ϸ����
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

		for (int j = 0; j < 9; j++) { // �ж����һ��
			if (bubbles[9][j] == null)
				break;

			if (bubbles[9][j].compareTo(bubbles[9][j + 1]))
				return false;
		}

		for (int i = 0; i < 9; i++) { // �ж�֮��һ��
			if (bubbles[i][9] == null)
				break;

			if (bubbles[i][9].compareTo(bubbles[i + 1][9]))
				return false;
		}
		return true;
	}

	/**
	 * �ж�bubbles[row][column] �Ƿ�Ϊ��
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public boolean containBubble(int row, int column) {
		return bubbles[row][column] != null;
	}

	// ********************************************************
	// ʹ�õ��ߺ�Ķ��������߼�
	/**
	 * ��Ƕ�����������
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

	private boolean moveLeftAfterProps = false; // һ������ʹ�õ��ߺ��ж�ʣ�µĶ����Ƿ��Զ����ƶ��ı�ǣ�true��ʾ����

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

	// �������𶹶��߼�����
	// ********************************************************************************

	/**
	 * bubbles[row][column]�����,�����ڿ��������Ķ���ʱ����ǳ��������Ժ��Զ����������Ķ���
	 * 
	 * @param row
	 * @param column
	 * @return true��ʾ������������
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
	 * ����������������ڿ��������Ķ�������ִ�б�������Ϊ���п�����Ϳ������ƶ��Ķ�����Ӷ���
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
				if (bubbles[r][c].tag) { // ��¼�������ܸ����͵�ǰ�еĸ���
					bubbles[r][c].waitDuration = wait;
					wait += 4;
					count++;
				} else if (count > 0) { // ����ƶ����������õȴ�ʱ�䣬�����������е�λ��
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
	 * ���𶹶�����Ҫ�ж��Ƿ�������ƣ��������Ϊ��Ӧ�Ķ�������ƶ�����
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
	 * ��Ϸ�������Զ���������ʣ��Ķ���
	 * 
	 * @return ����ʣ��Ķ�������������
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
					removeBubble(i, j); // �Ƴ����������Ķ���
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
			// ��ӷ���
			ScoreTip tip = scoreTipPools.obtain();
			tip.setPosition(bubbles[i][j].rectangle.x + Constants.bubbleWidth
					/ 2, bubbles[i][j].rectangle.y + Constants.bubbleHeight / 2);
			tip.setText(prefTipScore + "");
			prefTipScore += 10;
			tip.addAction();
			scoreArray.add(tip);

			// ��ʾ����������Ч��
			BubbleEffectManager.manager.show(bubbles[i][j].getValue(),
					bubbles[i][j].rectangle.x, bubbles[i][j].rectangle.y);
			bubblePools.free(bubbles[i][j]);
			bubbles[i][j] = null;

			if (removeCount == 0) {
				// move left
				autoMoveLeft();
			}
		} else {
			// ��ʾ����������Ч��
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
