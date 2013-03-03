package com.androworms;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/** Activité de démarrage de l'application. C'est aussi l'activité principale de l'application.
 *  Cette application gère le SplashScreen, le menu principale ainsi que les informations de l'application. */
public class ActiviteAndroworms extends Activity {
	
	private static final String TAG = "Androworms.ActiviteAndroworms";
	private static final String RETOUR_LIGNE_HTML = "<br/>";
	
	// Nom du dossier crée à la racine du téléphone pour stocker les cartes perso
	public static final String DOSSIER_CARTE = "Androworms/";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG,"Androworms : Bienvenue sur le spashscreen");
		
		/* Affiche la vue */
		setContentView(R.layout.splash_screen);
		
		/* Appel au BluetoothAdapter */
		// Dans les vieilles version d'android, il faut charger le bluetooth depuis une activité
		// avant de pouvoir l'utiliser en background
		// cf : http://code.google.com/p/android/issues/detail?id=16587
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		// Comme on l'a récupéré, autant l'utiliser dans toute l'application sans refaire un appel !
		Bluetooth.setBluetoothAdapter(bluetoothAdapter);
		
		/* Démarrage du Thread qui charge les données nécessaires à l'application */
		final TacheChargement ch = new TacheChargement();
		ch.execute(this);
		
		/* Gestion de l'utilisateur qui touche l'écran pour passer le SplashScreen */
		LinearLayout ll = (LinearLayout)findViewById(R.id.ll_splash_screen);
		ll.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ch.terminerChargement();
			}
		});
	}
	
	/** Chargement du menu principal quand le SplashScreen est fini */
	public void chargerMenuPrincipal() {
		Log.v(TAG,"Lancement de l'activité du menu principale");
		
		/* Affiche la vue */
		setContentView(R.layout.menu_principal);
		
		/* Evenements */
		OnClickListener cl = new ActiviteAndrowormsEvent(this);
		findViewById(R.id.btn_menu_jouer).setOnClickListener(cl);
		findViewById(R.id.btn_menu_editeur).setOnClickListener(cl);
		findViewById(R.id.btn_menu_score).setOnClickListener(cl);
		findViewById(R.id.btn_menu_parametres).setOnClickListener(cl);
		
		// Evenements sur les boutons de TEST
		findViewById(R.id.btn_DEBUG).setOnClickListener(cl);
		findViewById(R.id.btn_GYRO).setOnClickListener(cl);
		findViewById(R.id.btn_test_jeu).setOnClickListener(cl);
		
		/* Interface des credits */
		ImageView img = (ImageView)findViewById(R.id.img_info);
		img.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				chargerMenuCredits();
			}
		});
	}
	
	/** Chargement des credits */
	public void chargerMenuCredits() {
		/* Affiche la vue */
		setContentView(R.layout.credits);
		
		/* Affiche la liste des développeurs */
		String[] listeDev = getResources().getStringArray(R.array.liste_developpeurs);
		StringBuffer buf = new StringBuffer();
		for (int i=0;i<listeDev.length;i++) {
			buf.append(listeDev[i]);
			buf.append(RETOUR_LIGNE_HTML);
		}
		String txtDev = buf.toString();
		TextView tvDevelopers;
		tvDevelopers = (TextView)findViewById(R.id.txt_liste_developpeurs);
		tvDevelopers.setText(Html.fromHtml(txtDev));
		
		/* Revenir au menu principal */
		LinearLayout ll = (LinearLayout)findViewById(R.id.ll_credits);
		ll.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				chargerMenuPrincipal();
			}
		});
	}
}