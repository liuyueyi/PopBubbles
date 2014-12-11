package com.july.popbubbles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.july.popbubbles.sprite.Bg;
import com.july.popbubbles.sprite.BtnTexture;
import com.july.popbubbles.sprite.Honour;

public class Assets {
	public static Assets instance = new Assets();
	TextureAtlas menuAtlas;
	public TextureAtlas gameAtlas;
	TextureAtlas resultAtlas;
	public TextureAtlas effectAtlas;
	public TextureAtlas storeAtlas;

	public BitmapFont num;
	public LabelStyle numStyle;
	public BitmapFont tip;
	public LabelStyle tipStyle;
	BitmapFont result;
	public LabelStyle resultStyle;

	public Bg[] bg;
	public BtnTexture[] btn;
	public Honour[] honours;
	public TextureRegion[] bubbles;
	public TextureRegionDrawable btnDrwa;
	public TextureRegionDrawable btnDrwaOff;

	public Preferences setPreference; // 保存设置，如音乐，声音的开关状态，最高分
	public Preferences recordPreference; // 保存游戏状态，如当前分数，上一关分数，关卡，目标分数，豆豆布局
	public boolean soundOn;
	public boolean musicOn;
	public int bestScore;
	public int lastScore;

	public int heart;

	private Assets() {

	}

	public void init() {
		recordPreference = Gdx.app.getPreferences("record");

		setPreference = Gdx.app.getPreferences("set");
		heart = setPreference.getInteger("heart", 30);
		soundOn = setPreference.getBoolean("soundOn", true);
		musicOn = setPreference.getBoolean("musicOn", true);
		Gdx.app.log("wzb", "asset soundon : " + soundOn + " music on "
				+ musicOn);
		bestScore = setPreference.getInteger("best", 0);
		if (recordPreference.getBoolean("ifLoad", false)) {
			Gdx.app.log("wzb", "asset load last score : ");
			lastScore = setPreference.getInteger("last", 0);
		} else
			lastScore = 0;
		// Gdx.app.log("wzb", "asset last score : " + lastScore);

		num = new BitmapFont(Gdx.files.internal("font/num.fnt"));
		num.setScale(Constants.wrate, Constants.hrate);
		numStyle = new LabelStyle(num, Color.WHITE);
		tip = new BitmapFont(Gdx.files.internal("font/t.fnt"));
		tip.setScale(Constants.wrate, Constants.hrate);
		tipStyle = new LabelStyle(tip, Color.WHITE);
		result = new BitmapFont(Gdx.files.internal("font/r.fnt"));
		result.setScale(Constants.wrate, Constants.hrate);
		resultStyle = new LabelStyle(result, Color.WHITE);

		menuAtlas = new TextureAtlas(Gdx.files.internal("img/menu/menu.pack"));
		gameAtlas = new TextureAtlas(Gdx.files.internal("img/game/game.pack"));
		resultAtlas = new TextureAtlas(
				Gdx.files.internal("img/result/result.pack"));
		effectAtlas = new TextureAtlas(
				Gdx.files.internal("img/effect/effect.pack"));
		storeAtlas = new TextureAtlas(
				Gdx.files.internal("img/store/store.pack"));
		// sotre界面button的图片资源
		btnDrwa = new TextureRegionDrawable(storeAtlas.findRegion("btn_on"));
		btnDrwaOff = new TextureRegionDrawable(storeAtlas.findRegion("btn_off"));

		bg = new Bg[5];
		bg[Constants.MENU_BG] = new Bg(Constants.MENU_BG, menuAtlas);
		bg[Constants.GMAE_BG] = new Bg(Constants.GMAE_BG, gameAtlas);
		bg[Constants.RESULT_BG] = new Bg(Constants.RESULT_BG, resultAtlas);
		bg[Constants.STORE_BG] = new Bg(Constants.STORE_BG, storeAtlas);
		bg[Constants.HEART] = new Bg(Constants.HEART, gameAtlas);

		btn = new BtnTexture[14];
		btn[Constants.START_BTN] = new BtnTexture(Constants.START_BTN,
				menuAtlas);
		btn[Constants.COURSE_BTN] = new BtnTexture(Constants.COURSE_BTN,
				menuAtlas);
		btn[Constants.MUSIC_BTN] = new BtnTexture(Constants.MUSIC_BTN,
				menuAtlas);
		btn[Constants.SOUND_BTN] = new BtnTexture(Constants.SOUND_BTN,
				menuAtlas);
		btn[Constants.STORE_BTN] = new BtnTexture(Constants.STORE_BTN,
				menuAtlas);
		btn[Constants.MENU_BTN] = new BtnTexture(Constants.MENU_BTN, gameAtlas);
		btn[Constants.FRESH_BTN] = new BtnTexture(Constants.FRESH_BTN,
				gameAtlas);
		btn[Constants.BOMB_BTN] = new BtnTexture(Constants.BOMB_BTN, gameAtlas);
		btn[Constants.COLOR_BTN] = new BtnTexture(Constants.COLOR_BTN,
				gameAtlas);
		btn[Constants.HAMMER_BTN] = new BtnTexture(Constants.HAMMER_BTN,
				gameAtlas);
		btn[Constants.ADD_BTN] = new BtnTexture(Constants.ADD_BTN, gameAtlas);
		btn[Constants.CONTINUE_BTN] = new BtnTexture(Constants.CONTINUE_BTN,
				resultAtlas);
		btn[Constants.BACK_BTN] = new BtnTexture(Constants.BACK_BTN,
				resultAtlas);
		btn[Constants.NEW_BTN] = new BtnTexture(Constants.NEW_BTN, resultAtlas);

		bubbles = new TextureRegion[8];
		for (int i = 0; i < 7; i++) {
			bubbles[i] = gameAtlas.findRegion("d", i);
		}
		bubbles[7] = gameAtlas.findRegion("d", 8);

		honours = new Honour[4];
		honours[Constants.HONOUR_GOOD] = new Honour(Constants.HONOUR_GOOD,
				effectAtlas);
		honours[Constants.HONOUR_COOL] = new Honour(Constants.HONOUR_COOL,
				effectAtlas);
		honours[Constants.HONOUR_GREAT] = new Honour(Constants.HONOUR_GREAT,
				effectAtlas);
		honours[Constants.HONOUR_AMAZING] = new Honour(
				Constants.HONOUR_AMAZING, effectAtlas);
	}

	public void dispose() {
		// TODO Auto-generated method stub
		gameAtlas.dispose();
		menuAtlas.dispose();
		effectAtlas.dispose();
		storeAtlas.dispose();
		resultAtlas.dispose();
	}

	public void save() {
		setPreference.putBoolean("musicOn", musicOn).flush();
		setPreference.putBoolean("soundOn", soundOn).flush();
		setPreference.putInteger("heart", heart).flush();
		setPreference.putInteger("best", bestScore).flush();
		setPreference.putInteger("last", lastScore).flush();
		System.out.println("save last score: " + lastScore);
	}

}
