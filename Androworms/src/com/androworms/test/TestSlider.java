package com.androworms.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import com.androworms.ActiviteAndroworms;
import com.androworms.R;

public class TestSlider extends Activity implements OnClickListener, OnTouchListener {
	
	private ActiviteAndroworms activiteAndroworms;
	
	public TestSlider() {
		super();
	}
	public TestSlider(ActiviteAndroworms menuPrincipalActivity) {
		this.activiteAndroworms = menuPrincipalActivity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* Affiche la vue par défaut */
		setContentView(R.layout.test_slider);
	}

	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this.activiteAndroworms, TestSlider.class);
		this.activiteAndroworms.startActivity(intent);
	}
}
