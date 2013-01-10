package com.androworms;

import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.ToggleButton;

public class ActiviteMultiJoueur extends Activity {
	
	private static final String TAG = "Androworms.ActiviteMultiJoueur";
	
	// Codes de demande de l'Intent
	public static final int REQUEST_ENABLE_BT = 1;
	public static final int REQUEST_CONNECT_DEVICE = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multi_joueur);
		
		/* ToggleButton : Bluetooth On/Off */
		ToggleButton tb = (ToggleButton)findViewById(R.id.toggleButton1);
		// Désactivé si l'appareil est aps compatible Bluetooth
		tb.setEnabled(Informations.isCompatibleBluetooth());
		// Coché si le Bluetooth est déjà activé
		tb.setChecked(Informations.isBluetoothOn());
		// Action en cas d'appuie sur le bouton
		tb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			// TODO : ce bouton peux être faux si :
			// - on lance Androworms (Bluetooth OFF)
			// - On active le Bluetooth avec le bouton
			// - on appuie sur le bouton Home du tel
			// - on Désactive le Bluetooth
			// - on retourne sur l'appli
			// (bon il est mis à jour avec le bouton "Actualiser")
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					// Activation du Bluetooth
					Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
					startActivityForResult(enableBtIntent, ActiviteMultiJoueur.REQUEST_ENABLE_BT);
					// On actualise la liste des appareils jumelées
					actualiserListeAppareilBluetooth();
				} else {
					// Désactivation du Bluetooth
					BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
					mBluetoothAdapter.disable();
					// On actualise la liste des appareils jumelées
					actualiserListeAppareilBluetooth();
					// TODO : NE MARCHE PAS COMME IL DEVRAIT
					// ce refresh devrait vider la liste. mais je pense que la désactivation du Bletooth prend quelques secondes
					// et que du coup, la liste ne se vide pas.
				}
			}
		});
		
		/* Bouton d'actualisation */
		Button btn = (Button)findViewById(R.id.btn_refresh);
		btn.setOnClickListener(new OnClickListener() {
			// On click sur le bouton "Actualiser"
			public void onClick(View v) {
				// On actualise le bouton Bluetooth On/Off
				ToggleButton tb = (ToggleButton)findViewById(R.id.toggleButton1);
				tb.setChecked(Informations.isBluetoothOn());
				// On actualise la liste des appareils jumelées
				actualiserListeAppareilBluetooth();
			}
		});
	}
	
	public void actualiserListeAppareilBluetooth() {
		ListView lv = (ListView)findViewById(R.id.listView1);
		ArrayAdapter<String> adapter;
		String[] values;
		
		if (Informations.isBluetoothOn()) {
			BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
			// Si le téléphone à des appareils connectés
			if (pairedDevices.size() > 0) {
				values = new String[pairedDevices.size()];
				int i = 0;
				// Loop through paired devices
				for (BluetoothDevice device : pairedDevices) {
					// Add the name and address to an array adapter to show in a ListView
					Log.v(TAG,"Appareil jumelé : " + device.getName() + " : " + device.getAddress());
					values[i] = "Appareil jumelé : " + device.getName() + " : " + device.getAddress();
					i++;
				}
				adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
				lv.setAdapter(adapter);
			}
		} else {
			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
			adapter.clear();
			lv.setAdapter(adapter);
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.v(TAG,"Nous recevons une réponse d'une activité non Androworms (fonction onActivityResult())");
		switch (requestCode) {
		case REQUEST_ENABLE_BT:
			// On reçois une réponse de l'activation Bluetooth
			Log.v(TAG,"On reçois une réponse de l'activation Bluetooth (REQUEST_ENABLE_BT)");
			
			if (resultCode == Activity.RESULT_OK) {
				// L'utilisateur a accepté d'activé le Bluetooth
				Log.v(TAG,"L'utilisateur a dit OK pour l'activation du Bluetooth : Youpii !! on va pouvoir jouer !");
				
				// On actualise la liste des appareils Bluetooth reliées
				actualiserListeAppareilBluetooth();
			} else {
				// L'utilisateur a refusé d'activer le Bluetooth (ou il s'agit d'une erreur)
				Log.v(TAG,"L'utilisateur a refusé d'activé le Bluetooth...quelqu'un lui explique que c'est indispensable ? (ou alors il s'agit d'une erreur)");
			}
			break;
		case REQUEST_CONNECT_DEVICE:
			// Servira à l'Intent de connexion avec ses amis
			break;
		}
	}
}