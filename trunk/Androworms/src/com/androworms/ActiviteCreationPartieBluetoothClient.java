package com.androworms;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.androworms.utile.Informations;

public class ActiviteCreationPartieBluetoothClient {
	
	private static final String TAG = "ActiviteCreationPartieBluetoothClient";
	
	private ActiviteCreationPartie activiteCreationPartie;
	private ActiviteCreationPartieBluetooth activiteCreationPartieBluetooth;
	
	public ActiviteCreationPartieBluetoothClient(ActiviteCreationPartieBluetooth activiteCreationPartieBluetooth) {
		this.activiteCreationPartieBluetooth = activiteCreationPartieBluetooth;
		this.activiteCreationPartie = activiteCreationPartieBluetooth.getActiviteCreationPartie();
	}
	
	/** Chargement de l'interface Bluetooth > Client **/
	public void chargementInterfaceBluetoothClient() {
		/** Définition des composants **/
		ToggleButton tgEtatBluetooth = (ToggleButton)activiteCreationPartie.findViewById(R.id.tg_EtatBluetoothC);
		final Button btnAnalyse = (Button)activiteCreationPartie.findViewById(R.id.btn_analyse);
		final ListView lvAppareilsBluetooth = (ListView)activiteCreationPartie.findViewById(R.id.liste_appareils_bluetooth);
		
		/** Init des listes des appareils **/
		activiteCreationPartieBluetooth.appareilJumele = new ArrayList<BluetoothDevice>();
		activiteCreationPartieBluetooth.appareilProximite = new ArrayList<BluetoothDevice>();
		
		activiteCreationPartieBluetooth.listerAppareilsJumeles();
		
		/** Actualisation de l'interface **/
		actualisationInterfaceBluetoothClient();
		
		/** Création des évenements sur les composants graphiques **/
		tgEtatBluetooth.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			// FIXME : ce bouton peut ne pas indiquer l'état réel du Bluetooth
			// Exemple : on désactive le Bluetooth en Alt/Tab
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					// Activation du Bluetooth (2 possibilités)
					// La première est de faire une Intent pour demander à l'utilisateur s'il est d'accord.
					Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
					activiteCreationPartie.startActivityForResult(enableBtIntent, ActiviteCreationPartieBluetooth.DEMANDE_ACTIVATION_BLUETOOTH_CLIENT);
					// La seconde consiste à l'activer l'activer directement sans accord de l'utilisateur : mBluetoothAdapter.enable();
					// La documentation est formel sur le sujet : IL EST INTERDIT DE FAIRE LA METHODE 2 !
					// cf http://developer.android.com/reference/android/bluetooth/BluetoothAdapter.html#enable()
					
					// On refresh les infos
					actualisationInterfaceBluetoothClient();
				} else {
					// Désactivation du Bluetooth
					BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
					mBluetoothAdapter.disable();
					// On refresh les infos
					actualisationInterfaceBluetoothClient();
					// TODO : NE MARCHE PAS COMME IL DEVRAIT
					// ce refresh devrait vider la liste. mais je pense que la désactivation du Bletooth prend quelques secondes
					// et que du coup, la liste ne se vide pas.
				}
			}
		});
		
		/* Bouton d'analyse */
		btnAnalyse.setOnClickListener(new OnClickListener() {
			// On click sur le bouton "Analyse"
			public void onClick(View v) {
				
				activiteCreationPartieBluetooth.appareilProximite.clear();
				ProgressBar pb = (ProgressBar)activiteCreationPartie.findViewById(R.id.pb_bluetooth_analyse);
				pb.setVisibility(View.VISIBLE);
				TextView tvMessage = (TextView)activiteCreationPartie.findViewById(R.id.tv_message);
				tvMessage.setText("Recherche des appareils à proximité...");
				
				// Si j'étais déjà en mode analyse, je stop l'analyse
				if (ActiviteCreationPartieBluetooth.mBluetoothAdapter.isDiscovering()) {
					ActiviteCreationPartieBluetooth.mBluetoothAdapter.cancelDiscovery();
				}
				
				// Recherche des périphériques visibles
				boolean res = ActiviteCreationPartieBluetooth.mBluetoothAdapter.startDiscovery();
				if (!res) {
					new AlertDialog.Builder(activiteCreationPartie).setTitle("Androworms").setMessage("Une erreur c'est produite. Contacter le 5556 pour avoir plus d'infos.").setNeutralButton("Close", null).show();
				}
			}
		});
		
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		activiteCreationPartie.registerReceiver(activiteCreationPartieBluetooth.mReceiver, filter);
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		activiteCreationPartie.registerReceiver(activiteCreationPartieBluetooth.mReceiver, filter);
		activiteCreationPartieBluetooth.setServeurLance(true);
		
		/* Selection dans la liste */
		lvAppareilsBluetooth.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lvAppareilsBluetooth.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {
				activiteCreationPartieBluetooth.demarrerClientBluetooth((BluetoothDevice)lvAppareilsBluetooth.getItemAtPosition(position));
			}
		});
	}
	
	/** Actualisation de l'interface Bluetooth > Client **/
	public void actualisationInterfaceBluetoothClient() {
		/** Définition des composants **/
		ToggleButton tgEtatBluetooth = (ToggleButton)activiteCreationPartie.findViewById(R.id.tg_EtatBluetoothC);
		
		/** Configuration des composants **/
		// Désactivé si l'appareil est pas compatible Bluetooth :: TODO encore utile ??
		tgEtatBluetooth.setEnabled(Informations.isCompatibleBluetooth());
		// Coché si le Bluetooth est déjà activé
		tgEtatBluetooth.setChecked(Informations.isBluetoothOn());
		
		if (Informations.isBluetoothOn()) {
			activiteCreationPartieBluetooth.rafraichirListeAppareils();
		}
		
	}
}