package com.july.popbubbles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class MusicManager {
	public static MusicManager manager = new MusicManager();

	private Music bg;
	private Sound[] sound;

	public static final int BUTTON = 0;
	public static final int POP = 1;
	public static final int ADDCOIN = 2;
	public static final int GAMEOVER = 3;
	public static final int SUCCEED = 4;

	public static final int SELECT = 5;
	public static final int FRESH = 6;
	public static final int HAMMER = 7;
	public static final int BOMB = 8;
	public static final int LEVELUP = 9;

	public static final int GOOD = 10;
	public static final int GREAT = 11;
	public static final int COOL = 12;

	private MusicManager() {
		// loadMusic();
		// loadSound();
	}

	public void loadMusic() {
		bg = Gdx.audio.newMusic(Gdx.files.internal("audio/bg.mp3"));
		bg.setLooping(true);
		bg.setVolume(0.3f);
	}

	public void loadSound() {
		sound = new Sound[13];
		sound[BUTTON] = Gdx.audio.newSound(Gdx.files
				.internal("audio/button.mp3"));
		sound[POP] = Gdx.audio.newSound(Gdx.files.internal("audio/pop.ogg"));
		sound[SELECT] = Gdx.audio.newSound(Gdx.files
				.internal("audio/select.wav"));
		sound[ADDCOIN] = Gdx.audio.newSound(Gdx.files
				.internal("audio/addcoin.ogg"));
		sound[BOMB] = Gdx.audio.newSound(Gdx.files.internal("audio/bomb.ogg"));
		sound[HAMMER] = Gdx.audio.newSound(Gdx.files
				.internal("audio/hammer.mp3"));
		sound[FRESH] = Gdx.audio
				.newSound(Gdx.files.internal("audio/fresh.ogg"));
		sound[SUCCEED] = Gdx.audio.newSound(Gdx.files
				.internal("audio/succeed.ogg"));
		sound[GAMEOVER] = Gdx.audio.newSound(Gdx.files
				.internal("audio/gameover.mp3"));
		sound[LEVELUP] = Gdx.audio.newSound(Gdx.files
				.internal("audio/levelup.ogg"));
		sound[GOOD] = Gdx.audio.newSound(Gdx.files.internal("audio/cool.ogg"));
		sound[COOL] = Gdx.audio.newSound(Gdx.files.internal("audio/good.ogg"));
		sound[GREAT] = Gdx.audio
				.newSound(Gdx.files.internal("audio/great.ogg"));
	}

	public void playSound(int type) {
		if (Assets.instance.soundOn) {
			sound[type].setVolume(sound[type].play(), 0.3f);
		}
	}

	public void stopSound() {
		Assets.instance.soundOn = false;
	}

	public void playMusic() {
		if (Assets.instance.musicOn) {
			bg.play();
		}
	}

	public void stopMusic() {
		Assets.instance.musicOn = false;
		bg.stop();
	}

	public void dispose() {
		if (bg != null)
			bg.dispose();

		if (sound != null) {
			for (Sound s : sound) {
				s.dispose();
			}
		}
	}
}
