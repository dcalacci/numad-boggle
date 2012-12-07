package edu.madcourse.dancalacci.circletouch;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import edu.madcourse.dancalacci.R;

public class History_byTime_BarChart extends ListActivity{
	private static String TAG = "circletouch.History_byTime_BarChart";
	//	History_Adaptor adapter;
	final History_byTime_BarChart thisActivity = this;
	public ArrayList<String> entryList = new ArrayList<String>();
	private String current_date;
	private History_byTime_BarChart_LazyAdapter adapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Log.d(TAG, "onCreate");
		setContentView(R.layout.platechart_history);

		Button addChart_Button = (Button) this.findViewById(R.id.button_addChart);
		addChart_Button.setVisibility(Button.GONE);
		
		LinearLayout actionBar = (LinearLayout) this.findViewById(R.id.platechart_history_actionBar);
		actionBar.setVisibility(LinearLayout.GONE);
		
		this.current_date = this.getIntent().getExtras().getString("DATE");
		//addContent();
	}

	protected void onResume() {
		super.onResume();
	
		
		entryList.clear();
		getTimeList();
		if (entryList.size() == 0){
			finish();
		}

		Collections.sort(entryList);
		Collections.reverse(entryList);

		adapter = new History_byTime_BarChart_LazyAdapter(this.getApplicationContext(), entryList, current_date);
		setListAdapter(adapter);
		//this.setListAdapter(new ArrayAdapter<String>(this, R.layout.platechart_history_rows_barchart, R.id.label, entryList));

		ListView lv = getListView();

		lv.setOnItemClickListener(
				new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						// selected item
						String entry = adapter.getEntry(position);
            String date_entry = formatEntry(entry);
						/* String date_entry = formatEntry(entry.replace(":", "+")); */
						String fileName = current_date+"_"+ date_entry +".txt";
						String data = getEntryData(date_entry);

						Log.d(TAG, "File Data : " + data); 
						Intent i = new Intent(getApplicationContext(), AddChart.class);
						i.putExtra("DATA", data);
						i.putExtra("FileName", fileName);
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


	public void getTimeList(){
		String[] myFiles = this.fileList();
		SimpleDateFormat fileDate = 
				new SimpleDateFormat(AddChart.FILE_NAME_FORMAT, Locale.US);
		SimpleDateFormat desiredFormat = 
				new SimpleDateFormat("hh:mm:ss a");

		for (String file : myFiles) {
			// if the date is the date we're interested in
			if (file.contains(current_date)) {
				Date date = fileDate.parse(file.replace(".txt", ""),
						new ParsePosition(0));
				String entryName = desiredFormat.format(date);
				if (!entryList.contains(entryName)) {
					entryList.add(entryName);
				}
			}
		}
	}

	public String getEntryData(String entry){
		String data = "ERROR";
		String fileName = current_date+"_"+entry+".txt";
		Log.d(TAG, "ReadData");
		try {
			Log.d("readData", "File: "+fileName);
			FileInputStream fis = this.openFileInput(fileName);
			BufferedReader r = new BufferedReader(new InputStreamReader(fis));
			data = r.readLine();
			r.close(); 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		return data;

	}


	/**
	 * Formats the textview entry from the listadapter for reading from file
	 * names: HH+MM+ss
	 */
	public static String formatEntry(String entry){
		SimpleDateFormat fileFormat = 
				new SimpleDateFormat(AddChart.HISTORY_TIME_FORMAT, Locale.US);
		SimpleDateFormat entryFormat= 
				new SimpleDateFormat("hh:mm:ss");
		// substring because we can't parse AM/PM using SimpleDateFormat
		// get AM/PM TAG
		String AM_PM = entry.substring(entry.length() - 2, entry.length());
		entry = entry.substring(0, entry.length() - 3);
		// If it's PM time, add 12 to it 
		String hour = entry.substring(0, 2);
		int fileHour = (Integer.parseInt(hour));
		if (AM_PM.equals("PM")) {
			// don't change the time if it's 12pm
			if (fileHour != 12) {
				fileHour += 12;
			}
			entry = entry.substring(2, entry.length());
			entry = fileHour+ entry;
		} else {
			// edge case for 12AM
			if (fileHour == 12) {
				entry = "00" + entry;
			}
		}

		String entryName = "";
		try {
			Log.d(TAG, "time entry: "+entry);
			Date date = entryFormat.parse(entry);
			entryName = fileFormat.format(date);
		} catch(ParseException e) {
			Log.e(TAG, "error parsing time");
			e.printStackTrace();
			/* Toast.makeText(getBaseContext(), "Sorry, I couldn't open that entry...", */
			/* 		Toast.LENGTH_SHORT).show(); */
		}
		Log.d(TAG, "time entryName: "+entryName);
		return entryName;
	}


	public void onAddChartClick(View v){
		Intent i = new Intent(this, CategorySelection.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(i);
	}
}
