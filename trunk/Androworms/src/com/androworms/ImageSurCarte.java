package com.androworms;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageSurCarte extends ImageView {
	private static final String TAG = "ImageSurCarte";
	private ElementSurCarte elt;
	private TextView textV;
	
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
		
		
		
	/*	Animation rot = new RotateAnimation(0, 160);
		rot.setDuration(1000);
		this.setAnimation(rot);
		rot.start();*/
		
		if (elt instanceof Personnage) {
			Personnage p = (Personnage)elt;
			textV = new TextView(ctx);
			textV.setText(p.getNom());
			textV.layout((int) ptCourant.x,
				(int) ptCourant.y - 20, 
				(int)( ptCourant.x + taille.x),
				(int)( ptCourant.y + 10));
			textV.setGravity(Gravity.CENTER);
			mg.addView(textV);
			
			
			
		}
		
		this.actualiser();
	}
	
	public final void actualiser() {
		PointF ptCourant = elt.getPosition();
		PointF taille = new PointF(elt.getWidthImageTerrain(), elt.getHeightImageTerrain());
		
		//Calcul du carré où afficher l'image
		this.layout((int) anciennePosition.x,
				(int) anciennePosition.y, 
				(int)( anciennePosition.x + taille.x),
				(int)( anciennePosition.y + taille.y));
		
		if (textV != null) {
			textV.layout((int) ptCourant.x,
					(int) ptCourant.y - 20, 
					(int)( ptCourant.x + taille.x),
					(int)( ptCourant.y + 10));
			textV.setGravity(Gravity.CENTER);
			textV.setMinimumWidth((int)taille.x);
		}
	/*	Animation trans = new TranslateAnimation(0, 20, 0, 20);
		trans.setDuration(1000);
		this.setAnimation(trans);
		trans.start();*/
		
		if (!this.anciennePosition.equals(ptCourant.x, ptCourant.y)) {
			Log.v(TAG, "changement de position");
		/*	Thread r = new Thread() {
				public void run() {
					PointF pt = new PointF();
					pt.set(p)
				}
			};
			r.start();*/
		}
		
		this.anciennePosition.set(ptCourant);
	}
}