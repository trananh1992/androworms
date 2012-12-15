package com.androworms;

public class ObjetSurCarte {
	private Objet objet;
	private Position position;
	
	public ObjetSurCarte(Objet o, Position p)
	{
		this.objet = o;
		this.position = p;
	}

	public Objet getObjet() {
		return objet;
	}

	public void setObjet(Objet objet) {
		this.objet = objet;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
	
	public void setPosition(int x, int y)
	{
		position.setX(x);
		position.setY(y);
	}
	
	

}
