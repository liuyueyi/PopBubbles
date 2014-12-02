package com.july.popbubbles.sprite;

import com.badlogic.gdx.math.Rectangle;

public class GameObject {
	public Rectangle rectangle;

	public GameObject() {

	}

	public GameObject(GameObject object) {
		rectangle = new Rectangle();
		rectangle.x = object.rectangle.x;
		rectangle.y = object.rectangle.y;
		rectangle.width = object.rectangle.width;
		rectangle.height = object.rectangle.height;
	}
}
