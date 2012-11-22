package com.androworms;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class ActiviteMenuPrincipal extends Activity {

	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
        /* Changer l'orientation en mode paysage */
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        /* Affiche la vue par défaut */
        setContentView(R.layout.menu_principal);
	}
}
