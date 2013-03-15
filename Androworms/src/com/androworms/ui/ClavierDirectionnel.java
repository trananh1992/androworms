package com.androworms.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androworms.R;

/** Paddle de boutons pour gérer le déplacement
 *
 */
public class ClavierDirectionnel extends LinearLayout implements OnTouchListener {
	
	private static final int ALPHA_PAS_TRANSPARENT = 255;
	private static final int ALPHA_SEMI_TRANSPARENT = 128;
	
	public static final int BOUTON_DROITE = 1;
	public static final int BOUTON_HAUT = 2;
	public static final int BOUTON_GAUCHE = 3;
	
	private OnTouchListener onTouchListener;
	
	public ClavierDirectionnel(Context context) {
		super(context);
		constructeurPartage(context);
	}
	
	public ClavierDirectionnel(Context context, AttributeSet attrs) {
		super(context, attrs);
		constructeurPartage(context);
	}
	
	private void constructeurPartage(Context context) {
		ImageButton btnGauche = new ImageButton(context);
		ImageButton btnHaut = new ImageButton(context);
		ImageButton btnDroite = new ImageButton(context);
		
		btnGauche.setImageResource(R.drawable.navigation_gauche);
		btnHaut.setImageResource(R.drawable.navigation_haut);
		btnDroite.setImageResource(R.drawable.navigation_droite);
		
		btnGauche.setBackgroundColor(Color.TRANSPARENT);
		btnHaut.setBackgroundColor(Color.TRANSPARENT);
		btnDroite.setBackgroundColor(Color.TRANSPARENT);
		
		btnGauche.setId(BOUTON_GAUCHE);
		btnHaut.setId(BOUTON_HAUT);
		btnDroite.setId(BOUTON_DROITE);
		
		btnGauche.setOnTouchListener(this);
		btnHaut.setOnTouchListener(this);
		btnDroite.setOnTouchListener(this);
		
		this.addView(btnGauche);
		this.addView(btnHaut);
		this.addView(btnDroite);
	}
	
	public OnTouchListener getOnTouchListener() {
		return onTouchListener;
	}

	public void setOnTouchListener(OnTouchListener onTouchListener) {
		this.onTouchListener = onTouchListener;
	}
	
	public boolean onTouch(View v, MotionEvent event) {
		if (v instanceof ImageView) {
			ImageView iv = (ImageView)v;
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				iv.setAlpha(ALPHA_SEMI_TRANSPARENT);
				/* Déclenchement de l'action définit lors de l'utilisation du Paddle */
				if (this.getOnTouchListener() != null) {
					this.getOnTouchListener().onTouch(v, event);
				}
				break;
			case MotionEvent.ACTION_UP:
				iv.setAlpha(ALPHA_PAS_TRANSPARENT);
				/* Déclenchement de l'action définit lors de l'utilisation du Paddle */
				if (this.getOnTouchListener() != null) {
					this.getOnTouchListener().onTouch(v, event);
				}
				break;
			default:
				break;
			}
		}
		return true;
	}
}