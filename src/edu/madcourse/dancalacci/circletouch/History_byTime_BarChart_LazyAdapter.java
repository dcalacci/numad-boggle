package edu.madcourse.dancalacci.circletouch;

import java.util.ArrayList;

import edu.madcourse.dancalacci.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class History_byTime_BarChart_LazyAdapter extends BaseAdapter {

	private Activity context;
	private ArrayList<String> items;
	private static LayoutInflater inflater=null;

	public void LazyAdapter(Activity context, ArrayList<String> items) {
		this.context = context;
		this.items = items;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return items.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi=convertView;
		if(convertView==null)
			vi = inflater.inflate(R.layout.platechart_history_rows_barchart, null);

		TextView title = (TextView)vi.findViewById(R.id.label); // title
		TextView empty = (TextView)vi.findViewById(R.id.byTime_empty_text); // Empty entry 
		TextView protein = (TextView)vi.findViewById(R.id.byTime_protein_block); // Empty entry 
		TextView vegetable = (TextView)vi.findViewById(R.id.byTime_vegetable_block); // Empty entry 
		TextView dairy = (TextView)vi.findViewById(R.id.byTime_dairy_block); // Empty entry 
		TextView fruit = (TextView)vi.findViewById(R.id.byTime_fruit_block); // Empty entry 
		TextView grain = (TextView)vi.findViewById(R.id.byTime_grain_block); // Empty entry 
		TextView oil_sugar = (TextView)vi.findViewById(R.id.byTime_oil_block); // Empty entry 

		String name = items.get(position);

		// Setting all values in listview
		title.setText(name);
		
		
		return vi;
	}

}

