package com.androworms;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;

public class ActiviteCreationPartie extends Activity {
	
	private static final String TAG = "Androworms.ActiviteCreationPartie";
	private ActiviteCreationPartieBluetooth activiteCreationPartieBluetooth;
	private int etape = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "Début de la création de la partie");
		etape1();
	}
	
	private void etape1() {
		etape = 1;
		
		setContentView(R.layout.activite_creation_partie_1_mode);
		
		Button btnPartieSolo = (Button)findViewById(R.id.btn_partie_solo);
		btnPartieSolo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ParametresPartie.getParametresPartie().setModeJeu(ParametresPartie.MODE_SOLO);
				etape2();
			}
		});
		Button btnMultiJoueur = (Button)findViewById(R.id.btn_multi_joueur);
		btnMultiJoueur.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ParametresPartie.getParametresPartie().setModeJeu(ParametresPartie.MODE_2JOUEURS);
				etape2();
			}
		});
		Button btnBluetoothCreer = (Button)findViewById(R.id.btn_bluetooth_creer);
		btnBluetoothCreer.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ParametresPartie.getParametresPartie().setModeJeu(ParametresPartie.MODE_BLUETOOTH_SERVEUR);
				etape2();
			}
		});
		Button btnBluetoothRejoindre = (Button)findViewById(R.id.btn_bluetooth_rejoindre);
		btnBluetoothRejoindre.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ParametresPartie.getParametresPartie().setModeJeu(ParametresPartie.MODE_BLUETOOTH_CLIENT);
				etape2();
			}
		});
		Button btnWifiCreer = (Button)findViewById(R.id.btn_wifi_creer);
		btnWifiCreer.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ParametresPartie.getParametresPartie().setModeJeu(ParametresPartie.MODE_WIFI_SERVEUR);
				etape2();
			}
		});
		Button btnWifiRejoindre = (Button)findViewById(R.id.btn_wifi_rejoindre);
		btnWifiRejoindre.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ParametresPartie.getParametresPartie().setModeJeu(ParametresPartie.MODE_WIFI_CLIENT);
				etape2();
			}
		});
		
		
		ImageView imgAide1Telephone = (ImageView)findViewById(R.id.img_aide_1_telephone);
		imgAide1Telephone.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(ActiviteCreationPartie.this);
				builder.setMessage("Faire une partie quand vous n'avez que un seul téléphone à votre disposition.");
				builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
				builder.setCancelable(false);
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
		ImageView imgAide2Telephones = (ImageView)findViewById(R.id.img_aide_2_telephones);
		imgAide2Telephones.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(ActiviteCreationPartie.this);
				builder.setMessage("Faire une partie quand vous avez plusieurs téléphones à votre disposition.");
				builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
				builder.setCancelable(false);
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}
	
	private void etape2() {
		etape = 2;
		
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
		default :
			etape1();
		}
	}
	
	private void etape2ModeSolo() {
		/* Affichage de la vue */
		setContentView(R.layout.activite_creation_partie_2_solo);
		
		/* Bouton "Précédent" */
		Button btnPrecedent = (Button)findViewById(R.id.btn_precedent);
		btnPrecedent.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape1();
			}
		});
		/* Bouton "Suivant" */
		Button btnSuivant = (Button)findViewById(R.id.btn_suivant);
		btnSuivant.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SeekBar sbDifficulteIA = (SeekBar)findViewById(R.id.sb_difficulte_IA);
				ParametresPartie.getParametresPartie().setDifficuluteIA(sbDifficulteIA.getProgress());
				etape3();
			}
		});
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
		activiteCreationPartieBluetooth.chargementInterfaceBluetoothServeur();
		
		/* Bouton "Précédent" */
		Button btnPrecedent = (Button)findViewById(R.id.btn_precedent);
		btnPrecedent.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape1();
			}
		});
	}
	
	private void etape2ModeBluetoothClient() {
		activiteCreationPartieBluetooth = new ActiviteCreationPartieBluetooth(this);
		/* Affichage de la vue */
		setContentView(R.layout.activite_creation_partie_2_bluetooth_client);
		/* Chargement des composants */
		activiteCreationPartieBluetooth.chargementInterfaceBluetoothClient();
		
		/* Bouton "Précédent" */
		Button btnPrecedent = (Button)findViewById(R.id.btn_precedent);
		btnPrecedent.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape1();
			}
		});
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
		etape = 3;
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
				Log.e("test","adding item");
				adapter.add(sdDirList[i].getName());
				Log.e("test","added item "+sdDirList[i].getName());
			}
		}
		adapter.notifyDataSetChanged();
		mapChooser.setAdapter(adapter);		
		mapChooser.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {
				afficheCarte((String)parentView.getAdapter().getItem(position));
			}
		});
		
		/* Bouton "Précédent" */
		Button btnPrecedent = (Button)findViewById(R.id.btn_precedent);
		btnPrecedent.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape2();
			}
		});
		/* Bouton "Suivant" */
		Button btnSuivant = (Button)findViewById(R.id.btn_suivant);
		btnSuivant.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape4();
			}
		});
	}
	
	private void etape4() {
		setContentView(R.layout.activite_creation_partie_4);
		
		/* Bouton "Précédent" */
		Button btnPrecedent = (Button)findViewById(R.id.btn_precedent);
		btnPrecedent.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape3();
			}
		});
		
		/* Bouton "Démarrer la partie" */
		Button btnDemarrerPartie = (Button)findViewById(R.id.btn_demarrer_la_partie);
		btnDemarrerPartie.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ActiviteCreationPartie.this, ActiviteJeu.class);
				startActivity(intent);
				/* On arrête l'application comme ça quand on sera sur la partie de jeu et qu'on fait la flèche de "retour à l'activité précédente",
				   on arrivera sur le menu principal */
				finish();
			}
		});
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
	public void onStop() {
		super.onStop();
		Log.v(TAG,"onStop()");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (ParametresPartie.getParametresPartie().getModeJeu() == ParametresPartie.MODE_BLUETOOTH_SERVEUR
				|| ParametresPartie.getParametresPartie().getModeJeu() == ParametresPartie.MODE_BLUETOOTH_CLIENT) {
			// Destroy des éléments du Bluetooth
			activiteCreationPartieBluetooth.onDestroy();
		}
	}
	
	@Override
	public void onBackPressed() {
		Log.v(TAG, "Etape : "+etape);
		switch(etape) {
		case 1:
			// Marche aussi si on précise pas "finish()"
			finish();
			break;
		case 2:
			switch (ParametresPartie.getParametresPartie().getModeJeu()) {
			case ParametresPartie.MODE_SOLO:
			case ParametresPartie.MODE_2JOUEURS:
				etape1();
				break;
			case ParametresPartie.MODE_BLUETOOTH_SERVEUR:
				// TODO
				break;
			case ParametresPartie.MODE_BLUETOOTH_CLIENT:
				// TODO
				break;
			default:
				break;
			}
			break;
		case 3:
			// TODO
			break;
		case 4:
			//TODO : en cas de partie BLuetooth
			etape3();
			break;
		default :
			break;
		}
	}
	
	/** DEBUT DE SECTION : Fonctions pour le Bluetooth */
	
	/** Gestion des demandes d'activation/visibilité du Bluetooth */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		activiteCreationPartieBluetooth.onActivityResult(requestCode, resultCode, data);
	}
	
	public void passerEtape3DepuisServeurBluetooth() {
		etape3();
	}
	
	public void passerEtape3DepuisClientBluetooth() {
		etape3();
	}
	
	/** FIN DE SECTION : Fonctions pour le Bluetooth */
}