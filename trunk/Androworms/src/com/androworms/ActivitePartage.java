package com.androworms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class ActivitePartage extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activite_partage);
		
		
		
		// Bouton partager
		ImageButton ibPartagerGeneral = (ImageButton)findViewById(R.id.ib_partager_general);
		ibPartagerGeneral.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// création de l'Intent pour envoyer
				Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
				
				// on définit le type
				shareIntent.setType("text/plain");
				
				// on ajoute un sujet
				shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Viens jouer à Androworms !");
				
				// On définit le coprs du message à partager
				String shareMessage = "Toi aussi viens jouer à Androworms. " +
						"C'est un jeu qui est vraiment trop cool. " +
						"Il faut bouger le téléphone sans perdre la flèche rose !\n" +
						"Viens le télécharger https://code.google.com/p/androworms/";
				
				// On ajoute le message
				shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);
				
				// On démarre le sélécteur d'applications pour le partage
				startActivity(Intent.createChooser(shareIntent, "Partager l'application"));
			}
			
		});
	}
}