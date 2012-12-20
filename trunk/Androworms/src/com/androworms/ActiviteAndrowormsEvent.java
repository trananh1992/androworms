package com.androworms;

import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ActiviteAndrowormsEvent implements OnClickListener {
	
	private static final String TAG = "Androworms.MenuPrincipalActivityEvent";
	
	private ActiviteAndroworms activiteAndroworms;
	
	public ActiviteAndrowormsEvent(ActiviteAndroworms activiteAndroworms) {
		this.activiteAndroworms = activiteAndroworms;
	}

	public void onClick(View arg0) {
		// TODO pour savoir sur quelle bouton on a cliqué
		if (arg0 instanceof Button) {
			Button b = (Button)arg0;
			
			if (b.getId() == R.id.btn_solo) {
				Log.v(TAG,"Lancement du jeu");
				
				Intent intent = new Intent(this.activiteAndroworms, GameActivity.class);
				this.activiteAndroworms.startActivity(intent);
			}
			else if (b.getId() == R.id.btn_multi) {	
				/* Gestion du bluetooth */
				BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
				if (mBluetoothAdapter == null) {
					// L'appareil ne supporte pas le Bluetooth
					Log.v(TAG,"Le téléphone n'est pas compatible bluetooth !");
				} else {
					Log.v(TAG,"Le téléphone est compatible bluetooth !");
					
					if (!mBluetoothAdapter.isEnabled()) {
						Log.v(TAG,"Le bluetooth n'est pas activé");
						Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
						activiteAndroworms.startActivityForResult(enableBtIntent, ActiviteAndroworms.REQUEST_ENABLE_BT);
					} else {
						Log.v(TAG,"Le bluetooth est activé");
						
						Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
						// Si le téléphone à des appareils connectés
						if (pairedDevices.size() > 0) {
							// Loop through paired devices
							for (BluetoothDevice device : pairedDevices) {
								// Add the name and address to an array adapter to show in a ListView
								Log.v(TAG,"Appareil jumelé : " + device.getName() + " : " + device.getAddress());
							}
						}
					}
				}
				
			}
		}
		
	}
}