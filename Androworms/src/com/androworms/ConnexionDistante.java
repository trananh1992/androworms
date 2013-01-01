package com.androworms;

import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothAdapter;
import android.util.Log;

public class ConnexionDistante extends Connexion {
	// L'item 1 designe le client et l'item 2 le serveur (si il y a)
	private static final int serveurPosition = 1;
	private static final int clientPosition = 0;
	private static final String TAG_COM = "Androworms.Communication";
	private List<Communication> communications;


	public ConnexionDistante(Noyau n) {
		super(n);
		communications = new ArrayList<Communication>();
	}


	private Client getClient() {
		return (Client)communications.get(clientPosition);
	}

	private Serveur getServeur() {
		return (Serveur)communications.get(serveurPosition);
	}


	public void lancementServeur() {
		Log.v(TAG_COM, "Lancement de lancementServeur");
		Serveur serveur = new Serveur(this);
		communications.set(serveurPosition, serveur);
		new Thread(serveur).start();
	}


	public List<String> listeDesServeursPrimaires() {
		// On recherche l'ensemble des appareils dans le voisinage (bluetooth)
		// qui ont lance un serveurPrimaire.
		// Le joueur pourra alors se connecter a l'un d'entre eux si besoin.
		return new ArrayList<String>();
	}

	public boolean testBluetooth() {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			// Device does not support Bluetooth
			Log.v(TAG_COM, "Le device ne supporte pas le bluetooth");
			return false;
		} else {
			Log.v(TAG_COM, "Le device supporte le bluetooth");
			return true;
		}
	}


	public void deplacementJoueurDroite(String nomPersonnage) {
		getClient().deplacementJoueurDroite(nomPersonnage);
	}

	public void deplacementJoueurGauche(String nomPersonnage) {

	} 

	public void deplacementJoueurSaut(String nomPersonnage) {

	}

	public void arretServeur() {
		getServeur().arret();
	}

}
