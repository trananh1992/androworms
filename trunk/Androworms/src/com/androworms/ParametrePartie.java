package com.androworms;

public class ParametrePartie {
	
	public static final int MODE_SOLO = 1;
	public static final int MODE_2JOUEURS = 2;
	public static final int MODE_BLUETOOTH_SERVEUR = 3;
	public static final int MODE_BLUETOOTH_CLIENT = 4;
	public static final int MODE_WIFI_SERVEUR = 5;
	public static final int MODE_WIFI_CLIENT = 6;
	
	private static ParametrePartie parametres;
	
	private ParametrePartie() {
		// TODO Auto-generated constructor stub
	}
	
	public static ParametrePartie getParametrePartie()
	{
		if (parametres == null)
		{
			parametres = new ParametrePartie();
		}
		
		return parametres;
	}
}