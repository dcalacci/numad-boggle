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




	public String formatEntry(String entry){
		String record_log; 
		record_log = entry.replace(" ", "+");
		return record_log;		
	}


	public void onAddChartClick(View v){
		Intent i = new Intent(this, CategorySelection.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(i);
	}

}
