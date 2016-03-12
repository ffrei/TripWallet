package fr.hexaki.partywallet.db.elements;

public class Personne {

	public int id,id_sortie;
	public String nom;
	
	public Personne(int id,int ids,String n) {
		this.id=id;
		this.id_sortie=ids;
		this.nom=n;
	}
	
	public Personne(int ids,String n) {
		this.id=-1;
		this.id_sortie = ids;
		this.nom=n;
	}
	
	
}
