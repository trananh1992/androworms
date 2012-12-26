package com.androworms;


public class Client extends Connexion{
	
	// Ce contact communique seulement avec le serveur primaire 
	private Contact contactServeurPrimaire;
	
	// Pour un serveur primaire local.
	public Client()
	{
		contactServeurPrimaire = new Localhost();
	}
	
	// Pour un serveur primaire distant. 
	public Client(String address)
	{
		super();
	}

	public Client(Contact contactServeurPrimaire) {
		super();
		this.contactServeurPrimaire = contactServeurPrimaire;
	}

	public Contact getContactServeurPrimaire() {
		return contactServeurPrimaire;
	}

	public void setContactServeurPrimaire(Contact contactServeurPrimaire) {
		this.contactServeurPrimaire = contactServeurPrimaire;
	}

	public void deplacementJoueurDroite(String nomPersonnage)
	{
		// ENvoie d'une requete au serveurPrimaire 
		// LE texte "droite <NomPersonnage>"
	}
	
}
