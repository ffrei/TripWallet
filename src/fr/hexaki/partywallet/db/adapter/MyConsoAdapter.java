package fr.hexaki.partywallet.db.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import fr.hexaki.partywallet.R;
import fr.hexaki.partywallet.activity.MainActivity;
import fr.hexaki.partywallet.db.elements.Consomation;

public class MyConsoAdapter extends BaseAdapter {

	LayoutInflater li;
	ArrayList<Consomation> list;


	public static final String TAG=MainActivity.TAG ;


	public MyConsoAdapter(Context c, ArrayList<Consomation> p) {
		li= LayoutInflater.from(c);
		list=p;
	}


	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Consomation getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return list.get(position).id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = li.inflate(R.layout.row, null);
		TextView desc = (TextView)convertView.findViewById(R.id.desc);
		desc.setTextColor(Color.BLUE);

		Consomation c =getItem(position); 

		String type = ""; 

		if(c.type<Consomation.TYPE.length){
			type="("+Consomation.TYPE[c.type]+")";
		}else{
			type="(AUTRE)";
		}
		
		desc.setText(c.label + type);
		TextView val = (TextView)convertView.findViewById(R.id.val);
		val.setText(""+c.montant);
		
		convertView.setTag(c.id);
		
		return convertView;

	}

}
