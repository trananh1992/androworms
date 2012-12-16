package com.androworms;

import com.androworms.test.TestSlider;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class MenuPrincipalActivity extends Activity {
	
	private static final String TAG = "Androworms.MenuPrincipal";
	
	// Codes de demande de l'Intent
	public static final int REQUEST_ENABLE_BT = 1;
	public static final int REQUEST_CONNECT_DEVICE = 2;
	public static final int REQUEST_CAM = 3;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG,"Androworms : Bienvenue sur le menu principal");
		
		/* Changer l'orientation en mode paysage */
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
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
		
		
		/* Affiche la vue par défaut */
		setContentView(R.layout.menu_principal);
		
		OnClickListener soloCl = new MenuPrincipalActivityEvent(this);
		findViewById(R.id.btn_solo).setOnClickListener(soloCl);
		
		OnClickListener camCl = new ActiviteCreationCarte(this);
	//	OnTouchListener camtl = new ActiviteCreationCarte(this);
		findViewById(R.id.test_cam).setOnClickListener(camCl);
		//findViewById(R.id.test_cam).setOnTouchListener(camtl);
		
		OnClickListener clBluetooth = new TestBluetooth(this);
		OnTouchListener tlBluetooth = new TestBluetooth(this);
		findViewById(R.id.test_bluetooth).setOnClickListener(clBluetooth);
		findViewById(R.id.test_bluetooth).setOnTouchListener(tlBluetooth);
		
		OnClickListener clSlider = new TestSlider(this);
		OnTouchListener tlSlider = new TestSlider(this);
		findViewById(R.id.test_slider).setOnClickListener(clSlider);
		findViewById(R.id.test_slider).setOnTouchListener(tlSlider);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.v(TAG,"Nous recevons une réponse d'une activité non Androworms (fonction onActivityResult())");
		switch (requestCode)
		{
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