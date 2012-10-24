package edu.madcourse.dancalacci.boggle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.madcourse.dancalacci.R;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

public class Multiplayer_Received_array_adaptor extends BaseAdapter implements View.OnClickListener{
	private String TAG = "Multiplayer_Received_array_adaptor";

	private Activity activity;
	private ArrayList<String> data;
	private static LayoutInflater inflater=null;


	public Multiplayer_Received_array_adaptor(Activity a, ArrayList<String> d) {
		activity = a;
		data=d;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi=convertView;
		if(convertView==null)
			vi = inflater.inflate(R.layout.multiplayer_received_rows, null);

		TextView name = (TextView)vi.findViewById(R.id.multiplayer_requests_textView_content); // username
		Button btn_accept = (Button)vi.findViewById(R.id.multiplayer_received_accept_button); // accept button
		btn_accept.setOnClickListener(this);

		Button btn_reject = (Button)vi.findViewById(R.id.multiplayer_received_reject_button); // reject button
		btn_reject.setOnClickListener(this);

		String username = data.get(position);

		// Setting all values in listview
		name.setText(username);
		return vi;
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.multiplayer_received_accept_button:
			//do something
			break;
		case R.id.multiplayer_received_reject_button:
			//do something
			break;
		}
	}

}
