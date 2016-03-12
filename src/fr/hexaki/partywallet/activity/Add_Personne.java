package fr.hexaki.partywallet.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import fr.hexaki.partywallet.R;
import fr.hexaki.partywallet.R.id;
import fr.hexaki.partywallet.R.layout;
import fr.hexaki.partywallet.R.menu;
import fr.hexaki.partywallet.db.DatabaseHandler;
import fr.hexaki.partywallet.db.elements.Personne;

public class Add_Personne extends Activity implements OnClickListener	 {

	DatabaseHandler DB;

	public static final String TAG=MainActivity.TAG ;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_personne);

		DB= new DatabaseHandler(this); 

		Log.d(TAG, "DB handler created");
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group__creator, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			DB.reset();
			resetList();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
