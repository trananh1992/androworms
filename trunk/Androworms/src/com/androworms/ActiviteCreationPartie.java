package com.androworms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ActiviteCreationPartie extends Activity {
	
	private static final String TAG = "Androworms.ActiviteCreationPartie";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "Début de la création de la partie");
		etape1();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		etape1();
	}
	
	private void etape1() {
		setContentView(R.layout.activite_creation_partie_1);
		
		Button btn_suivant = (Button)findViewById(R.id.btn_suivant);
		btn_suivant.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape2();
			}
		});
	}
	
	private void etape2() {
		setContentView(R.layout.activite_creation_partie_2);
		
		Button btn_precedent = (Button)findViewById(R.id.btn_precedent);
		btn_precedent.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape1();
			}
		});
		
		Button btn_suivant = (Button)findViewById(R.id.btn_suivant);
		btn_suivant.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape3();
			}
		});
	}
	
	private void etape3() {
		setContentView(R.layout.activite_creation_partie_3);
		
		Button btn_precedent = (Button)findViewById(R.id.btn_precedent);
		btn_precedent.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape2();
			}
		});
		
		Button btn_suivant = (Button)findViewById(R.id.btn_suivant);
		btn_suivant.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape4();
			}
		});
	}
	
	private void etape4() {
		setContentView(R.layout.activite_creation_partie_4);
		
		Button btn_precedent = (Button)findViewById(R.id.btn_precedent);
		btn_precedent.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				etape3();
			}
		});
		
		Button btn_demarrer_la_partie = (Button)findViewById(R.id.btn_demarrer_la_partie);
		btn_demarrer_la_partie.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ActiviteCreationPartie.this, ActiviteJeu.class);
				ActiviteCreationPartie.this.startActivity(intent);
			}
		});
	}
}