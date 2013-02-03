package com.androworms;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActiviteAndroworms extends Activity {
	
	private static final String TAG = "Androworms.ActiviteAndroworms";
	
	// Codes de demande de l'Intent
	public static final int REQUEST_CAM = 3;
	
	private static final String RETOUR_LIGNE_HTML = "<br/>";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG,"Androworms : Bienvenue sur le spashscreen");
		
		/* Affiche la vue */
		setContentView(R.layout.splash_screen);
		
		/* Affiche la liste des développeurs */
		String[] listeDev = getResources().getStringArray(R.array.liste_developpeurs);
		StringBuffer buf = new StringBuffer();
		for (int i=0;i<listeDev.length;i++) {
			buf.append(listeDev[i]);
			buf.append(RETOUR_LIGNE_HTML);
		}
		String txtDev = buf.toString();
		TextView tvDevelopers;
		tvDevelopers = (TextView)findViewById(R.id.textView3);
		tvDevelopers.setText(Html.fromHtml(txtDev));
		
		/* Appel au BluetoothAdapter */
		//Dans les vieilles version d'android, il faut charger le bluetooth depuis une activité
		// avant de pouvoir l'utiliser en background
		// cf : http://code.google.com/p/android/issues/detail?id=16587
		BluetoothAdapter.getDefaultAdapter();
		
		/* Démarrage du Thread qui charge les données nécessaires à l'application */
		final Chargement ch = new Chargement();
		ch.execute(this);
		
		/* Gestion de l'utilisateur qui touche l'écran pour passer le SplashScreen */
		LinearLayout ll = (LinearLayout)findViewById(R.id.LinearLayout2);
		ll.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ch.terminerChargement();
			}
		});
	}
	
	/** Chargement du menu principal quand le SplashScreen est fini */
	public void chargerMenuPrincipal() {
		Log.v(TAG,"Lancement de l'activité du menu principale");
		this.setContentView(R.layout.menu_principal);
		
		/* Affiche la vue */
		setContentView(R.layout.menu_principal);
		
		/* Evenements */
		OnClickListener cl = new ActiviteAndrowormsEvent(this);
		findViewById(R.id.btn_menu_jouer).setOnClickListener(cl);
		findViewById(R.id.btn_menu_editeur).setOnClickListener(cl);
		findViewById(R.id.btn_menu_score).setOnClickListener(cl);
		findViewById(R.id.btn_menu_parametres).setOnClickListener(cl);
		
		// Evenements sur les boutons de TEST
		findViewById(R.id.btn_menu_multi).setOnClickListener(cl);
		findViewById(R.id.btn_DEBUG).setOnClickListener(cl);
		findViewById(R.id.btn_GYRO).setOnClickListener(cl);
	}
}