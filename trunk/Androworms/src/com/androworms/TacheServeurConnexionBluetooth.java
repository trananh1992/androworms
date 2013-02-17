package com.androworms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.androworms.R;

/** Lors de la création d'une partie en Multi-joueur en Bluetooth,
 *  ce Thread est crée pour crée pour tourner et attendre les connexions clientes.
 */
public class TacheServeurConnexionBluetooth extends AsyncTask<Void, String, Boolean> {
	
	private static final String TAG = "Androworms.TacheServeurConnexionBluetooth";
	
	// On crée une socket Bluetooth pour le serveur lors de l'initalisation du jeu pour attendre les connexions clientes.
	// Une fois que le jeu est démarré, on a plus besoin de cette socket (le serveur de connexion est fermé).
	private BluetoothServerSocket socketServeur;
	
	// Gestion de la liste des clients déjà connecté
	private ArrayAdapter<String> adaptateurListeClients;
	private List<String> listeClients;
	
	// On garde le statut du serveur pour pouvoir forcer sa fermeture
	private boolean fermetureConnexionsForce = false;
	
	public TacheServeurConnexionBluetooth(ActiviteCreationPartieBluetooth activiteCreationPartieBluetooth) {
		ActiviteCreationPartie activiteCreationPartie = activiteCreationPartieBluetooth.getActiviteCreationPartie();
		
		Log.v(TAG, "Création de la socket publique");
		
		// Gestion de la liste des clients déjà connecté
		listeClients = new ArrayList<String>();
		adaptateurListeClients = new ArrayAdapter<String>(activiteCreationPartie, android.R.layout.simple_list_item_1, android.R.id.text1, listeClients);
		
		// Affichage de la liste des clients déjà connecté
		ListView lv = (ListView)activiteCreationPartie.findViewById(R.id.liste_appareils_bluetoothS);
		lv.setAdapter(adaptateurListeClients);
		lv.setVisibility(View.VISIBLE);
		
		try {
			// Création de la socket avec le UUID (définit aléatoiremenet pour l'application)
			socketServeur = BluetoothAdapter.getDefaultAdapter().listenUsingRfcommWithServiceRecord("Androworms", Contact.ANDROWORMS_UUID);
		} catch (Exception e) {
			Log.e(TAG, "Erreur sur la creation de la socket publique ");
			Log.e(TAG, "\t"+e.getMessage());
		}
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		BluetoothSocket socket = null;
		
		while (!fermetureConnexionsForce) {
			try {
				socket = socketServeur.accept();
				
				if (socket.getRemoteDevice() != null) {
					Log.v(TAG, "Le serveur a accepté une connexion ! (device=" + socket.getRemoteDevice().getName() + ")");
				}
				else {
					Log.e(TAG,"Le serveur a accepté une connexion ! Mais ERREUR le device est est null");
				}
				
				// On ajoute le client dans la liste sur l'interface du client
				publishProgress(socket.getRemoteDevice().getName());
				
				// On ajoute la socket à la liste des sockets clients du serveur
				ParametresPartie.getParametresPartie().addSocketsClients(socket);
				
				//manageConnectedSocket(socket);
			} catch (Exception e) {
				if (fermetureConnexionsForce) {
					// C'est un cas prévu et exceptionnelle de fermeture forcé du serveur de connexions Bluetooth
					Log.v(TAG, "Fermeture forcé et controlé du serveur de connexions Bluetooth");
				} else {
					// C'est un cas non prévu -> génération d'une exception
					Log.e(TAG, "Erreur dans la reception d'une connexion cliente sur le ServeurConnexionBluetooth", e);
					break;
				}
			}
		}
		
		return true;
	}
	
	@Override
	protected void onProgressUpdate(String... name) {
		// Ajout d'un client dans la liste des clients sur le serveur
		listeClients.add(name[0]);
		adaptateurListeClients.notifyDataSetChanged();
	}
	
	/** On force la fermeture des connexions */
	public void fermetureConnexionsForce() {
		Log.v(TAG, "fermetureConnexionsForce()");
		// On note dans un flag qu'on a forcer la fermeture pour le traitement des erreurs
		fermetureConnexionsForce = true;
		try {
			// Comme écrit dans la documentation, pour arrêter un accept() on peut utiliser close()
			// http://developer.android.com/reference/android/bluetooth/BluetoothServerSocket.html#accept()
			socketServeur.close();
		} catch (IOException e) {
			
		}
	}
	
	private void manageConnectedSocket(BluetoothSocket mmSocket) {
		receiveMessage(mmSocket);
		sendMessage(mmSocket);
	}
	
	private void sendMessage(BluetoothSocket mmSocket) {
		/* Plus d'informations sur le passage d'objet sérialisable : //http://stackoverflow.com/questions/2836646/java-serializable-object-to-byte-array */
		try {
			Log.v(TAG,"Je suis le SERVEUR et je vais envoyer un objet 'Personnage' !");
			
			OutputStream os = mmSocket.getOutputStream();
			
			Personnage p = new Personnage("Chuck Norris", new ImageInformation());
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);  
			out.writeObject(p);
			byte[] yourBytes = bos.toByteArray();
			os.write(yourBytes);
			out.close();
			bos.close();
		} catch (IOException e) {
			Log.e(TAG, "Erreur dans l'envoi du message sur le Serveur Bluetooth", e);
		}
	}
	
	private void receiveMessage(BluetoothSocket mmSocket) {
		try {
			InputStream is = mmSocket.getInputStream();
			
			byte[] buffer = new byte[1024];
			is.read(buffer);
			
			String messageRecu = new String(buffer);
			Log.d(TAG, "MESSAGE RECU : " + messageRecu);
			
		} catch (IOException e) {
			Log.e(TAG, "Erreur dans la reception du message sur le Serveur Bluetooth", e);
		}
	}
}