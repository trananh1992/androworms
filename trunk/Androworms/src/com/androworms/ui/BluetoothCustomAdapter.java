package com.androworms.ui;

import java.util.LinkedHashMap;
import java.util.Map;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androworms.R;

public class BluetoothCustomAdapter extends BaseAdapter {
	
	/* Descrition de ce composant liste :
	 * 
	 * [Titre de section 1] (Non clicable)
	 *    BluetoothDevice A
	 *    BluetoothDevice B
	 *    BluetoothDevice C
	 * [Titre de section 2] (Non clicable)
	 *    BluetoothDevice D
	 *    BluetoothDevice E
	 * [Titre de section 3] (Non clicable)
	 *    <Message de section vide> (Non clicable)
	 * 
	 * Ce composant dispose donc de 3 types d'éléments.
	 */
	
	public static final String TAG = "BluetoothCustomAdapter";
	
	private final Map<String, Adapter> sections = new LinkedHashMap<String, Adapter>();
	private final ArrayAdapter<String> titres;
	private final ArrayAdapter<String> empty;
	
	// Type des éléments dans la liste
	private static final int TYPE_TITRE_SECTION = 0;
	private static final int TYPE_ELEMENT = 1;
	private static final int TYPE_MESSAGE_SECTION_VIDE = 2;
	
	public BluetoothCustomAdapter(Context context) {
		// On crée une liste contenant les titres de sections
		titres = new ArrayAdapter<String>(context, R.layout.liste_bluetooth_titre_section);
		empty = new ArrayAdapter<String>(context, R.layout.liste_bluetooth_message_section_vide);
		empty.add("Aucun périphérique détecté !");
	}
	
	/** Pour ajouter une section à la liste */
	public void ajouterSections(String titreSection, Adapter adapter) {
		// Lorsque l'ont ajoute une section,
		// on ajoute le titre de a section à la liste des titres de section
		this.titres.add(titreSection);
		// On map les elements de la section (dans adapter) avec comme clé, le titre de la section
		this.sections.put(titreSection, adapter);
	}
	
	/** Les éléments de la liste sont-ils séléctionnable ? */
	@Override
	public boolean areAllItemsEnabled() {
		// Non, les titres ou les messages de section vides ne sont pas sélectionnable.
		return false;
	}
	
	/** Obtenir le type d'un élément de la liste */
	@Override
	public int getItemViewType(int position) {
		int pos = position;
		// Pour chaque section
		for (Object section : this.sections.keySet()) {
			Adapter adapter = sections.get(section);
			
			// Taille de la section : titre + éléments de la section
			int taille = 1 + adapter.getCount();

			// Si la position demandé est un titre
			if (pos == 0) {
				return TYPE_TITRE_SECTION;
			}
			// Si la position demandé est un élément
			else if (pos < taille) {
				return TYPE_ELEMENT;
			}
			// Si la position demandé est un message de section vide (taille de la section : juste le titre)
			else if (taille == 1) {
				return TYPE_MESSAGE_SECTION_VIDE;
			}

			// On passe à la section suivante
			pos -= taille;
		}
		return -1;
	}
	
	/** On veux savoir le nombre de type d'élément différent il y a dans la liste */
	@Override
	public int getViewTypeCount() {
		return 3;
	}
	
	/** La liste est-elle vide ? */
	@Override
	public boolean isEmpty() {
		// La lsite est vide que s'il n'y a aucune section.
		return (this.titres.getCount() == 0);
	}
	
	/** On aimerait savoir si l'élément est actif (=séléctionnable) */
	@Override
	public boolean isEnabled(int position) {
		return (getItemViewType(position) != TYPE_TITRE_SECTION && getItemViewType(position) != TYPE_MESSAGE_SECTION_VIDE);
	}
	
	/** Nombre d'éléments dans la liste */
	public int getCount() {
		int total = 0;
		// Pour chaque section
		for (Adapter adapter : this.sections.values()) {
			// on compte le titre + le nombre d'élément dans la liste
			total += 1 + adapter.getCount();
			// si la section est vide, on ajoute 1 pour le message de section vide
			if (adapter.getCount() == 0) {
				total += 1;
			}
		}
		return total;
	}
	
	/** Obtenir l'élément de la liste à la position demandé */
	public Object getItem(int position) {
		int pos = position;
		
		// Pour chaque section
		for (Object section : this.sections.keySet()) {
			Adapter adapter = sections.get(section);
			
			// Taille de la section : titre + éléments de la section
			int taille = 1 + adapter.getCount();

			// Si la position demandé est un titre
			if (pos == 0) {
				return section;
			}
			// Si la position demandé est un élément
			else if (pos < taille) {
				return adapter.getItem(pos - 1);
			}
			// Si la position demandé est un message de section vide (taille de la section : juste le titre)
			else if (taille == 1) {
				return section;
			}
			
			// On passe à la section suivante
			pos -= taille;
		}
		return null;
	}
	
	/** Obtenir l'Id de l'item dans la liste. */
	public long getItemId(int position) {
		// On utilise sa position comme id
		return position;
	}
	
	/** Obtenir la vue de l'élément */
	public View getView(int position, View convertView, ViewGroup parent) {
		int pos = position;
		int sectionnum = 0;
		
		// Pour chaque section
		for (Object section : this.sections.keySet()) {
			Adapter adapter = sections.get(section);
			
			// Taille de la section : titre + éléments de la section
			int taille = 1 + adapter.getCount();
			
			// Si la position demandé est un titre
			if (pos == 0) {
				return titres.getView(sectionnum, convertView, parent);
			}
			// Si la position demandé est un élément
			else if (pos < taille) {
				TextView view = (TextView) adapter.getView(pos - 1, convertView, parent);
				BluetoothDevice bluetoothDevice = (BluetoothDevice) getItem(position);
				if (sectionnum == 0) {
					view.setText("Appareil jumelé : " + bluetoothDevice.getName() + " (" + bluetoothDevice.getAddress() + ")");
				}
				else if (sectionnum == 1) {
					view.setText("Appareil proximité : " + bluetoothDevice.getName() + " (" + bluetoothDevice.getAddress() + ")");
				}
				else {
					view.setText("Appareil inconnu : " + bluetoothDevice.getName() + " (" + bluetoothDevice.getAddress() + ")");
				}
				return view;
			}
			// Si la position demandé est un message de section vide (taille de la section : juste le titre)
			else if (taille == 1) {
				return empty.getView(0, convertView, parent);
			}

			// On passe à la section suivante
			pos -= taille;
			sectionnum++;
		}
		return null;
	}
}