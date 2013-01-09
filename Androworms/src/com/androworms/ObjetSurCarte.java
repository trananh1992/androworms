package com.androworms;

import android.graphics.PointF;

public class ObjetSurCarte extends ElementSurCarte{
	private Objet objet;
	private int id;
	
	public ObjetSurCarte(Objet o, PointF p, ImageInformation ii) {
		super(p, ii);
		this.objet = o;
	}

	public Objet getObjet() {
		return objet;
	}

	public void setObjet(Objet objet) {
		this.objet = objet;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}