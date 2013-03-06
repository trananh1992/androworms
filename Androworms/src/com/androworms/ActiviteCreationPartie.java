package com.androworms;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.androworms.utile.Informations;

public class ActiviteCreationPartie extends Activity {
	
	private static final String TAG = "Androworms.ActiviteCreationPartie";
	private ActiviteCreationPartieBluetooth activiteCreationPartieBluetooth;
	private ActiviteCreationPartieEvent evenements;
	
	// Etape dans la création d'une partie
	private int etape = 0;
	private static final int ETAPE_1_CHOIX_MODE_JEU = 1;
	private static final int ETAPE_2_CONFIGURATION_MODE_JEU = 2;
	private static final int ETAPE_3_CHOIX_CARTE = 3;
	private static final int ETAPE_4_CHOIX_EQUIPE = 4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "Début de la création de la partie");
		evenements = new ActiviteCreationPartieEvent(this);
		etapeSuivante();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		Log.v(TAG,"onRestart()");
		if (ParametresPartie.getParametresPartie().getModeJeu() == ParametresPartie.MODE_BLUETOOTH_SERVEUR) {
			Log.v(TAG, "SERVEUR");
			activiteCreationPartieBluetooth.getServeur().actualisationInterfaceBluetoothServeur();
		}
		else if (ParametresPartie.getParametresPartie().getModeJeu() == ParametresPartie.MODE_BLUETOOTH_CLIENT) {
			Log.v(TAG, "CLIENT");
			activiteCreationPartieBluetooth.getClient().actualisationInterfaceBluetoothClient();
		}
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.v(TAG,"onConfigurationChanged()");
	}
	
	private void etape1() {
		// Etape courante
		etape = ETAPE_1_CHOIX_MODE_JEU;
		
		// Affichage de la vue
		setContentView(R.layout.activite_creation_partie_1_mode);
		
		// Gestion des composants > Mode de jeu > 1 téléphone
		findViewById(R.id.btn_partie_solo).setOnClickListener(evenements);
		findViewById(R.id.btn_multi_joueur).setOnClickListener(evenements);
		
		// Gestion des composants > Mode de jeu > Bluetooth
		if (Informations.isCompatibleBluetooth()) {
			// Compatible Bluetooth
			findViewById(R.id.btn_bluetooth_creer).setOnClickListener(evenements);
			findViewById(R.id.btn_bluetooth_rejoindre).setOnClickListener(evenements);
		} else {
			// Pas compatible Bluetooth
			findViewById(R.id.btn_bluetooth_creer).setEnabled(false);
			findViewById(R.id.btn_bluetooth_rejoindre).setEnabled(false);
		}
		
		// Gestion des composants > Mode de jeu > Wifi Direct
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			// Compatible Wifi Direct
			findViewById(R.id.btn_wifi_creer).setOnClickListener(evenements);
			findViewById(R.id.btn_wifi_rejoindre).setOnClickListener(evenements);
		} else {
			// Pas compatible Wifi Direct
			findViewById(R.id.btn_wifi_creer).setEnabled(false);
			findViewById(R.id.btn_wifi_rejoindre).setEnabled(false);
		}
		
		// Gestion des composants > Aide
		findViewById(R.id.img_aide_1_telephone).setOnClickListener(evenements);
		findViewById(R.id.img_aide_2_telephones).setOnClickListener(evenements);
	}
	
	private void etape2() {
		etape = ETAPE_2_CONFIGURATION_MODE_JEU;
		
		switch (ParametresPartie.getParametresPartie().getModeJeu()) {
		case ParametresPartie.MODE_SOLO:
			etape2ModeSolo();
			break;
		case ParametresPartie.MODE_2JOUEURS:
			etape2ModeDeuxjoueurs();
			break;
		case ParametresPartie.MODE_BLUETOOTH_SERVEUR:
			etape2ModeBluetoothServeur();
			break;
		case ParametresPartie.MODE_BLUETOOTH_CLIENT:
			etape2ModeBluetoothClient();
			break;
		case ParametresPartie.MODE_WIFI_SERVEUR:
			etape2ModeWifiServeur();
			break;
		case ParametresPartie.MODE_WIFI_CLIENT:
			etape2ModeWifiClient();
			break;
		default:
			etape1();
		}
	}
	
	private void etape2ModeSolo() {
		/* Affichage de la vue */
		setContentView(R.layout.activite_creation_partie_2_solo);
		
		//FIXME : quand je clic sur "suivant", je dois sauvegarder l'état de la progressbar
		// Regression en r236
		// https://code.google.com/p/androworms/source/diff?path=/trunk/Androworms/src/com/androworms/ActiviteCreationPartie.java&format=side&r=236
		
		// Boutons "Précédent" et "Suivant"
		findViewById(R.id.btn_precedent).setOnClickListener(evenements);
		findViewById(R.id.btn_suivant).setOnClickListener(evenements);
	}
	
	private void etape2ModeDeuxjoueurs() {
		// Non disponible pour le moment
		new AlertDialog.Builder(this).setTitle(R.string.app_name).setMessage("Les parties 2 joueurs ne sont pas encore dispo !").setNeutralButton("Close", null).show();
	}
	
	private void etape2ModeBluetoothServeur() {
		activiteCreationPartieBluetooth = new ActiviteCreationPartieBluetooth(this);
		/* Affichage de la vue */
		setContentView(R.layout.activite_creation_partie_2_bluetooth_serveur);
		/* Chargement des composants */
		activiteCreationPartieBluetooth.getServeur().chargementInterfaceBluetoothServeur();
		
		// Boutons "Précédent"
		findViewById(R.id.btn_precedent).setOnClickListener(evenements);
	}
	
	private void etape2ModeBluetoothClient() {
		activiteCreationPartieBluetooth = new ActiviteCreationPartieBluetooth(this);
		/* Affichage de la vue */
		setContentView(R.layout.activite_creation_partie_2_bluetooth_client);
		/* Chargement des composants */
		activiteCreationPartieBluetooth.getClient().chargementInterfaceBluetoothClient();
		
		// Boutons "Précédent"
		findViewById(R.id.btn_precedent).setOnClickListener(evenements);
	}
	
	private void etape2ModeWifiServeur() {
		// Non disponible pour le moment
		new AlertDialog.Builder(this).setTitle(R.string.app_name).setMessage("Les parties en Wifi-Direct ne sont pas encore dispo !").setNeutralButton("Close", null).show();
	}
	
	private void etape2ModeWifiClient() {
		// Non disponible pour le moment
		new AlertDialog.Builder(this).setTitle(R.string.app_name).setMessage("Les parties en Wifi-Direct ne sont pas encore dispo !").setNeutralButton("Close", null).show();
	}
	
	private void etape3() {
		etape = ETAPE_3_CHOIX_CARTE;
		/* Affichage de la vue */
		setContentView(R.layout.activite_creation_partie_3);
		
		File root = Environment.getExternalStorageDirectory();
		File sd = new File(root, ActiviteAndroworms.DOSSIER_CARTE);
		
		//gets a list of the files
		File[] sdDirList = sd.listFiles(); 
		ListView mapChooser = (ListView)findViewById(R.id.mapChooser);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.text_view_spinner);
		int i = 0;
		if (sdDirList != null) {
			for(i=0;i<sdDirList.length;i++) {
				Log.v(TAG, "adding item");
				adapter.add(sdDirList[i].getName());
				Log.v(TAG, "added item "+sdDirList[i].getName());
			}
		}
		adapter.notifyDataSetChanged();
		mapChooser.setAdapter(adapter);		
		mapChooser.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {
				afficheCarte((String)parentView.getAdapter().getItem(position));
			}
		});
		
		// Boutons "Précédent" et "Suivant"
		findViewById(R.id.btn_precedent).setOnClickListener(evenements);
		findViewById(R.id.btn_suivant).setOnClickListener(evenements);
	}
	
	private void etape4() {
		etape = ETAPE_4_CHOIX_EQUIPE;
		setContentView(R.layout.activite_creation_partie_4);
		
		// Boutons "Précédent"
		findViewById(R.id.btn_precedent).setOnClickListener(evenements);
		
		// Bouton "Démarrer la partie"
		findViewById(R.id.btn_demarrer_la_partie).setOnClickListener(evenements);
	}
	
	private void afficheCarte(String map) {
		ParametresPartie.getParametresPartie().setEstCartePerso(true);
		ParametresPartie.getParametresPartie().setNomCarte(map);	
		ImageView v = (ImageView)findViewById(R.id.chosen_map);
		File root = Environment.getExternalStorageDirectory();
		File sd = new File(root, ActiviteAndroworms.DOSSIER_CARTE + map);
		Bitmap b = BitmapFactory.decodeFile(sd.getAbsolutePath());
		Bitmap thumbnail = Bitmap.createScaledBitmap(b, 300, 200, false);
		v.setImageBitmap(thumbnail);
	}
	
	@Override
	protected void onDestroy() {
		Log.v(TAG, "onDestroy()");
		super.onDestroy();
		if (ParametresPartie.getParametresPartie().getModeJeu() == ParametresPartie.MODE_BLUETOOTH_SERVEUR
				|| ParametresPartie.getParametresPartie().getModeJeu() == ParametresPartie.MODE_BLUETOOTH_CLIENT) {
			// Destroy des éléments du Bluetooth
			activiteCreationPartieBluetooth.onDestroy();
		}
	}
	
	/** Lors de l'appuie sur la touche "Back" du téléphone */
	@Override
	public void onBackPressed() {
		Log.v(TAG, "onBackPressed()");
		Log.v(TAG, "Etape : " + etape);
		etapePrecedente(false);
	}
	
	/** Passer à l'étape suivante */
	public void etapeSuivante() {
		Log.v(TAG, "On est à l'étape " + etape + " et on va à l'étape SUIVANTE");
		switch(etape) {
		case ETAPE_1_CHOIX_MODE_JEU:
			etape2();
			break;
		case ETAPE_2_CONFIGURATION_MODE_JEU:
			etape3();
			break;
		case ETAPE_3_CHOIX_CARTE:
			etape4();
			break;
		case ETAPE_4_CHOIX_EQUIPE:
			Intent intent = new Intent(ActiviteCreationPartie.this, ActiviteJeu.class);
			startActivity(intent);
			/* On arrête l'application comme ça quand on sera sur la partie de jeu et qu'on fait la flèche de "retour à l'activité précédente",
			   on arrivera sur le menu principal */
			finish();
			break;
		default :
			etape1();
			break;
		}
	}
	
	/** Passer à l'étape précédente */
	public void etapePrecedente(boolean avecApprobationUtilisateur) {
		Log.v(TAG, "On est à l'étape " + etape + " et on va à l'étape PRECEDENTE");
		if (avecApprobationUtilisateur || estOkPourPasserEtapePrecedente()) {
			Log.v(TAG, "Ok pour faire précédent");
			switch(etape) {
			case ETAPE_1_CHOIX_MODE_JEU:
				// Marche aussi si on précise pas "finish()"
				finish();
				break;
			case ETAPE_2_CONFIGURATION_MODE_JEU:
				etape1();
				break;
			case ETAPE_3_CHOIX_CARTE:
				etape2();
				break;
			case ETAPE_4_CHOIX_EQUIPE:
				etape3();
				break;
			default :
				break;
			}
		}
		else {
			Log.v(TAG, "Impossible de faire précédent car non OK");
		}
	}
	
	private boolean estOkPourPasserEtapePrecedente() {
		Log.v(TAG, "On est à l'étape " + etape + " et on va à l'étape PRECEDENTE");
		switch(etape) {
		case ETAPE_2_CONFIGURATION_MODE_JEU:
			switch (ParametresPartie.getParametresPartie().getModeJeu()) {
			case ParametresPartie.MODE_BLUETOOTH_SERVEUR:
				activiteCreationPartieBluetooth.getServeur().faireActionPrecedent();
				return false;
			case ParametresPartie.MODE_BLUETOOTH_CLIENT:
				activiteCreationPartieBluetooth.getClient().surActionPrecedent();
				return false;
			default:
				break;
			}
			break;
		case ETAPE_3_CHOIX_CARTE:
			// TODO
			break;
		case ETAPE_4_CHOIX_EQUIPE:
			//TODO : en cas de partie BLuetooth
			break;
		default :
			break;
		}
		return true;
	}
	
	
	
	/** DEBUT DE SECTION : Fonctions pour le Bluetooth */
	
	/** Gestion des demandes d'activation/visibilité du Bluetooth */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		activiteCreationPartieBluetooth.onActivityResult(requestCode, resultCode, data);
	}
	
	/** FIN DE SECTION : Fonctions pour le Bluetooth */
}