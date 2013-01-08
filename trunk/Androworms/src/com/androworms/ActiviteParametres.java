package com.androworms;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
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
		
		EditText tv = (EditText)findViewById(R.id.pseudo);
		tv.setText(pseudo);
	}
	
	protected void onStop(){
		super.onStop();
		// Sauvegarde des préférences
		EditText tv = (EditText)findViewById(R.id.pseudo);
		
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("pseudo", tv.getText().toString());
		editor.commit();
	}
}