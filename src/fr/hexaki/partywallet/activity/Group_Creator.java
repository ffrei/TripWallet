package fr.hexaki.partywallet.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import fr.hexaki.partywallet.R;
import fr.hexaki.partywallet.db.elements.MyDate;
import fr.hexaki.partywallet.db.elements.Sortie;

public class Group_Creator extends MyActivity implements OnClickListener	 {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_sortie);
		
		DatePicker calendar = (DatePicker) findViewById(R.id.calendar);
		calendar.setCalendarViewShown(false);

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

			MyActivity.log(this, "getting info");

			EditText dest= (EditText) findViewById(R.id.sortieName);
			EditText duree= (EditText) findViewById(R.id.sortieDuree);
			MyDate date= new MyDate((DatePicker)findViewById(R.id.calendar));
			Toast.makeText(this, dest.getText().toString()+" "+date.toString()+" "+duree.getText().toString(), Toast.LENGTH_SHORT).show();

			MyActivity.log(this, "rdy for adding");

			int temps = 0;

			try{
				temps =(Integer.parseInt(duree.getText().toString().split(" ")[0]));
			}catch (Exception e) {			}

			Sortie s = new Sortie(dest.getText().toString(),date,temps);

			DB.addSortie(s);
			MyActivity.log(this, "added sortie id="+s.id);
			if(s.id!=-1){
				MainActivity.updateSorite(this, s.id);
			}
			break;
		}		
	}
}






















