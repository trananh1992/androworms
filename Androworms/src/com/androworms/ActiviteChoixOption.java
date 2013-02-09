package com.androworms;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

public class ActiviteChoixOption extends Activity {
	private String carte;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* Affiche la vue par défaut */
		setContentView(R.layout.choix_options);
		
		File root = Environment.getExternalStorageDirectory();
		File sd = new File(root,"Androworms");
		
		//gets a list of the files
		File[] sdDirList = sd.listFiles(); 
		Spinner mapChooser = (Spinner)findViewById(R.id.mapChooser);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.text_view_spinner);
		int i = 0;
		if (sdDirList != null) {
			for(i=0;i<sdDirList.length;i++) {
				Log.e("test","adding item");
				adapter.add(sdDirList[i].getName());
				Log.e("test","added item "+sdDirList[i].getName());
			}
		}
		adapter.add("Create new");
		Log.e("ActiviteChoixOption","setting");
		adapter.notifyDataSetChanged();
		mapChooser.setAdapter(adapter);
		mapChooser.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				if(arg2 != (arg0.getAdapter().getCount()-1))
				{
					afficheCarte((String)arg0.getAdapter().getItem(arg2));
				}
				else
				{
					demarreCreationCarte();
				}
			}
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		Button btn = (Button) findViewById(R.id.startGame);
		btn.setOnClickListener(new OnClickListener(){
			
			public void onClick(View arg0)
			{
				lanceLeJeu();
			}
			
		});
		Log.e("ActiviteChoixOption","setted");
	}
	
	private void afficheCarte(String map)
	{
		carte = map;
		ImageView v = (ImageView)findViewById(R.id.chosen_map);
		File root = Environment.getExternalStorageDirectory();
		File sd = new File(root,"Androworms/"+map);
		Bitmap b = BitmapFactory.decodeFile(sd.getAbsolutePath());
		Bitmap thumbnail = Bitmap.createScaledBitmap(b, 300, 200, false);
		v.setImageBitmap(thumbnail);
	}
	
	private void demarreCreationCarte()
	{
		Intent intent = new Intent(this, ActiviteEditeur.class);
		this.startActivityForResult(intent, 0);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent retour)
	{
		Spinner sp = (Spinner) findViewById(R.id.mapChooser);
		Log.e("activiteChoixOptions","result");
		if(resultCode == RESULT_OK)
		{
			Log.e("activiteChoixOptions","result is ok");
			String photo= retour.getStringExtra("image");
			if(photo.length()>0)
			{
				Log.e("activiteChoixOptions","result is ok and photo is "+photo);
				ArrayAdapter<String> adapter = (ArrayAdapter<String>) sp.getAdapter();
				adapter.insert(photo,adapter.getCount()-1);
				adapter.notifyDataSetChanged();
				Spinner mapChooser = (Spinner)findViewById(R.id.mapChooser);
				mapChooser.setAdapter(adapter);
			}
		}
	}
	
	private void lanceLeJeu()
	{
		Intent intent = new Intent(this, ActiviteJeu.class);
		Bundle b = new Bundle();
		b.putInt("mode", ParametrePartie.MODE_SOLO);
		b.putBoolean("estCartePerso", true);
		b.putString("nomCarte", carte);
		intent.putExtras(b);
		this.startActivity(intent);
	}
}