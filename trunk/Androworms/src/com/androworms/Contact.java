package com.androworms;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public abstract class Contact {
	private BluetoothSocket socket;
	private BluetoothDevice device;
	private UUID uuid;
	
	public Contact() {
	}

	public BluetoothSocket getSocket() {
		return socket;
	}

	public void setSocket(BluetoothSocket socket) {
		this.socket = socket;
	}

	public BluetoothDevice getDevice() {
		return device;
	}

	public void setDevice(BluetoothDevice device) {
		this.device = device;
	}

	
	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	
	public void init(BluetoothAdapter ba, String adresse){
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
		BluetoothDevice bd = ba.getRemoteDevice(adresse);
		setDevice(bd);
		setUuid(UUID.randomUUID());
 
        // Get a BluetoothSocket to connect with the given BluetoothDevice
		BluetoothSocket tmp;
		try {
			// MY_UUID is the app's UUID string, also used by the server code
			tmp = bd.createRfcommSocketToServiceRecord(getUuid());
		} catch (IOException e) { 
			tmp = null;
			Log.v("Localhost", "Impossible de creer une socket vers le matériel local");
		}
		setSocket(tmp);		
	}
	
	public void connect() {
		// Cancel discovery because it will slow down the connection
		BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
		
		try {
			// Connect the device through the socket. This will block
			// until it succeeds or throws an exception
			getSocket().connect();
		} catch (IOException connectException) {
			// Unable to connect; close the socket and get out
			Log.v("Contact", "Impossible connecter la socket !");
			try {
				getSocket().close();
			} catch (IOException closeException) {
				Log.v("Contact", "Impossible de refermée la socket !");
			}
			return;
		}
	}
	
	/** Will cancel an in-progress connection, and close the socket */
	public void cancel() {
		try {
			getSocket().close();
		} catch (IOException e) { }
	}
}