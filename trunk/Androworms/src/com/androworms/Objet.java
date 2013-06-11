package com.androworms;


import android.graphics.PointF;

public class Objet  {
	private String nom;
	private ImageInformation imageTerrain;
	
	public Objet(String nom) {
		this.nom = nom ;
		imageTerrain =  new ImageInformation();
	}

	public Objet(String nom, ImageInformation ii ) {
		super();
		this.nom = nom;
		this.imageTerrain = ii;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public int getImageTerrain() {
		return imageTerrain.getIdImage();
	}
	
	public void setImageTerrain(int idImage, int height, int width) {
		imageTerrain.setIdImage(idImage);
		imageTerrain.setHeight(height);
		imageTerrain.setWidth(width);		
	}
	
	public PointF getTailleImageTerrain() {
		return new PointF(imageTerrain.getWidth(), imageTerrain.getHeight());
	}
}
