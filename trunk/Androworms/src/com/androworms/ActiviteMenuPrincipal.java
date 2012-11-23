package com.androworms;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class ActiviteMenuPrincipal extends Activity {

    private static final String TAG = "Androworms.MenuPrincipal";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG,"Androworms : Bienvenue sur le menu principal");
        
        /* Changer l'orientation en mode paysage */
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        /* Affiche la vue par défaut */
        setContentView(R.layout.menu_principal);
        
        
        OnClickListener cl = new TestBluetooth(this);
        OnTouchListener tl = new TestBluetooth(this);
        findViewById(R.id.testBluetooth).setOnClickListener(cl);
        findViewById(R.id.testBluetooth).setOnTouchListener(tl);
    }
}
