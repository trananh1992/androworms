package com.androworms;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActiviteAndroworms extends Activity {
	
	private static final String TAG = "Androworms.SplashScreen";
	
	// Codes de demande de l'Intent
	public static final int REQUEST_ENABLE_BT = 1;
	public static final int REQUEST_CONNECT_DEVICE = 2;
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
		
		
		
		final Chargement ch = new Chargement();
		ch.execute(this);
		
		LinearLayout ll = (LinearLayout)findViewById(R.id.LinearLayout2);
		ll.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ch.finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void chargerMenuPrincipal() {
		Log.v(TAG,"Lancement de l'activité du menu principale");
		this.setContentView(R.layout.menu_principal);
		
		/* Test pour générer une carte à partir d'une photo fixée (path en dur)
		try {
			File root = Environment.getExternalStorageDirectory();
			File androworms = new File(root,"Androworms");
			if (!androworms.exists()) {
				androworms.mkdir();
			}
			File photo = new File(androworms,"maPhoto2.png");
			File inputTest = new File(androworms,"inputTest.jpg");
			
			FileInputStream input = new FileInputStream(inputTest);
			byte data2[];
			data2 = new byte[input.available()];
			input.read(data2);
			Carte carte = new Carte(data2);
			
			carte.save(photo.getAbsolutePath());
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//*/
		
		
		/* Affiche la vue */
		setContentView(R.layout.menu_principal);
		
		/* Evenements */
		OnClickListener tr = new IHMTestReseau(new Noyau());
		OnClickListener cl = new ActiviteAndrowormsEvent(this);
		findViewById(R.id.btn_solo).setOnClickListener(cl);
		findViewById(R.id.btn_multi).setOnClickListener(cl);
		findViewById(R.id.btn_menu_score).setOnClickListener(cl);
		findViewById(R.id.btn_menu_settings).setOnClickListener(cl);
		findViewById(R.id.testReseau).setOnClickListener(tr);
		
		OnClickListener camCl = new ActiviteCreationCarte(this);
		findViewById(R.id.test_cam).setOnClickListener(camCl);
	}
	
	public void afficherInformationsTelephone() {
		/* Inforamtions sur le téléphone */
		StringBuffer buf = new StringBuffer();
		buf.append("Android = " + Informations.getAndroidVersion() + RETOUR_LIGNE_HTML);
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
		String txtInfo = buf.toString();
		TextView tv = (TextView)findViewById(R.id.textView2);
		tv.setText(Html.fromHtml(txtInfo));
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.v(TAG,"Nous recevons une réponse d'une activité non Androworms (fonction onActivityResult())");
		switch (requestCode) {
			case REQUEST_ENABLE_BT:
				// On reçois une réponse de l'activation Bluetooth
				Log.v(TAG,"On reçois une réponse de l'activation Bluetooth (REQUEST_ENABLE_BT)");
				
				if (resultCode == Activity.RESULT_OK) {
					// L'utilisateur a accepté d'activé le Bluetooth
					Log.v(TAG,"L'utilisateur a dit OK pour l'activation du Bluetooth : Youpii !! on va pouvoir jouer !");
				}
				else {
					// L'utilisateur a refusé d'activer le Bluetooth (ou il s'agit d'une erreur)
					Log.v(TAG,"L'utilisateur a refusé d'activé le Bluetooth...quelqu'un lui explique que c'est indispensable ? (ou alors il s'agit d'une erreur)");
				}
				break;
			case REQUEST_CONNECT_DEVICE:
				// Servira à l'Intent de connexion avec ses amis
				break;
		}
	}
}
