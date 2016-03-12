package fr.hexaki.partywallet.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import fr.hexaki.partywallet.R;
import fr.hexaki.partywallet.db.DatabaseHandler;
import fr.hexaki.partywallet.db.elements.MyDate;
import fr.hexaki.partywallet.db.elements.Sortie;

public class Group_Creator extends Activity implements OnClickListener	 {

	DatabaseHandler DB;

	public static final String TAG=MainActivity.TAG ;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_sortie);

		DB= new DatabaseHandler(this); 

		DatePicker calendar = (DatePicker) findViewById(R.id.calendar);

		calendar.setCalendarViewShown(false);

		Log.d(TAG, "DB handler created");
	}


	private void updateDuree(int val){
		EditText et = (EditText)findViewById(R.id.sortieDuree);
		int newVal =0;
		try {
			newVal= (Integer.parseInt(et.getText().toString().split(" ")[0])+val);
		} catch (Exception e) {
			newVal=val;
		}
		if(newVal<0){
			newVal=0;
		}
		et.setText(""+newVal+" jour(s)");

	}

	@Override
	public void onClick(View v) {
		EditText et=null;
		switch(v.getId()){
		case R.id.dureeSortieMoinsS:
			updateDuree(-7);
			break;
		case R.id.dureeSortieMoinsJ:
			updateDuree(-1);
			break;
		case R.id.dureeSortiePlusJ:
			updateDuree(1);
			break;
		case R.id.dureeSortiePlusS:
			updateDuree(7);
			break;
		case R.id.ValiderSortie:

			Log.d(TAG, "getting info");

			EditText dest= (EditText) findViewById(R.id.sortieName);
			EditText duree= (EditText) findViewById(R.id.sortieDuree);
			MyDate date= new MyDate((DatePicker)findViewById(R.id.calendar));
			Toast.makeText(this, dest.getText().toString()+" "+date.toString()+" "+duree.getText().toString(), Toast.LENGTH_SHORT).show();

			Log.d(TAG, "rdy for adding");

			int temps = 0;

			try{
				temps =(Integer.parseInt(duree.getText().toString().split(" ")[0]));
			}catch (Exception e) {			}

			Sortie s = new Sortie(dest.getText().toString(),date,temps);

			DB.addSortie(s);
			Log.d(TAG, "added sortie id="+s.id);
			if(s.id!=-1){
				MainActivity.updateSorite(this, s.id);
			}
			break;
		}		
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
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		if(item.getTitle()==getString(R.string.selectSortie)){

			final ArrayList<Sortie> list =  DB.getSorties();
			CharSequence[] names= new String[list.size()];
			for(int i=0; i<list.size();i++){
				names[i]=list.get(i).toString();
				Log.d(TAG, (String) names[i]);
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(Group_Creator.this);
			builder.setTitle("Selectionner une Sortie").setItems(names, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// The 'which' argument contains the index position
					// of the selected item
					Toast.makeText(getApplicationContext(),""+list.get(which) , Toast.LENGTH_SHORT).show();
					if(list.get(which).id!=-1){
						MainActivity.updateSorite(getApplicationContext(), list.get(which).id);
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

			AlertDialog.Builder builder = new AlertDialog.Builder(Group_Creator.this);
			builder.setTitle("Effacer ?").setMessage("Cette action effacera toutes vos sorties").setPositiveButton("Oui", dialogClickListener)
			.setNegativeButton("Non", dialogClickListener).show();
		}

		return super.onOptionsItemSelected(item);
	}
}






















