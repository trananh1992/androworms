package com.androworms;


/* 
 * Defini le joueur dans son integralite donc son personnage, 
 * son mode de contact...
 */
public class Joueur {
	private Personnage personnage;
	private Contact contact;
	
	
	public Personnage getPersonnage() {
		return personnage;
	}
	public void setPersonnage(Personnage personnage) {
		this.personnage = personnage;
	}
	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	
	
	

}
