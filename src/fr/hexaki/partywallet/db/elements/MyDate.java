package fr.hexaki.partywallet.db.elements;

import java.text.DateFormatSymbols;

import android.widget.DatePicker;

public class MyDate {
	
	private int jour,mois,annee;
	
	public MyDate() {
		jour=1;
		mois=1;
		annee=1970;
	}
	
	public MyDate(int j,int m,int a) {
		jour=j;
		mois=m;
		annee=a;
	}
	
	public MyDate(DatePicker d){
		jour=d.getDayOfMonth();
		mois=d.getMonth();
		annee=d.getYear();
	}
	
	public MyDate(String d){
		try {
			String[] donne = ( d.split(" ")[0]).split("-");
			jour=Integer.valueOf(donne[2]);
			mois=Integer.valueOf(donne[1]);
			annee=Integer.valueOf(donne[0]);
		} catch (Exception e) {
			jour=1;
			mois=1;
			annee=1970;
		}
	}
	
	
	public String toDate() {
		return annee+"-"+mois+"-"+jour+" 12:00:00";
	}
	
	
	@Override
	public String toString() {
		return ""+jour+" "+(new DateFormatSymbols()).getMonths()[mois-1]+" "+annee;
	}

}
