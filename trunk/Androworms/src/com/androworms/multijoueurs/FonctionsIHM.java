package com.androworms.multijoueurs;

import java.util.ArrayList;
import java.util.Set;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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

import com.androworms.R;
import com.androworms.utile.Informations;

public class FonctionsIHM {
	
	private static final String TAG = "Fonctions_IHM";
	private ActiviteMultiJoueur activiteMultiJoueur;
	
	public FonctionsIHM(ActiviteMultiJoueur activiteMultiJoueur) {
		this.activiteMultiJoueur = activiteMultiJoueur;
	}
	
	/** Chargement de l'interface Bluetooth > Serveur **/
	public void chargementInterfaceBluetoothServeur() {
		Log.v(TAG, "Chargement de l'interface : Bluetooth > Serveur");
		
		// Définition des composants
		ToggleButton tgEtatBluetooth = (ToggleButton)activiteMultiJoueur.findViewById(R.id.tg_EtatBluetoothS);
		Button btnDemarrerPartie = (Button)activiteMultiJoueur.findViewById(R.id.btn_demarrerPartie);
		Button btnMontrerBluetooth = (Button)activiteMultiJoueur.findViewById(R.id.btn_MontrerBluetooth);
		
		// Actualisation des éléments de l'interface graphique du Serveur
		actualisationInterfaceBluetoothServeur();
		
		/** Actions sur les composants **/
		tgEtatBluetooth.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			// FIXME : ce bouton peut ne pas indiquer l'état réel du Bluetooth
			// Exemple : on désactive le Bluetooth en Alt/Tab
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					// Activation du Bluetooth (2 possibilités)
					// La première est de faire une Intent pour demander à l'utilisateur s'il est d'accord.
					Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
					activiteMultiJoueur.startActivityForResult(enableBtIntent, ActiviteMultiJoueur.DEMANDE_ACTIVATION_BLUETOOTH);
					// La seconde consiste à l'activer l'activer directement sans accord de l'utilisateur : mBluetoothAdapter.enable();
					// La documentation est formel sur le sujet : IL EST INTERDIT DE FAIRE LA METHODE 2 !
					// cf http://developer.android.com/reference/android/bluetooth/BluetoothAdapter.html#enable()
					
					// On refresh les infos
					actualisationInterfaceBluetoothServeur();
				} else {
					// Désactivation du Bluetooth
					BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
					mBluetoothAdapter.disable();
					// On refresh les infos
					actualisationInterfaceBluetoothServeur();
					// TODO : NE MARCHE PAS COMME IL DEVRAIT
					// ce refresh devrait vider la liste. mais je pense que la désactivation du Bletooth prend quelques secondes
					// et que du coup, la liste ne se vide pas.
				}
			}
		});
		
		/* Montrer Bluetooth */
		btnMontrerBluetooth.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.v(TAG, "On rendre le Bluetooth visible pour x secondes");
				Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, ActiviteMultiJoueur.DUREE_VISIBILITE_BLUETOOTH);
				//startActivity(discoverableIntent);
				activiteMultiJoueur.startActivityForResult(discoverableIntent, ActiviteMultiJoueur.DEMANDE_VISIBILITE_BLUETOOTH);
				
				// On refresh les infos
				actualisationInterfaceBluetoothServeur();
			}
		});
		
		/* On démarrer la partie */
		btnDemarrerPartie.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.v(TAG,"On démarre la partie ?");
				if (activiteMultiJoueur.serveurConnexionBluetooth == null) {
					Log.v(TAG,"Vous n'avez jamais lancer le serveur");
				} else {
					Log.v(TAG,"Arret du Thread");
					
					// On arrête le serveur de connexion Bluetooth
					activiteMultiJoueur.serveurConnexionBluetooth.cancel(true);
					
					Log.v("TAG","Je suis le SERVEUR et je clos le serveur ! Terminé les inscriptions ! ! on va joueur...");
					
					// TODO le serveur doit dire aux clients qu'on commence à jouer !
					
					activiteMultiJoueur.lancerLeJeu();
				}
			}
		});
		
		/** Démarrage du serveur Bluetooth **/
		if (Informations.isBluetoothOn()) {
			activiteMultiJoueur.demarrerServeurBluetooth();
		}
	}
	
	/** Actualisation de l'interface Bluetooth > Serveur **/
	public void actualisationInterfaceBluetoothServeur() {
		Log.v(TAG, "Actualisation de l'interface : Bluetooth > Serveur");
		
		/** Définition des composants **/
		ToggleButton tgEtatBluetooth = (ToggleButton)activiteMultiJoueur.findViewById(R.id.tg_EtatBluetoothS);
		TextView tvMonNomBluetooth = (TextView)activiteMultiJoueur.findViewById(R.id.tv_monNomBluetooth);
		TextView tvMaVisibilite = (TextView)activiteMultiJoueur.findViewById(R.id.tv_maVisibilite);
		TextView tvInformation = (TextView)activiteMultiJoueur.findViewById(R.id.tv_information);
		Button btnMontrerBluetooth = (Button)activiteMultiJoueur.findViewById(R.id.btn_MontrerBluetooth);
		ProgressBar pbMinuteur = (ProgressBar)activiteMultiJoueur.findViewById(R.id.pb_Minuteur);
		ProgressBar pbAttenteConnexion = (ProgressBar)activiteMultiJoueur.findViewById(R.id.pb_AttenteConnexion);
		TextView tvAttenteConnexion = (TextView)activiteMultiJoueur.findViewById(R.id.tv_AttenteConnexion);
		
		/** Configuration des composants **/
		// Désactivé si l'appareil est pas compatible Bluetooth :: TODO encore utile ??
		tgEtatBluetooth.setEnabled(Informations.isCompatibleBluetooth());
		// Coché si le Bluetooth est déjà activé
		tgEtatBluetooth.setChecked(Informations.isBluetoothOn());
		
		//btn_demarrerPartie.setEnabled(false);
				
		if (!Informations.isBluetoothOn()) {
			// Le Bluetooth n'est pas actif
			
			// Elements visible + configuration
			tvInformation.setVisibility(View.VISIBLE);
			tvInformation.setText("Veuillez activer le Bluetooth pour pouvoir jouer !");
			
			// Elements masqués
			btnMontrerBluetooth.setVisibility(View.INVISIBLE);
			tvMonNomBluetooth.setVisibility(View.INVISIBLE);
			tvMaVisibilite.setVisibility(View.INVISIBLE);
			pbMinuteur.setVisibility(View.INVISIBLE);
			pbAttenteConnexion.setVisibility(View.INVISIBLE);
			tvAttenteConnexion.setVisibility(View.INVISIBLE);
			
		} else {
			// Le Bluetooth est actif
			
			
			// Elements visible + configuration
			btnMontrerBluetooth.setVisibility(View.VISIBLE);
			
			tvMonNomBluetooth.setVisibility(View.VISIBLE);
			tvMonNomBluetooth.setText("Mon nom : "+ActiviteMultiJoueur.mBluetoothAdapter.getName());
			
			tvMaVisibilite.setVisibility(View.VISIBLE);
			String tt = "Ma visibilité : ";
			if (ActiviteMultiJoueur.mBluetoothAdapter.getScanMode() == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
				tt += "visible";
			} else {
				tt += "invisible";
			}
			tvMaVisibilite.setText(tt);
			
			pbMinuteur.setVisibility(View.INVISIBLE);
			
			pbAttenteConnexion.setVisibility(View.VISIBLE);
			tvAttenteConnexion.setVisibility(View.VISIBLE);
			
			
			// Elements masqués
			tvInformation.setVisibility(View.INVISIBLE);
		}
	}

	/** Chargement de l'interface Bluetooth > Client **/
	public void chargementInterfaceBluetoothClient() {
		Log.v(TAG, "Chargement de l'interface : Bluetooth > Client");
		
		/** Définition des composants **/
		ToggleButton tgEtatBluetooth = (ToggleButton)activiteMultiJoueur.findViewById(R.id.tg_EtatBluetoothC);
		final Button btnAnalyse = (Button)activiteMultiJoueur.findViewById(R.id.btn_analyse);
		final ListView lvAppareilsBluetooth = (ListView)activiteMultiJoueur.findViewById(R.id.liste_appareils_bluetooth);
		ProgressBar pbBluetoothAnalyse = (ProgressBar)activiteMultiJoueur.findViewById(R.id.pb_bluetooth_analyse);
		
		/** Init des listes des appareils **/
		activiteMultiJoueur.appareilJumele = new ArrayList<BluetoothDevice>();
		activiteMultiJoueur.appareilProximite = new ArrayList<BluetoothDevice>();
		
		listerAppareilsJumeles();
		
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
					activiteMultiJoueur.startActivityForResult(enableBtIntent, ActiviteMultiJoueur.DEMANDE_ACTIVATION_BLUETOOTH);
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
				
				activiteMultiJoueur.appareilProximite.clear();
				ProgressBar pb = (ProgressBar)activiteMultiJoueur.findViewById(R.id.pb_bluetooth_analyse);
				pb.setVisibility(View.VISIBLE);
				
				// Recherche des périphériques  visibles
				Log.v(TAG,"State = "+ActiviteMultiJoueur.mBluetoothAdapter.getState());
				if (ActiviteMultiJoueur.mBluetoothAdapter.isDiscovering()) {
					ActiviteMultiJoueur.mBluetoothAdapter.cancelDiscovery();
					Log.e(TAG,"J'étais isDiscovering() et maintenant j'ai fais cancelDiscovery()");
					Log.e(TAG,"Ce n'est pas censé arriver !!");
				}
				
				boolean res = ActiviteMultiJoueur.mBluetoothAdapter.startDiscovery();
				if (!res) {
					new AlertDialog.Builder(activiteMultiJoueur).setTitle("Androworms").setMessage("Une erreur c'est produite. Contacter le 5556 pour avoir plus d'infos.").setNeutralButton("Close", null).show();
				}
				
				
				btnAnalyse.setEnabled(false);
			}
		});
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		activiteMultiJoueur.registerReceiver(activiteMultiJoueur.mReceiver, filter);
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		activiteMultiJoueur.registerReceiver(activiteMultiJoueur.mReceiver, filter);
		activiteMultiJoueur.estServeurLance = true;
		
		/* Selection dans la liste */
		lvAppareilsBluetooth.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lvAppareilsBluetooth.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {
				Log.v(TAG,"selection de : "+lvAppareilsBluetooth.getItemAtPosition(position));
				activiteMultiJoueur.demarrerClientBluetooth((BluetoothDevice)lvAppareilsBluetooth.getItemAtPosition(position));
			}
		});
		
		pbBluetoothAnalyse.setVisibility(View.INVISIBLE);
	}
	
	/** Actualisation de l'interface Bluetooth > Client **/
	public void actualisationInterfaceBluetoothClient() {
		Log.v(TAG, "REFRESH de l'interface : Bluetooth > Client");
		
		/** Définition des composants **/
		ToggleButton tgEtatBluetooth = (ToggleButton)activiteMultiJoueur.findViewById(R.id.tg_EtatBluetoothC);
		Button btnConnexion = (Button)activiteMultiJoueur.findViewById(R.id.btn_connexion);
		
		/** Configuration des composants **/
		// Désactivé si l'appareil est pas compatible Bluetooth :: TODO encore utile ??
		tgEtatBluetooth.setEnabled(Informations.isCompatibleBluetooth());
		// Coché si le Bluetooth est déjà activé
		tgEtatBluetooth.setChecked(Informations.isBluetoothOn());
		
		if (Informations.isBluetoothOn()) {
			rafraichirListeAppareils();
		}
		
		btnConnexion.setEnabled(false);
	}
	
	/** Liste les appareils bluetooth jumélés **/
	public void listerAppareilsJumeles() {
		Set<BluetoothDevice> pairedDevices = ActiviteMultiJoueur.mBluetoothAdapter.getBondedDevices();
		if (pairedDevices.size() > 0) {
			activiteMultiJoueur.appareilJumele.clear();
			for (BluetoothDevice device : pairedDevices) {
				// J'ajoute les appareils jumelés à une liste
				activiteMultiJoueur.appareilJumele.add(device);
			}
		}
	}
	
	/** Actualise la liste des appreils Bluetooth **/
	public void rafraichirListeAppareils() {
		final ListView lv = (ListView)activiteMultiJoueur.findViewById(R.id.liste_appareils_bluetooth);
		
		// On vide la liste pour éventullement la remplir après
		lv.setAdapter(null);
		ArrayAdapter<BluetoothDevice> adapterA,adapterB;
		BluetoothDevice[] valuesA, valuesB;
		
		
		valuesA = new BluetoothDevice[activiteMultiJoueur.appareilJumele.size()];
		for (int j=0;j<activiteMultiJoueur.appareilJumele.size();j++) {
			BluetoothDevice device = activiteMultiJoueur.appareilJumele.get(j);
			valuesA[j] = device;
		}
		valuesB = new BluetoothDevice[activiteMultiJoueur.appareilProximite.size()];
		for (int j=0;j<activiteMultiJoueur.appareilProximite.size();j++) {
			BluetoothDevice device = activiteMultiJoueur.appareilProximite.get(j);
			valuesB[j] = device;
		}
		
		// FIXME : j'ai pas compris les paramètres 2 et 3 du ArrayAdapter
		adapterA = new ArrayAdapter<BluetoothDevice>(activiteMultiJoueur, android.R.layout.simple_list_item_1, android.R.id.text1, valuesA) {
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView view = (TextView) super.getView(position, convertView, parent);
				BluetoothDevice bluetoothDevice = (BluetoothDevice) getItem(position);
				view.setText("Appareil jumelé : " + bluetoothDevice.getName() + " (" + bluetoothDevice.getAddress() + ")");
				return view;
			}
		};
		adapterB = new ArrayAdapter<BluetoothDevice>(activiteMultiJoueur, android.R.layout.simple_list_item_1, android.R.id.text1, valuesB) {
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView view = (TextView) super.getView(position, convertView, parent);
				BluetoothDevice bluetoothDevice = (BluetoothDevice) getItem(position);
				view.setText("Appareil à proximité : " + bluetoothDevice.getName() + " (" + bluetoothDevice.getAddress() + ")");
				return view;
			}
		};
		
		CustomAdapter adapter = new CustomAdapter(activiteMultiJoueur);
		
		adapter.addSection("Appareils appairés (jumelés ?)", adapterA);
		adapter.addSection("Appareils à proximité", adapterB);
		
		lv.setAdapter(adapter);
	}
	
	public void chargementComposantsCommuns() {
		// Fusion des 2 ToggleButton tg_etatBluetooth ??
	}
}