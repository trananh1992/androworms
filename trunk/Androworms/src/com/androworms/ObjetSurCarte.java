package com.androworms;

import android.graphics.PointF;

public class ObjetSurCarte {
	private Objet objet;
	private PointF position;
	private int id;
	
	public ObjetSurCarte(Objet o, PointF p) {
		this.objet = o;
		this.position = p;
	}

	public Objet getObjet() {
		return objet;
	}

	public void setObjet(Objet objet) {
		this.objet = objet;
	}

	public PointF getPosition() {
		return position;
	}

	public void setPosition(PointF position) {
		this.position = position;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}