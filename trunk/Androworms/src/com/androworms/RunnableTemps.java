package com.androworms;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/** Handler de gestion du temps pour chaque joueurs
 */
public class RunnableTemps extends Handler {
	private Connexion connexion;
	/** Temps qu'un joueur dispose pour jouer */
	private static final int TEMPS_TOUR = 14;
	private int tempsRestant;
	private boolean arretDemande = false;
	private static final String TAG = "Androworms.Noyau";
	
	private static final int TEMPS_ATTENTE = 1000;
	
	public RunnableTemps(Connexion co) {
		this.connexion = co;
		Log.v(TAG, "LAncement du chrono");
		tempsRestant = TEMPS_TOUR;
	}
	
	public void reInitialise() {
		tempsRestant = TEMPS_TOUR;
	}
	
	public void stop() {
		arretDemande = true;
	}
	
	public void reprise() {
		arretDemande = false;
		sleep();
	}
	
	@Override
	public void handleMessage(Message msg) {
		if(!arretDemande) {
			tempsRestant--;
			if(tempsRestant <= 0) {
				connexion.tempsEcoule();
			}
			Log.v(TAG, "Il reste " + tempsRestant + " secondes au joueur");
			sleep();
		}
	}

	public void sleep() {
		this.removeMessages(0);
		sendMessageDelayed(obtainMessage(0), TEMPS_ATTENTE);
	}

	/*
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(!arretDemande) {
			tempsRestant--;
			if(tempsRestant <= 0) {
				connexion.tempsEcoule();
			}
			postDelayed(this, 1000);
		}
		
	}
	*/
	
}
