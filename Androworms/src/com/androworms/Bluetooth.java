package com.androworms;

import android.bluetooth.BluetoothAdapter;


/*
 * Cette classe inclue toutes les informations pour contacter
 * un joueur en bluetooth.
 */
public class Bluetooth extends Contact {

	public Bluetooth(String adresse) {
		super();
		BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
		init(ba, adresse);
		// TODO Auto-generated constructor stub
	}

}
