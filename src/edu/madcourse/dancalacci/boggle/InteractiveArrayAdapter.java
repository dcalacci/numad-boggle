package edu.madcourse.dancalacci.boggle;

import java.util.List;

import edu.madcourse.dancalacci.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class InteractiveArrayAdapter extends ArrayAdapter<Model> {

	private Context appContext = null;
	private ArrayList<Model> items = null;
	public InteractiveArrayAdapter(Context context, int textViewResourceId, ArrayList<Model> items){
		super(context,textViewResourceId,items);
		this.appContext = context;
		this.items=items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.rowbuttonlayout, null);
		}
		Model o = items.get(position);
		if (o != null) {
			TextView name = (TextView) v.findViewById(R.id.lst_item_Name);
			TextView mobile = (TextView) v.findViewById(R.id.lst_item_Mobile);
			Button btnDelete = (Button)v.findViewById(R.id.lst_item_Delete);

			btnDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					Toast.makeText(appContext,"Hellow this is clicked", Toast.LENGTH_LONG).show();

				}
			});

			if (name != null) {
				name.setText(o.GetName());                            }
			if(mobile != null){
				mobile.setText(o.GetMobile());
			}
		}
		return v;
	}
}


