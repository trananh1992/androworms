package com.androworms;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.androworms.ui.BluetoothCustomAdapter;

public class ActiviteCreationPartieBluetoothClient {
	
	private static final String TAG = "Androworms.ActiviteCreationPartie.Bluetooth.Client";
	
	private ActiviteCreationPartie activiteCreationPartie;
	private ActiviteCreationPartieBluetooth activiteCreationPartieBluetooth;
	
	// Listes des appareils Bluetooth jumélés et à proximité
	private List<BluetoothDevice> appareilJumele;
	private List<BluetoothDevice> appareilProximite;
	
	public ActiviteCreationPartieBluetoothClient(ActiviteCreationPartieBluetooth activiteCreationPartieBluetooth) {
		this.activiteCreationPartieBluetooth = activiteCreationPartieBluetooth;
		this.activiteCreationPartie = activiteCreationPartieBluetooth.getActiviteCreationPartie();
	}
	
	public void addAppareilProximite(BluetoothDevice device) {
		appareilProximite.add(device);
	}
	
	/** Chargement de l'interface Bluetooth > Client **/
	public void chargementInterfaceBluetoothClient() {
		
		Log.v(TAG, "Chargement de l'interface : Bluetooth > Client");
		
		/** Définition des composants **/
		ToggleButton tgEtatBluetooth = (ToggleButton)activiteCreationPartie.findViewById(R.id.tg_EtatBluetoothC);
		final Button btnAnalyse = (Button)activiteCreationPartie.findViewById(R.id.btn_analyse);
		final ListView lvAppareilsBluetooth = (ListView)activiteCreationPartie.findViewById(R.id.lv_appareils_bluetooth);
		
		/** Init des listes des appareils **/
		appareilJumele = new ArrayList<BluetoothDevice>();
		appareilProximite = new ArrayList<BluetoothDevice>();
		
		listerAppareilsJumeles();
		
		/** Actualisation de l'interface **/
		actualisationInterfaceBluetoothClient();
		
		/** Création des évenements sur les composants graphiques **/
		tgEtatBluetooth.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					// Activation du Bluetooth (2 possibilités)
					// La première est de faire une Intent pour demander à l'utilisateur s'il est d'accord.
					Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
					activiteCreationPartie.startActivityForResult(enableBtIntent, ActiviteCreationPartieBluetooth.DEMANDE_ACTIVATION_BLUETOOTH_CLIENT);
					// La seconde consiste à l'activer l'activer directement sans accord de l'utilisateur : mBluetoothAdapter.enable();
					// La documentation est formel sur le sujet : IL EST INTERDIT DE FAIRE LA METHODE 2 !
					// cf http://developer.android.com/reference/android/bluetooth/BluetoothAdapter.html#enable()
				} else {
					// Désactivation du Bluetooth
					Bluetooth.getBluetoothAdapter().disable();
					
					// On lance une surveillance du changement d'état du Bluetooth car c'est une opération asynchrone.
					Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_STATE_CHANGED);
					activiteCreationPartie.startActivityForResult(enableBtIntent, ActiviteCreationPartieBluetooth.DEMANDE_DESACTIVATION_BLUETOOTH_CLIENT);
				}
			}
		});
		
		/* Bouton d'analyse */
		btnAnalyse.setOnClickListener(new OnClickListener() {
			// On click sur le bouton "Analyse"
			public void onClick(View v) {
				
				appareilProximite.clear();
				ProgressBar pb = (ProgressBar)activiteCreationPartie.findViewById(R.id.pb_bluetooth_analyse);
				pb.setVisibility(View.VISIBLE);
				TextView tvMessage = (TextView)activiteCreationPartie.findViewById(R.id.tv_message);
				tvMessage.setText("Recherche des appareils à proximité...");
				
				// Si j'étais déjà en mode analyse, je stop l'analyse
				if (Bluetooth.getBluetoothAdapter().isDiscovering()) {
					Bluetooth.getBluetoothAdapter().cancelDiscovery();
				}
				
				// Recherche des périphériques visibles
				boolean res = Bluetooth.getBluetoothAdapter().startDiscovery();
				if (!res) {
					new AlertDialog.Builder(activiteCreationPartie).setTitle("Androworms").setMessage("Une erreur c'est produite. Contacter le 5556 pour avoir plus d'infos.").setNeutralButton("Close", null).show();
				}
			}
		});
		
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		activiteCreationPartie.registerReceiver(activiteCreationPartieBluetooth.getmReceiver(), filter);
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		activiteCreationPartie.registerReceiver(activiteCreationPartieBluetooth.getmReceiver(), filter);
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
		// Coché si le Bluetooth est déjà activé
		tgEtatBluetooth.setChecked(Bluetooth.getBluetoothAdapter().isEnabled());
		
		if (Bluetooth.getBluetoothAdapter().isEnabled()) {
			rafraichirListeAppareils();
		}
		else {
			ListView lv = (ListView)activiteCreationPartie.findViewById(R.id.lv_appareils_bluetooth);
			lv.setAdapter(null);
		}
	}
	
	/** Liste les appareils bluetooth jumélés **/
	public void listerAppareilsJumeles() {
		Set<BluetoothDevice> pairedDevices = Bluetooth.getBluetoothAdapter().getBondedDevices();
		if (pairedDevices.size() > 0) {
			appareilJumele.clear();
			for (BluetoothDevice device : pairedDevices) {
				// J'ajoute les appareils jumelés à une liste
				appareilJumele.add(device);
			}
		}
	}
	
	/** Actualise la liste des appreils Bluetooth **/
	public void rafraichirListeAppareils() {
		final ListView lv = (ListView)activiteCreationPartie.findViewById(R.id.lv_appareils_bluetooth);
		
		// On vide la liste pour éventullement la remplir après
		lv.setAdapter(null);
		ArrayAdapter<BluetoothDevice> adapterA,adapterB;
		BluetoothDevice[] valuesA, valuesB;
		
		
		valuesA = new BluetoothDevice[appareilJumele.size()];
		for (int j=0;j<appareilJumele.size();j++) {
			BluetoothDevice device = appareilJumele.get(j);
			valuesA[j] = device;
		}
		valuesB = new BluetoothDevice[appareilProximite.size()];
		for (int j=0;j<appareilProximite.size();j++) {
			BluetoothDevice device = appareilProximite.get(j);
			valuesB[j] = device;
		}
		
		// FIXME : j'ai pas compris les paramètres 2 et 3 du ArrayAdapter
		// android.R.layout.simple_list_item_checked       simple_list_item_activated_1
		adapterA = new ArrayAdapter<BluetoothDevice>(activiteCreationPartie, android.R.layout.simple_list_item_checked, valuesA);
		adapterB = new ArrayAdapter<BluetoothDevice>(activiteCreationPartie, android.R.layout.simple_list_item_checked, valuesB);
		
		BluetoothCustomAdapter adapter = new BluetoothCustomAdapter(activiteCreationPartie);
		
		adapter.ajouterSections("Appareils jumelés", adapterA);
		adapter.ajouterSections("Appareils à proximité", adapterB);
		
		lv.setAdapter(adapter);
	}
	
	/** L'utilisateur demande à revenir au statut précédent.
	 *  Dans certains cas, on veux afficher une popup pour lui demander s'il veux vraiment quitter */
	public void surActionPrecedent() {
		//FIXME : si on a rejoint le serveur, il faut demander une validation
		activiteCreationPartie.etapePrecedente(true);
	}
}