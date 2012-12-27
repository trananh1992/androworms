package com.androworms;


public class Client extends Connexion{
	
	// Ce contact communique seulement avec le serveur primaire 
	private Contact contactServeur;
	
	// Pour un serveur primaire local.
	public Client()
	{
		contactServeur = new Localhost();
	}
	
	// Pour un serveur primaire distant. 
	public Client(String address)
	{
		super();
	}

	public Client(Contact contactServeur) {
		super();
		this.contactServeur = contactServeur;
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
	
}
