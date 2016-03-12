package fr.hexaki.partywallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import fr.hexaki.partywallet.R;
import fr.hexaki.partywallet.db.elements.Sortie;

public class MainActivity extends MyActivity implements OnClickListener{




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		MyActivity.log(this,"==========================================================");
		MyActivity.log(this,"=============Initialisation Party Wallet==================");
		MyActivity.log(this,"==========================================================");
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
