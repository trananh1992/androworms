package com.androworms;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;

public class ActiviteCamera extends Activity implements SurfaceHolder.Callback, OnClickListener, OnTouchListener {
	
	private static final String TAG = "Androworms.ActiviteCamera.Event";
	
	private Camera camera;
	private boolean isPreviewRunning = false;
	private Handler mAutoFocusHandler;
	private int mAutoFocusMessage;
	private OutputStream filoutputStream;
	private ActiviteCreationCarte activiteCreationCarte;
	static final int TAKE_PICTURE = 0;
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		SurfaceView surfaceView;
		SurfaceHolder surfaceHolder;
		Log.v(TAG,"Androworms : begining on create");
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		setContentView(R.layout.camera);
		surfaceView = (SurfaceView)findViewById(R.id.surface);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		ImageButton close = (ImageButton) findViewById(R.id.takepicture);
		close.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				takeThePicture();
			}
		});
		Log.v(TAG,"Androworms : ending on create");
	}
	
	private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
		public void onShutter() {
			Log.e(getClass().getSimpleName(), "SHUTTER CALLBACK");
		}
	};
	
	private Camera.PictureCallback mPictureCallbackJpeg = new Camera.PictureCallback() {
		public void onPictureTaken(byte[] data, Camera c) {
				try {
					filoutputStream.write(data);
					filoutputStream.flush();
					filoutputStream.close();
					Intent i = new Intent();
					i.putExtra("image", data);
					setResult(RESULT_OK,i);
					finish();
				} catch (IOException e) {
				}
		}
	};
	
	private void takeThePicture () {
		try {
			File root = Environment.getExternalStorageDirectory();
			File androworms = new File(root,"Androworms");
			boolean status = true;
			if (!androworms.exists()) {
				status = androworms.mkdir();
			}
			if(!status)
			{
				Log.e(TAG,"failed to create directory Androworms.");
				return;
			}
			File photo = new File(androworms,"maPhoto.jpg");
			
			filoutputStream = new FileOutputStream(photo);
			Log.e(TAG,photo.getAbsolutePath().toString());
			camera.takePicture(mShutterCallback, null, mPictureCallbackJpeg);
		} catch(Exception ex ){
			Log.e(getClass().getSimpleName(), ex.getMessage(), ex);
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			takeThePicture();
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return super.onKeyDown(keyCode, event);
		}
		return false;
	}
	
	protected void onResume() {
		Log.e(getClass().getSimpleName(), "onResume");
		super.onResume();
	}
	
	protected void onStop() {
		Log.e(getClass().getSimpleName(), "onStop");
		super.onStop();
	}
	
	public void requestAutoFocus(Handler handler, int message) {
		if (camera != null)
		{
			mAutoFocusHandler = handler;
			mAutoFocusMessage = message;
			camera.autoFocus(autoFocusCallback);
		}
	}
	
	public void surfaceCreated(SurfaceHolder holder) {
		Log.e(getClass().getSimpleName(), "surfaceCreated");
		camera = Camera.open();
		if(camera == null)
		{
			Log.e(TAG,"camera est null pas de back camera ?");
			camera = Camera.open(0);
			if(null == camera)
			{
				finish();
			}
		}
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		Log.e(getClass().getSimpleName(), "surfaceChanged");
		if (isPreviewRunning) {
			camera.stopPreview();
		}
		try {
			camera.setPreviewDisplay(holder);
		} catch (IOException e) {
		}
		camera.startPreview();
		isPreviewRunning = true;
	}
	
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.e(getClass().getSimpleName(), "surfaceDestroyed");
		camera.stopPreview();
		isPreviewRunning = false;
		camera.release();
	}
	
	private final Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			final int delai = 1500;
			if (mAutoFocusHandler != null) {
				Message message = mAutoFocusHandler.obtainMessage(mAutoFocusMessage, success);
				mAutoFocusHandler.sendMessageDelayed(message, delai);
				mAutoFocusHandler = null;
			}
		}
	};
	
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}
	
	public void onClick(View v) {
		Log.v(TAG,"Androworms : Vous avez cliqué sur cam!");
		Intent intent = new Intent(this.activiteCreationCarte, ActiviteCamera.class);
		Log.v(TAG,"Androworms : created intent!");
		this.activiteCreationCarte.startActivityForResult(intent,TAKE_PICTURE);
		Log.v(TAG,"Androworms : started activity!");
		
	}
	
	public ActiviteCamera(ActiviteCreationCarte activiteCreationCarte) {
		this.activiteCreationCarte = activiteCreationCarte;
	}
	
	public ActiviteCamera() {
		super();
	}
}
