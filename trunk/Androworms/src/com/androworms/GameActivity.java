package com.androworms;

import com.androworms.ui.TouchRelativeLayout;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class GameActivity extends Activity {
	
	private static final String TAG = "TESTAndroworms.MainActivity";
	
	//private static final int WIDTH = 640;
	//private static final int HEIGHT = 400;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG,"Start");
		
		/* Changer l'orientation en mode paysage */
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		
		/** Intialisations des composants graphiques **/
		RelativeLayout rl_globale = new RelativeLayout(this);
			RelativeLayout rl_commandes = new RelativeLayout(this);
				TextView txt_score = new TextView(this);
				Button btn_cmd = new Button(this);
			TouchRelativeLayout rl_carte = new TouchRelativeLayout(this);
				ImageView imgv_fond = new ImageView(this);
				ImageView imgv_jeu = new ImageView(this);
				ImageView imgv_bonhomme = new ImageView(this);
		
		/** Paramètrages des composants graphiques **/
		txt_score.setText("Score : 0");
		btn_cmd.setText("bombe");
		imgv_fond.setImageResource(R.drawable.image_fond_640x360);
		imgv_fond.setId(40);
		imgv_jeu.setImageResource(R.drawable.terrain_jeu_defaut_640x360);
		imgv_jeu.setId(41);
		imgv_bonhomme.setImageResource(R.drawable.logo_android_robot);
		imgv_bonhomme.setId(42);
		
		
		/** Paramètrages des composants graphiques dans les Layout**/
		LayoutParams params_txt_score = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params_txt_score.addRule(RelativeLayout.ALIGN_PARENT_TOP);
	    params_txt_score.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	    params_txt_score.rightMargin = 40;
	    params_txt_score.topMargin = 40;
		
	    LayoutParams params_btn_cmd = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    params_btn_cmd.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
	    params_btn_cmd.addRule(RelativeLayout.CENTER_VERTICAL);
	    
	    LayoutParams params_imgv_bonhomme = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    params_imgv_bonhomme.addRule(RelativeLayout.CENTER_HORIZONTAL);
	    params_imgv_bonhomme.addRule(RelativeLayout.CENTER_VERTICAL);
	    
		/** Construction de l'interface **/
		setContentView(rl_globale);
			rl_globale.addView(rl_carte);
				rl_carte.addView(imgv_fond);
				rl_carte.addView(imgv_jeu);
				rl_carte.addView(imgv_bonhomme,params_imgv_bonhomme);
			rl_globale.addView(rl_commandes);
				rl_commandes.addView(txt_score,params_txt_score);
				rl_commandes.addView(btn_cmd,params_btn_cmd);
		
		/** Actions post-construction **/
		rl_carte.init(this);
		
		
		// Solution 1 : je MATH_PARENT (donc image en taille max de l'écan et je peux le déplacer avec la matrice (sans "déborder de la taille du bonhmme)
		// Solution 2 : je WRAP_CONTENT sur le bonhomme et j'utilise pas la matrice pour déplacer le composant ImageView
	}
}
