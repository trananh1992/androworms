package com.androworms;

public class RunnableGravite implements Runnable {
	private MoteurPhysique physique;
	private Noyau noyau;
	
	public RunnableGravite (MoteurPhysique mp, Noyau n) {
		physique = mp;
		noyau = n;
	}

	@Override
	public void run() {
		physique.gravite();
		noyau.mouvementForces();
	}
}
