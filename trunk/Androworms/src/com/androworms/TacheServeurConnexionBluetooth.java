package com.androworms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/** Lors de la création d'une partie en Multi-joueur en Bluetooth,
 *  ce Thread est crée pour crée pour tourner et attendre les connexions clientes.
 */
public class TacheServeurConnexionBluetooth extends AsyncTask<Void, String, Boolean> {
	
	private static final String TAG = "Androworms.TacheServeurConnexionBluetooth";
	
	// Interface graphique
	private ActiviteCreationPartie activiteCreationPartie;
	
	// On crée une socket Bluetooth pour le serveur lors de l'initalisation du jeu pour attendre les connexions clientes.
	// Une fois que le jeu est démarré, on a plus besoin de cette socket (le serveur de connexion est fermé).
	private BluetoothServerSocket socketServeur;
	
	// Gestion de la liste des clients déjà connecté
	private ArrayAdapter<String> adaptateurListeClients;
	private List<String> listeClients;
	
	// On garde le statut du serveur pour pouvoir forcer sa fermeture
	private boolean fermetureConnexionsForce = false;
	
	public TacheServeurConnexionBluetooth(ActiviteCreationPartieBluetooth activiteCreationPartieBluetooth) {
		activiteCreationPartie = activiteCreationPartieBluetooth.getActiviteCreationPartie();
		
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
			Log.e(TAG, "Erreur sur la creation de la socket serveur");
			Log.e(TAG, "\t"+e.getMessage());
		}
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		BluetoothSocket socket = null;
		
		while (!fermetureConnexionsForce) {
			try {
				// on attend des connexions de clients
				socket = socketServeur.accept();
				
				if (socket.getRemoteDevice() != null) { // FIXME : mieux gérer cette erreur
					Log.v(TAG, "Le serveur a accepté une connexion ! (device=" + socket.getRemoteDevice().getName() + ")");
				}
				else {
					Log.e(TAG,"Le serveur a accepté une connexion ! Mais ERREUR le device est est null");
				}
				
				// On ajoute le client dans la liste sur l'interface du serveur
				publishProgress(socket.getRemoteDevice().getName());
				
				// On envoi à tous les clients le nom du nouveau joueur
				envoyerAuxClientsLeNomDuNouveauJoueur(socket.getRemoteDevice().getName());
				
				// On envoi au nouveau joueur
				envoyerAuNouveauJoueurLesNomsDesJoueursDejaConnecte(socket);
				
				// On ajoute la socket à la liste des sockets clients du serveur
				// On ajoute à la liste après avoir fais les envois aux client pour faciliter le traitement de celui-ci
				ParametresPartie.getParametresPartie().addSocketsClients(socket);
				
			} catch (Exception e) {
				if (fermetureConnexionsForce) {
					// C'est un cas prévu et exceptionnelle de fermeture forcé du serveur de connexions Bluetooth
					Log.v(TAG, "Fermeture forcé et controlé du serveur de connexions Bluetooth");
					
					// On communique avec les clients pour les informer qu'on passe à l'étape suivante
					for (BluetoothSocket socket2 : ParametresPartie.getParametresPartie().getSocketsClients()) {
						Bluetooth.envoyerObjet(socket2,true);
						if (Bluetooth.recevoirTexte(socket2).equals("ACK")) {
							Log.v(TAG,"ACK");
						}
						else {
							// FIXME : mieux traiter cette erreu
							// 1) relancer le client
							// 2) supprimer le client
							Log.e(TAG,"ERREUR --> !!");
						}
					}
				} else {
					// C'est un cas non prévu -> génération d'une exception
					Log.e(TAG, "Erreur dans la reception d'une connexion cliente sur le ServeurConnexionBluetooth", e);
					return false;
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
		
		Button btnSuivant = (Button)activiteCreationPartie.findViewById(R.id.btn_suivant);
		btnSuivant.setEnabled(true);
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		Context context = activiteCreationPartie.getApplicationContext();
		CharSequence text;
		int duration = Toast.LENGTH_LONG;
		
		if (result) {
			text = "Le serveur de connexion Bluetooth est fini : OK !";
		} else {
			text = "Le serveur de connexion Bluetooth est fini : ECHEC !";
		}
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		
		activiteCreationPartie.etapeSuivante();
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
			Log.e(TAG, "Erreur à l'arrêt du ServeurConnexionBluetooth");
			Log.e(TAG, "\t"+e.getMessage());
		}
	}
	
	/** Quand le serveur reçoit une connexion d'un client, il envoie à tous ses autres clients, le nom du nouveau client */
	private void envoyerAuxClientsLeNomDuNouveauJoueur(String nomNouveauJoueur) {
		for (BluetoothSocket socket : ParametresPartie.getParametresPartie().getSocketsClients()) {
			Bluetooth.envoyerObjet(socket, nomNouveauJoueur);
			if (Bluetooth.recevoirTexte(socket).equals("ACK")) {
				Log.v(TAG,"ACK");
			}
			else {
				// FIXME : mieux traiter cette erreur
				// 1) relancer le client
				// 2) supprimer le client
				Log.e(TAG,"ERREUR --> !!");
			}
		}
	}
	
	/** Quand un nouveau clietn se connecte, il reçoit le nom du serveur et le nom des joueurs déjà connecté */
	private void envoyerAuNouveauJoueurLesNomsDesJoueursDejaConnecte(BluetoothSocket socket) {
		// On envoie au nouveau client son nom pour qu'il se rajoute dans la liste
		Bluetooth.envoyerObjet(socket, socket.getRemoteDevice().getName());
		if (Bluetooth.recevoirTexte(socket).equals("ACK")) {
			Log.v(TAG,"ACK");
		}
		else {
			// FIXME : mieux traiter cette erreur
			// 1) relancer le client
			// 2) supprimer le client
			Log.e(TAG,"ERREUR --> !!");
		}
		
		// On envoie au nouveau client le nom du serveur (le serveur est aussi un joueur !)
		Bluetooth.envoyerObjet(socket, ActiviteCreationPartieBluetooth.mBluetoothAdapter.getName());
		if (Bluetooth.recevoirTexte(socket).equals("ACK")) {
			Log.v(TAG,"ACK");
		}
		else {
			// FIXME : mieux traiter cette erreur
			// 1) relancer le client
			// 2) supprimer le client
			Log.e(TAG,"ERREUR --> !!");
		}
		
		// On lui envoie le nom des joueurs déjà connecté
		for (BluetoothSocket bs : ParametresPartie.getParametresPartie().getSocketsClients()) {
			Bluetooth.envoyerObjet(socket, bs.getRemoteDevice().getName());
			if (Bluetooth.recevoirTexte(socket).equals("ACK")) {
				Log.v(TAG,"ACK");
			}
			else {
				// FIXME : mieux traiter cette erreur
				// 1) relancer le client
				// 2) supprimer le client
				Log.e(TAG,"ERREUR --> !!");
			}
		}
	}
}