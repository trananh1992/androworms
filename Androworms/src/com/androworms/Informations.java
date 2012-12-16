package com.androworms;

import android.view.Display;

public class Informations {
	
	private static int screenWidth = -1;
	private static int screenHeight = -1;
	
	public static void init(Display display) {
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
	}

	public static int getScreenWidth() {
		return screenWidth;
	}

	public static int getScreenHeight() {
		return screenHeight;
	}
}