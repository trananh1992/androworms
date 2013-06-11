package com.androworms;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.util.Log;

public class RunnableTir implements Runnable {
	private MoteurGraphique mg;
	private ImageSurCarte isc;
	private int milliseconde ;
	private static final String TAG = "Androworms.runnableMovementForce";
	private boolean explosion = false;
	private ObjetSurCarte objetExplosion;
	
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
		ii.setHeight(200);
		ii.setWidth(200);
		this.objetExplosion = new ObjetSurCarte(new Objet("explosion", ii), new PointF(0f, 0f), ii);

		
	}
	
	public void run() {
		/*
		isc.getElement().setPosition(nbSaut, nbSaut--);
		*/
		if(!isc.getElement().getMouvementForces().isEmpty()) {
			isc.getElement().setPosition(isc.getElement().getMouvementForces().remove(0));
			
			mg.actualiserGraphisme();
			mg.remetAplusTard(this, milliseconde);
		} else if(explosion){
			mg.supprimerElementSurCarte(isc.getElement());
		} else  {
			mg.supprimerElementSurCarte(isc.getElement());
			PointF position = isc.getElement().getPosition();
			objetExplosion.getPosition().set(position.x -100, position.y -100);
			isc = mg.ajouterElementSurCarte(objetExplosion);
			mg.actualiserGraphisme();
			mg.remetAplusTard(this, 2000);
			explosion = true;
		}

	}

}
