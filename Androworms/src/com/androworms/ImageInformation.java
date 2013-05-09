package com.androworms;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class ImageInformation {
	private int width;
	private int height;
	private int idImage;
	private float echelle;
	private Bitmap imageView;
	
	public ImageInformation(){}
	
	public ImageInformation(int pidImage, int width, int height, Context c) {
		this.idImage = pidImage;		
		this.height = height;
		this.width = width;
		this.echelle = 1f;
		imageView = ((BitmapDrawable)c.getResources().getDrawable(pidImage)).getBitmap();
	}
	
	public void set(int idImage, int width, int height) {
		this.idImage = idImage;
		this.height = height;
		this.width = width;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getIdImage() {
		return idImage;
	}

	public void setIdImage(int idImage) {
		this.idImage = idImage;
	}

	public Bitmap getImageView() {
		return Bitmap.createScaledBitmap(imageView,(int)(getWidth()*echelle), (int)(getHeight()*echelle), true);
	}

	public void setImageView(Bitmap imageView) {
		this.imageView = imageView;
	}
	
	
	
	
}
