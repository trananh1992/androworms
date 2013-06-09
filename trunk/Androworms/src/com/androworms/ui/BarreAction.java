package com.androworms.ui;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androworms.R;

/** Composant graphique pour simuler la barre d'action de Android 4.0+
 */
public class BarreAction extends LinearLayout {
	
	public BarreAction(Context context) {
		super(context);
		constructeurPartage(context);
	}
	
	public BarreAction(Context context, AttributeSet attrs) {
		super(context, attrs);
		constructeurPartage(context);
	}
	
	private void constructeurPartage(Context context) {
		// Mise en place du layout XML + style de la Barre d'Action
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout mBarView = (LinearLayout) mInflater.inflate(R.layout.barre_action, null);
		mBarView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, R.dimen.barre_action));
		this.addView(mBarView);
	}
	
	public void configure(Activity activite, boolean activitePrincipale, int titre, boolean aide) {
		// Gestion des evenements de la barre
		BarreActionEvent evenements = new BarreActionEvent(activite, activitePrincipale);
		
		ImageView ivIcone = (ImageView) findViewById(R.id.iv_barre_action_logo);
		if (activitePrincipale) {
			ivIcone.setImageResource(R.drawable.logo_seul);
		} else {
			ivIcone.setImageResource(R.drawable.logo_fleche);
		}
		ivIcone.setOnClickListener(evenements);
		
		TextView tvTitre = (TextView) findViewById(R.id.iv_barre_action_titre);
		tvTitre.setText(titre);
		
		ImageView ivAide = (ImageView) findViewById(R.id.iv_barre_action_aide);
		if (aide) {
			ivAide.setVisibility(View.VISIBLE);
		} else {
			ivAide.setVisibility(View.INVISIBLE);
		}
	}
}

class BarreActionEvent implements OnClickListener {
	
	private Activity activite;
	private boolean activitePrincipale;
	
	public BarreActionEvent(Activity activite, boolean activitePrincipale) {
		this.activite = activite;
		this.activitePrincipale = activitePrincipale;
	}
	
	public void onClick(View v) {
		if (!this.activitePrincipale) {
			this.activite.finish();
		}
	}
}