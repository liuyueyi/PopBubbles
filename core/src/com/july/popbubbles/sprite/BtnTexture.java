package com.july.popbubbles.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.july.popbubbles.Constants;

public class BtnTexture extends GameObject {
	public TextureRegionDrawable[] btn;

	public BtnTexture(int tag, TextureAtlas atlas) {
		btn = new TextureRegionDrawable[2];
		switch (tag) {
		case Constants.START_BTN:
			rectangle = new Rectangle(Constants.startBtnX, Constants.startBtnY,
					Constants.startBtnWidth, Constants.startBtnHeight);
			btn[0] = new TextureRegionDrawable(atlas.findRegion("play"));
			break;
		case Constants.STORE_BTN:
			rectangle = new Rectangle(Constants.storeBtnX, Constants.storeBtnY,
					Constants.storeBtnWidth, Constants.storeBtnHeight);
			btn[0] = new TextureRegionDrawable(atlas.findRegion("store"));
			break;
		case Constants.COURSE_BTN:
			rectangle = new Rectangle(Constants.btnX, Constants.courseBtnY,
					Constants.btnWidth, Constants.btnHeight);
			btn[0] = new TextureRegionDrawable(
					atlas.findRegion("Main_Btn_Course"));
			break;
		case Constants.MUSIC_BTN:
			rectangle = new Rectangle(Constants.btnX, Constants.musicBtnY,
					Constants.btnWidth, Constants.btnHeight);
			btn[0] = new TextureRegionDrawable(
					atlas.findRegion("Main_Music_On"));
			btn[1] = new TextureRegionDrawable(
					atlas.findRegion("Main_Music_Off"));
			break;
		case Constants.SOUND_BTN:
			rectangle = new Rectangle(Constants.btnX, Constants.soundBtnY,
					Constants.btnWidth, Constants.btnHeight);
			btn[0] = new TextureRegionDrawable(
					atlas.findRegion("Main_Sound_On"));
			btn[1] = new TextureRegionDrawable(
					atlas.findRegion("Main_Sound_Off"));
			break;

		case Constants.MENU_BTN:
			rectangle = new Rectangle(Constants.menuBtnX, Constants.menuBtnY,
					Constants.menuBtnWidth, Constants.menuBtnHeight);
			btn[0] = new TextureRegionDrawable(atlas.findRegion("menu"));
			btn[1] = new TextureRegionDrawable(atlas.findRegion("menu_off"));
			break;
		case Constants.BOMB_BTN:
			rectangle = new Rectangle(Constants.bombBtnX, Constants.toolBtnY,
					Constants.btnWidth, Constants.btnHeight);
			btn[0] = new TextureRegionDrawable(atlas.findRegion("bomb"));
			break;
		case Constants.FRESH_BTN:
			rectangle = new Rectangle(Constants.freshBtnX, Constants.toolBtnY,
					Constants.btnWidth, Constants.btnHeight);
			btn[0] = new TextureRegionDrawable(atlas.findRegion("fresh"));
			break;
		case Constants.HAMMER_BTN:
			rectangle = new Rectangle(Constants.hammerBtnX, Constants.toolBtnY,
					Constants.btnWidth, Constants.btnHeight);
			btn[0] = new TextureRegionDrawable(atlas.findRegion("hammer"));
			break;
		case Constants.COLOR_BTN:
			rectangle = new Rectangle(Constants.colorBtnX, Constants.toolBtnY,
					Constants.btnWidth, Constants.btnHeight);
			btn[0] = new TextureRegionDrawable(atlas.findRegion("color"));
			break;

		case Constants.ADD_BTN:
			rectangle = new Rectangle(Constants.addX, Constants.addY,
					Constants.addWidth, Constants.addHeight);
			btn[0] = new TextureRegionDrawable(
					atlas.findRegion("Main_Btn_AddCoin"));
			break;

		case Constants.CONTINUE_BTN:
			rectangle = new Rectangle(Constants.continueBtnX,
					Constants.continueBtnY, Constants.continueBtnWidth,
					Constants.continueBtnHeight);
			btn[0] = new TextureRegionDrawable(atlas.findRegion("continue"));
			break;

		case Constants.BACK_BTN:
			rectangle = new Rectangle(Constants.backBtnX, Constants.resultBtnY,
					Constants.resultBtnWidth, Constants.resultBtnHeight);
			btn[0] = new TextureRegionDrawable(atlas.findRegion("back"));
			break;

		case Constants.NEW_BTN:
			rectangle = new Rectangle(Constants.newBtnX, Constants.resultBtnY,
					Constants.resultBtnWidth, Constants.resultBtnHeight);
			btn[0] = new TextureRegionDrawable(atlas.findRegion("restart"));
			break;
		}
	}
}
