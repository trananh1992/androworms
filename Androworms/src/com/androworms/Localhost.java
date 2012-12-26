package com.androworms;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

/*
 * Cette classe permet de contacter un joueur en local.
 * Il y a toutes les informations necessaire a cela.
 */
public class Localhost extends Contact {
	
	public Localhost()
	{
		super();
	}
	
	public void connection()
	{
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
		BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
		BluetoothDevice bd = ba.getRemoteDevice(ba.getAddress());
		setDevice(bd);
        setUuid(UUID.randomUUID());
 
        // Get a BluetoothSocket to connect with the given BluetoothDevice
        BluetoothSocket tmp;
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = bd.createRfcommSocketToServiceRecord(getUuid());
        } catch (IOException e) 
        { 
        	tmp = null;
        	Log.v("Localhost", "Impossible de creer une socket vers le matériel local");
        }
        setSocket(tmp);
	}

 


}
