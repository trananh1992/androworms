package com.androworms;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Carte {
	private static final String TAG = "Androworms.Carte.Evenements";
	private Bitmap transformed;
	public Carte(byte[] data) {
		Bitmap b = BitmapFactory.decodeByteArray(data, 0, data.length);
		
		int height = b.getHeight();
		int width = b.getWidth();
		transformed = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
		int i,j;
		int color;
		int seuil = 200;
		int nComponents = 3;
		int maxComponentValue = 255;
		Log.v(TAG, "begin computing alpha");
		for(i=0;i<width;i++)
		{
			for(j=0;j<height;j++)
			{
				color = b.getPixel(i, j);
				int blue = Color.blue(color);
				int red = Color.red(color);
				int green = Color.green(color);
				if(((red + green + blue)/nComponents)>seuil)
				{
					transformed.setPixel(i, j, Color.argb(0,red,green,blue));
				}
				else
				{
					transformed.setPixel(i, j, Color.argb(maxComponentValue,red,green,blue));
				}
			}
		}
		Log.v(TAG, "Done computing alpha");
	}
	
	public void save(String path) {
		FileOutputStream s = null;
		int compression = 100;
		try {
			s = new FileOutputStream(path);
			transformed.compress(Bitmap.CompressFormat.PNG, compression, s);
			s.flush();
			s.close();
		} catch (FileNotFoundException e) {
			Log.e(TAG,"File not found for saving");
		} catch (IOException e) {
			try
			{
				if(s != null)
					s.close();
			}
			catch (IOException e2)
			{
			}
			Log.e(TAG,"IO Exception in save");
		}
	}
}
