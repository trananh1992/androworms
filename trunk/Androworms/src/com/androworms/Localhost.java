package com.androworms;

import android.bluetooth.BluetoothAdapter;


/*
 * Cette classe permet de contacter un joueur en local.
 * Il y a toutes les informations necessaire a cela.
 */
public class Localhost extends Contact {
	
	public Localhost()
	{
		super();
		BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
		init(ba, ba.getAddress());

	}

 


}
