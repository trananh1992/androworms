package com.androworms;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androworms.R;

/** Lors de la création d'une partie en Multi-joueur en Bluetooth,
 *  ce Thread est crée pour crée l'échange de socket entre le client et le serveur.
 */
public class ClientConnexionBluetooth extends AsyncTask<Void, Integer, Boolean> {
	
	private static final String TAG_CLIENT = "ClientBluetooth";
	private BluetoothSocket socketServeur;
	private ActiviteCreationPartie activiteCreationPartie;
	
	public ClientConnexionBluetooth(ActiviteCreationPartieBluetooth activiteCreationPartieBluetooth, BluetoothDevice device) {
		this.activiteCreationPartie = activiteCreationPartieBluetooth.getActiviteCreationPartie();
		try {
			socketServeur = device.createRfcommSocketToServiceRecord(Contact.ANDROWORMS_UUID);
		} catch (IOException e) {
			
		}
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		// On annule la recherche d'appareil à proximité si elle était lancé (elle sert plus à rien)
		ActiviteCreationPartieBluetooth.mBluetoothAdapter.cancelDiscovery();
		
		try {
			socketServeur.connect();
			Log.d(TAG_CLIENT, "Le client à accepté la connexion !");
			
		} catch (IOException connectException) {
			// Impossible de se connecter, donc on clos la connexion
			try {
				socketServeur.close();
			} catch (IOException closeException) {
				
			}
			return false;
		}
		
		// Do work to manage the connection (in a separate thread)
		//manageConnectedSocket(socketServeur);
		return true;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		// A la fin de l'opération :
		if (result) {
			// en cas de succès, on affiche un message pour dire que tout s'est bien passé et que l'on attends les insctructions du serveur
			Button btnConnexion = (Button)activiteCreationPartie.findViewById(R.id.btn_connexion);
			TextView tvMessage = (TextView)activiteCreationPartie.findViewById(R.id.tv_message);
			// On actualise l'interface graphique du client
			btnConnexion.setEnabled(true);
			tvMessage.setText("Tout est ok !");
		} else {
			// En cas d'échec, on affiche un message d'erreur
			Context context = activiteCreationPartie.getApplicationContext();
			CharSequence text = "Echec de la connexion avec le serveur.";
			int duration = Toast.LENGTH_LONG;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		
		ProgressBar pbBluetoothAnalyse = (ProgressBar)activiteCreationPartie.findViewById(R.id.pb_bluetooth_analyse);
		pbBluetoothAnalyse.setVisibility(View.INVISIBLE);
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
			Log.e(TAG_CLIENT, "Erreur dans l'envoi du message sur le Client Bluetooth", e);
		}
	}
	
	private void receiveMessage(BluetoothSocket mmSocket) {
		try {
			InputStream is = mmSocket.getInputStream();
			
			byte[] buffer = new byte[1024];
			is.read(buffer);
			
			ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
			ObjectInput in = null;
			try {
				in = new ObjectInputStream(bis);
				Object o = in.readObject(); 
				Personnage p = (Personnage)o;
				
				Log.d("MESSAGE RECU : ",p.getNom());
				
			} catch (ClassNotFoundException e) {
				Log.e(TAG_CLIENT, "Erreur dans la reception du message sur le Client Bluetooth", e);
			} finally {
				bis.close();
				in.close();
			}
			
			
		} catch (IOException e) {
			Log.e(TAG_CLIENT, "Erreur dans la reception du message sur le Client Bluetooth", e);
		}
	}
}