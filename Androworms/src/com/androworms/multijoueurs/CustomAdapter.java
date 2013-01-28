package com.androworms.multijoueurs;

import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.androworms.R;

public class CustomAdapter extends BaseAdapter {
	// [Titre] Appareils A
	//          bla
	//          bla
	//          bla
	// [Titre] Appareils B
	//          bla
	public static final String TAG = "BluetoothCustomAdapter";
	
	public final Map<String, Adapter> sections = new LinkedHashMap<String, Adapter>();
	public final ArrayAdapter<String> titres;
	public static final int TYPE_SECTION_HEADER = 0;

	public CustomAdapter(Context context) {
		// On crée une liste contenant les titres de sections
		titres = new ArrayAdapter<String>(context, R.layout.liste_bluetooth_section);
	}
	
	public void addSection(String section, Adapter adapter) {
		// Lorsque l'ont ajoute une section,
		// on ajoute le titre de a section à la liste des titres de section
		this.titres.add(section);
		// On map les elements de la section (dans adapter) avec comme clé, le titre de la section
		this.sections.put(section, adapter);
	}

	public Object getItem(int position) {
		for (Object section : this.sections.keySet()) {
			Adapter adapter = sections.get(section);
			int size = adapter.getCount() + 1;

			// check if position inside this section
			if (position == 0) {
				return section;
			}
			if (position < size) {
				return adapter.getItem(position - 1);
			}
			// otherwise jump into next section
			position -= size;
		}
		return null;
	}

	public int getCount() {
		// total together all sections, plus one for each section header
		int total = 0;
		for (Adapter adapter : this.sections.values()) {
			total += adapter.getCount() + 1;
		}
		Log.v(TAG,"getCount() = "+total);
		return total;
	}

	@Override
	public int getViewTypeCount() {
		// assume that headers count as one, then total all sections
		int total = 1;
		for (Adapter adapter : this.sections.values()) {
			total += adapter.getViewTypeCount();
		}
		return total;
	}

	@Override
	public int getItemViewType(int position) {
		int type = 1;
		for (Object section : this.sections.keySet()) {
			Adapter adapter = sections.get(section);
			int size = adapter.getCount() + 1;

			// check if position inside this section
			if (position == 0) {
				return TYPE_SECTION_HEADER;
			}
			if (position < size) {
				return type + adapter.getItemViewType(position - 1);
			}

			// otherwise jump into next section
			position -= size;
			type += adapter.getViewTypeCount();
		}
		return -1;
	}

	public boolean areAllItemsSelectable() {
		// On ne peux pas selectionner les titres
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		// On désactive l'élément si c'est un titre
		return (getItemViewType(position) != TYPE_SECTION_HEADER);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		int sectionnum = 0;
		for (Object section : this.sections.keySet()) {
			Adapter adapter = sections.get(section);
			int size = adapter.getCount() + 1;

			// check if position inside this section
			if (position == 0) {
				return titres.getView(sectionnum, convertView, parent);
			}
			if (position < size) {
				return adapter.getView(position - 1, convertView, parent);
			}

			// otherwise jump into next section
			position -= size;
			sectionnum++;
		}
		return null;
	}

	public long getItemId(int position) {
		return position;
	}
}