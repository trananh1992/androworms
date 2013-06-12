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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.androworms.ui.BarreAction;
import com.androworms.utile.Informations;

/** Activité de création d'une partie.
 * Lors de la création d'une partie on peut choisir de nombreux paramètres comme le nombre de joueurs où la carte du jeu.
 */
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
		Log.v(TAG, "onRestart()");
		if (ParametresPartie.getParametresPartie().getModeJeu() == ParametresPartie.MODE_BLUETOOTH_SERVEUR) {
			Log.v(TAG, "SERVEUR");
			activiteCreationPartieBluetooth.getServeur().actualisationInterfaceBluetoothServeur();
		} else if (ParametresPartie.getParametresPartie().getModeJeu() == ParametresPartie.MODE_BLUETOOTH_CLIENT) {
			Log.v(TAG, "CLIENT");
			activiteCreationPartieBluetooth.getClient().actualisationInterfaceBluetoothClient();
		}
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.v(TAG, "onConfigurationChanged()");
	}
	
	private void etape1() {
		// Etape courante
		etape = ETAPE_1_CHOIX_MODE_JEU;
		
		// Affichage de la vue
		setContentView(R.layout.activite_creation_partie_1_mode);
		
		// Barre d'action
		BarreAction barreAction = (BarreAction)findViewById(R.id.ba_barre_action);
		barreAction.configure(this, false, R.string.titre_choix_mode_jeu, false);
		
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
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			// Compatible Wifi Direct
			findViewById(R.id.btn_wifi_creer).setOnClickListener(evenements);
			findViewById(R.id.btn_wifi_rejoindre).setOnClickListener(evenements);
		} else {
			// Pas compatible Wifi Direct
			findViewById(R.id.btn_wifi_creer).setEnabled(false);
			findViewById(R.id.btn_wifi_rejoindre).setEnabled(false);
		}
		
		// Gestion des composants > Aide
		findViewById(R.id.iv_aide_1_telephone).setOnClickListener(evenements);
		findViewById(R.id.iv_aide_2_telephones).setOnClickListener(evenements);
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
		
		// Barre d'action
		BarreAction barreAction = (BarreAction)findViewById(R.id.ba_barre_action);
		barreAction.configure(this, false, R.string.titre_config_mode_jeu, true);
		
		// FIXME : quand je clic sur "suivant", je dois sauvegarder l'état de la progressbar
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
		
		// Barre d'action
		BarreAction barreAction = (BarreAction)findViewById(R.id.ba_barre_action);
		barreAction.configure(this, false, R.string.titre_choix_carte, false);
		
		// Gestion de l'affichage de la liste des cartes (soit des cartes systèmes ou des cartes persos)
		ListView lvChoixCarte1 = (ListView) findViewById(R.id.lv_choixCartesSystemes);
		ListView lvChoixCarte2 = (ListView) findViewById(R.id.lv_choixCartesPerso);
		
		ArrayAdapter<String> adapterCartesSystemes = listeCartesSystemes();
		ArrayAdapter<String> adapterCartesPersos = listeCartesPersos();
		
		lvChoixCarte1.setAdapter(adapterCartesSystemes);
		lvChoixCarte2.setAdapter(adapterCartesPersos);
		
		// Evenements sur les listes
		lvChoixCarte1.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {
				afficheCarteSysteme((String) parentView.getAdapter().getItem(position));
			}
		});
		lvChoixCarte2.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {
				afficheCartePerso((String) parentView.getAdapter().getItem(position));
			}
		});
		
		// Pour le chargement de l'interface, on affiche les cartes systèmes
		lvChoixCarte1.setAdapter(adapterCartesSystemes);
		lvChoixCarte2.setVisibility(View.GONE);
		
		// Choix de la liste des cartes systèmes ou des cartes persos à afficher
		RadioGroup d = (RadioGroup) findViewById(R.id.rg_choixCarte);
		d.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				ListView lvChoixCarte1 = (ListView) findViewById(R.id.lv_choixCartesSystemes);
				ListView lvChoixCarte2 = (ListView) findViewById(R.id.lv_choixCartesPerso);
				if (group.getCheckedRadioButtonId() == R.id.rb_choixCartesSystemes) {
					lvChoixCarte1.setVisibility(View.VISIBLE);
					lvChoixCarte2.setVisibility(View.GONE);
				} else if (group.getCheckedRadioButtonId() == R.id.rb_choixCartesPerso) {
					lvChoixCarte1.setVisibility(View.GONE);
					lvChoixCarte2.setVisibility(View.VISIBLE);
				}
			}
		});
		
		afficheCarteSysteme(adapterCartesSystemes.getItem(0));
		
		// Boutons "Précédent" et "Suivant"
		findViewById(R.id.btn_precedent).setOnClickListener(evenements);
		findViewById(R.id.btn_suivant).setOnClickListener(evenements);
	}
	
	public ArrayAdapter<String> listeCartesSystemes() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		adapter.add("terrain_jeu_defaut_1");
		adapter.add("terrain_jeu_defaut_2");
		adapter.add("terrain_jeu_defaut_3");
		adapter.add("terrain_jeu_defaut_4");
		return adapter;
	}
	
	public ArrayAdapter<String> listeCartesPersos() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		File root = Environment.getExternalStorageDirectory();
		File sd = new File(root, ActiviteAndroworms.DOSSIER_CARTE);
		File[] sdDirList = sd.listFiles();
		int i = 0;
		if (sdDirList != null) {
			for (i = 0; i < sdDirList.length; i++) {
				adapter.add(sdDirList[i].getName());
			}
		}
		return adapter;
	}
	
	private void etape4() {
		etape = ETAPE_4_CHOIX_EQUIPE;
		setContentView(R.layout.activite_creation_partie_4);
		
		// Barre d'action
		BarreAction barreAction = (BarreAction)findViewById(R.id.ba_barre_action);
		barreAction.configure(this, false, R.string.titre_choix_team, false);
		
		// Boutons "Précédent"
		findViewById(R.id.btn_precedent).setOnClickListener(evenements);
		
		// Bouton "Démarrer la partie"
		findViewById(R.id.btn_demarrer_la_partie).setOnClickListener(evenements);
	}
	
	private void afficheCarteSysteme(String nomCarte) {
		ParametresPartie.getParametresPartie().setEstCartePerso(false);
		ParametresPartie.getParametresPartie().setNomCarte(nomCarte);
		ImageView v = (ImageView) findViewById(R.id.iv_choisir_carte);
		if (nomCarte.equals("terrain_jeu_defaut_1")) {
			v.setImageResource(R.drawable.terrain_jeu_defaut_1);
		} else if (nomCarte.equals("terrain_jeu_defaut_2")) {
			v.setImageResource(R.drawable.terrain_jeu_defaut_2);
		} else if (nomCarte.equals("terrain_jeu_defaut_3")) {
			v.setImageResource(R.drawable.terrain_jeu_defaut_3);
		} else if (nomCarte.equals("terrain_jeu_defaut_4")) {
			v.setImageResource(R.drawable.terrain_jeu_defaut_4);
		}
		
		TextView tvNomImage = (TextView) findViewById(R.id.tv_nomImage);
		tvNomImage.setText(nomCarte);
		Button btnRenommerCarte = (Button) findViewById(R.id.btn_renommerCarte);
		btnRenommerCarte.setVisibility(View.INVISIBLE);
	}
	
	private void afficheCartePerso(String nomCarte) {
		ParametresPartie.getParametresPartie().setEstCartePerso(true);
		ParametresPartie.getParametresPartie().setNomCarte(nomCarte);
		ImageView v = (ImageView) findViewById(R.id.iv_choisir_carte);
		File root = Environment.getExternalStorageDirectory();
		File sd = new File(root, ActiviteAndroworms.DOSSIER_CARTE + nomCarte);
		Bitmap b = BitmapFactory.decodeFile(sd.getAbsolutePath());
		Bitmap thumbnail = Bitmap.createScaledBitmap(b, 300, 200, false);
		v.setImageBitmap(thumbnail);
		
		TextView tvNomImage = (TextView) findViewById(R.id.tv_nomImage);
		tvNomImage.setText(nomCarte);
		Button btnRenommerCarte = (Button) findViewById(R.id.btn_renommerCarte);
		btnRenommerCarte.setVisibility(View.VISIBLE);
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
		switch (etape) {
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
		default:
			etape1();
			break;
		}
	}
	
	/** Passer à l'étape précédente */
	public void etapePrecedente(boolean avecApprobationUtilisateur) {
		Log.v(TAG, "On est à l'étape " + etape + " et on va à l'étape PRECEDENTE");
		if (avecApprobationUtilisateur || estOkPourPasserEtapePrecedente()) {
			Log.v(TAG, "Ok pour faire précédent");
			switch (etape) {
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
			default:
				break;
			}
		} else {
			Log.v(TAG, "Impossible de faire précédent car non OK");
		}
	}
	
	private boolean estOkPourPasserEtapePrecedente() {
		Log.v(TAG, "On est à l'étape " + etape + " et on va à l'étape PRECEDENTE");
		switch (etape) {
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
			// TODO : en cas de partie Bluetooth
			break;
		default:
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