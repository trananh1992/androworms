package com.androworms.multijoueurs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.androworms.Personnage;

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
		manageConnectedSocket(mmSocket);
	}
	
	private void manageConnectedSocket(BluetoothSocket mmSocket) {
		sendMessage(mmSocket);
		receiveMessage(mmSocket);
	}
	
	private void sendMessage(BluetoothSocket mmSocket) {
		Log.v("a","Je suis le client et j'envoie un MESSAGE STRING !");
		try {
			OutputStream os =mmSocket.getOutputStream();
			
			String s = "Hello, je suis le client et j'envoie un message String !";
			os.write(s.getBytes());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void receiveMessage(BluetoothSocket mmSocket) {
		try {
			InputStream is = mmSocket.getInputStream();
			
			byte[] buffer = new byte[1024];
			is.read(buffer);
			/*Personnage p = buffer;
			Log.d("MESSAGE RECU",buffer.toString());*/
			
			ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
			ObjectInput in = null;
			try {
				in = new ObjectInputStream(bis);
				Object o = in.readObject(); 
				Personnage p = (Personnage)o;
				
				Log.d("MESSAGE RECU",p.getNom());
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				bis.close();
				in.close();
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
 
	/** Will cancel an in-progress connection, and close the socket */
	public void cancel() {
		try {
			mmSocket.close();
		} catch (IOException e) { }
	}
}