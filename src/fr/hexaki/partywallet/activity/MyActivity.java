package fr.hexaki.partywallet.activity;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import fr.hexaki.partywallet.R;
import fr.hexaki.partywallet.db.DatabaseHandler;
import fr.hexaki.partywallet.db.elements.Sortie;

public abstract class MyActivity extends Activity	 {


	public static final String TAG="PARTY_WALLET" ;
	public static final String PREFS_NAME = "PrefSortie";
	public static int numSortie=0;
	public DatabaseHandler DB;

	public static void updateSorite(Context ctxt, int num) {
		SharedPreferences settings = ctxt.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putInt("numSortie", num);
		editor.commit();
		MyActivity.numSortie=num;

	}


	public static void log(Object o, String mess){
		Log.d(TAG,o.getClass().getName()+":"+mess);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DB= new DatabaseHandler(this); 

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		numSortie= settings.getInt("numSortie", 0);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.add(getString(R.string.selectSortie));
		menu.add(getString(R.string.clearDB));
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if(item.getItemId()==android.R.id.home){
			this.finish();
			return true;
		}else if(item.getTitle()==getString(R.string.selectSortie)){

			final ArrayList<Sortie> list =  DB.getSorties();
			CharSequence[] names= new String[list.size()];
			for(int i=0; i<list.size();i++){
				names[i]=list.get(i).toString();
				Log.d(TAG, (String) names[i]);
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity.this);
			builder.setTitle("Selectionner une Sortie").setItems(names, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// The 'which' argument contains the index position
					// of the selected item
					Toast.makeText(getApplicationContext(),""+list.get(which) , Toast.LENGTH_SHORT).show();
					if(list.get(which).id!=-1){
						MyActivity.updateSorite(getApplicationContext(), list.get(which).id);
					}
				}
			});
			builder.create();
			builder.show();
		}else if(item.getTitle()==getString(R.string.clearDB)){

			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which){
					case DialogInterface.BUTTON_POSITIVE:
						DB.reset();
						break;

					case DialogInterface.BUTTON_NEGATIVE:
						Toast.makeText(getApplicationContext(), "Annuler ", Toast.LENGTH_SHORT).show();
						break;
					}
				}
			};

			AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity.this);
			builder.setTitle("Effacer ?").setMessage("Cette action effacera toutes vos sorties").setPositiveButton("Oui", dialogClickListener)
			.setNegativeButton("Non", dialogClickListener).show();
		}

		return super.onOptionsItemSelected(item);
	}
}






















