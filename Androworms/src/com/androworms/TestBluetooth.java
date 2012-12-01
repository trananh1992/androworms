package com.androworms;

import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class TestBluetooth implements OnClickListener, OnTouchListener {
	
	private static final String TAG = "Androworms.Bluetooth";
	
	ActiviteMenuPrincipal activiteMenuPrincipal;
	
	public TestBluetooth(ActiviteMenuPrincipal activiteMenuPrincipal) {
		this.activiteMenuPrincipal = activiteMenuPrincipal;
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
			Log.v(TAG,"Le téléphone n'est pas compatible bluetooth !");
		} else {
			Log.v(TAG,"Le téléphone est compatible bluetooth !");
			
			if (!mBluetoothAdapter.isEnabled()) {
				Log.v(TAG,"Le bluetooth n'est pas activé");
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				activiteMenuPrincipal.startActivityForResult(enableBtIntent, activiteMenuPrincipal.REQUEST_ENABLE_BT);
			} else {
				Log.v(TAG,"Le bluetooth est activé");
				
				
				Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
				// If there are paired devices
				if (pairedDevices.size() > 0) {
					// Loop through paired devices
					for (BluetoothDevice device : pairedDevices) {
						// Add the name and address to an array adapter to show in a ListView
						//mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
						Log.v(TAG,"Appareil jumelé : " + device.getName() + " : " + device.getAddress());
					}
				}
			}
		}
		
	}
}