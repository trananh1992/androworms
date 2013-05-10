package com.androworms;

import android.graphics.PointF;

public class ObjetSurCarte extends ElementSurCarte{
	private Objet objet;
	
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

	@Override
	public ElementSurCarte clone() {
		// TODO Auto-generated method stub
		return new ObjetSurCarte(objet, getPosition(), getImageInformation());
	}
}