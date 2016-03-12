package fr.hexaki.partywallet.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import fr.hexaki.partywallet.R;
import fr.hexaki.partywallet.R.id;
import fr.hexaki.partywallet.R.layout;
import fr.hexaki.partywallet.db.DatabaseHandler;
import fr.hexaki.partywallet.db.adapter.MyPersonneAdapter;
import fr.hexaki.partywallet.db.elements.Consomation;
import fr.hexaki.partywallet.db.elements.Lien;
import fr.hexaki.partywallet.db.elements.Personne;

public class Add_conso extends MyActivity implements OnClickListener{
	
	ArrayList<Personne> list ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_add_conso);

		DB= new DatabaseHandler(this);

		Spinner sp = (Spinner)findViewById(R.id.selectType);
		String[] list = Consomation.TYPE;
		ArrayAdapter<String > aa=new ArrayAdapter<String> (this,android.R.layout.simple_spinner_item, list);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(aa);
		/*
		for(int i = 0; i<sp.getChildCount();i++){
			((TextView)sp.getChildAt(i)).setTextSize(20);
		}*/
		
		resetList();

	}


	private void resetList(){
		list = DB.getPersonnes(MainActivity.numSortie);

		if(list.size()!=0){
			MyPersonneAdapter ad = new MyPersonneAdapter(this, list);
			((ListView)findViewById(R.id.list_from)).setAdapter(ad);
			ad = new MyPersonneAdapter(this, list);
			((ListView)findViewById(R.id.list_for)).setAdapter(ad);
		}
	}


	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.AjouterConso){

			Spinner sp = (Spinner)findViewById(R.id.selectType);
			EditText label = (EditText)findViewById(R.id.Label);
			EditText montant = (EditText)findViewById(R.id.Montant);
			
			int type = sp.getSelectedItemPosition();
			String desc = ""+label.getText();
			
			double val= 0.0;
			
			if(""+montant.getText()!=""){
				val = Double.valueOf(""+montant.getText());
			}
			
			
			boolean cont=true;
			
			if(val==0.0){
			
				AlertDialog.Builder builder = new AlertDialog.Builder(this);

			    builder.setTitle("Anulation");
			    builder.setMessage("Veuillez saisir un montant ! ");

			    builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {

			        public void onClick(DialogInterface dialog, int which) {
			        	
			            dialog.dismiss();
			        }

			    });

			    AlertDialog alert = builder.create();
			    alert.show();
			    
			    cont=false;

				
			}
			
			if(cont){
			
				MyActivity.log(this,"La cons : "+desc+" est de type "+type+" pour un montant de : "+val  );
				
				Consomation c = new Consomation(MainActivity.numSortie,val, desc, type);
				DB.addConsomation(c);
				
				MyActivity.log(this,"ID de la conso : "+c.id);

				
				
				ListView lFROM =((ListView)findViewById(R.id.list_from));
				ListView lFOR =((ListView)findViewById(R.id.list_for));
				
				for(int i = 0; i<lFROM.getChildCount();i++){
					LinearLayout llfrom = (LinearLayout) lFROM.getChildAt(i);
					LinearLayout llfor = (LinearLayout) lFOR.getChildAt(i);
					
					String nom ="" +((TextView)llfor.getChildAt(1)).getText(); 
					
					MyActivity.log(this, "	"+nom+" :");
				
					
					
					boolean paye = ((CheckBox)llfrom.getChildAt(2)).isChecked();
					boolean recu = ((CheckBox)llfor.getChildAt(2)).isChecked();
					
					((CheckBox)llfrom.getChildAt(2)).setChecked(false);
					((CheckBox)llfor.getChildAt(2)).setChecked(false);
					
					int lien;
				
					if(paye){
						if(recu){
							// il a payé et recu
							MyActivity.log(this,"a payer et recu");
							lien=DB.PAYE_ET_RECU;
						}else{
							// il paye et recois pas
							MyActivity.log(this,"paye mais recois pas");
							lien=DB.PAYE;
						}
					}else{
						if(recu){
							// il recu 
							MyActivity.log(this,"recois");
							lien=DB.RECU;
						}else{
							// il n'as aucun lien
							MyActivity.log(this,"n'as aucun lien");
							lien=-1;
						}
					}
					if(lien!=-1){
						Lien l = new Lien(list.get(i).id,c.id, lien);
						DB.addLiens(l);
					}
					
					
				}

				
				Toast.makeText(this, "Consommation ajouter", Toast.LENGTH_SHORT).show();
				
				

				
				label.setText("");
				montant.setText("0");
				
				
				
			}
						
			
			
			
						
		}
	}







}
