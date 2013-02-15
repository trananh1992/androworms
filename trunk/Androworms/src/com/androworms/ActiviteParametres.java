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
	
	// Constante de nom du fichier de préférences
	public static final String PARAMETRES_CLE = "Parametres";
	// Constantes de clé et de valeurs par défaut des préférences
	public static final String PARAMETRE_PSEUDO_CLE = "Pseudo";
	public static final String PARAMETRE_PSEUDO_DEFAUT_1 = "";
	public static final String PARAMETRE_PSEUDO_DEFAUT_2 = "Joueur 1";
	public static final String PARAMETRE_VIBRATIONS_CLE = "vibrations";
	public static final boolean PARAMETRE_VIBRATIONS_DEFAUT = false;
	public static final String PARAMETRE_SONS_CLE = "Sons";
	public static final boolean PARAMETRE_SONS_DEFAUT = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activite_parametres);
		
		// Chargement des préférences
		SharedPreferences settings = getSharedPreferences(PARAMETRES_CLE, 0);
		String paramPseudo = settings.getString(PARAMETRE_PSEUDO_CLE, PARAMETRE_PSEUDO_DEFAUT_1);
		boolean paramVibrations = settings.getBoolean(PARAMETRE_VIBRATIONS_CLE, PARAMETRE_VIBRATIONS_DEFAUT);
		boolean paramSons = settings.getBoolean(PARAMETRE_SONS_CLE, PARAMETRE_SONS_DEFAUT);
		
		// Configuration des composants
		EditText etPseudo = (EditText)findViewById(R.id.et_pseudo);
		etPseudo.setText(paramPseudo);
		CheckBox chkVibrations = (CheckBox)findViewById(R.id.chk_vibrations);
		chkVibrations.setChecked(paramVibrations);
		CheckBox chkSons = (CheckBox)findViewById(R.id.chk_sons);
		chkSons.setChecked(paramSons);
		
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
		CheckBox chkSons = (CheckBox)findViewById(R.id.chk_sons);
		
		SharedPreferences settings = getSharedPreferences(PARAMETRES_CLE, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(PARAMETRE_PSEUDO_CLE, etPseudo.getText().toString());
		editor.putBoolean(PARAMETRE_VIBRATIONS_CLE, chkVibrations.isChecked());
		editor.putBoolean(PARAMETRE_SONS_CLE, chkSons.isChecked());
		editor.commit();
	}
}