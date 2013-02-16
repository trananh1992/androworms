package com.androworms.debug;

import java.util.List;

import android.app.Activity;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.androworms.R;
import com.androworms.utile.Informations;

public class ActiviteDebug extends Activity {
	
	private static final String RETOUR_LIGNE_HTML = "<br/>";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.informations_debug);
		
		// On affiche les informations sur le téléphone
		afficherInformationsTelephone();
	}
	
	public void afficherInformationsTelephone() {
		StringBuffer buf = new StringBuffer();
		
		// SECTION : Android
		buf.append("<b>Android</b>" + RETOUR_LIGNE_HTML);
		buf.append("Version = " + Informations.getAndroidVersion() + RETOUR_LIGNE_HTML);
		buf.append("SDK = " + Informations.getAndroidSdk() + RETOUR_LIGNE_HTML);
		buf.append(RETOUR_LIGNE_HTML);
		
		// SECTION : Taille de l'écran
		buf.append(tailleEcran());
		
		// SECTION : Densité de l'écran
		buf.append(densiteEcran());
		
		buf.append("<b>Bluetooth</b>" + RETOUR_LIGNE_HTML);
		buf.append("Compatible Bluetooth : " + Informations.isCompatibleBluetooth() + RETOUR_LIGNE_HTML);
		
		buf.append(RETOUR_LIGNE_HTML);
		buf.append("<b>Capteurs</b>");
		TableLayout tl = (TableLayout)findViewById(R.id.tl_DEBUG);
		SensorManager sm = (SensorManager)getSystemService(SENSOR_SERVICE);
		List<Sensor> l = sm.getSensorList(Sensor.TYPE_ALL);
		TableRow tr = generationLigne("<b>Type</b>", "<b>Name</b>", "<b>Version</b>", "<b>Vendor</b>", "<b>Power</b>",
				"<b>Résolution</b>", "<b>MinDelay</b>", "<b>MaximunRange</b>");
		tl.addView(tr);
		for (int i=0;i<l.size();i++) {
			tr = generationLigne(""+l.get(i).getType(), l.get(i).getName(), ""+l.get(i).getVersion(), l.get(i).getVendor(), ""+l.get(i).getPower(),
					""+l.get(i).getResolution(), ""+l.get(i).getMinDelay(), ""+l.get(i).getMaximumRange());
			tl.addView(tr);
		}
		String txtInfo = buf.toString();
		TextView tv = (TextView)findViewById(R.id.tv_DEBUG);
		tv.setText(Html.fromHtml(txtInfo));
	}
	
	public StringBuffer tailleEcran() {
		StringBuffer buf = new StringBuffer();
		
		buf.append("<b>Taille de l'écran</b>" + RETOUR_LIGNE_HTML);
		buf.append("WidthPixels = " + Informations.getWidthPixels() + " px" + RETOUR_LIGNE_HTML);
		buf.append("HeightPixels = " + Informations.getHeightPixels() + " px" + RETOUR_LIGNE_HTML);
		buf.append("Taille = ");
		switch(Informations.getScreenLayoutSizeMask()) {
		case Configuration.SCREENLAYOUT_SIZE_SMALL:
			buf.append("SMALL");
			break;
		case Configuration.SCREENLAYOUT_SIZE_NORMAL:
			buf.append("NORMAL");
			break;
		case Configuration.SCREENLAYOUT_SIZE_LARGE:
			buf.append("LARGE");
			break;
		case Configuration.SCREENLAYOUT_SIZE_XLARGE:
			buf.append("XLARGE");
			break;
		default:
			buf.append("???");
			break;
		}
		buf.append(RETOUR_LIGNE_HTML);
		buf.append(RETOUR_LIGNE_HTML);
		
		return buf;
	}
	
	public StringBuffer densiteEcran() {
		StringBuffer buf = new StringBuffer();
		
		buf.append("<b>Densité de l'écran</b>" + RETOUR_LIGNE_HTML);
		buf.append("Density = " + Informations.getDensity() + RETOUR_LIGNE_HTML);
		buf.append("DensityDPI = " + Informations.getDensityDpi() + " dp   (");
		switch (Informations.getDensityDpi()) {
		case DisplayMetrics.DENSITY_LOW:
			buf.append("ldpi -- LOW");
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			buf.append("mdpi -- MEDIUM");
			break;
		case DisplayMetrics.DENSITY_HIGH:
			buf.append("hdpi -- HIGH");
			break;
		case DisplayMetrics.DENSITY_XHIGH:
			buf.append("xhdpi -- XHIGH");
			break;
		case DisplayMetrics.DENSITY_XXHIGH:
			buf.append("xxhdpi -- XXHIGH");
			break;
		default:
			buf.append("??? -- ???");
			break;
		}
		buf.append(")" + RETOUR_LIGNE_HTML);
		buf.append(RETOUR_LIGNE_HTML);
		
		return buf;
	}
	
	public TableRow generationLigne(String type, String name, String version, String vendor, String power, String resolution, String minDelay, String maximunRange) {
		TableRow res = new TableRow(this);
		
		res.addView(generationCaseRow(type));
		res.addView(generationCaseRow(name));
		res.addView(generationCaseRow(version));
		res.addView(generationCaseRow(vendor));
		res.addView(generationCaseRow(power));
		res.addView(generationCaseRow(resolution));
		res.addView(generationCaseRow(minDelay));
		res.addView(generationCaseRow(maximunRange));
		
		return res;
	}
	
	public TableRow generationCaseRow(String value) {
		TableRow res = new TableRow(this);
		res.addView(generationCaseView(value));
		return res;
	}
	public TextView generationCaseView(String value) {
		TextView res = new TextView(this);
		res.setGravity(Gravity.CENTER);
		res.setText(Html.fromHtml("   " + value + "   "));
		return res;
	}
}