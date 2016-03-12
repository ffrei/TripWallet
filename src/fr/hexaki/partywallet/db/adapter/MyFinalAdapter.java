package fr.hexaki.partywallet.db.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import fr.hexaki.partywallet.R;
import fr.hexaki.partywallet.activity.MainActivity;
import fr.hexaki.partywallet.db.DatabaseHandler;
import fr.hexaki.partywallet.db.elements.Consomation;
import fr.hexaki.partywallet.db.elements.Personne;

public class MyFinalAdapter extends BaseAdapter {

	LayoutInflater li;
	ArrayList<Personne> list;
	HashMap<Integer, Double> soldes;

	public static final String TAG=MainActivity.TAG ;


	public MyFinalAdapter(Context c, ArrayList<Personne> p) {
		Log.d(TAG,"1");
		li= LayoutInflater.from(c);
		Log.d(TAG,"1.1");
		list=p;
		soldes=new HashMap<Integer, Double>();
		Log.d(TAG,"1.2 "+p.size());
		for (int i = 0; i < list.size(); i++) {
			Log.d(TAG,"1.3 "+list.get(i).id);
			soldes.put(list.get(i).id, 0.0);
			Log.d(TAG,"1.4");
		}
		DatabaseHandler db = new DatabaseHandler(c);
		Log.d(TAG,"2");

		ArrayList<Consomation> consos = db.getConsomationsSorite(MainActivity.numSortie);
		Log.d(TAG,"3 "+consos.size());

		for (int i = 0; i < consos.size(); i++) {
			Consomation conso = consos.get(i);
			Log.d(TAG,"4 "+conso.id+" "+conso.id_sortie);
			ArrayList<Personne> payeur = db.getPersonnesPaye(conso.id);
			Log.d(TAG,"4.1 "+payeur.size());
			if(payeur.size()==0){
				for (Integer key : soldes.keySet()) {
					soldes.put(key, (soldes.get(key) + conso.montant/list.size()));
				}
			}else{
				for(int j=0;j<payeur.size();j++){
					int key = payeur.get(j).id;
					Log.d(TAG,"4.2 " +key+ " "+ soldes.get(key) );
					double nvoMontant= soldes.get(key) + conso.montant/payeur.size();
					Log.d(TAG,"4.3" +nvoMontant);
					soldes.put(key, nvoMontant);
				}
			}
			Log.d(TAG,"5");

			ArrayList<Personne> receveur = db.getPersonnesRecu(conso.id);
			if(receveur.size()==0){
				for (Integer key : soldes.keySet()) {
					soldes.put(key, (soldes.get(key) - conso.montant/list.size()));
				}
			}else{
				for(int j=0;j<receveur.size();j++){
					int key = receveur.get(j).id;
					soldes.put(key, (soldes.get(key) - conso.montant/receveur.size()));
				}
			}
			
		}
		
		
	}


	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Personne getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return list.get(position).id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG,"6");

		Personne p = list.get(position);
		Log.d(TAG,"7");
		convertView = li.inflate(R.layout.row_show_final, null);
		Log.d(TAG,"8");
		TextView nom = (TextView)convertView.findViewById(R.id.nomFinal);
		nom.setTextColor(Color.BLUE);
		nom.setText(p.nom);
		Log.d(TAG,"9");
		double solde = soldes.get(p.id);
		Log.d(TAG,"11");
		TextView montant = (TextView)convertView.findViewById(R.id.total_amount);
		if(solde>=0){
			montant.setTextColor(Color.BLACK);
		}else{
			montant.setTextColor(Color.RED);
		}
		Log.d(TAG,"12");
		DecimalFormat df = new DecimalFormat("#.##");      
		montant.setText(df.format(solde));
		Log.d(TAG,"13");

		return convertView;

	}

}
