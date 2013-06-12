package com.androworms;

/** Gestion des armes du jeu
 */
public class Arme extends Objet {
	
	private ImageInformation imageMenu;
	private int portee;
	private Munition munition;
	
	public Arme (String nom) {
		super(nom);
		imageMenu = new ImageInformation();		
	}
	
	public int getPortee() {
		return portee;
	}
	public void setPortee(int portee) {
		this.portee = portee;
	}
	public Munition getMunition() {
		return munition;
	}
	public void setMunition(Munition munition) {
		this.munition = munition;
	}

	public ImageInformation getImageMenu() {
		return imageMenu;
	}

	public void setImageMenu(ImageInformation imageMenu) {
		this.imageMenu = imageMenu;
	}
	
	public void setImageMenu(int idImage, int width, int height) {
		imageMenu.set(idImage, height, width);
	}
}