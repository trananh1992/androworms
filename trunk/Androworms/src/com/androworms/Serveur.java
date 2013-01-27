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
}