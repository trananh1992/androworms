package com.androworms;

import com.androworms.utile.Informations;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActiviteAndroworms extends Activity {
	
	private static final String TAG = "Androworms.SplashScreen";
	
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
		findViewById(R.id.btn_solo).setOnClickListener(cl);
		findViewById(R.id.btn_multi).setOnClickListener(cl);
		findViewById(R.id.btn_menu_score).setOnClickListener(cl);
		findViewById(R.id.btn_menu_settings).setOnClickListener(cl);
		// Evenements sur les boutons de TEST
		//OnClickListener camCl = new ActiviteCreationCarte(this);
		//findViewById(R.id.test_cam).setOnClickListener(camCl);
		TestClassListener testL = new TestClassListener(this);
		findViewById(R.id.test_cam).setOnClickListener(testL); 
		// Afficher els informations du téléphone
		afficherInformationsTelephone();
	}
	
	public void afficherInformationsTelephone() {
		/* Informations sur le téléphone */
		StringBuffer buf = new StringBuffer();
		buf.append("<b>Android</b>" + RETOUR_LIGNE_HTML);
		buf.append("Version = " + Informations.getAndroidVersion() + RETOUR_LIGNE_HTML);
		buf.append("SDK = " + Informations.getAndroidSdk() + RETOUR_LIGNE_HTML);
		buf.append(RETOUR_LIGNE_HTML);
		buf.append("<b>Taille de l'écran</b>" + RETOUR_LIGNE_HTML);
		buf.append("WidthPixels = " + Informations.getWidthPixels() + " px" + RETOUR_LIGNE_HTML);
		buf.append("HeightPixels = " + Informations.getHeightPixels() + " px" + RETOUR_LIGNE_HTML);
		buf.append("Taille = ");
		switch(Informations.getScreenLayoutSizeMask()) {
		case Configuration.SCREENLAYOUT_SIZE_SMALL:
			buf.append("SMALL");
			break;
		case Configuration.SCREENLAYOUT_SIZE_NORMAL:
			buf.append("NORMAL");
			break;
		case Configuration.SCREENLAYOUT_SIZE_LARGE:
			buf.append("LARGE");
			break;
		case Configuration.SCREENLAYOUT_SIZE_XLARGE:
			buf.append("XLARGE");
			break;
		default:
			buf.append("???");
			break;
		}
		buf.append(RETOUR_LIGNE_HTML);
		buf.append(RETOUR_LIGNE_HTML);
		buf.append("<b>Densité de l'écran</b>" + RETOUR_LIGNE_HTML);
		buf.append("Density = " + Informations.getDensity() + RETOUR_LIGNE_HTML);
		buf.append("DensityDPI = " + Informations.getDensityDpi() + " dp   (");
		switch (Informations.getDensityDpi()) {
		case DisplayMetrics.DENSITY_LOW:
			buf.append("ldpi -- LOW");
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			buf.append("mdpi -- MEDIUM");
			break;
		case DisplayMetrics.DENSITY_HIGH:
			buf.append("hdpi -- HIGH");
			break;
		case DisplayMetrics.DENSITY_XHIGH:
			buf.append("xhdpi -- XHIGH");
			break;
		default:
			buf.append("??? -- ???");
			break;
		}
		buf.append(")" + RETOUR_LIGNE_HTML);
		buf.append(RETOUR_LIGNE_HTML);
		buf.append("<b>Bluetooth</b>" + RETOUR_LIGNE_HTML);
		buf.append("Compatible Bluetooth : " + Informations.isCompatibleBluetooth() + RETOUR_LIGNE_HTML);
		buf.append("Bluetooth On (valeur au démarrage de l'apps) : " + Informations.isBluetoothOn() + RETOUR_LIGNE_HTML);
		String txtInfo = buf.toString();
		TextView tv = (TextView)findViewById(R.id.textView2);
		tv.setText(Html.fromHtml(txtInfo));
	}
	
	private class TestClassListener implements OnClickListener
	{
		private ActiviteAndroworms parent;
		public TestClassListener(ActiviteAndroworms p)
		{
			parent = p;
		}
		
		public void onClick(View v)
		{
			Intent intent = new Intent(parent, ActiviteChoixOption.class);
			this.parent.startActivity(intent);
		}
		
	}
}
