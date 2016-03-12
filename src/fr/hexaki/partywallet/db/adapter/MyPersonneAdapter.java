package fr.hexaki.partywallet.db.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import fr.hexaki.partywallet.R;
import fr.hexaki.partywallet.R.id;
import fr.hexaki.partywallet.R.layout;
import fr.hexaki.partywallet.activity.MainActivity;
import fr.hexaki.partywallet.db.elements.Personne;

public class MyPersonneAdapter extends BaseAdapter {

	LayoutInflater li;
	ArrayList<Personne> list;
	

	public static final String TAG=MainActivity.TAG ;
	
	public MyPersonneAdapter(Context c, ArrayList<Personne> p) {
		li= LayoutInflater.from(c);
		list=p;
	}
	

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Personne getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return list.get(position).id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = li.inflate(R.layout.name_list, null);
		TextView nom = (TextView)convertView.findViewById(R.id.nom);
		nom.setTextColor(Color.BLUE);
		nom.setText(getItem(position).nom);
		return convertView;
		
	}

}
