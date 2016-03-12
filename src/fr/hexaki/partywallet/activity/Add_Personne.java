package fr.hexaki.partywallet.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import fr.hexaki.partywallet.R;
import fr.hexaki.partywallet.db.elements.Personne;

public class Add_Personne extends MyActivity implements OnClickListener	 {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_personne);
		resetList();
	}
	
	private void resetList(){
		ArrayList<Personne> list = DB.getPersonnes(MainActivity.numSortie);
		String[] noms = new String[list.size()];
		
		for(int i=0;i<list.size();i++){
			noms[i]=list.get(i).nom;
		}
		
        ListView mListView = (ListView)findViewById(R.id.nameList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Add_Personne.this,
                android.R.layout.simple_list_item_1, noms);
        mListView.setAdapter(adapter);
		
	}
	
	
	
	

	@Override
	public void onClick(View v) {

		if(v.getId()==R.id.addName){
			EditText et = (EditText)findViewById(R.id.newName);
			
			String n = et.getText().toString();
			et.setText("");
			if(!n.equals("")){
				DB.addPersonne(new Personne(MainActivity.numSortie,n));
				resetList();
				Toast.makeText(this, "Ajouter", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(this, "Entrer un nom", Toast.LENGTH_SHORT).show();
			}
		}else{
			ArrayList<Personne> list = DB.getPersonnes(MainActivity.numSortie);
			Toast.makeText(this, "Il y a : "+list.size()+" personnes", Toast.LENGTH_SHORT).show();
			finish();
		}
		
		
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean res =super.onOptionsItemSelected(item);
		resetList();
		return res;
	}
	
}
