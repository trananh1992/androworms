package com.androworms;

import java.util.List;

public class RunnableMouvementForce implements Runnable{
	private MoteurGraphique mg;
	private Noyau noyau;
	
	public RunnableMouvementForce(MoteurGraphique mg, Noyau noyau) {
		super();
		this.mg = mg;
		this.noyau = noyau;
	}

	public void run() {
		// TODO Auto-generated method stub
		List<Personnage> lp = noyau.getMonde().getListePersonnage();
		boolean mouvement = false;
		for(int i =0; i < lp.size(); i++) {
			if(!lp.get(i).getMouvementForces().isEmpty()) {
				lp.get(i).setPosition(lp.get(i).getMouvementForces().remove(0));
				mouvement = true;
			}
		}
		if(mouvement) {
			mg.actualiserGraphisme();
			mg.remetAplusTard(this, 1);
		}

	}
}
