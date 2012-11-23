package com.androworms;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class TestBluetooth implements OnClickListener, OnTouchListener {
	
	private static final String TAG = "Androworms.Bluetooth";
	private final int REQUEST_ENABLE_BT = 1;
	
	ActiviteMenuPrincipal activiteMenuPrincipal;
	
	public TestBluetooth(ActiviteMenuPrincipal activiteMenuPrincipal) {
		// TODO Auto-generated constructor stub
	}

	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		Log.v(TAG,"onTouch() début");
		
		return false;
	}

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		Log.v(TAG,"onCLick() début");
		
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
		    // Device does not support Bluetooth
			Log.v(TAG,"Le téléphone n'est pas compatible !");
		} else {
			Log.v(TAG,"Le téléphone est compatible !");
			
			if (!mBluetoothAdapter.isEnabled()) {
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			    activiteMenuPrincipal.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
		}
		
	}

}