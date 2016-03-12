package fr.hexaki.partywallet.db.elements;

public class Sortie {

	public int id,duree;
	public MyDate date;
	public String nom;
	
	public Sortie(int id,String n,MyDate d,int duree) {
		this.id=id;
		this.date=d;
		this.duree=duree;
		this.nom=n;
	}
	
	public Sortie(String n,MyDate d,int duree) {
		this.id=-1;
		this.date=d;
		this.duree=duree;
		this.nom=n;
	}
	
	@Override
	public String toString() {
		return nom+" ("+date+")"+" "+duree+" jour(s)";
	}
	
}
