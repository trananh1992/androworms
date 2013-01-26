package com.androworms.multijoueurs;

import java.io.IOException;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class ClientBluetooth extends Thread {
	
	private static final String TAG = "ClientBluetooth";
	
	//private final BluetoothSocket mmSocket;
	private final BluetoothDevice mmDevice;
	
	public BluetoothSocket mmSocket = null;
	
	public ClientBluetooth(BluetoothDevice device) {
		// Use a temporary object that is later assigned to mmSocket,
		// because mmSocket is final
		BluetoothSocket tmp = null;
		mmDevice = device;
		
		// Get a BluetoothSocket to connect with the given BluetoothDevice
		try {
			tmp = device.createRfcommSocketToServiceRecord(ActiviteMultiJoueur.ANDROWORMS_UUID);
		} catch (IOException e) { }
		mmSocket = tmp;
	}
	
	public void run() {
		// Cancel discovery because it will slow down the connection
		ActiviteMultiJoueur.mBluetoothAdapter.cancelDiscovery();
		
		try {
			// Connect the device through the socket. This will block
			// until it succeeds or throws an exception
			mmSocket.connect();
			Log.d("plop","Le client à accepté la connexion !");
		} catch (IOException connectException) {
			// Unable to connect; close the socket and get out
			try {
				mmSocket.close();
			} catch (IOException closeException) { }
			return;
		}
		
		// Do work to manage the connection (in a separate thread)
		//manageConnectedSocket(mmSocket);
	}
 
	/** Will cancel an in-progress connection, and close the socket */
	public void cancel() {
		try {
			mmSocket.close();
		} catch (IOException e) { }
	}
}