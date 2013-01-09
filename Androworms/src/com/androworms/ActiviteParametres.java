package com.androworms;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ActiviteParametres extends Activity {
	
	public static final String PREFS_NAME = "Parametres";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parametres);
		
		// Chargement des préférences
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		
		String pseudo = settings.getString("pseudo", "vide");
		
		EditText et = (EditText)findViewById(R.id.pseudo);
		et.setText(pseudo);
		
		/* Bouton enregistrer */
		Button btn = (Button)findViewById(R.id.btn_enregistrer);
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	protected void onStop() {
		super.onStop();
		// Sauvegarde des préférences
		EditText tv = (EditText)findViewById(R.id.pseudo);
		
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("pseudo", tv.getText().toString());
		editor.commit();
	}
}