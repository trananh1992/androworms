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
public class Paddle extends LinearLayout implements OnTouchListener {
	
	private static final int ALPHA_PAS_TRANSPARENT = 255;
	private static final int ALPHA_SEMI_TRANSPARENT = 128;
	
	private static final int POSITION_LAYOUT = 128;
	
	public static final int BOUTON_DROITE = 1;
	public static final int BOUTON_HAUT = 2;
	public static final int BOUTON_GAUCHE = 3;
	
	private OnTouchListener onTouchListener;
	
	public Paddle(Context context) {
		super(context);
		constructeurPartage(context);
	}
	
	public Paddle(Context context, AttributeSet attrs) {
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
		
		LayoutParams paramsLayout = new LinearLayout.LayoutParams(POSITION_LAYOUT, POSITION_LAYOUT);
		
		this.addView(btnGauche,paramsLayout);
		this.addView(btnHaut,paramsLayout);
		this.addView(btnDroite,paramsLayout);
	}
	
	public OnTouchListener getOnTouchListener() {
		return onTouchListener;
	}

	public void setOnTouchListener(OnTouchListener onTouchListener) {
		this.onTouchListener = onTouchListener;
	}
	
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				((ImageView)v).setAlpha(ALPHA_SEMI_TRANSPARENT);
				/* Déclenchement de l'action définit lors de l'utilisation du Paddle */
				if (this.getOnTouchListener() != null) {
					this.getOnTouchListener().onTouch(v, event);
				}
				break;
			case MotionEvent.ACTION_UP:
				((ImageView)v).setAlpha(ALPHA_PAS_TRANSPARENT);
				/* Déclenchement de l'action définit lors de l'utilisation du Paddle */
				if (this.getOnTouchListener() != null) {
					this.getOnTouchListener().onTouch(v, event);
				}
				break;
			default:
				break;
		}
		return true;
	}
}