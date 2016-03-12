package fr.hexaki.partywallet.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import fr.hexaki.partywallet.R;
import fr.hexaki.partywallet.R.id;
import fr.hexaki.partywallet.R.layout;
import fr.hexaki.partywallet.db.DatabaseHandler;
import fr.hexaki.partywallet.db.adapter.MyFinalAdapter;
import fr.hexaki.partywallet.db.elements.Personne;

public class ShowFinal extends Activity{

	DatabaseHandler DB;

	public static final String TAG=MainActivity.TAG ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_final);

		TextView textView = (TextView) findViewById(R.id.note_show_final);
		textView.setText(Html.fromHtml((String) textView.getText()));
		
		DB= new DatabaseHandler(this);
		resetList();
	}


	private void resetList(){
		ArrayList<Personne> list = DB.getPersonnes(MainActivity.numSortie);
		
		Log.d(TAG,"il y a :"+list.size()+" Personne(s)");

		if(list.size()!=0){
			MyFinalAdapter ad = new MyFinalAdapter(this, list);
			((ListView)findViewById(R.id.final_list)).setAdapter(ad);
			
		}
	}

}
