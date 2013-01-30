package com.androworms;

import android.util.Log;


public class Client extends Communication {
	
	// Ce contact communique seulement avec le serveur primaire 
	private Contact contactServeur;
	private static final String TAG = "Androworms.Client";
	
	// Pour un serveur primaire local.
	public Client(ConnexionDistante n) {
		super(n);
		contactServeur = new Localhost();
	}

	public Contact getContactServeur() {
		return contactServeur;
	}

	public void setContactServeur(Contact contactServeur) {
		this.contactServeur = contactServeur;
	}

	public void deplacementJoueurDroite(String nomPersonnage)
	{
		// ENvoie d'une requete au serveur
		// LE texte "D D <NomPersonnage>" pour "Deplacement Droite <NomPersonnage>"
	}
	
	public void traitementMessage() {
		Log.v(TAG, "Je suis le client et je recois un message !");
		//Traitement
		Log.v(TAG, "Je vais surement le renvoyer au noyau !");
		
	}
	
}
