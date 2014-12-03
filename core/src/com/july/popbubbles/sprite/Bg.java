package com.july.popbubbles.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.july.popbubbles.Assets;
import com.july.popbubbles.Constants;

public class Bg extends GameObject {
	private TextureRegion back;

	public void updateGameBg() {
		back = Assets.instance.gameAtlas.findRegion("bg",
				(int) (4 * Math.random()));
	}

	public Bg(int tag, TextureAtlas atlas) {
		switch (tag) {
		case Constants.GMAE_BG:
			rectangle = new Rectangle(0, 0, Gdx.graphics.getWidth(),
					Gdx.graphics.getHeight());
			back = atlas.findRegion("bg", (int) (4 * Math.random()));
			break;
		case Constants.MENU_BG:
			rectangle = new Rectangle(0, 0, Gdx.graphics.getWidth(),
					Gdx.graphics.getHeight());
			back = atlas.findRegion("bg");
			break;
		case Constants.RESULT_BG:
			rectangle = new Rectangle(Constants.resultX, Constants.resultY,
					Constants.resultWidth, Constants.resultHeight);
			back = atlas.findRegion("pauseBg");
			break;
		case Constants.STORE_BG:
			rectangle = new Rectangle(Constants.storeX, Constants.storeY,
					Constants.storeWidth, Constants.storeHeight);
			back = atlas.findRegion("store");
			break;
		case Constants.HEART:
			rectangle = new Rectangle(Constants.heartImgX, Constants.heartImgY,
					Constants.heartImgWidth, Constants.heartImgHeight);
			back = atlas.findRegion("Main_Life");
			break;
		}
	}

	// »æÖÆ
	public void draw(Batch batch) {
		batch.draw(back, rectangle.x, rectangle.y, rectangle.width,
				rectangle.height);
	}
}
