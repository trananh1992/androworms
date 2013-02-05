package com.androworms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ActiviteCreationPartie extends Activity {
	
	private static final String TAG = "Androworms.ActiviteCreationPartie";
	
	private static final int MODE_SOLO = 1;
	private static final int MODE_2JOUEURS = 2;
	private static final int MODE_BLUETOOTH_SERVEUR = 3;
	private static final int MODE_BLUETOOTH_CLIENT = 4;
	private static final int MODE_WIFI_SERVEUR = 5;
	private static final int MODE_WIFI_CLIENT = 6;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "Début de la création de la partie");
		etape1();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		etape1();
	}
	
	private void etape1() {
		setContentView(R.layout.activite_creation_partie_1_mode);
		
		Button btnPartieSolo = (Button)findViewById(R.id.btn_partie_solo);
		btnPartieSolo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape2ModeSolo();
			}
		});
		
		Button btnMultiJoueur = (Button)findViewById(R.id.btn_multi_joueur);
		btnMultiJoueur.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape2ModeDeuxjoueurs();
			}
		});
		
		Button btnBluetoothCreer = (Button)findViewById(R.id.btn_bluetooth_creer);
		btnBluetoothCreer.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape2ModeBluetoothServeur();
			}
		});
		
		Button btnBluetoothRejoindre = (Button)findViewById(R.id.btn_bluetooth_rejoindre);
		btnBluetoothRejoindre.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape2ModeBluetoothClient();
			}
		});
		
		
		Button btnSuivant = (Button)findViewById(R.id.btn_suivant);
		btnSuivant.setEnabled(false);
	}
	
	private void etape2ModeSolo() {
		/* Affichage de la vue */
		setContentView(R.layout.activite_creation_partie_2_solo);
		
		Button btnPrecedent = (Button)findViewById(R.id.btn_precedent);
		btnPrecedent.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape1();
			}
		});
		
		Button btnSuivant = (Button)findViewById(R.id.btn_suivant);
		btnSuivant.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape3(ActiviteCreationPartie.MODE_SOLO);
			}
		});
	}
	
	private void etape2ModeDeuxjoueurs() {
		// Non disponible pour le moment
		new AlertDialog.Builder(this).setTitle("Androworms").setMessage("Les parties 2 joueurs ne sont pas encore dispo !").setNeutralButton("Close", null).show();
	}
	
	private void etape2ModeBluetoothServeur() {
		/* Affichage de la vue */
		setContentView(R.layout.multi_joueur_bluetooth_serveur);
		/* Chargement des composants */
//		chargementInterfaceBluetoothServeur();
	}
	
	private void etape2ModeBluetoothClient() {
		/* Affichage de la vue */
		setContentView(R.layout.multi_joueur_bluetooth_client);
		/* Chargement des composants */
//		chargementInterfaceBluetoothClient();
	}
	
	private void etape3(final int mode) {
		/* Affichage de la vue */
		setContentView(R.layout.activite_creation_partie_3);
		
		Button btnPrecedent = (Button)findViewById(R.id.btn_precedent);
		btnPrecedent.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				switch (mode) {
				case ActiviteCreationPartie.MODE_SOLO:
					etape2ModeSolo();
					break;
				case ActiviteCreationPartie.MODE_2JOUEURS:
					etape2ModeDeuxjoueurs();
					break;
				case ActiviteCreationPartie.MODE_BLUETOOTH_SERVEUR:
					etape2ModeBluetoothServeur();
					break;
				case ActiviteCreationPartie.MODE_BLUETOOTH_CLIENT:
					etape2ModeBluetoothClient();
					break;
				default :
					etape1();
				}
			}
		});
		
		Button btnSuivant = (Button)findViewById(R.id.btn_suivant);
		btnSuivant.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape4(mode);
			}
		});
	}
	
	private void etape4(final int mode) {
		setContentView(R.layout.activite_creation_partie_4);
		
		Button btnPrecedent = (Button)findViewById(R.id.btn_precedent);
		btnPrecedent.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape3(mode);
			}
		});
		
		Button btnDemarrerPartie = (Button)findViewById(R.id.btn_demarrer_la_partie);
		btnDemarrerPartie.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ActiviteCreationPartie.this, ActiviteJeu.class);
				startActivity(intent);
				finish();
			}
		});
	}
}