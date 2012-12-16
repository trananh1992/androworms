package com.androworms;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.androworms.ui.Paddle;
import com.androworms.ui.TouchRelativeLayout;

public class GameActivity extends Activity {
	
	private static final String TAG = "TESTAndroworms.MainActivity";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG,"Start");
		
		/* Changer l'orientation en mode paysage */
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		/** Intialisations des composants graphiques **/
		RelativeLayout rlGlobale = new RelativeLayout(this);
			RelativeLayout rlCommandes = new RelativeLayout(this);
				TextView txtScore = new TextView(this);
				Button btnArmes = new Button(this);
				Paddle paddle = new Paddle(this);
			TouchRelativeLayout rlCarte = new TouchRelativeLayout(this);
				ImageView imgvFond = new ImageView(this);
				ImageView imgvJeu = new ImageView(this);
				ImageView imgvJoueur1 = new ImageView(this);
		
		
		/** Paramètrages des composants graphiques **/
		txtScore.setText("Score : 0");
		btnArmes.setText("bombe");
		
		imgvFond.setImageResource(R.drawable.image_fond_640x360);
		imgvJeu.setImageResource(R.drawable.terrain_jeu_defaut_640x360);
		imgvJoueur1.setImageResource(R.drawable.logo_android_robot);
		
		/** Paramètrages des composants graphiques dans les Layout**/
		LayoutParams paramsTxtScore = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsTxtScore.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		paramsTxtScore.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		paramsTxtScore.rightMargin = 40;
		paramsTxtScore.topMargin = 40;
		
		LayoutParams paramsBtnArmes = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsBtnArmes.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		paramsBtnArmes.addRule(RelativeLayout.CENTER_VERTICAL);
		
		LayoutParams paramsImgvJoueur1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsImgvJoueur1.addRule(RelativeLayout.CENTER_HORIZONTAL);
		paramsImgvJoueur1.addRule(RelativeLayout.CENTER_VERTICAL);
		
		LayoutParams paramsPaddle = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsPaddle.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		paramsPaddle.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		
		
		/** Construction de l'interface **/
		setContentView(rlGlobale);
			rlGlobale.addView(rlCarte);
				rlCarte.addView(imgvFond);
				rlCarte.addView(imgvJeu);
				rlCarte.addView(imgvJoueur1,paramsImgvJoueur1);
			rlGlobale.addView(rlCommandes);
				rlCommandes.addView(txtScore,paramsTxtScore);
				rlCommandes.addView(btnArmes,paramsBtnArmes);
				rlCommandes.addView(paddle,paramsPaddle);
		
		/** Actions post-construction **/
		rlCarte.init(this);
		
		
		// Solution 1 : je MATH_PARENT (donc image en taille max de l'écan et je peux le déplacer avec la matrice (sans "déborder de la taille du bonhmme)
		// Solution 2 : je WRAP_CONTENT sur le bonhomme et j'utilise pas la matrice pour déplacer le composant ImageView
	}
}
