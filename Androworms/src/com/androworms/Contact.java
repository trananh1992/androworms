package com.androworms;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class Contact {
	private BluetoothSocket socket;
    private BluetoothDevice device;
	private UUID uuid;
	
	public Contact()
	{
	}
	
	public Contact(BluetoothDevice device)
	{
		this.device = device;
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

    public void connect() {
        // Cancel discovery because it will slow down the connection
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
 
        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            getSocket().connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            try {
                getSocket().close();
            } catch (IOException closeException) { }
            return;
        }
 
        // Do work to manage the connection (in a separate thread)
        //manageConnectedSocket(mmSocket);
    }
 
    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            getSocket().close();
        } catch (IOException e) { }
    }

	
}
