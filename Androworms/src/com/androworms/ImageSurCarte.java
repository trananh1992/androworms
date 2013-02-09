package com.androworms;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageSurCarte extends ImageView {
	
	private ElementSurCarte elt;
	private TextView textV;
	private TextView tvVie;
	
	private PointF anciennePosition;
	
	public ImageSurCarte(Context ctx) {
		super(ctx);
	}
	
	public ElementSurCarte getElement() {
		return elt;
	}
	
	public ImageSurCarte(Context ctx, ElementSurCarte elt, MoteurGraphique mg) {
		super(ctx);
		this.elt = elt;
		
		mg.addView(this);
		
		Bitmap bm = MoteurGraphique.getBitmap(ctx, elt.getImageTerrain(), elt.getWidthImageTerrain(), elt.getHeightImageTerrain());
		//On dessine le personnage
		this.setImageBitmap(bm);
		//Pour pouvoir ensuite appliquer une matrice
		this.setScaleType(ScaleType.MATRIX);
		
		this.anciennePosition = elt.getPosition();
		
		PointF ptCourant = elt.getPosition();
		PointF taille = new PointF(elt.getWidthImageTerrain(), elt.getHeightImageTerrain());
		this.layout((int) ptCourant.x,
				(int) ptCourant.y, 
				(int)( ptCourant.x + taille.x),
				(int)( ptCourant.y + taille.y));
		
		if (elt instanceof Personnage) {
			Personnage p = (Personnage)elt;
			textV = new TextView(ctx);
			textV.setText(p.getNom());
			textV.layout((int) ptCourant.x,
				(int) ptCourant.y - 20,
				(int)( ptCourant.x + taille.x),
				(int)( ptCourant.y + 10));
			textV.setGravity(Gravity.CENTER);
			textV.setTextSize(8);
			mg.addView(textV);
			
			tvVie = new TextView(ctx);
			tvVie.setText("100");
			tvVie.setTextSize(8);
			tvVie.layout((int)ptCourant.x,
				(int)ptCourant.y - 40,
				(int)(ptCourant.x + 70),
				(int)(ptCourant.y));
			 // ça affiche l'image à gauche et le texte à droite
			tvVie.setCompoundDrawablesWithIntrinsicBounds(R.drawable.vie_2, 0, 0, 0);
			tvVie.setCompoundDrawablePadding(0);
			
			mg.addView(tvVie);
		}
		
		this.actualiser();
	}
	
	public final void actualiser() {
		PointF ptCourant = elt.getPosition();
		PointF taille = new PointF(elt.getWidthImageTerrain(), elt.getHeightImageTerrain());
		
		//Calcul du carré où afficher l'image
		this.layout((int) ptCourant.x,
				(int) ptCourant.y, 
				(int)( ptCourant.x + taille.x),
				(int)( ptCourant.y + taille.y));
		
		if (textV != null) {
			textV.layout((int) ptCourant.x,
					(int) ptCourant.y - 10,
					(int)( ptCourant.x + taille.x),
					(int)( ptCourant.y +10));
		}

		if (tvVie != null) {
			tvVie.layout((int)ptCourant.x,
					(int)ptCourant.y - 40,
					(int)(ptCourant.x + 70),
					(int)(ptCourant.y));
		}
		
		this.anciennePosition.set(ptCourant);
	}
}