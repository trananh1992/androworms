package com.androworms;

import android.graphics.Bitmap;
import android.graphics.PointF;

public class RunnableTir implements Runnable {
	private MoteurGraphique mg;
	private ImageSurCarte isc;
	private int milliseconde ;
	private Noyau noyau;
	private boolean explosion = false;
	private ObjetSurCarte objetExplosion;
	private static final int TAILLE_TIR = 200;
	private static final int TEMPS_ATTENTE = 2000;
	
	public RunnableTir(ObjetSurCarte esc, MoteurGraphique mg, Noyau noyau, int milis) {
		super();
		this.mg = mg;
		this.milliseconde = milis;
		if(!esc.getMouvementForces().isEmpty()) {
			esc.setPosition(esc.getMouvementForces().remove(0));
		}
		this.isc = mg.ajouterElementSurCarte(esc);
		Bitmap image = mg.getImage(R.drawable.explosion);
		ImageInformation ii = new ImageInformation(image, R.drawable.explosion);
		ii.setHeight(TAILLE_TIR);
		ii.setWidth(TAILLE_TIR);
		this.objetExplosion = new ObjetSurCarte(new Objet("explosion", ii), new PointF(0f, 0f), ii);
		this.noyau = noyau;
		
	}
	
	public void run() {
		if(!isc.getElement().getMouvementForces().isEmpty()) {
			isc.getElement().setPosition(isc.getElement().getMouvementForces().remove(0));
			
			mg.actualiserGraphisme();
			mg.remetAplusTard(this, milliseconde);
		} else if(explosion) {
			// Explosion
			mg.supprimerElementSurCarte(isc.getElement());
			noyau.finDuTourFromIHM();
		} else {
			mg.vibration();
			mg.supprimerElementSurCarte(isc.getElement());
			PointF position = isc.getElement().getPosition();
			objetExplosion.getPosition().set(position.x -TAILLE_TIR/2, position.y -TAILLE_TIR/2);
			isc = mg.ajouterElementSurCarte(objetExplosion);
			mg.actualiserGraphisme();
			mg.remetAplusTard(this, TEMPS_ATTENTE);
			explosion = true;
		}
	}
}