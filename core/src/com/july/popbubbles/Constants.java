package com.july.popbubbles;

import com.badlogic.gdx.Gdx;

public class Constants {
	// game status
	public final static int RUN = 0;
	public final static int PAUSE = 1;
	public final static int PASSED = 2; // 表示本关结束
	public final static int FAILED = 3; // 表示正式显示失败
	public final static int PROPS = 4; // 使用道具状态
	public final static int STORE = 5; // 商店

	public final static int MENU_BG = 0;
	public final static int GMAE_BG = 1;
	public final static int RESULT_BG = 2; // 即暂停按钮的背景
	public final static int STORE_BG = 3;
	public final static int HEART = 4;

	public final static float width = Gdx.graphics.getWidth();
	public final static float height = Gdx.graphics.getHeight();
	public final static float wrate = width / 480f;
	public final static float hrate = height / 800f;

	// 暂停界面 constants
	public final static float resultWidth = 0.8f * width;
	public final static float resultHeight = resultWidth;
	public final static float resultX = 0.1f * width;
	public final static float resultY = (height - resultHeight) / 2;

	// store constants
	public final static float storeWidth = 0.96f * width;
	public final static float storeHeight = storeWidth * 300 / 344;
	public final static float storeX = storeWidth * 0.02f;
	public final static float storeY = (height - storeHeight) / 2;

	// btn
	public final static int START_BTN = 0;
	public final static int COURSE_BTN = 1;
	public final static int MUSIC_BTN = 2;
	public final static int SOUND_BTN = 3;
	public final static int STORE_BTN = 4;

	public final static int MENU_BTN = 13;
	public final static int COLOR_BTN = 5;
	public final static int FRESH_BTN = 6;
	public final static int HAMMER_BTN = 7;
	public final static int BOMB_BTN = 8;
	public final static int ADD_BTN = 9;

	public final static int CONTINUE_BTN = 10;
	public final static int NEW_BTN = 11;
	public final static int BACK_BTN = 12;

	// btn constants
	public final static float startBtnWidth = 182 * wrate;
	public final static float startBtnHeight = 130 * hrate;
	public final static float startBtnX = (width - startBtnWidth) / 2;
	public final static float startBtnY = (height - startBtnHeight) / 2;

	public final static float btnWidth = 56 * wrate;
	public final static float btnHeight = btnWidth;
	public final static float btnX = btnWidth / 4;
	public final static float courseBtnY = startBtnY - 0.8f * btnHeight;
	public final static float musicBtnY = courseBtnY - 1.4f * btnHeight;
	public final static float soundBtnY = musicBtnY - 1.4f * btnHeight;
	public final static float toolBtnY = height - 2.9f * btnHeight;
	public final static float freshBtnX = width - btnWidth - 5 * wrate;
	public final static float colorBtnX = freshBtnX - btnWidth - 5 * wrate;
	public final static float hammerBtnX = colorBtnX - btnWidth - 5 * wrate;
	public final static float bombBtnX = hammerBtnX - btnWidth - 5 * wrate;
	public final static float menuBtnWidth = 38 * wrate;
	public final static float menuBtnHeight = 34 * hrate;
	public final static float menuBtnX = 5;
	public final static float menuBtnY = toolBtnY + (btnHeight - menuBtnHeight)
			/ 2;

	public final static float storeBtnWidth = 120 * wrate;
	public final static float storeBtnHeight = storeBtnWidth;
	public final static float storeBtnX = width - 1.1f * storeBtnWidth;
	public final static float storeBtnY = startBtnY + 0.4f * startBtnHeight;

	// 暂停界面继续开始按钮信息
	public final static float continueBtnWidth = resultWidth * 180 / 440;
	public final static float continueBtnHeight = continueBtnWidth;
	public final static float continueBtnX = resultX
			+ (resultWidth - continueBtnWidth) / 2;
	public final static float continueBtnY = resultY
			+ (resultHeight - continueBtnHeight) / 2;

	public final static float resultBtnWidth = 128f / 440f * resultWidth;
	public final static float resultBtnHeight = 47f / 440f * resultWidth;
	public final static float resultBtnY = continueBtnY - 1.5f
			* resultBtnHeight;
	public final static float backBtnX = resultX + resultWidth / 4
			- resultBtnWidth / 2; // 暂停界面中返回菜单按钮信息
	public final static float newBtnX = backBtnX + resultWidth / 2; // 暂停界面重新开始按钮信息

	// 游戏界面，红心、背景、加号三个的布局位置
	public final static float addWidth = 42 * wrate;
	public final static float addHeight = 45 * hrate;
	public final static float addX = width - addWidth;
	public final static float addY = toolBtnY + btnHeight * 1.2f;

	public final static float heartImgWidth = 47 * wrate;
	public final static float heartImgHeight = addHeight;
	public final static float heartImgX = addX - 0.92f * 125 * wrate;
	public final static float heartImgY = addY;

	// level, target, best score label position
	public final static float levelWidth = 110 * wrate;
	public final static float levelHeight = heartImgHeight;
	public final static float levelX = 0;
	public final static float levelY = heartImgY;
	public final static float targetWdith = heartImgX - levelWidth;
	public final static float targetHeight = levelHeight;
	public final static float targetX = levelX + levelWidth;
	public final static float targetY = levelY;
	public final static float bestX = 0;
	public final static float bestY = height - btnHeight;// + levelHeight;
	public final static float bestWidth = width;
	public final static float bestHeight = btnHeight;
	public final static float scoreX = menuBtnX + menuBtnWidth;
	public final static float scoreY = menuBtnY;
	public final static float scoreWidth = bombBtnX - scoreX;
	public final static float scoreHeight = menuBtnHeight;

	public final static float bubbleWidth = width / 10;
	public final static float bubbleHeight = bubbleWidth;

	// honour
	public final static int HONOUR_GOOD = 0;
	public final static int HONOUR_COOL = 1;
	public final static int HONOUR_GREAT = 2;
	public final static int HONOUR_AMAZING = 3;

	public final static float honourWidth = width;
	public final static float honourHeight = 56 * width / 167;
	public final static float honourX = 0;
	public final static float honourY = (height - honourHeight) / 2;
	
	public final static int EXIT = 0;
	public final static int CHAPING = 1;
}
