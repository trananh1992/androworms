package com.androworms;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.text.Html;
import android.view.Menu;
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
        String[] liste_dev = getResources().getStringArray(R.array.liste_developpeurs);
        String txt_dev = "";
        for (int i=0;i<liste_dev.length;i++) {
        	txt_dev += liste_dev[i] + "<br/>";
        }
        TextView tv_developers;
        tv_developers = (TextView)findViewById(R.id.textView3);
        tv_developers.setText(Html.fromHtml(txt_dev));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
