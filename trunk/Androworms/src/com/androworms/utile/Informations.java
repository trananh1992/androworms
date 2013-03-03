package com.androworms.utile;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import com.androworms.Bluetooth;

/** Classes qui contient des éléments sur les caractéristiques du téléphone.
 */
public final class Informations {
	
	private static final String ANDROID_VERSION = Build.VERSION.RELEASE;
	private static final int ANDROID_SDK = Build.VERSION.SDK_INT;
	private static int widthPixels = -1;
	private static int heightPixels = -1;
	private static int screenLayoutSizeMask = -1;
	private static float density = -1;
	private static int densityDpi = -1;
	private static boolean compatibleBluetooth = false;
	
	private Informations() {
		
	}
	
	public static void init(Resources r) {
		/* Chercher les informations sur la taille et la densité de l'écran */
		DisplayMetrics metrics = r.getDisplayMetrics();
		widthPixels = metrics.widthPixels;
		heightPixels = metrics.heightPixels;
		density = metrics.density;
		densityDpi = metrics.densityDpi;
		Configuration c = r.getConfiguration();
		screenLayoutSizeMask = c.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
		
		/* Vérifie la compatibilité du Bluetooth */
		compatibleBluetooth = (Bluetooth.getBluetoothAdapter() != null);
	}

	public static String getAndroidVersion() {
		return ANDROID_VERSION;
	}

	public static int getAndroidSdk() {
		return ANDROID_SDK;
	}

	public static int getWidthPixels() {
		return widthPixels;
	}

	public static int getHeightPixels() {
		return heightPixels;
	}

	public static float getDensity() {
		return density;
	}

	public static int getDensityDpi() {
		return densityDpi;
	}

	public static int getScreenLayoutSizeMask() {
		return screenLayoutSizeMask;
	}

	public static boolean isCompatibleBluetooth() {
		return compatibleBluetooth;
	}
	
	public static boolean isBluetoothOn() {
		if (compatibleBluetooth) {
			// Si le téléphone supporte le Bluetooth
			return Bluetooth.getBluetoothAdapter().isEnabled();
		}
		return false;
	}
}