package com.androworms;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageSurCarte extends ImageView {
	
	// Constantes
	private static final int DECALAGE_NOM_JOUEUR = 10;
	private static final Point DECALAGE_POINTS_VIE = new Point(70, 40);
	private static final int TAILLE_TEXTE_VIE = 8;
	
	private ElementSurCarte elt;
	private TextView tvNomJoueur;
	private TextView tvPointsVie;
	
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
			tvNomJoueur = new TextView(ctx);
			tvNomJoueur.setText(p.getNom());
			tvNomJoueur.layout((int) ptCourant.x,
				(int) ptCourant.y - DECALAGE_NOM_JOUEUR,
				(int)( ptCourant.x + taille.x),
				(int)( ptCourant.y + DECALAGE_NOM_JOUEUR));
			tvNomJoueur.setGravity(Gravity.CENTER);
			tvNomJoueur.setTextSize(TAILLE_TEXTE_VIE);
			mg.addView(tvNomJoueur);
			
			tvPointsVie = new TextView(ctx);
			tvPointsVie.setText("100");
			tvPointsVie.setTextSize(TAILLE_TEXTE_VIE);
			tvPointsVie.layout((int)ptCourant.x,
				(int)ptCourant.y - DECALAGE_POINTS_VIE.y,
				(int)(ptCourant.x + DECALAGE_POINTS_VIE.x),
				(int)(ptCourant.y));
			 // ça affiche l'image à gauche et le texte à droite
			tvPointsVie.setCompoundDrawablesWithIntrinsicBounds(R.drawable.vie_3, 0, 0, 0);
			tvPointsVie.setCompoundDrawablePadding(0);
			
			mg.addView(tvPointsVie);
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
		
		if (tvNomJoueur != null) {
			tvNomJoueur.layout((int) ptCourant.x,
					(int) ptCourant.y - DECALAGE_NOM_JOUEUR,
					(int)( ptCourant.x + taille.x),
					(int)( ptCourant.y + DECALAGE_NOM_JOUEUR));
		}

		if (tvPointsVie != null) {
			tvPointsVie.layout((int)ptCourant.x,
					(int)ptCourant.y - DECALAGE_POINTS_VIE.y,
					(int)(ptCourant.x + DECALAGE_POINTS_VIE.x),
					(int)(ptCourant.y));
		}
		
		this.anciennePosition.set(ptCourant);
	}
}