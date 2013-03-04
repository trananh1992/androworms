package com.androworms;

import com.androworms.utile.Informations;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;


public class ActiviteGyro extends Activity implements SensorEventListener {
	
	public static final int TAILLE_IMAGE = 50;
	
	private SensorManager sensorManager;
	private PointF pos;
	private Matrix matrix;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activite_gyro);
		
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		matrix = new Matrix();
		
		// Position de départ
		pos = new PointF(Informations.getWidthPixels() / (float)2, Informations.getHeightPixels() / (float)2);
		
		ImageView iv = (ImageView)findViewById(R.id.iv_missile);
		iv.setScaleType(ScaleType.MATRIX);
	}
	
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			getAccelerometer(event);
		}
	}
	
	private void getAccelerometer(SensorEvent event) {
		float[] values = event.values;
		float x = values[0];
		float y = values[1];
		float z = values[2];
		ImageView iv = (ImageView)findViewById(R.id.iv_missile);
		
		
		Log.v("Gyro","(x,y,z)=(" + x + "," + y + "," + z + ")");
		
		pos.x += y * SensorManager.GRAVITY_EARTH;
		pos.y += x * SensorManager.GRAVITY_EARTH;
		
		float angle = (float) (Math.toDegrees(Math.atan2(y, -x)));
		
		// Translation
		iv.layout((int)pos.x, (int)pos.y, (int)pos.x + TAILLE_IMAGE, (int)pos.y + TAILLE_IMAGE);
		// Rotation
		matrix = new Matrix();
		matrix.postRotate(angle, TAILLE_IMAGE/2, TAILLE_IMAGE/2);
		iv.setImageMatrix(matrix);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// register this class as a listener for the orientation and accelerometer sensors
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		// unregister listener
		super.onPause();
		sensorManager.unregisterListener(this);
	}
}