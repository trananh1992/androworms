package com.androworms;

import android.util.Log;


/*
 * Le noyau gere tous les interactions.
 * Echange entre IHM/reseau/moteurGraphique.
 */
public class Noyau {

	// private ihm...
	private Communication communication;

	
	public Noyau() {
		communication = new Communication(this);
	}
	
	public void testReseau() {
		Log.v("Noyau", "Lancement de TestReseau");
		if(communication.testBluetooth()) {
			communication.lancementServeur();			
		}
	}
	

	
	
}
