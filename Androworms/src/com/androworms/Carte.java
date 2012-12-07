package com.androworms;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Carte {
	private static final String TAG = "Androworms.Carte.Evenements";
	Bitmap b;
	Bitmap transformed;
	public Carte(byte[] data)
	{
		b = BitmapFactory.decodeByteArray(data, 0, data.length);
		
		int height = b.getHeight();
		int width = b.getWidth();
		transformed = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
		int i,j;
		int color;
		Log.v(TAG, "begin computing alpha");
		for(i=0;i<width;i++)
		{
			for(j=0;j<height;j++)
			{
				color = b.getPixel(i, j);
				int blue = Color.blue(color);
				int red = Color.red(color);
				int green = Color.green(color);
				if(((red + green + blue)/3)>200)
				{
					transformed.setPixel(i, j, Color.argb(0,red,green,blue));
				}
				else
				{
					transformed.setPixel(i, j, Color.argb(255,red,green,blue));
				}
			}
		}
		Log.v(TAG, "Done computing alpha");
	}
	
	public void save(String path)
	{
		FileOutputStream s;
		try {
			s = new FileOutputStream(path);
			transformed.compress(Bitmap.CompressFormat.PNG, 100, s);
			s.flush();
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
