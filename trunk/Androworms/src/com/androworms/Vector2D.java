package com.androworms;

import android.graphics.PointF;

public class Vector2D {
	public float x;
	public float y;

	public Vector2D(float x, float y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	
	public static Vector2D vecteurAB(PointF a, PointF b) {
		return new Vector2D(b.x - a.x, b.y - a.y);
	}
	
	public float size() {
		return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	

}
