package com.androworms;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;

public class TerrainMonde {
	
	private Bitmap premierPlan;
	private Bitmap arrierePlan;
	private Bitmap terrain;
	
	public Bitmap getPremierPlan() {
		return premierPlan;
	}
	public void setPremierPlan(Bitmap premierPlan) {
		this.premierPlan = Bitmap.createScaledBitmap(premierPlan, MoteurGraphique.MAP_WIDTH, MoteurGraphique.MAP_HEIGHT, true);
		setTerrain();
	}
	public Bitmap getArrierePlan() {
		return arrierePlan;
	}
	public void setArrierePlan(Bitmap arrierePlan) {
		this.arrierePlan = Bitmap.createScaledBitmap(arrierePlan, MoteurGraphique.MAP_WIDTH, MoteurGraphique.MAP_HEIGHT, true);
		setTerrain();
	}
	
	public Bitmap getTerrain() {
		return terrain;//.copy(terrain.getConfig(), true);
	}
	
	public void dessinPersonnageSurCarte(Bitmap personnage, PointF position, Bitmap carte) {
		for(int i = 0; i < personnage.getWidth(); i++) {
			for(int j = 0; j < personnage.getHeight(); j++) {
				if(!estTransparent(personnage, i, j)) {
					carte.setPixel((int)(position.x) + i, (int)(position.y) + j, personnage.getPixel(i, j));
				}
			}
		}
	}
	
	private boolean estTransparent(Bitmap b, int x, int y) {
		return Color.alpha(b.getPixel(x, y)) == Color.TRANSPARENT;
	}

	
	private void setTerrain() {
		if( arrierePlan != null && premierPlan != null) {
			terrain = arrierePlan.copy(arrierePlan.getConfig(), true);
			for(int i = 0; i < premierPlan.getWidth(); i++) {
				for(int j = 0; j < premierPlan.getHeight(); j++) {
					if(!estTransparent(premierPlan, i, j)) {
						terrain.setPixel(i, j, premierPlan.getPixel(i, j));
					}
				}
			}
		}
	}
	

	
	
	

}
