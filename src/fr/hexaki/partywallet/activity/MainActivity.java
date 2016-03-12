package fr.hexaki.partywallet.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import fr.hexaki.partywallet.R;
import fr.hexaki.partywallet.db.DatabaseHandler;
import fr.hexaki.partywallet.db.elements.Sortie;

public class MainActivity extends Activity implements OnClickListener{

	public static final String TAG="PARTY_WALLET" ;
	public static final String PREFS_NAME = "PrefSortie";
	public static int numSortie=0;
	
	DatabaseHandler DB;
	
	public static void updateSorite(Context ctxt, int num) {
		
		SharedPreferences settings = ctxt.getSharedPreferences(PREFS_NAME, 0);
		Editor editor = settings.edit();
		editor.putInt("numSortie", num);
		editor.commit();
		MainActivity.numSortie=num;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Log.d(TAG,"==========================================================");
		Log.d(TAG,"=============Initialisation Party Wallet==================");
		Log.d(TAG,"==========================================================");
		
		DB= new DatabaseHandler(this); 
		
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		numSortie= settings.getInt("numSortie", 0);
		
		
		
	}
	@Override
	protected void onResume() {
		TextView t = (TextView)findViewById(R.id.outputSorite);
		Sortie s = DB.getSortie(numSortie);
		if(s!=null){
			t.setText(""+s.toString());	
		}
		super.onResume();
	}
	

	@Override
	public void onClick(View v) {
		Intent intent;
		switch(v.getId()){
		case R.id.CreateGroupe : 
			//Toast.makeText(this, "Creer un groupe", Toast.LENGTH_SHORT).show();
			intent = new Intent(this, Group_Creator.class);
			startActivity(intent);
			break;
		case R.id.AddPerso : 
			//Toast.makeText(this, "Creer un groupe", Toast.LENGTH_SHORT).show();
			intent = new Intent(this, Add_Personne.class);
			startActivity(intent);
			break;
		case R.id.AddConso : 
			//Toast.makeText(this, "Ajouter conso", Toast.LENGTH_SHORT).show();
			intent = new Intent(this, Add_conso.class);
			startActivity(intent);
			break;
		case R.id.ShowStats : 
			//Toast.makeText(this, "Afficher les stats", Toast.LENGTH_SHORT).show();
			intent = new Intent(this,ShowConso.class);
			startActivity(intent);
			break;
		case R.id.FinalStats :
			intent = new Intent(this,ShowFinal.class);
			startActivity(intent);
			break;
		default : 
			Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show();
		}
	}
}
