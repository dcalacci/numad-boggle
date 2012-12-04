package edu.madcourse.dancalacci.circletouch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import edu.madcourse.dancalacci.R;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class History_byDate extends ListActivity{
	private String TAG = "circletouch.History";
//	History_Adaptor adapter;
	final History_byDate thisActivity = this;
	ArrayList<String> entryList = new ArrayList<String>();

	//NOT USED FOR NOW
	//History_View hist;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		setContentView(R.layout.platechart_history);

		//addContent();
	}



	protected void onResume() {
		super.onResume();
		entryList.clear();
		getDateList();

		Collections.sort(entryList);
		Collections.reverse(entryList);

		this.setListAdapter(new ArrayAdapter<String>(this, R.layout.platechart_history_rows, R.id.label, entryList));

		ListView lv = getListView();

		// listening to single list item on click

		lv.setOnItemClickListener(
				new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						// selected item
						String entry = ((TextView) view).getText().toString();
						String date_entry = formatEntry(entry);
						// Launching new Activity on selecting single List Item
						Intent i = new Intent(getApplicationContext(), History_byTime.class);
						// sending data to new activity
						i.putExtra("DATE", date_entry);
						startActivity(i);

					}
					
					
				}
				);

		//		ListView listView = getListView();
		//		
		//		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		//		
		//		listView.setEmptyView(findViewById(R.layout.platechart_history_empty));
		//		
		//		adapter = new History_Adaptor(thisActivity, R.layout.platechart_history, entryList);
		//		
		//		setListAdapter(adapter);
	}

	/**
	 * Gets the current date and time based on the system settings/timezone
	 * @return String formattedDate
	 */
	public String getTime(){
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = df.format(c.getTime());

		return formattedDate;
	}

	public void addContent(String content){
		//entryList.add(content);
	}


	public void getDateList(){
		String[] myFiles = this.fileList();
		Log.d(TAG, "list: "+ myFiles);
		for(String file : myFiles){
			String[] fileName = file.split("_");
			String date = fileName[0];
			if(!(entryList.contains(date))){
				entryList.add(date);
			}
		}
	}


//
//	/**
//	 * Opens Chart based on date
//	 * Sets ListItem Click Listener
//	 * (non-Javadoc)
//	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
//	 */
//	protected void onListItemClick(ListView l, View v, int position, long id) {
//		// TODO Auto-generated method stub
//		String chart = adapter.getContent(position);
//		Log.d(TAG, "onListItemClick: " + chart);
//		if(! (chart.equals("ERROR"))){
//			TextView entry = (TextView) v.findViewById(R.id.platechart_history_textView_content);
//			entry.setBackgroundColor(getResources().getColor(R.color.game_over));
//			String date_entry = formatEntry(entry.getText().toString());
//			if(!(date_entry.equalsIgnoreCase("ERROR")) || date_entry != null ){
//				Intent i = new Intent(this, History_byTime.class);
//				i.putExtra("DATE", date_entry);
//				startActivity(i);
//			}else{
//				Toast.makeText(v.getContext(), "ERROR: Entry not found!", Toast.LENGTH_SHORT).show();
//			}
//		}
//
//	}

	public String formatEntry(String entry){
		String record_log; 
		record_log = entry.replace(" ", "+");
		return record_log;		
	}




//	public class History_Adaptor extends BaseAdapter{
//		private ArrayList<String> mHistoryList = new ArrayList<String>();
//		private Context mContext;
//		private int rowResID;
//
//		public History_Adaptor(Context c, int rowResID, ArrayList<String> historyList){
//			this.mContext = c;
//			this.mHistoryList = historyList;
//			this.rowResID = rowResID;
//		}
//
//
//		public int getCount() {
//			// TODO Auto-generated method stub
//			return mHistoryList.size();
//		}
//
//		public String getContent(int pos){
//			return mHistoryList.get(pos);
//		}
//
//
//		public Object getItem(int position) {
//			// TODO Auto-generated method stub
//			return position;
//		}
//
//		public long getItemId(int position) {
//			// TODO Auto-generated method stub
//			return position;
//		}
//
//		public boolean isEmptyList(){
//			return mHistoryList.equals(null);
//		}
//
//		public View getView(int position, View view, ViewGroup parent) {
//			boolean isEmpty = isEmptyList();
//
//			if(view == null){
//				getListView().setEmptyView(findViewById(android.R.id.empty));
//				if(!isEmpty){
//					LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//					view = inflater.inflate(R.layout.platechart_history_rows, parent, false);
//				}else{
//					LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//					view = inflater.inflate(R.layout.platechart_history_empty, parent, false);
//				}
//			}
//
//			TextView date = (TextView) 
//					view.findViewById(R.id);
//			if(!isEmpty){
//				date.setText(mHistoryList.get(position));
//			}else{
//				TextView emptyDate = (TextView)
//						view.findViewById(R.id.platechart_history_empty_textView_content);
//				emptyDate.setText("No Entries Found");
//			}
//
//			// Give it a nice background
//			return view;
//		}			
//
//
//	}


	public void onAddChartClick(View v){
		Intent i = new Intent(this, CategorySelection.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(i);
	}

}
