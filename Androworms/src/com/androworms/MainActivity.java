package com.androworms;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        /* Changer l'orientation en mode paysage */
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        /* Affiche la vue par défaut */
        setContentView(R.layout.splash_screen);
        
        /* Affiche la liste des développeurs */
        String[] listeDev = getResources().getStringArray(R.array.liste_developpeurs);
        StringBuilder txtDev = new StringBuilder();
        for (int i=0;i<listeDev.length;i++) {
        	txtDev.append(listeDev[i]);
        	txtDev.append("<br/>");
        }
        TextView tvDevelopers;
        tvDevelopers = (TextView)findViewById(R.id.textView3);
        tvDevelopers.setText(Html.fromHtml(txtDev.toString()));
        
        
        
        OnClickListener event = new MainActivityEvent(this);
        findViewById(R.id.LinearLayout2).setOnClickListener(event);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
