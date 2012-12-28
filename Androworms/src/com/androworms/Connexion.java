package com.androworms;



public class Connexion {

	private Communication communication;
	
	public Connexion(Communication c) {
		this.setCommunication(c);
	}

	public Communication getCommunication() {
		return communication;
	}

	public void setCommunication(Communication communication) {
		this.communication = communication;
	}
	

}
