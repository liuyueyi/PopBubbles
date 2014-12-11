package com.july.popbubbles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.july.popbubbles.dialog.Failure;
import com.july.popbubbles.dialog.Pause;
import com.july.popbubbles.dialog.Store;
import com.july.popbubbles.sprite.BubbleFactory;
import com.july.popbubbles.sprite.Honour;
import com.july.popbubbles.sprite.HonourManager;
import com.july.popbubbles.sprite.PrideLabel;
import com.july.popbubbles.sprite.PropsManager;
import com.july.popbubbles.sprite.ScoreTip;
import com.july.popbubbles.sprite.ToolSprite;

public class GameScreen extends MyScreen {
	int gameState;// 游戏状态

	InputMultiplexer mult;
	Stage gameStage;
	SpriteBatch batch;

	Store store;
	Pause pause;
	Failure failure;
	ToolSprite toolSprite;
	int removeCount; // 记录每次消除豆豆的个数
	int prefValue; // 记录每个豆豆值得的分数
	int time; // 时间，主要用于延迟添加分数和延迟进行下一关的时间间隔

	PrideLabel prideLabel; // 用于显示剩余豆豆数给出奖励的标签

	public GameScreen(MainGame game) {
		this.game = game;

		batch = new SpriteBatch();
		gameStage = new Stage();
		Assets.instance.bg[Constants.GMAE_BG].updateGameBg();
		mult = new InputMultiplexer();
		mult.addProcessor(this);
		mult.addProcessor(gameStage);
		Gdx.input.setInputProcessor(mult);

		toolSprite = new ToolSprite();
		gameStage.addActor(toolSprite);
		toolSprite.addListener(listener);
		Gdx.input.setCatchBackKey(true);

		prideLabel = new PrideLabel();
		gameStage.addActor(prideLabel);

		for (Honour h : Assets.instance.honours) {
			gameStage.addActor(h);
		}
		loadState();
	}

	public void init() {
		removeCount = 0;
		prefValue = 5;
		time = 0;
		gameState = Constants.RUN;
		prideLabel.setText("");
		BubbleFactory.instance.init();
	}

	/**
	 * 每次首次进入游戏都是从上次的记录开始
	 */
	public void loadState() {
		removeCount = 0;
		prefValue = 5;
		time = 0;
		gameState = Constants.RUN;
		prideLabel.setText("");
		BubbleFactory.instance.loadState();
		toolSprite.loadState();
		Assets.instance.recordPreference.putBoolean("ifLoad", false).flush();
	}

	/**
	 * 保存当前的游戏状态，下次进入时，可以直接恢复
	 */
	public void saveState() {
		Assets.instance.recordPreference.putBoolean("ifLoad", true).flush();
		BubbleFactory.instance.saveState();
		toolSprite.saveState();
	}

	/**
	 * 重新开始玩耍本关卡
	 */
	public void restart() {
		init();
		toolSprite.restart();
	}

	private EventListener listener = new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			if (gameState == Constants.PASSED) // 显示结束动画时，不接受按钮触发事件
				return;

			MusicManager.manager.playSound(MusicManager.BUTTON);

			gameState = Constants.PROPS;
			if (event.getListenerActor() == toolSprite.menu) {
				// menu btn click
				if (pause == null)
					pause = new Pause(GameScreen.this);
				gameState = Constants.PAUSE;
				pause.show();
			} else if (event.getListenerActor() == toolSprite.add) {
				if (store == null)
					store = new Store();
				gameState = Constants.STORE;
				store.show(GameScreen.this);
			} else if (event.getListenerActor() == toolSprite.hammer) {
				PropsManager.manager.show(Constants.HAMMER_BTN);
			} else if (event.getListenerActor() == toolSprite.bomb) {
				PropsManager.manager.show(Constants.BOMB_BTN);
			} else if (event.getListenerActor() == toolSprite.color) {
				PropsManager.manager.show(Constants.COLOR_BTN);
			} else if (event.getListenerActor() == toolSprite.fresh) {
				PropsManager.manager.show(Constants.FRESH_BTN);
			}
		}
	};

	/**
	 * 设置gamescreen监听input event
	 */
	@Override
	public void setInputProcessor() {
		if (gameState == Constants.STORE) {
			toolSprite.updateLevel();
		}
		Gdx.input.setInputProcessor(mult);
		gameState = Constants.RUN;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		Assets.instance.bg[Constants.GMAE_BG].draw(batch);
		Assets.instance.bg[Constants.HEART].draw(batch);
		BubbleFactory.instance.draw(batch);
		batch.end();

		gameStage.draw();
		gameStage.act();

		switch (gameState) {
		case Constants.PAUSE:
			pause.draw(batch);
			return;
		case Constants.FAILED:
			failure.draw(batch);
			return;
		case Constants.STORE:
			store.draw(batch);
			return;
		case Constants.PROPS:
			PropsManager.manager.draw(batch, delta);
			if (PropsManager.manager.isOver()) {
				gameState = Constants.RUN;
				BubbleFactory.instance.removeBubblesByProps();
			}
		}

		if (removeCount > 0) {
			if (--time == 0)
				time = 12;
			if (time < 13 && time % 4 == 0) { // 动态显示增加分数的过程，纯粹为了显示效果-*-
				toolSprite.updateScore(prefValue);
				prefValue += 10;
				removeCount--;
			}
		} else if (gameState == Constants.RUN
				&& BubbleFactory.instance.ifOver()) {
			// 结束
			if (toolSprite.levelUpSucceed())
				MusicManager.manager.playSound(MusicManager.LEVELUP);
			gameState = Constants.PASSED;
			time = BubbleFactory.instance.removeMantain();
			toolSprite.updateScore(prideLabel.setText(time / 2));
			if (time < 80)
				time = 100;// 设置本关结束后用于消灭豆豆和显示奖励分数的时间
			else
				time += 20;
		} else if (gameState == Constants.PASSED && time > 0) {
			if (--time == 0) {
				if (toolSprite.updateLevel()) { // 成功
					init();
				} else { // 失败
					gameState = Constants.FAILED;
					if (null == failure)
						failure = new Failure(this, toolSprite.getScore());
					failure.show();
				}
			}
		}

		for (ScoreTip tip : BubbleFactory.instance.scoreArray) {
			if (tip.getParent() == null) {
				gameStage.addActor(tip);
			}
		}
	}

	@Override
	public void pause() {
		Gdx.app.log("wzb", "game screen paused!");
		switch (gameState) {
		case Constants.FAILED:
			System.out.println("game screen init");
			toolSprite.init();
			// Assets.instance.lastScore = 0;
		case Constants.PASSED:
			if (!toolSprite.updateLevel()) {
				// Assets.instance.lastScore = 0;
				toolSprite.init();
			}
			BubbleFactory.instance.init();
		case Constants.PROPS:
		case Constants.PAUSE:
		case Constants.RUN:
			Assets.instance.save();
			saveState();
		}
	}

	@Override
	public void dispose() {
		gameStage.dispose();
		if (pause != null)
			pause.dispose();

		if (failure != null) {
			failure.dispose();
		}
		Gdx.app.log("wzb", "game dispose ");
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		MusicManager.manager.playSound(MusicManager.BUTTON);

		if (keycode == Keys.BACK || keycode == Keys.A) {
			// if (gameState == Constants.PAUSE || gameState == Constants.STORE)
			// {
			// setInputProcessor();
			// return false;
			// }
			// return to menu
			if (pause == null)
				pause = new Pause(GameScreen.this);
			gameState = Constants.PAUSE;
			pause.show();
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		if (removeCount > 0) // 表示还处于消灭豆子的动画之中
			return false;

		int row = (int) ((Constants.height - screenY) / Constants.bubbleHeight);
		int column = (int) (screenX / Constants.bubbleWidth);
		if (row >= 10)
			return false;

		if (gameState == Constants.PROPS
				&& (PropsManager.manager.type == Constants.FRESH_BTN || BubbleFactory.instance
						.containBubble(row, column))) {
			PropsManager.manager.act(row, column);
			toolSprite.updateHeart();
			if (store != null)
				store.updateHeartNum();
			return true;
		}

		if (BubbleFactory.instance.clicked(row, column)) { // 表示成功消除豆豆
			prefValue = 5;
			time = 30;
			removeCount = BubbleFactory.instance.removeCount;
			toolSprite.updateTip(removeCount);
			HonourManager.manager.show(removeCount);
		} else {
			// MusicManager.manager.playSound(MusicManager.SELECT);
		}
		return true;
	}
}
