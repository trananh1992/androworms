package com.androworms;

import java.util.List;

import android.util.Log;

public class RunnableMouvementForce implements Runnable {
	private MoteurGraphique mg;
	private Noyau noyau;
	private int milliseconde ;
	private static final String TAG = "Androworms.runnableMovementForce";
	
	public RunnableMouvementForce(MoteurGraphique mg, Noyau noyau, int milis) {
		super();
		this.mg = mg;
		this.noyau = noyau;
		milliseconde = milis;
	}

	public void run() {
		List<Personnage> lp = noyau.getMonde().getListePersonnage();
		boolean mouvement = false;
		for(int i =0; i < lp.size(); i++) {
			if(!lp.get(i).getMouvementForces().isEmpty()) {
				Log.v(TAG, "déplacemet de " + lp.get(i).getNom() + " a " + lp.get(i).getMouvementForces().get(0).y);
				lp.get(i).setPosition(lp.get(i).getMouvementForces().remove(0));
				mouvement = true;
			}
		}
		if(mouvement) {
			mg.actualiserGraphisme();
			mg.remetAplusTard(this, milliseconde);
		}
	}
}
