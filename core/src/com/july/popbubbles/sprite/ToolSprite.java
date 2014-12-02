package com.july.popbubbles.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.july.popbubbles.Assets;
import com.july.popbubbles.Constants;

public class ToolSprite extends Group {
	Label score;
	Label level;
	Label target;
	Label best;
	Label star;
	Label tip; // ��ʾ ������ ���ٷ�
	public Image fresh;
	public Image color;
	public Image hammer;
	public Image bomb;
	public Button menu;
	public Image add;

	int currentLevel;
	int targetScore;
	int scoreValue;

	public ToolSprite() {
		Color c = new Color(0.5f, 0, 0, 1);
		setBounds(0, 0, Constants.width, Constants.height);
		menu = new Button(Assets.instance.btn[Constants.MENU_BTN].btn[0],
				Assets.instance.btn[Constants.MENU_BTN].btn[1]);
		menu.setBounds(Assets.instance.btn[Constants.MENU_BTN].rectangle.x,
				Assets.instance.btn[Constants.MENU_BTN].rectangle.y,
				Assets.instance.btn[Constants.MENU_BTN].rectangle.width,
				Assets.instance.btn[Constants.MENU_BTN].rectangle.height);
		addActor(menu);

		bomb = new BtnSprite(Constants.BOMB_BTN, 0);
		addActor(bomb);

		hammer = new BtnSprite(Constants.HAMMER_BTN, 0);
		addActor(hammer);

		fresh = new BtnSprite(Constants.FRESH_BTN, 0);
		addActor(fresh);

		init();
		score = new Label("" + scoreValue, Assets.instance.numStyle);
		score.setColor(Color.BLACK);
		score.setBounds(menu.getX() + menu.getWidth(), menu.getY(), bomb.getX()
				- menu.getX() - menu.getWidth(), menu.getHeight());
		score.setAlignment(Align.center);
		addActor(score);

		level = new Label("�ؿ�:" + currentLevel, Assets.instance.tipStyle);
		level.setColor(c);
		level.setBounds(Constants.levelX, Constants.levelY,
				Constants.levelWidth, Constants.levelHeight);
		level.setAlignment(Align.left);
		addActor(level);

		target = new Label("Ŀ��:" + targetScore, Assets.instance.tipStyle);
		target.setColor(c);
		target.setBounds(Constants.targetX, Constants.targetY,
				Constants.targetWdith, Constants.targetHeight);
		target.setAlignment(Align.center);
		addActor(target);

		add = new BtnSprite(Constants.ADD_BTN, 0);
		addActor(add);

		star = new Label("1000", Assets.instance.tipStyle);
		star.setColor(Color.BLACK);
		star.setPosition(Constants.heartImgX + Constants.heartImgWidth,
				Constants.heartImgY);
		star.setSize(Constants.addX - star.getX(), Constants.heartImgHeight);
		star.setAlignment(Align.center);
		addActor(star);

		best = new Label("��ʷ��߷�:" + Assets.instance.bestScore,
				Assets.instance.tipStyle);
		best.setColor(c);
		best.setBounds(Constants.bestX, Constants.bestY, Constants.bestWidth,
				Constants.bestHeight);
		best.setAlignment(Align.center);
		addActor(best);

		tip = new Label("", Assets.instance.numStyle);
		tip.setColor(Color.BLACK);
		tip.setBounds(0, Constants.width * 1.05f, Constants.width,
				score.getHeight());
		tip.setAlignment(Align.center);
		addActor(tip);
	}

	public void init() {
		Gdx.app.log("tool", "init");
		// Assets.instance.lastScore = 0;
		currentLevel = 1;
		targetScore = 1000;
		scoreValue = 0;
	}

	public void saveState() {
		Assets.instance.recordPreference.putInteger("level", currentLevel)
				.flush();
		Assets.instance.recordPreference.putInteger("score", scoreValue)
				.flush();
		Assets.instance.recordPreference.putInteger("target", targetScore)
				.flush();
	}

	public void loadState() {
		if (Assets.instance.recordPreference.getBoolean("ifLoad", false)) { // ��ʾ����֮ǰ����Ϸ
			currentLevel = Assets.instance.recordPreference.getInteger("level",
					1);
			targetScore = Assets.instance.recordPreference.getInteger("target",
					2000);
			scoreValue = Assets.instance.recordPreference
					.getInteger("score", 0);
			level.setText("�ؿ�:" + currentLevel);
			target.setText("Ŀ��:" + targetScore);
			score.setText("" + scoreValue);
			if (currentLevel == 1)
				Assets.instance.lastScore = 0;
		} else {
			Assets.instance.lastScore = 0; // ��ʾ���¿�ʼ
		}
	}

	public void restart() {
		scoreValue = Assets.instance.lastScore;
		System.out.println("restart lastscore " + scoreValue);
		score.setText("" + scoreValue);
		tip.setText("");
	}

	public int getScore() {
		return scoreValue;
	}

	/**
	 * ���»�õķ���
	 * 
	 * @param value
	 *            ���ӵķ���
	 */
	public void updateScore(int value) {
		scoreValue += value;
		score.setText("" + scoreValue);
	}

	/**
	 * ������ʾ��Ϣ
	 * 
	 * @param removeNum
	 *            �����Ķ�����Ŀ
	 */
	public void updateTip(int removeNum) {
		int s = removeNum * removeNum * 5;
		tip.setText(removeNum + "����" + s + "��");
	}

	/**
	 * ��һ��
	 */
	public boolean updateLevel() {
		if (scoreValue > Assets.instance.bestScore) { // ������߷�
			Assets.instance.bestScore = scoreValue;
			best.setText("��ʷ��߷�:" + scoreValue);
		}

		if (scoreValue < targetScore) {
			return false;
		}

		// ����ɹ��������������һ�ط���
		Assets.instance.lastScore = scoreValue;

		currentLevel++;
		if (currentLevel == 2) {
			targetScore = 2500;
		} else {
			targetScore += 1940 + 20 * currentLevel;
		}
		level.setText("�ؿ�:" + currentLevel);
		target.setText("Ŀ��:" + targetScore);
		tip.setText("");
		return true;
	}

	@Override
	public boolean addListener(EventListener listener) {
		menu.addListener(listener);
		bomb.addListener(listener);
		fresh.addListener(listener);
		hammer.addListener(listener);
		add.addListener(listener);
		return true;
	}
}
