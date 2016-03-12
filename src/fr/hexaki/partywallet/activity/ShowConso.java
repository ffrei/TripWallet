package fr.hexaki.partywallet.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import fr.hexaki.partywallet.R;
import fr.hexaki.partywallet.R.id;
import fr.hexaki.partywallet.R.layout;
import fr.hexaki.partywallet.db.DatabaseHandler;
import fr.hexaki.partywallet.db.adapter.MyConsoAdapter;
import fr.hexaki.partywallet.db.elements.Consomation;
import fr.hexaki.partywallet.db.elements.Personne;
import android.widget.ListView;
import android.widget.Toast;

public class ShowConso extends MyActivity implements OnItemClickListener,OnItemLongClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_conso);
		
		resetList();
		
		((ListView)findViewById(R.id.conso_list)).setOnItemClickListener(this);
		((ListView)findViewById(R.id.conso_list)).setOnItemLongClickListener(this);

	}


	private void resetList(){
		ArrayList<Consomation> list = DB.getConsomationsSorite(MainActivity.numSortie);
		
		MyActivity.log(this,"il y a :"+list.size()+" conso(s)");

		if(list.size()!=0){
			MyConsoAdapter ad = new MyConsoAdapter(this, list);
			((ListView)findViewById(R.id.conso_list)).setAdapter(ad);
			
		}
	}

	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		detail((int)id);
	}
	
	public void removeConso(int id){
		Consomation c =DB.getConsommation( id);
		MyActivity.log(this," effacement de :"+c.id);
		DB.removeConsomation(c.id);
		MyActivity.log(this,"effacé");
		resetList();
	}
	
	public void detail(int id){
		MyActivity.log(this,"");
		
		Consomation c =DB.getConsommation(id);
		ArrayList<Personne> payeur = DB.getPersonnesPaye(c.id);
		MyActivity.log(this,""+payeur.toString());
		ArrayList<Personne> recoi = DB.getPersonnesRecu(c.id);
		MyActivity.log(this,""+recoi.toString());
		String txt="";

		if(payeur.size()==0){
			txt+="Tous ont payer pour :";
		}else if(payeur.size()==1){
			txt+=payeur.get(0).nom+" a payer pour : ";
		}else{
			txt+=payeur.get(0).nom;
			for(int i=1; i<payeur.size();i++){
				txt+=", "+payeur.get(i).nom;
			}
			txt+=" ont payer pour : ";
		}
		
		txt+="\n";
		
		if(recoi.size()==0){
			txt+="Tous";
		}else{
			txt+=recoi.get(0).nom;
			for(int i=1; i<recoi.size();i++){
				txt+=", "+recoi.get(i).nom;
			}	
		}
		txt+=" !";
		Toast.makeText(this, txt, Toast.LENGTH_LONG).show();
	}


	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    
		List<String> listItems = new ArrayList<String>();

		listItems.add("Effacer");
		listItems.add("Detail");
		listItems.add("Annuler");

		final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);

		
		builder.setTitle("Que voulez vous faire ?").setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch(which){
				case 0 : 
					removeConso((int)id);
					break;
				case 1 : 
					detail((int)id);
					break;
				case 2 :
					break;
				}
				
			}
	    });
		builder.create().show();
		
		
		return false;
	}

}
