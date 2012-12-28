package com.androworms;

import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothAdapter;
import android.util.Log;

/*
 * Classe mere pour la classe client et la classe serveur.
 * Les deux classes suivantes permettent de contacter les joueurs.
 */
public class Communication {
	private Client client;
	private Serveur serveur;
	private Noyau noyau;
	
	public Communication(Noyau n) {
		this.noyau = n;
	}
	
	public void lancementServeur() {
		Log.v("Communication", "Lancement de lancementServeur");
		serveur = new Serveur(this);
		new Thread(serveur).start();
	}
	
	public void ajouterContactServeur(Contact c) {
		client.setContactServeur(c);
	}
	
	public void deplacementJoueurDroite(String nomPersonnage) {
		client.deplacementJoueurDroite(nomPersonnage);
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
			Log.v("Communication", "Le device ne supporte pas le bluetooth");
			return false;
		} else {
			Log.v("Communication", "Le device supporte le bluetooth");
			return true;
		}
	}
}