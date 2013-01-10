package com.androworms;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.widget.ImageView;


public class ImageSurCarte extends ImageView {
	private ElementSurCarte elt;
	
	public ImageSurCarte(Context ctx) {
		super(ctx);
	}
	
	public ImageSurCarte(Context ctx, ElementSurCarte elt) {
		super(ctx);
		
		this.elt = elt;
		
		Bitmap bm = MoteurGraphique.getBitmap(ctx, elt.getImageTerrain(), elt.getWidthImageTerrain(), elt.getHeightImageTerrain());
		//On dessine le personnage
		this.setImageBitmap(bm);
		//Pour pouvoir ensuite appliquer une matrice
		this.setScaleType(ScaleType.MATRIX);
		
		this.actualiser();
	}
	
	public void actualiser() {
		PointF pp = elt.getPosition();
		PointF taille = new PointF(elt.getWidthImageTerrain(), elt.getHeightImageTerrain());
		
		//Calcul du carré où afficher l'image
		this.layout((int) pp.x,
				(int) pp.y, 
				(int)( pp.x + taille.x),
				(int)( pp.y + taille.y));
	}

}
