package com.androworms;

/*
 * Cette classe determine la position d'un objet/joueur sur une map
 */
public class Position {
	private int x;
	private int y;
	
	public Position() {
		x = 0;
		y = 0;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
