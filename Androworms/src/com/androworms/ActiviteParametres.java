package com.androworms;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class ActiviteParametres extends Activity {
	
	public static final String PREFS_NAME = "Parametres";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activite_parametres);
		
		// Chargement des préférences
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		String pseudo = settings.getString("pseudo", "");
		boolean vibrations = settings.getBoolean("vibrations", false);
		
		// Configuration des composants
		EditText etPseudo = (EditText)findViewById(R.id.et_pseudo);
		etPseudo.setText(pseudo);
		CheckBox chkVibrations = (CheckBox)findViewById(R.id.chk_vibrations);
		chkVibrations.setChecked(vibrations);
		
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
		EditText etPseudo = (EditText)findViewById(R.id.et_pseudo);
		CheckBox chkVibrations = (CheckBox)findViewById(R.id.chk_vibrations);
		
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("pseudo", etPseudo.getText().toString());
		editor.putBoolean("vibrations", chkVibrations.isChecked());
		editor.commit();
	}
}