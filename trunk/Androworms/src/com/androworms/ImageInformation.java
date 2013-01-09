package com.androworms;

public class ImageInformation {
	public int width;
	public int height;
	public int idImage;
	
	public ImageInformation(){}
	
	public ImageInformation(int idImage, int width, int height) {
		this.idImage = idImage;		
		this.height = height;
		this.width = width;
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
	
	
	
}
