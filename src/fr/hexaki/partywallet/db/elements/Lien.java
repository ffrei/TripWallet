package fr.hexaki.partywallet.db.elements;

public class Lien {

	public int idPers;
	public int idCons;
	public int payeur;
	
	public Lien(int ip,int ic,int p) {
		this.idPers=ip;
		this.idCons=ic;
		this.payeur=p;
	}
	
}
