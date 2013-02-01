package com.androworms;

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
	
	private SensorManager sensorManager;
	private PointF pos;
	private Matrix matrix;
	private float rotation;
	private PointF cumul;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activite_gyro);
		
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		pos = new PointF(600,300);
		matrix = new Matrix();
		rotation = 0;
		cumul = new PointF(0,0);
		
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
		
		cumul.x += x;
		cumul.y += y;
		
		Log.v("Gyro","(x,y,z)=(" + x + "," + y + "," + z + ")    cumul=(" + cumul.x + "," + cumul.y + ")");
		
		pos.x += y*SensorManager.GRAVITY_EARTH;
		pos.y += x*SensorManager.GRAVITY_EARTH;
		
		// Translation
		iv.layout((int)pos.x, (int)pos.y, (int)pos.x+50, (int)pos.y+50);
		// Rotation
		matrix.postRotate(rotation, 25, 25);
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