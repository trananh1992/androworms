package com.androworms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/** Lors de la création d'une partie en Multi-joueur en Bluetooth,
 *  ce Thread est crée pour crée l'échange de socket entre le client et le serveur.
 */
public class TacheClientConnexionBluetooth extends AsyncTask<Void, Integer, Boolean> {
	
	private static final String TAG = "Androworms.TacheClientConnexionBluetooth";
	
	// Interface graphique
	private ActiviteCreationPartie activiteCreationPartie;
	
	// Socket du serveur
	private BluetoothSocket socketServeur;
	
	// Gestion de la liste des clients qui vont aussi jouer (ce que le serveur nous envoie)
	private ArrayAdapter<String> adapter;
	private List<String> listeClients;
	
	public TacheClientConnexionBluetooth(ActiviteCreationPartieBluetooth activiteCreationPartieBluetooth, BluetoothDevice device) {
		this.activiteCreationPartie = activiteCreationPartieBluetooth.getActiviteCreationPartie();
		try {
			socketServeur = device.createRfcommSocketToServiceRecord(Contact.ANDROWORMS_UUID);
		} catch (IOException e) {
			Log.e(TAG, "Erreur sur la creation de la socket serveur");
			Log.e(TAG, "\t"+e.getMessage());
		}
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		// On annule la recherche d'appareil à proximité si elle était lancé (elle sert plus à rien)
		Bluetooth.getBluetoothAdapter().cancelDiscovery();
		
		// On tente de se connecter su serveur
		try {
			// Connexion au serveur
			socketServeur.connect();
		} catch (IOException e) {
			// Impossible de se connecter, donc on clos la connexion
			try {
				socketServeur.close();
			} catch (IOException ex) {
				Log.e(TAG, "Erreur sur la creation de la socket serveur");
				Log.e(TAG, "\t"+ex.getMessage());
			}
			publishProgress(-1);
			return false;
		}
		
		// On a réussi à se connecter au serveur.
		publishProgress(1);
		ParametresPartie.getParametresPartie().setSocketServeur(socketServeur);
		
		// On attend que le serveur passe à l'étape suivante.
		// En attendant, il me transmet la liste des clients qui se connectent !
		boolean flag = true;
		while (flag) {
			// Le serveur m'envoi le nom des nouveau client ou le top départ pour passer à l'étape suivante
			Object obj = Bluetooth.recevoirObjet(socketServeur);
			listeClients.add(obj.toString());
			if (obj instanceof Boolean) {
				boolean b = (Boolean)obj;
				if (b) {
					// Je reçois un booléen à "true", c'est le signe que le serveur passe à l'étape suivante
					flag = false;
				}
			}
			// On affiche le nouveau nom dans la liste
			publishProgress(2);
		}
		
		return true;
	}
	
	@Override
	protected void onProgressUpdate(Integer... code) {
		// Récupération des composants graphiques
		TextView tvMessage = (TextView)activiteCreationPartie.findViewById(R.id.tv_message);
		ProgressBar pbBluetoothAnalyse = (ProgressBar)activiteCreationPartie.findViewById(R.id.pb_bluetooth_analyse);
		
		switch (code[0]) {
		case -1:
			// Erreur de connexion
			Context context = activiteCreationPartie.getApplicationContext();
			CharSequence text = "Echec à la connexion.";
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			
			// On affiche sur l'interface graphique un message
			tvMessage.setText(R.string.selectionner_appareil_Bluetooth);
			pbBluetoothAnalyse.setVisibility(View.INVISIBLE);
			break;
		case 1:
			// on affiche un message à l'écran
			tvMessage.setText(R.string.message_bluetooth_client_connexion_reussi);
			
			
			// Gestion de la liste des clients qui vont aussi jouer (ce que le serveur nous envoie)
			listeClients = new ArrayList<String>();
			adapter = new ArrayAdapter<String>(activiteCreationPartie, android.R.layout.simple_list_item_checked, listeClients);
			
			// Affichage de la liste des clients qui vont aussi jouer (ce que le serveur nous envoie)
			ListView lvAppareilsBluetooth = (ListView)activiteCreationPartie.findViewById(R.id.liste_appareils_bluetooth);
			lvAppareilsBluetooth.setAdapter(adapter);
			
			
			pbBluetoothAnalyse.setVisibility(View.INVISIBLE);
			break;
		case 2:
			// Je rafraichit la liste des noms des joueurs
			adapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		if (result) {
			activiteCreationPartie.etapeSuivante();
		}
	}
}