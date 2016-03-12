package fr.hexaki.partywallet.db.elements;

public class Consomation {

	public int id,id_sortie;
	public String label;
	public double montant;
	public int type;
	
	public static final String[] TYPE = {"Autre","Nourriture","Transport","Hebergement"};
	
	
	public Consomation(int id,int id_s,double montant,String label,int type) {
		this.id=id;
		this.id_sortie=id_s;
		this.label=label;
		this.montant=montant;
		this.type=type;
	}
	
	public Consomation(int id_s,double m,String l,int type) {
		this.id_sortie=id_s;
		this.id=-2;
		this.label=l;
		this.montant=m;
		this.type=type;
	}

	
	
}
