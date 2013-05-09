package com.androworms;

import android.graphics.Bitmap;
import android.graphics.PointF;

public abstract class ElementSurCarte {
	
	private PointF position;
	private ImageInformation imageTerrain;
	
	public ElementSurCarte(PointF position, ImageInformation ii) {
		this.position = position;
		this.imageTerrain = ii;
	}
	
	public abstract ElementSurCarte clone();

	public PointF getPosition() {
		return position;
	}

	public void setPosition(PointF position) {
		this.position = position;
	}
	
	public ImageInformation getImageInformation() {
		return imageTerrain;
	}
	
	public void setImageTerrain(ImageInformation imageTerrain) {
		this.imageTerrain = imageTerrain;
	}

	public void setPosition(float x, float y) {
		position.x = x;
		position.y = y;
	}
	
	public int getImageTerrain() {
		return imageTerrain.getIdImage() ;
	}
	
	public int getWidthImageTerrain() {
		return imageTerrain.getWidth();
	}
	public int getHeightImageTerrain() {
		return imageTerrain.getHeight();
	}
	
	public Bitmap getImageView() {
		return imageTerrain.getImageView();
	}

	public void setPosition(double d, double e) {
		setPosition((float)d, (float)e);		
	}
	

}
