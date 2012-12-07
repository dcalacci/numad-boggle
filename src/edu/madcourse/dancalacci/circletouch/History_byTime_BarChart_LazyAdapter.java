package edu.madcourse.dancalacci.circletouch;

import java.util.ArrayList;

import edu.madcourse.dancalacci.R;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;

public class History_byTime_BarChart_LazyAdapter extends BaseAdapter {
	private static final String TAG = "circletouch.History_byTime_BarChart";
	private Context context;
	private ArrayList<String> items;
	private static LayoutInflater inflater=null;
	ProfileBar profileBar = new ProfileBar(context);

	public History_byTime_BarChart_LazyAdapter(Context context, ArrayList<String> items) {
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

	public String getEntry(int position){
		return items.get(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi=convertView;
		if(convertView==null)
			vi = inflater.inflate(R.layout.platechart_history_rows_barchart, null);

		TextView title = (TextView)vi.findViewById(R.id.label); // title
		TextView empty = (TextView)vi.findViewById(R.id.byTime_empty_text); // Empty entry 
		empty.setVisibility(View.INVISIBLE);
		
		String entry = items.get(position);
		title.setText(entry);
		// Setting all values in listview
		
		setDefaultsegments(vi);

		return vi;
	}

	private void setDefaultsegments(View vi){
		TextView protein = (TextView)vi.findViewById(R.id.byTime_protein_block); 			// protein  
		TextView vegetable = (TextView)vi.findViewById(R.id.byTime_vegetable_block);	// vegetable  
		TextView dairy = (TextView)vi.findViewById(R.id.byTime_dairy_block); 					// dairy  
		TextView fruit = (TextView)vi.findViewById(R.id.byTime_fruit_block); 					// fruit  
		TextView grain = (TextView)vi.findViewById(R.id.byTime_grain_block); 					// grain  
		TextView oil_sugar = (TextView)vi.findViewById(R.id.byTime_oil_block); 				// oil/sugar  

		protein.setBackgroundColor(context.getResources().getColor(R.color.Protein));
		protein.setVisibility(View.VISIBLE);
		
		vegetable.setBackgroundColor(context.getResources().getColor(R.color.Vegetable));
		vegetable.setVisibility(View.VISIBLE);
		
		dairy.setBackgroundColor(context.getResources().getColor(R.color.Dairy));
		dairy.setVisibility(View.VISIBLE);
		
		fruit.setBackgroundColor(context.getResources().getColor(R.color.Fruit));
		fruit.setVisibility(View.VISIBLE);
		
		grain.setBackgroundColor(context.getResources().getColor(R.color.Grain));
		grain.setVisibility(View.VISIBLE);
		
		oil_sugar.setBackgroundColor(context.getResources().getColor(R.color.Oil_Sugar));
		oil_sugar.setVisibility(View.VISIBLE);
		
		protein.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.FILL_PARENT, (float)0.16667));
		
//		LinearLayout.LayoutParams lp_protein =
//				(LinearLayout.LayoutParams)protein.getLayoutParams();
//		lp_protein.weight = (float)16.667;
		Log.d(TAG, "Protein displayed");
		
//		LinearLayout.LayoutParams lp_vegetable =
//				(LinearLayout.LayoutParams)vegetable.getLayoutParams();
//		lp_vegetable.weight = (float)16.667;
		vegetable.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.FILL_PARENT, (float)0.16667));
		Log.d(TAG, "vegetable displayed");
		
//		LinearLayout.LayoutParams lp_dairy =
//				(LinearLayout.LayoutParams)dairy.getLayoutParams();
//		lp_dairy.weight = (float)16.667;
		dairy.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.FILL_PARENT, (float)0.16667));
		Log.d(TAG, "dairy displayed");
		
//		LinearLayout.LayoutParams lp_fruit =
//				(LinearLayout.LayoutParams)fruit.getLayoutParams();
//		lp_fruit.weight = (float)16.667;
		fruit.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.FILL_PARENT, (float)0.16667));
		Log.d(TAG, "fruit displayed");

//		LinearLayout.LayoutParams lp_grain =
//				(LinearLayout.LayoutParams)grain.getLayoutParams();
//		lp_grain.weight = (float)16.667;
		grain.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.FILL_PARENT, (float)0.16667));
		Log.d(TAG, "grain displayed");

//		LinearLayout.LayoutParams lp_oil_sugar =
//				(LinearLayout.LayoutParams)oil_sugar.getLayoutParams();
//		lp_oil_sugar.weight = (float)16.667;
		oil_sugar.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.FILL_PARENT, (float)0.16667));
		Log.d(TAG, "oil displayed");
	}

	
	
}

