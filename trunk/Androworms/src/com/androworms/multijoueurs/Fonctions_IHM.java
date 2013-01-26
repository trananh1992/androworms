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

import com.androworms.Informations;
import com.androworms.R;

public class Fonctions_IHM {
	
	private static final String TAG = "Fonctions_IHM";
	private ActiviteMultiJoueur activiteMultiJoueur;
	
	public Fonctions_IHM(ActiviteMultiJoueur activiteMultiJoueur) {
		this.activiteMultiJoueur = activiteMultiJoueur;
	}
	
	/** Chargement de l'interface Bluetooth > Serveur **/
	public void chargementInterfaceBluetoothServeur() {
		Log.v(TAG, "Chargement de l'interface : Bluetooth > Serveur");
		
		// Définition des composants
		ToggleButton tg_etatBluetooth = (ToggleButton)activiteMultiJoueur.findViewById(R.id.tg_EtatBluetoothS);
		Button btn_demarrerPartie = (Button)activiteMultiJoueur.findViewById(R.id.btn_demarrerPartie);
		Button btn_MontrerBluetooth = (Button)activiteMultiJoueur.findViewById(R.id.btn_MontrerBluetooth);
		
		// Actualisation des
		actualisationInterfaceBluetoothServeur();
		
		
		
		/** Actions sur les composants **/
		tg_etatBluetooth.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			// TODO : ce bouton peux être faux si :
			// - on lance Androworms (Bluetooth OFF)
			// - On active le Bluetooth avec le bouton
			// - on appuie sur le bouton Home du tel
			// - on Désactive le Bluetooth
			// - on retourne sur l'appli
			// (bon il est mis à jour avec le bouton "Actualiser")
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					// Activation du Bluetooth (2 possibilités)
					// La première est de faire une Intent pour demander à l'utilisateur s'il est d'accord.
					Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
					activiteMultiJoueur.startActivityForResult(enableBtIntent, ActiviteMultiJoueur.DEMANDE_ACTIVATION_BLUETOOTH);
					// La seconde consiste à l'activer l'activer directement sans accord de l'utilisateur
//					mBluetoothAdapter.enable();
					// La documentation est formele sur le sujet : IL EST INTERDIT DE FAIRE LA METHODE 2 !
					// cf http://developer.android.com/reference/android/bluetooth/BluetoothAdapter.html#enable()
					
					// On refresh les infos
//					refresh_UI_multijoueur_bluetooth_serveur();
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
		btn_MontrerBluetooth.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.v(TAG, "On rendre le Bluetooth visible pour x secondes");
				Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, ActiviteMultiJoueur.DUREE_VISIBILITE_BLUETOOTH); // DUREE_VISIBILITE_BLUETOOTH/1000
				//startActivity(discoverableIntent);
				activiteMultiJoueur.startActivityForResult(discoverableIntent, ActiviteMultiJoueur.DEMANDE_VISIBILITE_BLUETOOTH);
				
			/*	refresh_UI_minuteur();
				
				Log.v(TAG, "Début du minuteur");
				ch = new Minuteur();
				ch.execute(ActiviteMultiJoueur.this);*/
				// On refresh les infos
				actualisationInterfaceBluetoothServeur();
			}
		});
		
		
		/* On démarrer la partie */
		btn_demarrerPartie.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.v(TAG,"On démarre la partie ?");
				if (activiteMultiJoueur.sb == null) {
					Log.v(TAG,"Vous n'avez jamais lancer le serveur");
				} else {
					Log.v(TAG,"Arret du Thread");
					activiteMultiJoueur.sb.cancel();// TODO-MH :: vérifier si c'est bien fait
					
					// TODO : arreter le thread sb
					
					Log.v("TAG","Je suis le SERVEUR et je clos le serveur ! Terminé les inscriptions ! !");
				}
			}
		});
		
		/** Démarrage du serveur Bluetooth **/
		if (Informations.isBluetoothOn()) {
			activiteMultiJoueur.start_bluetooth_serveur();
		}
	}
	
	/** Actualisation de l'interface Bluetooth > Serveur **/
	public void actualisationInterfaceBluetoothServeur() {
		Log.v(TAG, "Actualisation de l'interface : Bluetooth > Serveur");
		
		/** Définition des composants **/
		ToggleButton tg_etatBluetooth = (ToggleButton)activiteMultiJoueur.findViewById(R.id.tg_EtatBluetoothS);
		TextView tv_monNomBluetooth = (TextView)activiteMultiJoueur.findViewById(R.id.tv_monNomBluetooth);
		TextView tv_maVisibilite = (TextView)activiteMultiJoueur.findViewById(R.id.tv_maVisibilite);
		TextView tv_information = (TextView)activiteMultiJoueur.findViewById(R.id.tv_information);
		Button btn_MontrerBluetooth = (Button)activiteMultiJoueur.findViewById(R.id.btn_MontrerBluetooth);
		ProgressBar pb_minuteur = (ProgressBar)activiteMultiJoueur.findViewById(R.id.pb_Minuteur);
		ProgressBar pb_AttenteConnexion = (ProgressBar)activiteMultiJoueur.findViewById(R.id.pb_AttenteConnexion);
		TextView tv_AttenteConnexion = (TextView)activiteMultiJoueur.findViewById(R.id.tv_AttenteConnexion);
		
		/** Configuration des composants **/
		// Désactivé si l'appareil est pas compatible Bluetooth :: TODO encore utile ??
		tg_etatBluetooth.setEnabled(Informations.isCompatibleBluetooth());
		// Coché si le Bluetooth est déjà activé
		tg_etatBluetooth.setChecked(Informations.isBluetoothOn());
		
		//btn_demarrerPartie.setEnabled(false);
				
		if (!Informations.isBluetoothOn()) {
			// Le Bluetooth n'est pas actif
			
			// Elements visible + configuration
			tv_information.setVisibility(View.VISIBLE);
			tv_information.setText("Veuillez activer le Bluetooth pour pouvoir jouer !");
			
			// Elements masqués
			btn_MontrerBluetooth.setVisibility(View.INVISIBLE);
			tv_monNomBluetooth.setVisibility(View.INVISIBLE);
			tv_maVisibilite.setVisibility(View.INVISIBLE);
			pb_minuteur.setVisibility(View.INVISIBLE);
			pb_AttenteConnexion.setVisibility(View.INVISIBLE);
			tv_AttenteConnexion.setVisibility(View.INVISIBLE);
			
		} else {
			// Le Bluetooth est actif
			
			
			// Elements visible + configuration
			btn_MontrerBluetooth.setVisibility(View.VISIBLE);
			
			tv_monNomBluetooth.setVisibility(View.VISIBLE);
			tv_monNomBluetooth.setText("Mon nom : "+ActiviteMultiJoueur.mBluetoothAdapter.getName());
			
			tv_maVisibilite.setVisibility(View.VISIBLE);
			String tt = "Ma visibilité : ";
			if (ActiviteMultiJoueur.mBluetoothAdapter.getScanMode() == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
				tt += "visible";
			} else {
				tt += "invisible";
			}
			tv_maVisibilite.setText(tt);
			
			pb_minuteur.setVisibility(View.INVISIBLE);
			
			pb_AttenteConnexion.setVisibility(View.VISIBLE);
			tv_AttenteConnexion.setVisibility(View.VISIBLE);
			
			
			// Elements masqués
			tv_information.setVisibility(View.INVISIBLE);
		}
	}

	/** Chargement de l'interface Bluetooth > Client **/
	public void chargementInterfaceBluetoothClient() {
		Log.v(TAG, "Chargement de l'interface : Bluetooth > Client");
		
		/** Définition des composants **/
		ToggleButton tg_etatBluetooth = (ToggleButton)activiteMultiJoueur.findViewById(R.id.tg_EtatBluetoothC);
		final Button btn_analyse = (Button)activiteMultiJoueur.findViewById(R.id.btn_analyse);
		final ListView lv = (ListView)activiteMultiJoueur.findViewById(R.id.liste_appareils_bluetooth);
		ProgressBar pb = (ProgressBar)activiteMultiJoueur.findViewById(R.id.pb_bluetooth_analyse);
		
		/** Init des listes des appareils **/
		ActiviteMultiJoueur.appareilJumele = new ArrayList<BluetoothDevice>();
		ActiviteMultiJoueur.appareilProximite = new ArrayList<BluetoothDevice>();
		
		listerAppareilsJumeles();
		
		/** Actualisation de l'interface **/
		actualisationInterfaceBluetoothClient();
		
		/** Création des évenements sur les composants graphiques **/
		tg_etatBluetooth.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			// TODO : ce bouton peux être faux si :
			// - on lance Androworms (Bluetooth OFF)
			// - On active le Bluetooth avec le bouton
			// - on appuie sur le bouton Home du tel
			// - on Désactive le Bluetooth
			// - on retourne sur l'appli
			// (bon il est mis à jour avec le bouton "Actualiser")
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					// Activation du Bluetooth (2 possibilités)
					// La première est de faire une Intent pour demander à l'utilisateur s'il est d'accord.
					Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
					activiteMultiJoueur.startActivityForResult(enableBtIntent, ActiviteMultiJoueur.DEMANDE_ACTIVATION_BLUETOOTH);
					// La seconde consiste à l'activer l'activer directement sans accord de l'utilisateur
//					mBluetoothAdapter.enable();
					// La documentation est formele sur le sujet : IL EST INTERDIT DE FAIRE LA METHODE 2 !
					// cf http://developer.android.com/reference/android/bluetooth/BluetoothAdapter.html#enable()
					
					// On refresh les infos
					actualisationInterfaceBluetoothServeur();
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
		btn_analyse.setOnClickListener(new OnClickListener() {
			// On click sur le bouton "Analyse"
			public void onClick(View v) {
				
				ActiviteMultiJoueur.appareilProximite.clear();
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
				
				
				btn_analyse.setEnabled(false);
			}
		});
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		activiteMultiJoueur.registerReceiver(activiteMultiJoueur.mReceiver, filter);
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		activiteMultiJoueur.registerReceiver(activiteMultiJoueur.mReceiver, filter);
		ActiviteMultiJoueur.estRegister = true;
		
		/* Selection dans la liste */
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {
				Log.v(TAG,"selection de : "+lv.getItemAtPosition(position));
				activiteMultiJoueur.start_bluetooth_client((BluetoothDevice)lv.getItemAtPosition(position));
			}
		});
		
		
		pb.setVisibility(View.INVISIBLE);
	}
	
	/** Actualisation de l'interface Bluetooth > Client **/
	public void actualisationInterfaceBluetoothClient() {
		Log.v(TAG, "REFRESH de l'interface : Bluetooth > Client");
		
		/** Définition des composants **/
		ToggleButton tg_etatBluetooth = (ToggleButton)activiteMultiJoueur.findViewById(R.id.tg_EtatBluetoothC);
		Button btn_connexion = (Button)activiteMultiJoueur.findViewById(R.id.btn_connexion);
		
		/** Configuration des composants **/
		// Désactivé si l'appareil est pas compatible Bluetooth :: TODO encore utile ??
		tg_etatBluetooth.setEnabled(Informations.isCompatibleBluetooth());
		// Coché si le Bluetooth est déjà activé
		tg_etatBluetooth.setChecked(Informations.isBluetoothOn());
		
		
		if (Informations.isBluetoothOn()) {
			
			rafraichirListeAppareils();
		} else {
			// rien....pour le moment !
		}
		
		
		btn_connexion.setEnabled(true);
	}
	
	/** Liste les appareils bluetooth jumélés **/
	public void listerAppareilsJumeles() { // OK
		Set<BluetoothDevice> pairedDevices = ActiviteMultiJoueur.mBluetoothAdapter.getBondedDevices();
		if (pairedDevices.size() > 0) {
			ActiviteMultiJoueur.appareilJumele.clear();
			for (BluetoothDevice device : pairedDevices) {
				// J'ajoute les appareils jumelés à une liste
				ActiviteMultiJoueur.appareilJumele.add(device);
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
		
		
		valuesA = new BluetoothDevice[ActiviteMultiJoueur.appareilJumele.size()];
		for (int j=0;j<ActiviteMultiJoueur.appareilJumele.size();j++) {
			BluetoothDevice device = ActiviteMultiJoueur.appareilJumele.get(j);
			valuesA[j] = device;
		}
		valuesB = new BluetoothDevice[ActiviteMultiJoueur.appareilProximite.size()];
		for (int j=0;j<ActiviteMultiJoueur.appareilProximite.size();j++) {
			BluetoothDevice device = ActiviteMultiJoueur.appareilProximite.get(j);
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
	
	/** Ajout de la connexion d'un client sur l'interface du serveur **/
	public void addNewPlayer(BluetoothDevice device) {
		// FIXME : ne fonctionne pas !
		
		ListView lv = (ListView)activiteMultiJoueur.findViewById(R.id.liste_appareils_bluetoothD);
		
		BluetoothDevice[] values;
		values = new BluetoothDevice[1];
		
		ArrayAdapter<BluetoothDevice> adapter = new ArrayAdapter<BluetoothDevice>(activiteMultiJoueur, android.R.layout.simple_list_item_1, android.R.id.text1, values);
		
		lv.setAdapter(adapter);
	}
}