package com.androworms;

import android.util.Log;

public class Serveur extends Communication implements Runnable {
	
	private static final String TAG_SERVEUR = "Androworms.Serveur";
	
	public Serveur(ConnexionDistante c) {
		super(c);
		Log.v(TAG_SERVEUR, "Création du Serveur Bluetooth");
	}
	
	public void run() {
		
	}
	
	public void traitementMessage() {
		Log.v(TAG_SERVEUR, "Je suis le serveur et je recois un message !");
		//Traitement
		Log.v(TAG_SERVEUR, "Je vais surement renvoyer un autre message a tous les joueurs!");
	}
	
	public void arret() {
		
	}
}