package com.androworms;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class ActiviteMenuPrincipal extends Activity {

    private static final String TAG = "Androworms.MenuPrincipal";
    
    // Codes de demande de l'Intent
    public final int REQUEST_ENABLE_BT = 1;
    public final int REQUEST_CONNECT_DEVICE = 2;
    public final int REQUEST_CAM = 3;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG,"Androworms : Bienvenue sur le menu principal");
        
        /* Changer l'orientation en mode paysage */
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        /* Affiche la vue par défaut */
        setContentView(R.layout.menu_principal);
        
        OnClickListener camCl = new CamHandler(this);
        OnTouchListener camtl = new CamHandler(this);
        findViewById(R.id.test_cam).setOnClickListener(camCl);
        findViewById(R.id.test_cam).setOnTouchListener(camtl);
        
        OnClickListener cl = new TestBluetooth(this);
        OnTouchListener tl = new TestBluetooth(this);
        findViewById(R.id.testBluetooth).setOnClickListener(cl);
        findViewById(R.id.testBluetooth).setOnTouchListener(tl);
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