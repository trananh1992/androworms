package com.androworms;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class ActiviteParametresEvent implements OnClickListener, OnTouchListener {
	
	private ActiviteParametres activiteParametres;
	
	public ActiviteParametresEvent(ActiviteParametres activiteParametres) {
		this.activiteParametres = activiteParametres;
	}
	
	@Override
	public void onClick(View v) {
		if (v instanceof ImageView) {
			switch (v.getId()) {
			case R.id.iv_return_home:
				this.activiteParametres.finish();
				break;
			
			default:
				
			}
		}
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v instanceof ImageView) {
			
			switch (v.getId()) {
			case R.id.iv_return_home:
				ImageView ib = (ImageView) activiteParametres.findViewById(R.id.iv_return_home);
				switch (event.getActionMasked()) {
				case MotionEvent.ACTION_DOWN:
					ib.setBackgroundResource(R.color.barre_action_appuye);
					break;
				case MotionEvent.ACTION_UP:
					ib.setBackgroundResource(R.color.barre_action);
					break;
				default:
					break;
				}
				break;
			
			default:
			}
		}
		
		return false;
	}
	
}